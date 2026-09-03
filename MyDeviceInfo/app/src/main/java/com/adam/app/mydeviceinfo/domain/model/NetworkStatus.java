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

package com.adam.app.mydeviceinfo.domain.model;

import androidx.annotation.NonNull;

/**
 * Domain model for network status.
 */
public final class NetworkStatus {
    private final String mWifiStatus;
    private final String mIpV4;
    private final String mIpV6;
    private final int mWifiRssi;
    private final int mWifiLinkSpeed;
    private final int mWifiFrequency;
    private final String mCarrierName;
    private final String mNetworkType;
    private final String mSimStatus;
    private final boolean mBluetoothEnabled;
    private final boolean mNfcEnabled;

    // Legacy support
    private final String mStatus;

    private NetworkStatus(Builder builder) {
        this.mWifiStatus = builder.mWifiStatus;
        this.mIpV4 = builder.mIpV4;
        this.mIpV6 = builder.mIpV6;
        this.mWifiRssi = builder.mWifiRssi;
        this.mWifiLinkSpeed = builder.mWifiLinkSpeed;
        this.mWifiFrequency = builder.mWifiFrequency;
        this.mCarrierName = builder.mCarrierName;
        this.mNetworkType = builder.mNetworkType;
        this.mSimStatus = builder.mSimStatus;
        this.mBluetoothEnabled = builder.mBluetoothEnabled;
        this.mNfcEnabled = builder.mNfcEnabled;
        this.mStatus = builder.mStatus;
    }

    public static final class Builder {
        private String mWifiStatus = "";
        private String mIpV4 = "";
        private String mIpV6 = "";
        private int mWifiRssi;
        private int mWifiLinkSpeed;
        private int mWifiFrequency;
        private String mCarrierName = "";
        private String mNetworkType = "";
        private String mSimStatus = "";
        private boolean mBluetoothEnabled;
        private boolean mNfcEnabled;
        private String mStatus = "";

        public Builder setWifiStatus(@NonNull String wifiStatus) { this.mWifiStatus = wifiStatus; return this; }
        public Builder setIpV4(@NonNull String ipV4) { this.mIpV4 = ipV4; return this; }
        public Builder setIpV6(@NonNull String ipV6) { this.mIpV6 = ipV6; return this; }
        public Builder setWifiRssi(int wifiRssi) { this.mWifiRssi = wifiRssi; return this; }
        public Builder setWifiLinkSpeed(int wifiLinkSpeed) { this.mWifiLinkSpeed = wifiLinkSpeed; return this; }
        public Builder setWifiFrequency(int wifiFrequency) { this.mWifiFrequency = wifiFrequency; return this; }
        public Builder setCarrierName(@NonNull String carrierName) { this.mCarrierName = carrierName; return this; }
        public Builder setNetworkType(@NonNull String networkType) { this.mNetworkType = networkType; return this; }
        public Builder setSimStatus(@NonNull String simStatus) { this.mSimStatus = simStatus; return this; }
        public Builder setBluetoothEnabled(boolean bluetoothEnabled) { this.mBluetoothEnabled = bluetoothEnabled; return this; }
        public Builder setNfcEnabled(boolean nfcEnabled) { this.mNfcEnabled = nfcEnabled; return this; }
        public Builder setStatus(@NonNull String status) { this.mStatus = status; return this; }

        /**
         * Finalizes the building of NetworkStatus.
         * @return A new NetworkStatus instance.
         */
        public NetworkStatus build() {
            return new NetworkStatus(this);
        }
    }

    @NonNull public String getWifiStatus() { return mWifiStatus; }
    @NonNull public String getIpV4() { return mIpV4; }
    @NonNull public String getIpV6() { return mIpV6; }
    public int getWifiRssi() { return mWifiRssi; }
    public int getWifiLinkSpeed() { return mWifiLinkSpeed; }
    public int getWifiFrequency() { return mWifiFrequency; }
    @NonNull public String getCarrierName() { return mCarrierName; }
    @NonNull public String getNetworkType() { return mNetworkType; }
    @NonNull public String getSimStatus() { return mSimStatus; }
    public boolean isBluetoothEnabled() { return mBluetoothEnabled; }
    public boolean isNfcEnabled() { return mNfcEnabled; }
    @NonNull public String getStatus() { return mStatus; }
}
