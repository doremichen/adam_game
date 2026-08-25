/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.util;

public final class Constants {
    private Constants() {}

    public static final int SCORE_PER_LINE = 100;
    
    public static final long SPEED_SLOW = 1000L;
    public static final long SPEED_NORMAL = 700L;
    public static final long SPEED_FAST = 400L;

    public static final int MAX_SCORE_RECORDS = 100;
    public static final int RESERVE_SCORE_RECORDS = 99;

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public static final String PREF_SOUND_EFFECT = "sound_effect";
    public static final String PREF_SPEED = "speed";
    public static final String PREF_HIGH_SCORE = "high_score";
}
