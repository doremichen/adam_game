/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.util;

/**
 * Global constants for the RacingGame2D project.
 */
public final class Constants {

    // === Game logic constants ===
    public static final int CAR_MOVE_STEP = 20;
    public static final int UPDATE_INTERVAL_MS = 16;
    public static final float DELTA_TIME = UPDATE_INTERVAL_MS / 1000f;
    public static final int INITIAL_LIVES = 3;
    public static final float MAX_SPEED = 900f;
    public static final float MIN_SPEED = 100f;
    public static final float HORIZONTAL_RATIO = 0.7f;
    public static final int BOUNDARY_VALUE = 40;
    public static final float HORIZONTAL_INCREMENT = 12f;
    public static final float DEFAULT_SPEED = 300f;
    public static final float DEFAULT_ACCELERATOR = 50f;
    public static final int COLLISION_SCORE = 50;
    public static final int MAX_CAR_HP = 3;

    // === SharedPreferences Keys ===
    public static final String PREF_NAME = "racing_game_prefs";
    public static final String PREF_PLAYER_NAME = "player_name";
    public static final String PREF_HIGH_SCORE = "high_score";
    public static final String PREF_SETTINGS = "game_settings";

    // === Sound resource constants ===
    public static final int SOUND_COLLISION = 1;
    public static final int SOUND_ENGINE = 2;
    public static final int SOUND_BUTTON = 3;

    // === Intent keys ===
    public static final String PLAYER_NAME = "key.player.name";
    public static final String CAR_ID = "key.car.id";
    public static final String CAR_NAME = "key.car.name";

    private Constants() {
        // Prevent instantiation
    }
}
