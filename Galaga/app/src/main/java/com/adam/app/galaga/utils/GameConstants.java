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

package com.adam.app.galaga.utils;

public final class GameConstants {

    private GameConstants() {
        throw new UnsupportedOperationException("This is Constants!!!");
    }

    // --- System & Screen ---
    public static final int GAME_WIDTH = 1080;
    public static final int GAME_HEIGHT = 1920;
    public static final int FPS = 60;
    public static final long FRAME_PERIOD_MS = 1000 / FPS;

    // --- Player Settings ---
    public static final float PLAYER_START_X = 500f;
    public static final float PLAYER_START_Y = 1500f;
    public static final float PLAYER_SPEED = 15.0f;
    public static final int PLAYER_WIDTH = 100;
    public static final int PLAYER_HEIGHT = 100;
    public static final int MAX_ANIM_FRAMES = 4;
    public static final int ANIM_FRAME_DELAY = 5;

    // --- Weapon & Bullet ---
    public static final float BULLET_SPEED = 20.0f;
    public static final int BULLET_WIDTH = 10;
    public static final int BULLET_HEIGHT = 20;
    public static final int LASER_WIDTH = 8;
    public static final int LASER_HEIGHT = 250;
    public static final long AUTO_FIRE_INTERVAL = 500;

    // --- Enemy (General) ---
    public static final int BEE_WIDTH = 80;
    public static final int BEE_HEIGHT = 80;
    public static final int BEE_SPACING = 120;
    public static final int DEFAULT_BEES_COLS = 5;
    public static final int BEE_INITIAL_OFFSET_X = 150;
    public static final int BEE_INITIAL_OFFSET_Y = 200;
    public static final float BEE_DIVE_PROBABILITY = 0.005f;
    public static final int BEE_TURN_INTERVAL_MS = 1000;

    // --- Enemy Spawning (Waves) ---
    public static final int WAVE_SIZE = 5;
    public static final long WAVE_DELAY_MS = 3000;
    public static final long INTER_ENEMY_DELAY_MS = 200;
    public static final long LEVEL_DURATION_MS = 30000L;

    // --- Entry Strategies ---
    // Arc
    public static final long ARC_DURATION = 2000;
    public static final float ARC_SCREEN_OFFSET = 100f;
    public static final float ARC_CONTROL_POINT_Y_RATIO = 0.25f;
    
    // Circle
    public static final long CIRCLE_DURATION = 3000;
    public static final float CIRCLE_PHASE_THRESHOLD = 0.7f;
    public static final float CIRCLE_RADIUS_START = 200f;
    public static final float CIRCLE_RADIUS_END = 50f;
    public static final float CIRCLE_TOTAL_ANGLES = (float) (Math.PI * 4);
    public static final float CIRCLE_Y_OFFSET_START = 300f;
    public static final float CIRCLE_CENTER_Y_RATIO = 0.33f;

    // --- Scoring ---
    public static final int SCORE_PER_BEE = 5;

    // --- Sound Effects Keys ---
    public static final String SFX_FIRE = "fire";
    public static final String SFX_LASER = "laser";
    public static final String SFX_EXPLOSION = "explosion";
    public static final String SFX_LEVEL_START = "level_start";

    // --- Misc ---
    public static final String NOT_SUPPORT_INFO = "Not support!!!";
}
