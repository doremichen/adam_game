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

package com.adam.app.mydeviceinfo.domain.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.adam.app.mydeviceinfo.domain.model.DeviceInfo;

/**
 * Interface for device information repository.
 */
public interface IDeviceRepository {
    /**
     * Fetches current device information.
     * @return DeviceInfo object containing comprehensive info.
     */
    @NonNull
    DeviceInfo fetchDeviceInfo();

    /**
     * Gets a live stream of device information.
     * @return LiveData containing the latest DeviceInfo.
     */
    @NonNull
    LiveData<DeviceInfo> getDeviceInfoStream();

    /**
     * Exports device information to a file.
     * @param deviceInfo The information to export.
     * @return true if successful, false otherwise.
     */
    boolean exportDeviceInfo(@NonNull DeviceInfo deviceInfo);

    /**
     * Runs a hardware test (vibration, etc).
     * @param type The test type.
     */
    void runHardwareTest(int type);
}
