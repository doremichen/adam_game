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

package com.adam.app.mydeviceinfo.ui.network;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.mydeviceinfo.R;
import com.adam.app.mydeviceinfo.application.InfoUseCase;
import com.adam.app.mydeviceinfo.domain.model.DeviceInfo;
import com.adam.app.mydeviceinfo.domain.model.NetworkStatus;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * ViewModel for the network status feature.
 */
@HiltViewModel
public final class NetworkViewModel extends ViewModel {
    private final Context mContext;
    private final InfoUseCase mUseCase;
    private final MediatorLiveData<DeviceInfo> mInfoSource = new MediatorLiveData<>();
    
    private final MutableLiveData<String> mWifiStatus = new MutableLiveData<>("");
    private final MutableLiveData<String> mIpAddress = new MutableLiveData<>("");
    private final MutableLiveData<String> mWifiDetails = new MutableLiveData<>("");
    private final MutableLiveData<String> mCarrierName = new MutableLiveData<>("");
    private final MutableLiveData<String> mNetworkType = new MutableLiveData<>("");
    private final MutableLiveData<String> mSimStatus = new MutableLiveData<>("");
    private final MutableLiveData<String> mBluetoothState = new MutableLiveData<>("");
    private final MutableLiveData<String> mNfcState = new MutableLiveData<>("");

    @Inject
    public NetworkViewModel(@ApplicationContext @NonNull Context context, @NonNull InfoUseCase useCase) {
        this.mContext = context;
        this.mUseCase = useCase;
        setupSource();
    }

    private void setupSource() {
        LiveData<DeviceInfo> stream = mUseCase.execute(InfoUseCase.Action.SUBSCRIBE_INFO, null);
        if (stream != null) {
            mInfoSource.addSource(stream, info -> {
                if (info != null) {
                    updateNetwork(info.getNetworkStatus());
                }
            });
        }
    }

    private void updateNetwork(NetworkStatus status) {
        mWifiStatus.postValue(getLocalizedNetStatus(status.getWifiStatus()));
        mIpAddress.postValue(status.getIpV4().isEmpty() ? status.getIpV6() : status.getIpV4());
        mWifiDetails.postValue(mContext.getString(R.string.label_rssi_speed, 
                status.getWifiRssi(), status.getWifiLinkSpeed()));
        
        mCarrierName.postValue(status.getCarrierName().isEmpty() ? mContext.getString(R.string.net_disconnected) : status.getCarrierName());
        mNetworkType.postValue(status.getNetworkType());
        mSimStatus.postValue(mContext.getString(R.string.label_sim_prefix, getLocalizedSimStatus(status.getSimStatus())));
        
        mBluetoothState.postValue(status.isBluetoothEnabled() ? mContext.getString(R.string.state_on) : mContext.getString(R.string.state_off));
        mNfcState.postValue(status.isNfcEnabled() ? mContext.getString(R.string.state_ready) : mContext.getString(R.string.state_na));
    }

    private String getLocalizedNetStatus(String status) {
        switch (status) {
            case "wifi": return mContext.getString(R.string.net_wifi);
            case "cellular": return mContext.getString(R.string.net_cellular);
            case "disconnected": return mContext.getString(R.string.net_disconnected);
            default: return mContext.getString(R.string.net_other);
        }
    }

    private String getLocalizedSimStatus(String status) {
        switch (status) {
            case "ready": return mContext.getString(R.string.sim_ready);
            case "absent": return mContext.getString(R.string.sim_absent);
            default: return mContext.getString(R.string.sim_unknown);
        }
    }

    @NonNull public LiveData<String> getWifiStatus() { return mWifiStatus; }
    @NonNull public LiveData<String> getIpAddress() { return mIpAddress; }
    @NonNull public LiveData<String> getWifiDetails() { return mWifiDetails; }
    @NonNull public LiveData<String> getCarrierName() { return mCarrierName; }
    @NonNull public LiveData<String> getNetworkType() { return mNetworkType; }
    @NonNull public LiveData<String> getSimStatus() { return mSimStatus; }
    @NonNull public LiveData<String> getBluetoothState() { return mBluetoothState; }
    @NonNull public LiveData<String> getNfcState() { return mNfcState; }
    @NonNull public LiveData<DeviceInfo> getInfoSource() { return mInfoSource; }

    public void refreshData() {
        // Auto-updated
    }
}
