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

package com.adam.app.mydeviceinfo.infrastructure;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.mydeviceinfo.IInfoCallback;
import com.adam.app.mydeviceinfo.IInfoService;
import com.adam.app.mydeviceinfo.R;
import com.adam.app.mydeviceinfo.common.Constants;
import com.adam.app.mydeviceinfo.common.Utils;
import com.adam.app.mydeviceinfo.domain.model.DashboardData;
import com.adam.app.mydeviceinfo.domain.model.DeviceInfo;
import com.adam.app.mydeviceinfo.domain.model.NetworkStatus;
import com.adam.app.mydeviceinfo.domain.model.SystemSpecs;
import com.adam.app.mydeviceinfo.domain.repository.IDeviceRepository;
import com.adam.app.mydeviceinfo.infrastructure.dto.DeviceStateDto;
import com.adam.app.mydeviceinfo.infrastructure.service.InfoService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * Infrastructure implementation of IDeviceRepository.
 * Delegates data fetching and observing to a background service via AIDL.
 */
@Singleton
public final class InfoRepositoryImpl implements IDeviceRepository {
    private final Context mContext;
    private IInfoService mService;
    private final MutableLiveData<DeviceInfo> mInfoLiveData = new MutableLiveData<>();

    private final IInfoCallback mCallback = new IInfoCallback.Stub() {
        @Override
        public void onDeviceStateChanged(DeviceStateDto dto) {
            mInfoLiveData.postValue(mapDtoToEntity(dto));
        }
    };

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Utils.logDebug(Constants.TAG_INFO_REPOSITORY, "Service connected");
            mService = IInfoService.Stub.asInterface(service);
            registerCallback();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Utils.logDebug(Constants.TAG_INFO_REPOSITORY, "Service disconnected");
            mService = null;
        }
    };

    /**
     * Constructs the repository and initiates service binding.
     * @param context Application context.
     */
    @Inject
    public InfoRepositoryImpl(@ApplicationContext @NonNull Context context) {
        this.mContext = context;
        bindToService();
    }

    /**
     * Binds to the background InfoService.
     */
    private void bindToService() {
        Intent intent = new Intent(mContext, InfoService.class);
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Registers the state change callback with the service.
     */
    private void registerCallback() {
        if (mService != null) {
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
                Utils.logDebug(Constants.TAG_INFO_REPOSITORY, "Failed to register callback: " + e.getMessage());
            }
        }
    }

    /**
     * Triggers a hardware test (e.g., vibration).
     * @param type The test type identifier.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void runHardwareTest(int type) {
        if (type == Constants.TEST_TYPE_VIBRATION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibratorManager vm = (VibratorManager) mContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                if (vm != null) {
                    vm.getDefaultVibrator().vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            } else {
                Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                if (v != null) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            }
        }
    }

    /**
     * Returns a stream of real-time device information.
     * @return LiveData containing device info updates.
     */
    @Override
    @NonNull
    public LiveData<DeviceInfo> getDeviceInfoStream() {
        return mInfoLiveData;
    }

    /**
     * Fetches the current snapshot of device information.
     * @return DeviceInfo entity.
     */
    @Override
    @NonNull
    public DeviceInfo fetchDeviceInfo() {
        if (mService == null) {
            Utils.logDebug(Constants.TAG_INFO_REPOSITORY, "Service not bound, attempting to bind");
            bindToService();
            String errorMsg = mContext.getString(R.string.error_service_not_ready);
            return createErrorDeviceInfo(errorMsg);
        }

        try {
            DeviceStateDto dto = mService.getDeviceState();
            return mapDtoToEntity(dto);
        } catch (RemoteException e) {
            Utils.logDebug(Constants.TAG_INFO_REPOSITORY, "RemoteException: " + e.getMessage());
            return createErrorDeviceInfo(mContext.getString(R.string.error_fetching_cpu_info));
        }
    }

    /**
     * Maps a Data Transfer Object to a Domain Entity.
     * @param dto The source DTO from AIDL.
     * @return The domain DeviceInfo entity.
     */
    @NonNull
    private DeviceInfo mapDtoToEntity(@NonNull DeviceStateDto dto) {
        DashboardData dashboardData = new DashboardData.Builder()
                .setRamUsed(dto.getRamUsed())
                .setRamTotal(dto.getRamTotal())
                .setStorageUsed(dto.getStorageUsed())
                .setStorageTotal(dto.getStorageTotal())
                .setBatteryPct(dto.getBatteryPct())
                .setBatteryTemp(dto.getBatteryTemp())
                .setBatteryVolt(dto.getBatteryVolt())
                .setBatteryStatus(dto.getBatteryStatus())
                .setBatteryHealth(dto.getBatteryHealth())
                .setUptime(dto.getUptime())
                .setCpuInfo(dto.getCpuInfo())
                .setMemoryInfo(dto.getMemoryInfo())
                .build();

        SystemSpecs systemSpecs = new SystemSpecs.Builder()
                .setManufacturer(dto.getManufacturer())
                .setBrand(dto.getBrand())
                .setModel(dto.getModel())
                .setCodename(dto.getCodename())
                .setScreenRes(dto.getScreenRes())
                .setScreenDpi(dto.getScreenDpi())
                .setRefreshRate(dto.getRefreshRate())
                .setOsVersion(dto.getOsVersion())
                .setSdkLevel(dto.getSdkLevel())
                .setSecurityPatch(dto.getSecurityPatch())
                .setKernelVersion(dto.getKernelVersion())
                .setFingerprint(dto.getFingerprint())
                .setCpuAbi(dto.getCpuAbi())
                .setCpuCores(dto.getCpuCores())
                .setSensorList(dto.getSensorList())
                .build();

        NetworkStatus networkStatus = new NetworkStatus.Builder()
                .setWifiStatus(dto.getWifiStatus())
                .setIpV4(dto.getIpV4())
                .setIpV6(dto.getIpV6())
                .setWifiRssi(dto.getWifiRssi())
                .setWifiLinkSpeed(dto.getWifiLinkSpeed())
                .setWifiFrequency(dto.getWifiFrequency())
                .setCarrierName(dto.getCarrierName())
                .setNetworkType(dto.getNetworkType())
                .setSimStatus(dto.getSimStatus())
                .setBluetoothEnabled(dto.isBluetoothEnabled())
                .setNfcEnabled(dto.isNfcEnabled())
                .setStatus(dto.getNetworkStatus())
                .build();

        return new DeviceInfo(dashboardData, systemSpecs, networkStatus);
    }

    /**
     * Creates a fallback DeviceInfo entity containing an error message.
     * @param message The error message to display.
     * @return DeviceInfo with error states.
     */
    @NonNull
    private DeviceInfo createErrorDeviceInfo(@NonNull String message) {
        DashboardData dashboard = new DashboardData.Builder()
                .setBatteryStatus(message)
                .setBatteryHealth(message)
                .setCpuInfo(message)
                .setMemoryInfo(message)
                .build();

        SystemSpecs specs = new SystemSpecs.Builder()
                .setManufacturer(message)
                .setBrand(message)
                .setModel(message)
                .setCodename(message)
                .setScreenRes(message)
                .setScreenDpi(message)
                .setRefreshRate(message)
                .setOsVersion(message)
                .setSecurityPatch(message)
                .setKernelVersion(message)
                .setFingerprint(message)
                .setCpuAbi(message)
                .setSensorList(new ArrayList<>())
                .build();

        NetworkStatus net = new NetworkStatus.Builder()
                .setWifiStatus(message)
                .setIpV4(message)
                .setIpV6(message)
                .setCarrierName(message)
                .setNetworkType(message)
                .setSimStatus(message)
                .setStatus(message)
                .build();

        return new DeviceInfo(dashboard, specs, net);
    }

    @Override
    public boolean exportDeviceInfo(@NonNull DeviceInfo deviceInfo) {
        String content = generateReportText(deviceInfo);
        return saveReportToFile(content);
    }

    /**
     * Generates a complete report text from device information.
     * @param deviceInfo The entity containing all device details.
     * @return Formatted report string.
     */
    @NonNull
    private String generateReportText(@NonNull DeviceInfo deviceInfo) {
        StringBuilder sb = new StringBuilder();
        appendDashboardSection(sb, deviceInfo.getDashboardData());
        appendSystemSection(sb, deviceInfo.getSystemSpecs());
        appendNetworkSection(sb, deviceInfo.getNetworkStatus());
        return sb.toString();
    }

    /**
     * Appends the dashboard metrics section to the report.
     * @param sb The builder to append to.
     * @param dashboard Dashboard data entity.
     */
    private void appendDashboardSection(@NonNull StringBuilder sb, @NonNull DashboardData dashboard) {
        sb.append(mContext.getString(R.string.export_header_dashboard)).append("\n")
                .append(mContext.getString(R.string.export_label_ram,
                        dashboard.getRamUsed() / Constants.BYTES_IN_MB,
                        dashboard.getRamTotal() / Constants.BYTES_IN_MB)).append("\n")
                .append(mContext.getString(R.string.export_label_storage,
                        dashboard.getStorageUsed() / Constants.BYTES_IN_MB,
                        dashboard.getStorageTotal() / Constants.BYTES_IN_MB)).append("\n")
                .append(mContext.getString(R.string.export_label_battery,
                        dashboard.getBatteryPct(), dashboard.getBatteryStatus())).append("\n")
                .append(mContext.getString(R.string.export_label_uptime,
                        dashboard.getUptime() / 1000)).append("\n\n");
    }

    /**
     * Appends the system specifications section to the report.
     * @param sb The builder to append to.
     * @param system System specs entity.
     */
    private void appendSystemSection(@NonNull StringBuilder sb, @NonNull SystemSpecs system) {
        sb.append(mContext.getString(R.string.export_header_system)).append("\n")
                .append(mContext.getString(R.string.label_manufacturer)).append(": ").append(system.getManufacturer()).append("\n")
                .append(mContext.getString(R.string.label_model)).append(": ").append(system.getModel()).append("\n")
                .append(mContext.getString(R.string.label_os_version)).append(": ").append(system.getOsVersion()).append(" (API ").append(system.getSdkLevel()).append(")\n")
                .append(mContext.getString(R.string.label_cpu)).append(": ").append(system.getCpuAbi()).append(" (").append(system.getCpuCores()).append(" Cores)\n\n");
    }

    /**
     * Appends the network status section to the report.
     * @param sb The builder to append to.
     * @param network Network status entity.
     */
    private void appendNetworkSection(@NonNull StringBuilder sb, @NonNull NetworkStatus network) {
        sb.append(mContext.getString(R.string.export_header_network)).append("\n")
                .append(mContext.getString(R.string.label_wifi)).append(": ").append(network.getWifiStatus()).append("\n")
                .append(mContext.getString(R.string.label_ip_prefix, network.getIpV4())).append("\n");
    }

    /**
     * Saves the provided content to a text file in the Downloads directory using MediaStore.
     * @param content The text to save.
     * @return true if saved successfully, false otherwise.
     */
    private boolean saveReportToFile(@NonNull String content) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, Constants.EXPORT_FILE_NAME);
        values.put(MediaStore.Downloads.MIME_TYPE, "text/plain");
        values.put(MediaStore.Downloads.IS_PENDING, 1);

        ContentResolver resolver = mContext.getContentResolver();
        Uri collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri fileUri = resolver.insert(collection, values);

        if (fileUri == null) {
            return false;
        }

        try (OutputStream os = resolver.openOutputStream(fileUri)) {
            if (os != null) {
                os.write(content.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
            values.clear();
            values.put(MediaStore.Downloads.IS_PENDING, 0);
            resolver.update(fileUri, values, null, null);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
