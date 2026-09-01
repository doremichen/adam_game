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

package com.adam.app.mydeviceinfo;

import com.adam.app.mydeviceinfo.infrastructure.dto.DeviceStateDto;
import com.adam.app.mydeviceinfo.IInfoCallback;

/**
 * AIDL interface for device information service.
 */
interface IInfoService {
    /**
     * Gets the comprehensive device state.
     * @return DeviceStateDto object.
     */
    DeviceStateDto getDeviceState();

    /**
     * Registers a callback for real-time updates.
     * @param callback The callback implementation.
     */
    void registerCallback(IInfoCallback callback);

    /**
     * Unregisters a previously registered callback.
     * @param callback The callback implementation.
     */
    void unregisterCallback(IInfoCallback callback);
}
