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

import android.os.BatteryManager;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.adam.app.mydeviceinfo.R;

/**
 * Enum representing battery charging status.
 */
public enum BatteryStatus {
    CHARGING(BatteryManager.BATTERY_STATUS_CHARGING, R.string.battery_status_charging),
    DISCHARGING(BatteryManager.BATTERY_STATUS_DISCHARGING, R.string.battery_status_discharging),
    NOT_CHARGING(BatteryManager.BATTERY_STATUS_NOT_CHARGING, R.string.battery_status_not_charging),
    FULL(BatteryManager.BATTERY_STATUS_FULL, R.string.battery_status_full),
    UNKNOWN(BatteryManager.BATTERY_STATUS_UNKNOWN, R.string.battery_status_unknown);

    private final int mCode;
    private final int mResId;

    BatteryStatus(int code, @StringRes int resId) {
        this.mCode = code;
        this.mResId = resId;
    }

    public int getCode() { return mCode; }
    @StringRes public int getResId() { return mResId; }

    /**
     * Finds the enum instance associated with the given battery code.
     * @param code BatteryManager constant code.
     * @return Matching BatteryStatus or UNKNOWN if not found.
     */
    @NonNull
    public static BatteryStatus fromCode(int code) {
        for (BatteryStatus status : values()) {
            if (status.mCode == code) return status;
        }
        return UNKNOWN;
    }

    /**
     * Parses a string representation of a battery code into an enum.
     * @param statusStr String representation of the code.
     * @return Matching BatteryStatus or UNKNOWN if invalid.
     */
    @NonNull
    public static BatteryStatus fromString(String statusStr) {
        try {
            return fromCode(Integer.parseInt(statusStr));
        } catch (NumberFormatException e) {
            return UNKNOWN;
        }
    }
}
