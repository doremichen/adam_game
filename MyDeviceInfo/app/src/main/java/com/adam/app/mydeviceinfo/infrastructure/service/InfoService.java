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
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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
import android.util.DisplayMetrics;
import android.view.WindowManager;

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

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public IBinder onBind(Intent intent) {
        mActiveClients++;
        Utils.logDebug(Constants.TAG_INFO_SERVICE, Constants.MSG_ON_BIND_PREFIX + mActiveClients);
        if (mActiveClients == 1) {
            mScheduler.start(this::updateDeviceInfo);
        }
        return new BnInfoService(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mActiveClients--;
        Utils.logDebug(Constants.TAG_INFO_SERVICE, Constants.MSG_ON_UNBIND_PREFIX + mActiveClients);
        if (mActiveClients == 0) {
            mScheduler.stop();
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mScheduler.stop();
        mCallbacks.kill();
        unregisterReceiver(mBatteryReceiver);
        super.onDestroy();
    }

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
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            builder.setScreenRes(metrics.widthPixels + "x" + metrics.heightPixels)
                   .setScreenDpi(metrics.densityDpi + " dpi")
                   .setRefreshRate(wm.getDefaultDisplay().getRefreshRate() + " Hz");
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

    private void collectConnectivityState(DeviceStateDto.Builder builder) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return;

        Network activeNetwork = cm.getActiveNetwork();
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);

        if (capabilities == null) {
            builder.setWifiStatus("disconnected");
        } else {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                builder.setWifiStatus("wifi");
                collectWifiDetails(builder);
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                builder.setWifiStatus("cellular");
            } else {
                builder.setWifiStatus("other");
            }
        }

        collectCellularDetails(builder);
        collectOtherConnections(builder);
        
        builder.setNetworkStatus(getNetworkStatusInternal());
    }

    private void collectWifiDetails(DeviceStateDto.Builder builder) {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm != null) {
            @SuppressLint("HardwareIds") WifiInfo info = wm.getConnectionInfo();
            builder.setWifiRssi(info.getRssi())
                   .setWifiLinkSpeed(info.getLinkSpeed())
                   .setWifiFrequency(info.getFrequency());
            // IP
            int ip = info.getIpAddress();
            String ipStr = String.format(Locale.getDefault(), "%d.%d.%d.%d",
                    (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
            builder.setIpV4(ipStr);
        }
    }

    private void collectCellularDetails(DeviceStateDto.Builder builder) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            try {
                builder.setCarrierName(tm.getNetworkOperatorName());
                builder.setSimStatus(getSimStateKey(tm.getSimState()));
                builder.setNetworkType(getNetworkTypeString(tm.getDataNetworkType()));
            } catch (SecurityException e) {
                builder.setCarrierName("Permission Denied");
            }
        }
    }

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

    private String getSimStateKey(int state) {
        switch (state) {
            case TelephonyManager.SIM_STATE_READY: return "ready";
            case TelephonyManager.SIM_STATE_ABSENT: return "absent";
            default: return "unknown";
        }
    }

    private String getNetworkTypeString(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_NR: return "5G";
            case TelephonyManager.NETWORK_TYPE_LTE: return "4G";
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSPAP: return "3G";
            default: return "Unknown";
        }
    }

    private void broadcastUpdate(DeviceStateDto dto) {
        int n = mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                mCallbacks.getBroadcastItem(i).onDeviceStateChanged(dto);
            } catch (RemoteException e) {
                Utils.logDebug(Constants.TAG_INFO_SERVICE, "Broadcast failed: " + e.getMessage());
            }
        }
        mCallbacks.finishBroadcast();
    }

    private String getNetworkStatusInternal() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return "disconnected";

        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) return "disconnected";

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
        if (capabilities == null) return "disconnected";

        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return "wifi";
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return "cellular";
        return "other";
    }

    private String formatMemoryInfo(ActivityManager.MemoryInfo memInfo) {
        return String.format(
                Locale.getDefault(),
                getString(R.string.info_total_available),
                memInfo.totalMem / Constants.BYTES_IN_MB,
                memInfo.availMem / Constants.BYTES_IN_MB);
    }

    private String readFile(String path) {
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
