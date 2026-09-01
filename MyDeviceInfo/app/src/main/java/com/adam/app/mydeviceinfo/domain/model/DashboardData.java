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
 * Domain model for dashboard information.
 */
public final class DashboardData {
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
    
    // Legacy support
    private final String mCpuInfo;
    private final String mMemoryInfo;

    public DashboardData(long ramUsed, long ramTotal, long storageUsed, long storageTotal,
                         int batteryPct, int batteryTemp, int batteryVolt,
                         @NonNull String batteryStatus, @NonNull String batteryHealth,
                         long uptime, @NonNull String cpuInfo, @NonNull String memoryInfo) {
        this.mRamUsed = ramUsed;
        this.mRamTotal = ramTotal;
        this.mStorageUsed = storageUsed;
        this.mStorageTotal = storageTotal;
        this.mBatteryPct = batteryPct;
        this.mBatteryTemp = batteryTemp;
        this.mBatteryVolt = batteryVolt;
        this.mBatteryStatus = batteryStatus;
        this.mBatteryHealth = batteryHealth;
        this.mUptime = uptime;
        this.mCpuInfo = cpuInfo;
        this.mMemoryInfo = memoryInfo;
    }

    public long getRamUsed() { return mRamUsed; }
    public long getRamTotal() { return mRamTotal; }
    public long getStorageUsed() { return mStorageUsed; }
    public long getStorageTotal() { return mStorageTotal; }
    public int getBatteryPct() { return mBatteryPct; }
    public int getBatteryTemp() { return mBatteryTemp; }
    public int getBatteryVolt() { return mBatteryVolt; }
    @NonNull public String getBatteryStatus() { return mBatteryStatus; }
    @NonNull public String getBatteryHealth() { return mBatteryHealth; }
    public long getUptime() { return mUptime; }
    @NonNull public String getCpuInfo() { return mCpuInfo; }
    @NonNull public String getMemoryInfo() { return mMemoryInfo; }
}
