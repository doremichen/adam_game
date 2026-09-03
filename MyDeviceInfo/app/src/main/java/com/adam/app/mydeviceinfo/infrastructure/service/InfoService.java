/*
 * Copyright (c) 2026 Adam Chen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.adam.app.mydeviceinfo.infrastructure.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.TransportInfo;
import android.net.wifi.WifiInfo;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.StatFs;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;

import com.adam.app.mydeviceinfo.IInfoCallback;
import com.adam.app.mydeviceinfo.IInfoService;
import com.adam.app.mydeviceinfo.R;
import com.adam.app.mydeviceinfo.common.Constants;
import com.adam.app.mydeviceinfo.common.Utils;
import com.adam.app.mydeviceinfo.common.scheduler.TaskScheduler;
import com.adam.app.mydeviceinfo.infrastructure.dto.DeviceStateDto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Service that provides device information via AIDL with power-saving periodic updates.
 */
@AndroidEntryPoint
public final class InfoService extends Service {

    @Inject
    TaskScheduler mScheduler;

    private int mActiveClients = 0;
    private volatile DeviceStateDto mCachedState = new DeviceStateDto.Builder().build();
    private final RemoteCallbackList<IInfoCallback> mCallbacks = new RemoteCallbackList<>();

    private int mBatteryPct = 0;
    private int mBatteryTemp = 0;
    private int mBatteryVolt = 0;
    private String mBatteryStatus = "";
    private String mBatteryHealth = "";

    private final BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                mBatteryPct = (int) (level * 100 / (float) scale);
                mBatteryTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10;
                mBatteryVolt = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                mBatteryStatus = String.valueOf(status);

                int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
                mBatteryHealth = String.valueOf(health);
            }
        }
    };

    /**
     * Called when the service is first created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        mActiveClients++;
        Utils.logDebug(Constants.TAG_INFO_SERVICE, Constants.MSG_ON_BIND_PREFIX + mActiveClients);
        if (mActiveClients == 1) {
            mScheduler.start(this::updateDeviceInfo);
        }
        return new BnInfoService(this);
    }

    /**
     * Called when a client unbinds from the service.
     * @param intent The unbind intent.
     * @return true if onRebind should be called.
     */
    @Override
    public boolean onUnbind(Intent intent) {
        mActiveClients--;
        Utils.logDebug(Constants.TAG_INFO_SERVICE, Constants.MSG_ON_UNBIND_PREFIX + mActiveClients);
        if (mActiveClients == 0) {
            mScheduler.stop();
        }
        return super.onUnbind(intent);
    }

    /**
     * Called when the service is being destroyed.
     */
    @Override
    public void onDestroy() {
        mScheduler.stop();
        mCallbacks.kill();
        unregisterReceiver(mBatteryReceiver);
        super.onDestroy();
    }

    /**
     * Main task that orchestrates the collection of device information.
     */
    private void updateDeviceInfo() {
        DeviceStateDto.Builder builder = new DeviceStateDto.Builder();

        // 1. Live Dashboard Data
        collectDashboardMetrics(builder);

        // 2. Static System Specs
        collectHardwareSpecs(builder);

        // 3. Connectivity State
        collectConnectivityState(builder);

        DeviceStateDto dto = builder.build();
        mCachedState = dto;
        Utils.logDebug(Constants.TAG_INFO_SERVICE, Constants.MSG_DEVICE_INFO_UPDATED);
        broadcastUpdate(dto);
    }

    /**
     * Collects real-time metrics for the dashboard (RAM, Storage, Battery).
     * @param builder The DTO builder to populate.
     */
    private void collectDashboardMetrics(DeviceStateDto.Builder builder) {
        // RAM
        ActivityManager.MemoryInfo memInfo = getMemoryInfoInternal();
        builder.setRamUsed(memInfo.totalMem - memInfo.availMem)
               .setRamTotal(memInfo.totalMem);

        // Storage
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        builder.setStorageTotal(totalBlocks * blockSize)
               .setStorageUsed((totalBlocks - availableBlocks) * blockSize);

        // Battery (from receiver)
        builder.setBatteryPct(mBatteryPct)
               .setBatteryTemp(mBatteryTemp)
               .setBatteryVolt(mBatteryVolt)
               .setBatteryStatus(mBatteryStatus)
               .setBatteryHealth(mBatteryHealth);

        // Uptime
        builder.setUptime(SystemClock.elapsedRealtime());

        // CPU (Traditional text info as fallback)
        builder.setCpuInfo(readFile(Constants.PATH_CPU_INFO));
        builder.setMemoryInfo(formatMemoryInfo(memInfo));
    }

    /**
     * Collects static hardware specifications (Model, OS, CPU).
     * @param builder The DTO builder to populate.
     */
    private void collectHardwareSpecs(DeviceStateDto.Builder builder) {
        builder.setManufacturer(Build.MANUFACTURER)
               .setBrand(Build.BRAND)
               .setModel(Build.MODEL)
               .setCodename(Build.DEVICE)
               .setOsVersion(Build.VERSION.RELEASE)
               .setSdkLevel(Build.VERSION.SDK_INT)
               .setSecurityPatch(Build.VERSION.SECURITY_PATCH)
               .setKernelVersion(System.getProperty("os.version"))
               .setFingerprint(Build.FINGERPRINT)
               .setCpuAbi(Build.SUPPORTED_ABIS[0])
               .setCpuCores(Runtime.getRuntime().availableProcessors());

        // Screen
        WindowManager wm = getSystemService(WindowManager.class);
        if (wm != null) {
            WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
            Rect bounds = windowMetrics.getBounds();
            builder.setScreenRes(bounds.width() + "x" + bounds.height())
                   .setScreenDpi(getResources().getConfiguration().densityDpi + " dpi");
            // Refresh rate still needs display object
            DisplayManager dm = getSystemService(DisplayManager.class);
            Display display = (dm != null) ? dm.getDisplay(Display.DEFAULT_DISPLAY) : null;
            if (display != null) {
                builder.setRefreshRate(display.getRefreshRate() + " Hz");
            }
        }

        // Sensors
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sm != null) {
            List<Sensor> list = sm.getSensorList(Sensor.TYPE_ALL);
            List<String> names = new ArrayList<>();
            for (Sensor s : list) names.add(s.getName());
            builder.setSensorList(names);
        }
    }

    /**
     * Collects connectivity state (WiFi, Cellular, BT, NFC).
     * @param builder The DTO builder to populate.
     */
    private void collectConnectivityState(DeviceStateDto.Builder builder) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return;

        Network activeNetwork = cm.getActiveNetwork();
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);

        if (capabilities == null) {
            builder.setWifiStatus(Constants.NET_STATUS_DISCONNECTED);
        } else {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && activeNetwork != null) {
                builder.setWifiStatus(Constants.NET_STATUS_WIFI);
                collectWifiDetails(builder, activeNetwork, capabilities);
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                builder.setWifiStatus(Constants.NET_STATUS_CELLULAR);
            } else {
                builder.setWifiStatus(Constants.NET_STATUS_OTHER);
            }
        }

        collectCellularDetails(builder);
        collectOtherConnections(builder);
        
        builder.setNetworkStatus(getNetworkStatusInternal());
    }

    /**
     * Collects detailed WiFi information and extracts IP addresses.
     * @param builder The DTO builder to populate.
     * @param network The active network object.
     * @param caps The network capabilities.
     */
    private void collectWifiDetails(@NonNull DeviceStateDto.Builder builder, @NonNull Network network, @NonNull NetworkCapabilities caps) {
        TransportInfo transportInfo = caps.getTransportInfo();
        if (transportInfo instanceof WifiInfo info) {
            builder.setWifiRssi(info.getRssi())
                   .setWifiLinkSpeed(info.getLinkSpeed())
                   .setWifiFrequency(info.getFrequency());
        }

        ConnectivityManager cm = getSystemService(ConnectivityManager.class);
        if (cm == null) return;

        LinkProperties lp = cm.getLinkProperties(network);
        if (lp == null) return;

        extractIpAddresses(builder, lp);
    }

    /**
     * Extracts and categorizes IPv4 and IPv6 addresses from link properties.
     * @param builder The DTO builder to populate.
     * @param lp The link properties containing network addresses.
     */
    private void extractIpAddresses(@NonNull DeviceStateDto.Builder builder, @NonNull LinkProperties lp) {
        for (LinkAddress la : lp.getLinkAddresses()) {
            InetAddress addr = la.getAddress();
            if (addr instanceof Inet4Address) {
                builder.setIpV4(addr.getHostAddress());
            } else if (addr instanceof Inet6Address) {
                builder.setIpV6(addr.getHostAddress());
            }
        }
    }

    /**
     * Collects cellular network details (Carrier, SIM status).
     * @param builder The DTO builder to populate.
     */
    private void collectCellularDetails(DeviceStateDto.Builder builder) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            try {
                builder.setCarrierName(tm.getNetworkOperatorName());
                builder.setSimStatus(getSimStateKey(tm.getSimState()));
                // Network type is better determined from capabilities or via TelephonyCallback for API 31+
                // For simplicity and avoiding deprecation of tm.getDataNetworkType()
                builder.setNetworkType(getNetworkTypeFromTelephony(tm));
            } catch (SecurityException e) {
                builder.setCarrierName(Constants.PERMISSION_DENIED);
            }
        }
    }

    /**
     * Collects states for Bluetooth and NFC.
     * @param builder The DTO builder to populate.
     */
    @SuppressLint("MissingPermission")
    private void collectOtherConnections(DeviceStateDto.Builder builder) {
        BluetoothManager bm = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bm != null) {
            BluetoothAdapter ba = bm.getAdapter();
            if (ba != null) {
                builder.setBluetoothEnabled(ba.isEnabled());
            }
        }

        NfcManager nm = (NfcManager) getSystemService(Context.NFC_SERVICE);
        if (nm != null) {
            NfcAdapter na = nm.getDefaultAdapter();
            if (na != null) {
                builder.setNfcEnabled(na.isEnabled());
            }
        }
    }

    /**
     * Maps SIM state code to a constant string key.
     * @param state The SIM state from TelephonyManager.
     * @return Domain constant for SIM status.
     */
    private String getSimStateKey(int state) {
        return switch (state) {
            case TelephonyManager.SIM_STATE_READY -> Constants.SIM_STATUS_READY;
            case TelephonyManager.SIM_STATE_ABSENT -> Constants.SIM_STATUS_ABSENT;
            default -> Constants.SIM_STATUS_UNKNOWN;
        };
    }

    /**
     * Maps TelephonyManager data network type to a localized constant string.
     * @param tm TelephonyManager instance.
     * @return Localized network type string (e.g., "5G", "4G").
     */
    @SuppressLint("MissingPermission")
    private String getNetworkTypeFromTelephony(TelephonyManager tm) {
        try {
            return switch (tm.getDataNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_NR -> Constants.NET_TYPE_5G;
                case TelephonyManager.NETWORK_TYPE_LTE -> Constants.NET_TYPE_4G;
                case TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_HSPAP -> Constants.NET_TYPE_3G;
                default -> Constants.VAL_UNKNOWN;
            };
        } catch (Exception e) {
            return Constants.VAL_UNKNOWN;
        }
    }

    /**
     * Broadcasts the updated device state to all registered AIDL clients.
     * @param dto The device state data to broadcast.
     */
    private void broadcastUpdate(DeviceStateDto dto) {
        int count = mCallbacks.beginBroadcast();
        for (int i = 0; i < count; i++) {
            try {
                mCallbacks.getBroadcastItem(i).onDeviceStateChanged(dto);
            } catch (RemoteException e) {
                Utils.logDebug(Constants.TAG_INFO_SERVICE, "Broadcast failed: " + e.getMessage());
            }
        }
        mCallbacks.finishBroadcast();
    }

    /**
     * Returns the high-level connectivity status.
     * @return Constant string representing connectivity.
     */
    private String getNetworkStatusInternal() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return Constants.NET_STATUS_DISCONNECTED;

        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) return Constants.NET_STATUS_DISCONNECTED;

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
        if (capabilities == null) return Constants.NET_STATUS_DISCONNECTED;

        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return Constants.NET_STATUS_WIFI;
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return Constants.NET_STATUS_CELLULAR;
        return Constants.NET_STATUS_OTHER;
    }

    /**
     * Formats memory info into a displayable string.
     * @param memInfo Memory information structure.
     * @return Formatted string.
     */
    private String formatMemoryInfo(ActivityManager.MemoryInfo memInfo) {
        return String.format(
                Locale.getDefault(),
                getString(R.string.info_total_available),
                memInfo.totalMem / Constants.BYTES_IN_MB,
                memInfo.availMem / Constants.BYTES_IN_MB);
    }

    /**
     * Reads a file from the system and returns its content.
     * @param path The absolute path to the file.
     * @return Content of the file as a string.
     */
    @NonNull
    private String readFile(@NonNull String path) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            sb.append("Failed to read: ").append(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * Retrieves current memory info from ActivityManager.
     * @return MemoryInfo structure.
     */
    @NonNull
    private ActivityManager.MemoryInfo getMemoryInfoInternal() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        if (am != null) {
            am.getMemoryInfo(info);
        }
        return info;
    }

    private static final class BnInfoService extends IInfoService.Stub {
        private final WeakReference<InfoService> mServiceRef;

        public BnInfoService(InfoService service) {
            this.mServiceRef = new WeakReference<>(service);
        }

        @Override
        public DeviceStateDto getDeviceState() {
            InfoService service = mServiceRef.get();
            if (service != null) {
                return service.mCachedState;
            }
            return new DeviceStateDto.Builder().build();
        }

        @Override
        public void registerCallback(IInfoCallback callback) {
            InfoService service = mServiceRef.get();
            if (service != null && callback != null) {
                service.mCallbacks.register(callback);
                try {
                    callback.onDeviceStateChanged(service.mCachedState);
                } catch (RemoteException e) {
                    Utils.logDebug(Constants.TAG_INFO_SERVICE, "Initial push failed: " + e.getMessage());
                }
            }
        }

        @Override
        public void unregisterCallback(IInfoCallback callback) {
            InfoService service = mServiceRef.get();
            if (service != null && callback != null) {
                service.mCallbacks.unregister(callback);
            }
        }
    }
}
