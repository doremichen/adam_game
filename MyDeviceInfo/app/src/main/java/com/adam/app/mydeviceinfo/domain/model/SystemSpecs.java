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

    public SystemSpecs(@NonNull String manufacturer, @NonNull String brand, @NonNull String model,
                       @NonNull String codename, @NonNull String screenRes, @NonNull String screenDpi,
                       @NonNull String refreshRate, @NonNull String osVersion, int sdkLevel,
                       @NonNull String securityPatch, @NonNull String kernelVersion,
                       @NonNull String fingerprint, @NonNull String cpuAbi, int cpuCores,
                       @NonNull List<String> sensorList) {
        this.mManufacturer = manufacturer;
        this.mBrand = brand;
        this.mModel = model;
        this.mCodename = codename;
        this.mScreenRes = screenRes;
        this.mScreenDpi = screenDpi;
        this.mRefreshRate = refreshRate;
        this.mOsVersion = osVersion;
        this.mSdkLevel = sdkLevel;
        this.mSecurityPatch = securityPatch;
        this.mKernelVersion = kernelVersion;
        this.mFingerprint = fingerprint;
        this.mCpuAbi = cpuAbi;
        this.mCpuCores = cpuCores;
        this.mSensorList = sensorList;
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
