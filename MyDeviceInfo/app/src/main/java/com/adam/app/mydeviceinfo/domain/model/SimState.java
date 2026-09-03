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
import androidx.annotation.StringRes;

import com.adam.app.mydeviceinfo.R;
import com.adam.app.mydeviceinfo.common.Constants;

/**
 * Enum representing SIM card states.
 */
public enum SimState {
    READY(Constants.SIM_STATUS_READY, R.string.sim_ready),
    ABSENT(Constants.SIM_STATUS_ABSENT, R.string.sim_absent),
    UNKNOWN(Constants.SIM_STATUS_UNKNOWN, R.string.sim_unknown);

    private final String mKey;
    private final int mResId;

    SimState(String key, @StringRes int resId) {
        this.mKey = key;
        this.mResId = resId;
    }

    @NonNull public String getKey() { return mKey; }
    @StringRes public int getResId() { return mResId; }

    /**
     * Finds the enum instance associated with the given key.
     * @param key The constant string key.
     * @return Matching SimState or UNKNOWN if not found.
     */
    @NonNull
    public static SimState fromKey(String key) {
        for (SimState state : values()) {
            if (state.mKey.equals(key)) return state;
        }
        return UNKNOWN;
    }
}
