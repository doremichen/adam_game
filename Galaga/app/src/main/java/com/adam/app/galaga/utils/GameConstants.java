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

    public static final float BULLET_SPEED = 20.0f;
    public static final int DEFAULT_BEES_COLS = 5;

    private GameConstants() {
        throw new UnsupportedOperationException("This is Constants!!!");
    }

    // screen size
    public static final int GAME_WIDTH = 1080;
    public static final int GAME_HEIGHT = 1920;
    public static final float REFERENCE_SCREEN_WIDTH = 1080f;
    public static final float REFERENCE_SCREEN_HEIGHT = 1920f;

    public static final int FPS = 60;
    public static final long FRAME_PERIOD_MS = 1000 / FPS;

    // Game rule
    public static final int SCORE_PER_BEE = 5;

    // player
    public static final float PLAYER_START_X = 500f;
    public static final float PLAYER_START_Y = 1500f;
    public static final float PLAYER_SPEED = 15.0f;
    public static final int PLAYER_WIDTH = 100;
    public static final int PLAYER_HEIGHT = 100;
    public static final int MAX_ANIM_FRAMES = 4; // assume fire animation has 4 frames
    public static final int ANIM_FRAME_DELAY = 5;// assume fire animation has 5 frames per second


    // bee
    public static final int BEE_ROWS = 3;
    public static final int BEE_COLS = 6;
    public static final int BEE_WIDTH = 80;
    public static final int BEE_HEIGHT = 80;
    public static final float BEE_SPEED_BASE = 5.0f;
    public static final int BEE_SPACING = 120;
    public static final int BEE_INITIAL_OFFSET_X = 150;
    public static final int BEE_INITIAL_OFFSET_Y = 200;

}
