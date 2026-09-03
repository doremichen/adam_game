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

    private DashboardData(Builder builder) {
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
        this.mCpuInfo = builder.mCpuInfo;
        this.mMemoryInfo = builder.mMemoryInfo;
    }

    public static final class Builder {
        private long mRamUsed;
        private long mRamTotal;
        private long mStorageUsed;
        private long mStorageTotal;
        private int mBatteryPct;
        private int mBatteryTemp;
        private int mBatteryVolt;
        private String mBatteryStatus = "";
        private String mBatteryHealth = "";
        private long mUptime;
        private String mCpuInfo = "";
        private String mMemoryInfo = "";

        public Builder setRamUsed(long ramUsed) { this.mRamUsed = ramUsed; return this; }
        public Builder setRamTotal(long ramTotal) { this.mRamTotal = ramTotal; return this; }
        public Builder setStorageUsed(long storageUsed) { this.mStorageUsed = storageUsed; return this; }
        public Builder setStorageTotal(long storageTotal) { this.mStorageTotal = storageTotal; return this; }
        public Builder setBatteryPct(int batteryPct) { this.mBatteryPct = batteryPct; return this; }
        public Builder setBatteryTemp(int batteryTemp) { this.mBatteryTemp = batteryTemp; return this; }
        public Builder setBatteryVolt(int batteryVolt) { this.mBatteryVolt = batteryVolt; return this; }
        public Builder setBatteryStatus(@NonNull String batteryStatus) { this.mBatteryStatus = batteryStatus; return this; }
        public Builder setBatteryHealth(@NonNull String batteryHealth) { this.mBatteryHealth = batteryHealth; return this; }
        public Builder setUptime(long uptime) { this.mUptime = uptime; return this; }
        public Builder setCpuInfo(@NonNull String cpuInfo) { this.mCpuInfo = cpuInfo; return this; }
        public Builder setMemoryInfo(@NonNull String memoryInfo) { this.mMemoryInfo = memoryInfo; return this; }

        /**
         * Finalizes the building of DashboardData.
         * @return A new DashboardData instance.
         */
        public DashboardData build() {
            return new DashboardData(this);
        }
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
