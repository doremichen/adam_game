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

package com.adam.app.mydeviceinfo.ui.dashboard;

import android.content.Context;
import android.os.BatteryManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.mydeviceinfo.R;
import com.adam.app.mydeviceinfo.application.InfoUseCase;
import com.adam.app.mydeviceinfo.domain.model.DashboardData;
import com.adam.app.mydeviceinfo.domain.model.DeviceInfo;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * ViewModel for the dashboard feature.
 */
@HiltViewModel
public final class DashboardViewModel extends ViewModel {
    private final Context mContext;
    private final InfoUseCase mUseCase;
    private final MediatorLiveData<DeviceInfo> mInfoSource = new MediatorLiveData<>();
    
    private final MutableLiveData<String> mRamUsage = new MutableLiveData<>("");
    private final MutableLiveData<Integer> mRamPct = new MutableLiveData<>(0);
    private final MutableLiveData<String> mStorageUsage = new MutableLiveData<>("");
    private final MutableLiveData<Integer> mStoragePct = new MutableLiveData<>(0);
    private final MutableLiveData<String> mBatteryPct = new MutableLiveData<>("0");
    private final MutableLiveData<String> mBatteryStatus = new MutableLiveData<>("");
    private final MutableLiveData<String> mBatteryHealth = new MutableLiveData<>("");
    private final MutableLiveData<String> mBatteryDetails = new MutableLiveData<>("");
    private final MutableLiveData<String> mUptime = new MutableLiveData<>("");

    @Inject
    public DashboardViewModel(@ApplicationContext @NonNull Context context, @NonNull InfoUseCase useCase) {
        this.mContext = context;
        this.mUseCase = useCase;
        setupSource();
    }

    private void setupSource() {
        LiveData<DeviceInfo> stream = mUseCase.execute(InfoUseCase.Action.SUBSCRIBE_INFO, null);
        if (stream != null) {
            mInfoSource.addSource(stream, info -> {
                if (info != null) {
                    updateDashboard(info.getDashboardData());
                }
            });
        }
    }

    private void updateDashboard(DashboardData data) {
        // RAM
        float ramUsedGb = data.getRamUsed() / (1024.0f * 1024.0f * 1024.0f);
        float ramTotalGb = data.getRamTotal() / (1024.0f * 1024.0f * 1024.0f);
        mRamUsage.postValue(String.format(Locale.getDefault(), "%.1f / %.1f GB", ramUsedGb, ramTotalGb));
        mRamPct.postValue((int) (data.getRamUsed() * 100 / (float) Math.max(1, data.getRamTotal())));

        // Storage
        float storeUsedGb = data.getStorageUsed() / (1024.0f * 1024.0f * 1024.0f);
        float storeTotalGb = data.getStorageTotal() / (1024.0f * 1024.0f * 1024.0f);
        mStorageUsage.postValue(String.format(Locale.getDefault(), "%.1f / %.1f GB", storeUsedGb, storeTotalGb));
        mStoragePct.postValue((int) (data.getStorageUsed() * 100 / (float) Math.max(1, data.getStorageTotal())));

        // Battery
        mBatteryPct.postValue(String.valueOf(data.getBatteryPct()));
        mBatteryStatus.postValue(getLocalizedBatteryStatus(data.getBatteryStatus()));
        mBatteryHealth.postValue(mContext.getString(R.string.label_health, getLocalizedBatteryHealth(data.getBatteryHealth())));
        mBatteryDetails.postValue(mContext.getString(R.string.label_temp_volt, 
                data.getBatteryTemp(), data.getBatteryVolt()));

        // Uptime
        long seconds = data.getUptime() / 1000;
        long days = seconds / (24 * 3600);
        long hours = (seconds % (24 * 3600)) / 3600;
        long minutes = (seconds % 3600) / 60;
        mUptime.postValue(String.format(Locale.getDefault(), "%dd %02dh %02dm", days, hours, minutes));
    }

    private String getLocalizedBatteryStatus(String status) {
        try {
            int s = Integer.parseInt(status);
            switch (s) {
                case BatteryManager.BATTERY_STATUS_CHARGING: return mContext.getString(R.string.battery_status_charging);
                case BatteryManager.BATTERY_STATUS_DISCHARGING: return mContext.getString(R.string.battery_status_discharging);
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING: return mContext.getString(R.string.battery_status_not_charging);
                case BatteryManager.BATTERY_STATUS_FULL: return mContext.getString(R.string.battery_status_full);
                default: return mContext.getString(R.string.battery_status_unknown);
            }
        } catch (NumberFormatException e) {
            return mContext.getString(R.string.battery_status_unknown);
        }
    }

    private String getLocalizedBatteryHealth(String health) {
        try {
            int h = Integer.parseInt(health);
            switch (h) {
                case BatteryManager.BATTERY_HEALTH_GOOD: return mContext.getString(R.string.battery_health_good);
                case BatteryManager.BATTERY_HEALTH_OVERHEAT: return mContext.getString(R.string.battery_health_overheat);
                case BatteryManager.BATTERY_HEALTH_DEAD: return mContext.getString(R.string.battery_health_dead);
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE: return mContext.getString(R.string.battery_health_over_voltage);
                case BatteryManager.BATTERY_HEALTH_COLD: return mContext.getString(R.string.battery_health_cold);
                default: return mContext.getString(R.string.battery_health_unknown);
            }
        } catch (NumberFormatException e) {
            return mContext.getString(R.string.battery_health_unknown);
        }
    }

    @NonNull public LiveData<String> getRamUsage() { return mRamUsage; }
    @NonNull public LiveData<Integer> getRamPct() { return mRamPct; }
    @NonNull public LiveData<String> getStorageUsage() { return mStorageUsage; }
    @NonNull public LiveData<Integer> getStoragePct() { return mStoragePct; }
    @NonNull public LiveData<String> getBatteryPct() { return mBatteryPct; }
    @NonNull public LiveData<String> getBatteryStatus() { return mBatteryStatus; }
    @NonNull public LiveData<String> getBatteryHealth() { return mBatteryHealth; }
    @NonNull public LiveData<String> getBatteryDetails() { return mBatteryDetails; }
    @NonNull public LiveData<String> getUptime() { return mUptime; }
    @NonNull public LiveData<DeviceInfo> getInfoSource() { return mInfoSource; }

    public void refreshData() {
        // Auto-updated
    }
}
