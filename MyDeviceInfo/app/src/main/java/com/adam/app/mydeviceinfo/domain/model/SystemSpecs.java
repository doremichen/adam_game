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
import java.util.List;

/**
 * Domain model for system specifications.
 */
public final class SystemSpecs {
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

    private SystemSpecs(Builder builder) {
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
    }

    public static final class Builder {
        private String mManufacturer = "";
        private String mBrand = "";
        private String mModel = "";
        private String mCodename = "";
        private String mScreenRes = "";
        private String mScreenDpi = "";
        private String mRefreshRate = "";
        private String mOsVersion = "";
        private int mSdkLevel;
        private String mSecurityPatch = "";
        private String mKernelVersion = "";
        private String mFingerprint = "";
        private String mCpuAbi = "";
        private int mCpuCores;
        private List<String> mSensorList;

        public Builder setManufacturer(@NonNull String manufacturer) { this.mManufacturer = manufacturer; return this; }
        public Builder setBrand(@NonNull String brand) { this.mBrand = brand; return this; }
        public Builder setModel(@NonNull String model) { this.mModel = model; return this; }
        public Builder setCodename(@NonNull String codename) { this.mCodename = codename; return this; }
        public Builder setScreenRes(@NonNull String screenRes) { this.mScreenRes = screenRes; return this; }
        public Builder setScreenDpi(@NonNull String screenDpi) { this.mScreenDpi = screenDpi; return this; }
        public Builder setRefreshRate(@NonNull String refreshRate) { this.mRefreshRate = refreshRate; return this; }
        public Builder setOsVersion(@NonNull String osVersion) { this.mOsVersion = osVersion; return this; }
        public Builder setSdkLevel(int sdkLevel) { this.mSdkLevel = sdkLevel; return this; }
        public Builder setSecurityPatch(@NonNull String securityPatch) { this.mSecurityPatch = securityPatch; return this; }
        public Builder setKernelVersion(@NonNull String kernelVersion) { this.mKernelVersion = kernelVersion; return this; }
        public Builder setFingerprint(@NonNull String fingerprint) { this.mFingerprint = fingerprint; return this; }
        public Builder setCpuAbi(@NonNull String cpuAbi) { this.mCpuAbi = cpuAbi; return this; }
        public Builder setCpuCores(int cpuCores) { this.mCpuCores = cpuCores; return this; }
        public Builder setSensorList(@NonNull List<String> sensorList) { this.mSensorList = sensorList; return this; }

        /**
         * Finalizes the building of SystemSpecs.
         * @return A new SystemSpecs instance.
         */
        public SystemSpecs build() {
            return new SystemSpecs(this);
        }
    }

    @NonNull public String getManufacturer() { return mManufacturer; }
    @NonNull public String getBrand() { return mBrand; }
    @NonNull public String getModel() { return mModel; }
    @NonNull public String getCodename() { return mCodename; }
    @NonNull public String getScreenRes() { return mScreenRes; }
    @NonNull public String getScreenDpi() { return mScreenDpi; }
    @NonNull public String getRefreshRate() { return mRefreshRate; }
    @NonNull public String getOsVersion() { return mOsVersion; }
    public int getSdkLevel() { return mSdkLevel; }
    @NonNull public String getSecurityPatch() { return mSecurityPatch; }
    @NonNull public String getKernelVersion() { return mKernelVersion; }
    @NonNull public String getFingerprint() { return mFingerprint; }
    @NonNull public String getCpuAbi() { return mCpuAbi; }
    public int getCpuCores() { return mCpuCores; }
    @NonNull public List<String> getSensorList() { return mSensorList; }
}
