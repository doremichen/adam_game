/*
 * Copyright (c) 2026 Adam Chen
 */

package com.adam.app.mydeviceinfo;

import com.adam.app.mydeviceinfo.infrastructure.dto.DeviceStateDto;

/**
 * AIDL callback interface for device information updates.
 */
interface IInfoCallback {
    /**
     * Called when the device state has changed.
     * @param dto The latest device state data.
     */
    void onDeviceStateChanged(in DeviceStateDto dto);
}
