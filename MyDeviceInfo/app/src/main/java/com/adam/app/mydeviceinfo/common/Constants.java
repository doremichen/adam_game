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

package com.adam.app.mydeviceinfo.common;

/**
 * Global constants for the project.
 */
public final class Constants {
    /**
     * Private constructor to prevent instantiation.
     */
    private Constants() {
        // Prevent instantiation
    }

    // Logging
    public static final String GLOBAL_TAG = "MyDeviceInfo";
    public static final String TAG_INFO_REPOSITORY = "InfoRepositoryImpl";
    public static final String TAG_INFO_SERVICE = "InfoService";

    // System Paths
    public static final String PATH_CPU_INFO = "/proc/cpuinfo";

    // File Names
    public static final String EXPORT_FILE_NAME = "system_info.txt";

    // Service Status & Logs
    public static final String MSG_DEVICE_INFO_UPDATED = "Device info updated periodically";
    public static final String MSG_ON_BIND_PREFIX = "onBind: Active clients = ";
    public static final String MSG_ON_UNBIND_PREFIX = "onUnbind: Active clients = ";

    // Polling & Units
    public static final float BYTES_IN_MB = 1024.0f * 1024.0f;
    public static final float BYTES_IN_GB = 1024.0f * 1024.0f * 1024.0f;
    public static final long POLLING_INTERVAL_MS = 1000L;
    public static final long INITIAL_DELAY_MS = 0L;

    // Common Strings
    public static final String EMPTY_STRING = "";
    public static final String DEFAULT_VAL_ZERO_STR = "0";
    public static final String PERMISSION_DENIED = "Permission Denied";
    public static final String VAL_UNKNOWN = "Unknown";

    // Network Status Keys
    public static final String NET_STATUS_WIFI = "wifi";
    public static final String NET_STATUS_CELLULAR = "cellular";
    public static final String NET_STATUS_DISCONNECTED = "disconnected";
    public static final String NET_STATUS_OTHER = "other";

    // SIM Status Keys
    public static final String SIM_STATUS_READY = "ready";
    public static final String SIM_STATUS_ABSENT = "absent";
    public static final String SIM_STATUS_UNKNOWN = "unknown";

    // Network Types
    public static final String NET_TYPE_5G = "5G";
    public static final String NET_TYPE_4G = "4G";
    public static final String NET_TYPE_3G = "3G";

    // Hardware Test Types
    public static final int TEST_TYPE_VIBRATION = 1;
}
