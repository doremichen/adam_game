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

    public NetworkStatus(@NonNull String wifiStatus, @NonNull String ipV4, @NonNull String ipV6,
                         int wifiRssi, int wifiLinkSpeed, int wifiFrequency,
                         @NonNull String carrierName, @NonNull String networkType, @NonNull String simStatus,
                         boolean bluetoothEnabled, boolean nfcEnabled, @NonNull String status) {
        this.mWifiStatus = wifiStatus;
        this.mIpV4 = ipV4;
        this.mIpV6 = ipV6;
        this.mWifiRssi = wifiRssi;
        this.mWifiLinkSpeed = wifiLinkSpeed;
        this.mWifiFrequency = wifiFrequency;
        this.mCarrierName = carrierName;
        this.mNetworkType = networkType;
        this.mSimStatus = simStatus;
        this.mBluetoothEnabled = bluetoothEnabled;
        this.mNfcEnabled = nfcEnabled;
        this.mStatus = status;
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
