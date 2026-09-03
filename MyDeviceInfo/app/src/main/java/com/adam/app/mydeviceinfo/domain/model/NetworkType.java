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

import com.adam.app.mydeviceinfo.common.Constants;

/**
 * Enum representing mobile network types.
 */
public enum NetworkType {
    TYPE_5G(Constants.NET_TYPE_5G),
    TYPE_4G(Constants.NET_TYPE_4G),
    TYPE_3G(Constants.NET_TYPE_3G),
    UNKNOWN(Constants.VAL_UNKNOWN);

    private final String mValue;

    NetworkType(String value) {
        this.mValue = value;
    }

    @NonNull public String getValue() { return mValue; }

    /**
     * Finds the enum instance associated with the given value.
     * @param value The network type string value.
     * @return Matching NetworkType or UNKNOWN if not found.
     */
    @NonNull
    public static NetworkType fromValue(String value) {
        for (NetworkType type : values()) {
            if (type.mValue.equals(value)) return type;
        }
        return UNKNOWN;
    }
}
