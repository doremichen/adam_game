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
import androidx.lifecycle.ViewModel;

import com.adam.app.mydeviceinfo.R;
import com.adam.app.mydeviceinfo.application.InfoUseCase;
import com.adam.app.mydeviceinfo.common.Constants;
import com.adam.app.mydeviceinfo.domain.model.ConnectivityState;
import com.adam.app.mydeviceinfo.domain.model.DeviceInfo;
import com.adam.app.mydeviceinfo.domain.model.NetworkStatus;
import com.adam.app.mydeviceinfo.domain.model.SimState;

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
    private final MediatorLiveData<NetworkUiState> mUiState = new MediatorLiveData<>();

    /**
     * UI State for the network screen.
     */
    public static final class NetworkUiState {
        private final String mWifiStatus;
        private final String mIpAddress;
        private final String mWifiDetails;
        private final String mCarrierName;
        private final String mNetworkType;
        private final String mSimStatus;
        private final String mBluetoothState;
        private final String mNfcState;

        /**
         * Private constructor using the Builder.
         * @param builder The state builder.
         */
        private NetworkUiState(Builder builder) {
            this.mWifiStatus = builder.mWifiStatus;
            this.mIpAddress = builder.mIpAddress;
            this.mWifiDetails = builder.mWifiDetails;
            this.mCarrierName = builder.mCarrierName;
            this.mNetworkType = builder.mNetworkType;
            this.mSimStatus = builder.mSimStatus;
            this.mBluetoothState = builder.mBluetoothState;
            this.mNfcState = builder.mNfcState;
        }

        @NonNull public String getWifiStatus() { return mWifiStatus; }
        @NonNull public String getIpAddress() { return mIpAddress; }
        @NonNull public String getWifiDetails() { return mWifiDetails; }
        @NonNull public String getCarrierName() { return mCarrierName; }
        @NonNull public String getNetworkType() { return mNetworkType; }
        @NonNull public String getSimStatus() { return mSimStatus; }
        @NonNull public String getBluetoothState() { return mBluetoothState; }
        @NonNull public String getNfcState() { return mNfcState; }

        /**
         * Builder for NetworkUiState.
         */
        public static final class Builder {
            private String mWifiStatus = Constants.EMPTY_STRING;
            private String mIpAddress = Constants.EMPTY_STRING;
            private String mWifiDetails = Constants.EMPTY_STRING;
            private String mCarrierName = Constants.EMPTY_STRING;
            private String mNetworkType = Constants.EMPTY_STRING;
            private String mSimStatus = Constants.EMPTY_STRING;
            private String mBluetoothState = Constants.EMPTY_STRING;
            private String mNfcState = Constants.EMPTY_STRING;

            @NonNull public Builder wifiStatus(String status) { mWifiStatus = status; return this; }
            @NonNull public Builder ipAddress(String ip) { mIpAddress = ip; return this; }
            @NonNull public Builder wifiDetails(String details) { mWifiDetails = details; return this; }
            @NonNull public Builder carrierName(String name) { mCarrierName = name; return this; }
            @NonNull public Builder networkType(String type) { mNetworkType = type; return this; }
            @NonNull public Builder simStatus(String status) { mSimStatus = status; return this; }
            @NonNull public Builder bluetoothState(String state) { mBluetoothState = state; return this; }
            @NonNull public Builder nfcState(String state) { mNfcState = state; return this; }

            /**
             * Finalizes the building of NetworkUiState.
             * @return A new NetworkUiState instance.
             */
            @NonNull public NetworkUiState build() { return new NetworkUiState(this); }
        }
    }

    /**
     * Constructs the NetworkViewModel.
     * @param context Application context.
     * @param useCase The use case for device information.
     */
    @Inject
    public NetworkViewModel(@ApplicationContext @NonNull Context context, @NonNull InfoUseCase useCase) {
        this.mContext = context;
        this.mUseCase = useCase;
        setupSource();
    }

    /**
     * Initializes the reactive source for network information.
     */
    private void setupSource() {
        LiveData<DeviceInfo> stream = mUseCase.execute(InfoUseCase.Action.SUBSCRIBE_INFO, null);
        if (stream != null) {
            mUiState.addSource(stream, info -> {
                if (info != null) {
                    mUiState.setValue(mapToUiState(info.getNetworkStatus()));
                }
            });
        }
    }

    /**
     * Maps the domain NetworkStatus entity to the UI State object.
     * @param status The network status from the domain layer.
     * @return A new instance of NetworkUiState.
     */
    @NonNull
    private NetworkUiState mapToUiState(@NonNull NetworkStatus status) {
        String ip = status.getIpV4().isEmpty() ? status.getIpV6() : status.getIpV4();
        String wifiDetails = mContext.getString(R.string.label_rssi_speed,
                status.getWifiRssi(), status.getWifiLinkSpeed());
        String carrier = status.getCarrierName().isEmpty() ?
                mContext.getString(R.string.net_disconnected) : status.getCarrierName();
        String sim = mContext.getString(R.string.label_sim_prefix,
                getLocalizedSimStatus(status.getSimStatus()));
        String bluetooth = status.isBluetoothEnabled() ?
                mContext.getString(R.string.state_on) : mContext.getString(R.string.state_off);
        String nfc = status.isNfcEnabled() ?
                mContext.getString(R.string.state_ready) : mContext.getString(R.string.state_na);

        return new NetworkUiState.Builder()
                .wifiStatus(getLocalizedNetStatus(status.getWifiStatus()))
                .ipAddress(ip)
                .wifiDetails(wifiDetails)
                .carrierName(carrier)
                .networkType(status.getNetworkType())
                .simStatus(sim)
                .bluetoothState(bluetooth)
                .nfcState(nfc)
                .build();
    }

    /**
     * Returns the localized string for a connectivity state key.
     * @param status The status key from Constants.
     * @return Localized string from resources.
     */
    @NonNull
    private String getLocalizedNetStatus(@NonNull String status) {
        return mContext.getString(ConnectivityState.fromKey(status).getResId());
    }

    /**
     * Returns the localized string for a SIM state key.
     * @param status The status key from Constants.
     * @return Localized string from resources.
     */
    @NonNull
    private String getLocalizedSimStatus(@NonNull String status) {
        return mContext.getString(SimState.fromKey(status).getResId());
    }

    @NonNull public LiveData<NetworkUiState> getUiState() { return mUiState; }

}
