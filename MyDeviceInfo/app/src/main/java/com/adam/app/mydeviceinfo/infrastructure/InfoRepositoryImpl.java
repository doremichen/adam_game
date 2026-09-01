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

    @Inject
    public InfoRepositoryImpl(@ApplicationContext @NonNull Context context) {
        this.mContext = context;
        bindToService();
    }

    private void bindToService() {
        Intent intent = new Intent(mContext, InfoService.class);
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void registerCallback() {
        if (mService != null) {
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
                Utils.logDebug(Constants.TAG_INFO_REPOSITORY, "Failed to register callback: " + e.getMessage());
            }
        }
    }

    @Override
    public void runHardwareTest(int type) {
        if (type == 1) { // Vibration
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

    @Override
    @NonNull
    public LiveData<DeviceInfo> getDeviceInfoStream() {
        return mInfoLiveData;
    }

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

    private DeviceInfo mapDtoToEntity(DeviceStateDto dto) {
        DashboardData dashboardData = new DashboardData(
                dto.getRamUsed(), dto.getRamTotal(),
                dto.getStorageUsed(), dto.getStorageTotal(),
                dto.getBatteryPct(), dto.getBatteryTemp(), dto.getBatteryVolt(),
                dto.getBatteryStatus(), dto.getBatteryHealth(),
                dto.getUptime(), dto.getCpuInfo(), dto.getMemoryInfo());

        SystemSpecs systemSpecs = new SystemSpecs(
                dto.getManufacturer(), dto.getBrand(), dto.getModel(),
                dto.getCodename(), dto.getScreenRes(), dto.getScreenDpi(),
                dto.getRefreshRate(), dto.getOsVersion(), dto.getSdkLevel(),
                dto.getSecurityPatch(), dto.getKernelVersion(),
                dto.getFingerprint(), dto.getCpuAbi(), dto.getCpuCores(),
                dto.getSensorList());

        NetworkStatus networkStatus = new NetworkStatus(
                dto.getWifiStatus(), dto.getIpV4(), dto.getIpV6(),
                dto.getWifiRssi(), dto.getWifiLinkSpeed(), dto.getWifiFrequency(),
                dto.getCarrierName(), dto.getNetworkType(), dto.getSimStatus(),
                dto.isBluetoothEnabled(), dto.isNfcEnabled(),
                dto.getNetworkStatus());

        return new DeviceInfo(dashboardData, systemSpecs, networkStatus);
    }

    private DeviceInfo createErrorDeviceInfo(String message) {
        return new DeviceInfo(
                new DashboardData(0, 0, 0, 0, 0, 0, 0, message, message, 0, message, message),
                new SystemSpecs(message, message, message, message, message, message, message, message, 0, message, message, message, message, 0, new ArrayList<>()),
                new NetworkStatus(message, message, message, 0, 0, 0, message, message, message, false, false, message));
    }

    @Override
    public boolean exportDeviceInfo(@NonNull DeviceInfo deviceInfo) {
        StringBuilder sb = new StringBuilder();
        DashboardData dashboard = deviceInfo.getDashboardData();
        SystemSpecs system = deviceInfo.getSystemSpecs();
        NetworkStatus network = deviceInfo.getNetworkStatus();

        sb.append("--- Dashboard ---\n")
                .append("RAM: ").append(dashboard.getRamUsed() / Constants.BYTES_IN_MB).append(" / ").append(dashboard.getRamTotal() / Constants.BYTES_IN_MB).append(" MB\n")
                .append("Storage: ").append(dashboard.getStorageUsed() / Constants.BYTES_IN_MB).append(" / ").append(dashboard.getStorageTotal() / Constants.BYTES_IN_MB).append(" MB\n")
                .append("Battery: ").append(dashboard.getBatteryPct()).append("% (").append(dashboard.getBatteryStatus()).append(")\n")
                .append("Uptime: ").append(dashboard.getUptime() / 1000).append("s\n\n")
                .append("--- System ---\n")
                .append("Manufacturer: ").append(system.getManufacturer()).append("\n")
                .append("Model: ").append(system.getModel()).append("\n")
                .append("OS: ").append(system.getOsVersion()).append(" (API ").append(system.getSdkLevel()).append(")\n")
                .append("CPU: ").append(system.getCpuAbi()).append(" (").append(system.getCpuCores()).append(" Cores)\n\n")
                .append("--- Network ---\n")
                .append("WiFi Status: ").append(network.getWifiStatus()).append("\n")
                .append("IP: ").append(network.getIpV4()).append("\n");

        String content = sb.toString();

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
