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

package com.adam.app.galaga.engine;

import com.adam.app.galaga.R;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum representing movement direction.
 */
public enum Direction {
    UP(R.id.btnUp),
    DOWN(R.id.btnDown),
    LEFT(R.id.btnLeft),
    RIGHT(R.id.btnRight);

    private static final Map<Integer, Direction> sResIdToDirection = new HashMap<>();

    static {
        for (Direction direction : values()) {
            sResIdToDirection.put(direction.mResId, direction);
        }
    }

    private final int mResId;

    Direction(int resId) {
        this.mResId = resId;
    }

    public static Direction fromResId(int resId) {
        return sResIdToDirection.get(resId);
    }
}
