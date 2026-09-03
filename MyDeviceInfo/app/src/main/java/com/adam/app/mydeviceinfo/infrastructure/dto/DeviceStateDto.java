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

package com.adam.app.mydeviceinfo.infrastructure.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adam.app.mydeviceinfo.common.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for device state, used for AIDL communication.
 * Adheres to Builder pattern and encapsulation rules.
 */
public final class DeviceStateDto implements Parcelable {
    // Legacy / Compat
    private final String mCpuInfo;
    private final String mMemoryInfo;
    private final String mNetworkStatus;

    // Dashboard Data
    private final long mRamUsed;
    private final long mRamTotal;
    private final long mStorageUsed;
    private final long mStorageTotal;
    private final int mBatteryPct;
    private final int mBatteryTemp;
    private final int mBatteryVolt;
    private final String mBatteryStatus;
    private final String mBatteryHealth;
    private final long mUptime;

    // System Specs
    private final String mManufacturer;
    private final String mBrand;
    private final String mModel;
    private final String mCodename;
    private final String mScreenRes;
    private final String mScreenDpi;
    private final String mRefreshRate;
    private final String mOsVersion;
    private final int mSdkLevel;
    private final String mSecurityPatch;
    private final String mKernelVersion;
    private final String mFingerprint;
    private final String mCpuAbi;
    private final int mCpuCores;
    private final List<String> mSensorList;

    // Network Status
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

    private DeviceStateDto(Builder builder) {
        this.mCpuInfo = builder.mCpuInfo;
        this.mMemoryInfo = builder.mMemoryInfo;
        this.mNetworkStatus = builder.mNetworkStatus;

        this.mRamUsed = builder.mRamUsed;
        this.mRamTotal = builder.mRamTotal;
        this.mStorageUsed = builder.mStorageUsed;
        this.mStorageTotal = builder.mStorageTotal;
        this.mBatteryPct = builder.mBatteryPct;
        this.mBatteryTemp = builder.mBatteryTemp;
        this.mBatteryVolt = builder.mBatteryVolt;
        this.mBatteryStatus = builder.mBatteryStatus;
        this.mBatteryHealth = builder.mBatteryHealth;
        this.mUptime = builder.mUptime;

        this.mManufacturer = builder.mManufacturer;
        this.mBrand = builder.mBrand;
        this.mModel = builder.mModel;
        this.mCodename = builder.mCodename;
        this.mScreenRes = builder.mScreenRes;
        this.mScreenDpi = builder.mScreenDpi;
        this.mRefreshRate = builder.mRefreshRate;
        this.mOsVersion = builder.mOsVersion;
        this.mSdkLevel = builder.mSdkLevel;
        this.mSecurityPatch = builder.mSecurityPatch;
        this.mKernelVersion = builder.mKernelVersion;
        this.mFingerprint = builder.mFingerprint;
        this.mCpuAbi = builder.mCpuAbi;
        this.mCpuCores = builder.mCpuCores;
        this.mSensorList = builder.mSensorList;

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
    }

    private DeviceStateDto(Parcel in) {
        mCpuInfo = in.readString();
        mMemoryInfo = in.readString();
        mNetworkStatus = in.readString();

        mRamUsed = in.readLong();
        mRamTotal = in.readLong();
        mStorageUsed = in.readLong();
        mStorageTotal = in.readLong();
        mBatteryPct = in.readInt();
        mBatteryTemp = in.readInt();
        mBatteryVolt = in.readInt();
        mBatteryStatus = in.readString();
        mBatteryHealth = in.readString();
        mUptime = in.readLong();

        mManufacturer = in.readString();
        mBrand = in.readString();
        mModel = in.readString();
        mCodename = in.readString();
        mScreenRes = in.readString();
        mScreenDpi = in.readString();
        mRefreshRate = in.readString();
        mOsVersion = in.readString();
        mSdkLevel = in.readInt();
        mSecurityPatch = in.readString();
        mKernelVersion = in.readString();
        mFingerprint = in.readString();
        mCpuAbi = in.readString();
        mCpuCores = in.readInt();
        mSensorList = in.createStringArrayList();

        mWifiStatus = in.readString();
        mIpV4 = in.readString();
        mIpV6 = in.readString();
        mWifiRssi = in.readInt();
        mWifiLinkSpeed = in.readInt();
        mWifiFrequency = in.readInt();
        mCarrierName = in.readString();
        mNetworkType = in.readString();
        mSimStatus = in.readString();
        mBluetoothEnabled = in.readByte() != 0;
        mNfcEnabled = in.readByte() != 0;
    }

    public static final Creator<DeviceStateDto> CREATOR = new Creator<>() {
        @Override
        public DeviceStateDto createFromParcel(Parcel in) {
            return new DeviceStateDto(in);
        }

        @Override
        public DeviceStateDto[] newArray(int size) {
            return new DeviceStateDto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCpuInfo);
        dest.writeString(mMemoryInfo);
        dest.writeString(mNetworkStatus);

        dest.writeLong(mRamUsed);
        dest.writeLong(mRamTotal);
        dest.writeLong(mStorageUsed);
        dest.writeLong(mStorageTotal);
        dest.writeInt(mBatteryPct);
        dest.writeInt(mBatteryTemp);
        dest.writeInt(mBatteryVolt);
        dest.writeString(mBatteryStatus);
        dest.writeString(mBatteryHealth);
        dest.writeLong(mUptime);

        dest.writeString(mManufacturer);
        dest.writeString(mBrand);
        dest.writeString(mModel);
        dest.writeString(mCodename);
        dest.writeString(mScreenRes);
        dest.writeString(mScreenDpi);
        dest.writeString(mRefreshRate);
        dest.writeString(mOsVersion);
        dest.writeInt(mSdkLevel);
        dest.writeString(mSecurityPatch);
        dest.writeString(mKernelVersion);
        dest.writeString(mFingerprint);
        dest.writeString(mCpuAbi);
        dest.writeInt(mCpuCores);
        dest.writeStringList(mSensorList);

        dest.writeString(mWifiStatus);
        dest.writeString(mIpV4);
        dest.writeString(mIpV6);
        dest.writeInt(mWifiRssi);
        dest.writeInt(mWifiLinkSpeed);
        dest.writeInt(mWifiFrequency);
        dest.writeString(mCarrierName);
        dest.writeString(mNetworkType);
        dest.writeString(mSimStatus);
        dest.writeByte((byte) (mBluetoothEnabled ? 1 : 0));
        dest.writeByte((byte) (mNfcEnabled ? 1 : 0));
    }

    // --- Getters ---

    @NonNull public String getCpuInfo() { return mCpuInfo != null ? mCpuInfo : Constants.EMPTY_STRING; }
    @NonNull public String getMemoryInfo() { return mMemoryInfo != null ? mMemoryInfo : Constants.EMPTY_STRING; }
    @NonNull public String getNetworkStatus() { return mNetworkStatus != null ? mNetworkStatus : Constants.EMPTY_STRING; }

    public long getRamUsed() { return mRamUsed; }
    public long getRamTotal() { return mRamTotal; }
    public long getStorageUsed() { return mStorageUsed; }
    public long getStorageTotal() { return mStorageTotal; }
    public int getBatteryPct() { return mBatteryPct; }
    public int getBatteryTemp() { return mBatteryTemp; }
    public int getBatteryVolt() { return mBatteryVolt; }
    @NonNull public String getBatteryStatus() { return mBatteryStatus != null ? mBatteryStatus : Constants.EMPTY_STRING; }
    @NonNull public String getBatteryHealth() { return mBatteryHealth != null ? mBatteryHealth : Constants.EMPTY_STRING; }
    public long getUptime() { return mUptime; }

    @NonNull public String getManufacturer() { return mManufacturer != null ? mManufacturer : Constants.EMPTY_STRING; }
    @NonNull public String getBrand() { return mBrand != null ? mBrand : Constants.EMPTY_STRING; }
    @NonNull public String getModel() { return mModel != null ? mModel : Constants.EMPTY_STRING; }
    @NonNull public String getCodename() { return mCodename != null ? mCodename : Constants.EMPTY_STRING; }
    @NonNull public String getScreenRes() { return mScreenRes != null ? mScreenRes : Constants.EMPTY_STRING; }
    @NonNull public String getScreenDpi() { return mScreenDpi != null ? mScreenDpi : Constants.EMPTY_STRING; }
    @NonNull public String getRefreshRate() { return mRefreshRate != null ? mRefreshRate : Constants.EMPTY_STRING; }
    @NonNull public String getOsVersion() { return mOsVersion != null ? mOsVersion : Constants.EMPTY_STRING; }
    public int getSdkLevel() { return mSdkLevel; }
    @NonNull public String getSecurityPatch() { return mSecurityPatch != null ? mSecurityPatch : Constants.EMPTY_STRING; }
    @NonNull public String getKernelVersion() { return mKernelVersion != null ? mKernelVersion : Constants.EMPTY_STRING; }
    @NonNull public String getFingerprint() { return mFingerprint != null ? mFingerprint : Constants.EMPTY_STRING; }
    @NonNull public String getCpuAbi() { return mCpuAbi != null ? mCpuAbi : Constants.EMPTY_STRING; }
    public int getCpuCores() { return mCpuCores; }
    @NonNull public List<String> getSensorList() { return mSensorList != null ? mSensorList : new ArrayList<>(); }

    @NonNull public String getWifiStatus() { return mWifiStatus != null ? mWifiStatus : Constants.EMPTY_STRING; }
    @NonNull public String getIpV4() { return mIpV4 != null ? mIpV4 : Constants.EMPTY_STRING; }
    @NonNull public String getIpV6() { return mIpV6 != null ? mIpV6 : Constants.EMPTY_STRING; }
    public int getWifiRssi() { return mWifiRssi; }
    public int getWifiLinkSpeed() { return mWifiLinkSpeed; }
    public int getWifiFrequency() { return mWifiFrequency; }
    @NonNull public String getCarrierName() { return mCarrierName != null ? mCarrierName : Constants.EMPTY_STRING; }
    @NonNull public String getNetworkType() { return mNetworkType != null ? mNetworkType : Constants.EMPTY_STRING; }
    @NonNull public String getSimStatus() { return mSimStatus != null ? mSimStatus : Constants.EMPTY_STRING; }
    public boolean isBluetoothEnabled() { return mBluetoothEnabled; }
    public boolean isNfcEnabled() { return mNfcEnabled; }

    /**
     * Builder for DeviceStateDto.
     */
    public static final class Builder {
        private String mCpuInfo = Constants.EMPTY_STRING;
        private String mMemoryInfo = Constants.EMPTY_STRING;
        private String mNetworkStatus = Constants.EMPTY_STRING;

        private long mRamUsed = 0;
        private long mRamTotal = 0;
        private long mStorageUsed = 0;
        private long mStorageTotal = 0;
        private int mBatteryPct = 0;
        private int mBatteryTemp = 0;
        private int mBatteryVolt = 0;
        private String mBatteryStatus = Constants.EMPTY_STRING;
        private String mBatteryHealth = Constants.EMPTY_STRING;
        private long mUptime = 0;

        private String mManufacturer = Constants.EMPTY_STRING;
        private String mBrand = Constants.EMPTY_STRING;
        private String mModel = Constants.EMPTY_STRING;
        private String mCodename = Constants.EMPTY_STRING;
        private String mScreenRes = Constants.EMPTY_STRING;
        private String mScreenDpi = Constants.EMPTY_STRING;
        private String mRefreshRate = Constants.EMPTY_STRING;
        private String mOsVersion = Constants.EMPTY_STRING;
        private int mSdkLevel = 0;
        private String mSecurityPatch = Constants.EMPTY_STRING;
        private String mKernelVersion = Constants.EMPTY_STRING;
        private String mFingerprint = Constants.EMPTY_STRING;
        private String mCpuAbi = Constants.EMPTY_STRING;
        private int mCpuCores = 0;
        private List<String> mSensorList = new ArrayList<>();

        private String mWifiStatus = Constants.EMPTY_STRING;
        private String mIpV4 = Constants.EMPTY_STRING;
        private String mIpV6 = Constants.EMPTY_STRING;
        private int mWifiRssi = 0;
        private int mWifiLinkSpeed = 0;
        private int mWifiFrequency = 0;
        private String mCarrierName = Constants.EMPTY_STRING;
        private String mNetworkType = Constants.EMPTY_STRING;
        private String mSimStatus = Constants.EMPTY_STRING;
        private boolean mBluetoothEnabled = false;
        private boolean mNfcEnabled = false;

        public Builder setCpuInfo(@Nullable String cpuInfo) { mCpuInfo = cpuInfo; return this; }
        public Builder setMemoryInfo(@Nullable String memoryInfo) { mMemoryInfo = memoryInfo; return this; }
        public Builder setNetworkStatus(@Nullable String networkStatus) { mNetworkStatus = networkStatus; return this; }

        public Builder setRamUsed(long ramUsed) { mRamUsed = ramUsed; return this; }
        public Builder setRamTotal(long ramTotal) { mRamTotal = ramTotal; return this; }
        public Builder setStorageUsed(long storageUsed) { mStorageUsed = storageUsed; return this; }
        public Builder setStorageTotal(long storageTotal) { mStorageTotal = storageTotal; return this; }
        public Builder setBatteryPct(int batteryPct) { mBatteryPct = batteryPct; return this; }
        public Builder setBatteryTemp(int batteryTemp) { mBatteryTemp = batteryTemp; return this; }
        public Builder setBatteryVolt(int batteryVolt) { mBatteryVolt = batteryVolt; return this; }
        public Builder setBatteryStatus(@Nullable String batteryStatus) { mBatteryStatus = batteryStatus; return this; }
        public Builder setBatteryHealth(@Nullable String batteryHealth) { mBatteryHealth = batteryHealth; return this; }
        public Builder setUptime(long uptime) { mUptime = uptime; return this; }

        public Builder setManufacturer(@Nullable String manufacturer) { mManufacturer = manufacturer; return this; }
        public Builder setBrand(@Nullable String brand) { mBrand = brand; return this; }
        public Builder setModel(@Nullable String model) { mModel = model; return this; }
        public Builder setCodename(@Nullable String codename) { mCodename = codename; return this; }
        public Builder setScreenRes(@Nullable String screenRes) { mScreenRes = screenRes; return this; }
        public Builder setScreenDpi(@Nullable String screenDpi) { mScreenDpi = screenDpi; return this; }
        public Builder setRefreshRate(@Nullable String refreshRate) { mRefreshRate = refreshRate; return this; }
        public Builder setOsVersion(@Nullable String osVersion) { mOsVersion = osVersion; return this; }
        public Builder setSdkLevel(int sdkLevel) { mSdkLevel = sdkLevel; return this; }
        public Builder setSecurityPatch(@Nullable String securityPatch) { mSecurityPatch = securityPatch; return this; }
        public Builder setKernelVersion(@Nullable String kernelVersion) { mKernelVersion = kernelVersion; return this; }
        public Builder setFingerprint(@Nullable String fingerprint) { mFingerprint = fingerprint; return this; }
        public Builder setCpuAbi(@Nullable String cpuAbi) { mCpuAbi = cpuAbi; return this; }
        public Builder setCpuCores(int cpuCores) { mCpuCores = cpuCores; return this; }
        public Builder setSensorList(@Nullable List<String> sensorList) { mSensorList = sensorList; return this; }

        public Builder setWifiStatus(@Nullable String wifiStatus) { mWifiStatus = wifiStatus; return this; }
        public Builder setIpV4(@Nullable String ipV4) { mIpV4 = ipV4; return this; }
        public Builder setIpV6(@Nullable String ipV6) { mIpV6 = ipV6; return this; }
        public Builder setWifiRssi(int wifiRssi) { mWifiRssi = wifiRssi; return this; }
        public Builder setWifiLinkSpeed(int wifiLinkSpeed) { mWifiLinkSpeed = wifiLinkSpeed; return this; }
        public Builder setWifiFrequency(int wifiFrequency) { mWifiFrequency = wifiFrequency; return this; }
        public Builder setCarrierName(@Nullable String carrierName) { mCarrierName = carrierName; return this; }
        public Builder setNetworkType(@Nullable String networkType) { mNetworkType = networkType; return this; }
        public Builder setSimStatus(@Nullable String simStatus) { mSimStatus = simStatus; return this; }
        public Builder setBluetoothEnabled(boolean bluetoothEnabled) { mBluetoothEnabled = bluetoothEnabled; return this; }
        public Builder setNfcEnabled(boolean nfcEnabled) { mNfcEnabled = nfcEnabled; return this; }

        @NonNull
        public DeviceStateDto build() {
            return new DeviceStateDto(this);
        }
    }
}
