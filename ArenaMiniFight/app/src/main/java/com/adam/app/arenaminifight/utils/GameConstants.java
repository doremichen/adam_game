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

package com.adam.app.arenaminifight.utils;

public final class GameConstants {

    private GameConstants() {
        throw new UnsupportedOperationException("Constant class; do not instantiate.");
    }

    public static final String DefaultPlayerName = "abb";

    // Magic number elimination (CQ-01)
    public static final long FRAME_DELAY_MS = 16L;
    public static final long NETWORK_SIM_DELAY_MS = 2000L;
    public static final long SERVICE_TERMINATION_TIMEOUT_MS = 500L;
    public static final float PLAYER_RADIUS = 30f;
    public static final float BLOOD_BAR_OFFSET_Y = 40f;
    public static final float BLOOD_BAR_HEIGHT = 10f;
    public static final float BLOOD_BAR_WIDTH = 80f;
    public static final float NAME_OFFSET_Y = 50f;
    public static final int PLAYER_MAX_HP = 100;
    public static final float WORLD_MIN_X = 50.0f;
    public static final float WORLD_MAX_X = 1030.0f;
    public static final float WORLD_MIN_Y = 100.0f;
    public static final float WORLD_MAX_Y = 1820.0f;
}
