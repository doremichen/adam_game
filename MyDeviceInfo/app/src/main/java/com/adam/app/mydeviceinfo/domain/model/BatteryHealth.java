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
 * Enum representing battery health status.
 */
public enum BatteryHealth {
    GOOD(BatteryManager.BATTERY_HEALTH_GOOD, R.string.battery_health_good),
    OVERHEAT(BatteryManager.BATTERY_HEALTH_OVERHEAT, R.string.battery_health_overheat),
    DEAD(BatteryManager.BATTERY_HEALTH_DEAD, R.string.battery_health_dead),
    OVER_VOLTAGE(BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE, R.string.battery_health_over_voltage),
    COLD(BatteryManager.BATTERY_HEALTH_COLD, R.string.battery_health_cold),
    UNKNOWN(BatteryManager.BATTERY_HEALTH_UNKNOWN, R.string.battery_health_unknown);

    private final int mCode;
    private final int mResId;

    BatteryHealth(int code, @StringRes int resId) {
        this.mCode = code;
        this.mResId = resId;
    }

    public int getCode() { return mCode; }
    @StringRes public int getResId() { return mResId; }

    /**
     * Finds the enum instance associated with the given health code.
     * @param code BatteryManager constant code.
     * @return Matching BatteryHealth or UNKNOWN if not found.
     */
    @NonNull
    public static BatteryHealth fromCode(int code) {
        for (BatteryHealth health : values()) {
            if (health.mCode == code) return health;
        }
        return UNKNOWN;
    }

    /**
     * Parses a string representation of a health code into an enum.
     * @param healthStr String representation of the code.
     * @return Matching BatteryHealth or UNKNOWN if invalid.
     */
    @NonNull
    public static BatteryHealth fromString(String healthStr) {
        try {
            return fromCode(Integer.parseInt(healthStr));
        } catch (NumberFormatException e) {
            return UNKNOWN;
        }
    }
}
