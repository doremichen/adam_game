/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.domain.model;

import com.adam.app.snake.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Special food model
 */
public final class SpecialFood {
    // String resource map key: type, value: id
    private static final Map<Integer, Integer> mResourceMap = new HashMap<>() {
        {
            put(TYPE.SPEED_UP, R.string.snake_game_special_food_speedup);
            put(TYPE.SLOW_DOWN, R.string.snake_game_special_food_slowdown);
            put(TYPE.SHORTEN, R.string.snake_game_special_food_shorten);
            put(TYPE.EXTEND, R.string.snake_game_special_food_extend);
            put(TYPE.INVINCIBLE, R.string.snake_game_special_food_invincible);
            put(TYPE.INVISIBLE, R.string.snake_game_special_food_invisible);
            put(TYPE.SCORE_DOUBLE, R.string.snake_game_special_food_score_double);
            put(TYPE.BOMB, R.string.snake_game_special_food_bomb);
        }
    };
    // x int
    private final int mX;
    // y int
    private final int mY;
    // type TYPE
    private final int mType;

    /**
     * constructor
     *
     * @param x    int
     * @param y    int
     * @param type int
     */
    public SpecialFood(int x, int y, int type) {
        this.mX = x;
        this.mY = y;
        this.mType = type;
    }

    /**
     * Get X
     * @return int
     */
    public int getX() {
        return mX;
    }

    /**
     * Get Y
     * @return int
     */
    public int getY() {
        return mY;
    }

    /**
     * Get Type
     * @return int
     */
    public int getType() {
        return mType;
    }


    /**
     * toString
     * @return String
     */
    @Override
    public String toString() {
        return "SpecialFood{" + "mX=" + mX + ", mY=" + mY + ", mType=" + mType + '}';
    }

    /**
     * toResource
     *
     * @return int
     */
    public int toResource() {
        Integer resId = mResourceMap.get(mType);
        return resId != null ? resId : 0;
    }


    /**
     * class TYPE
     */
    public static class TYPE {
        public static final int SPEED_UP = 0;
        public static final int SLOW_DOWN = 1;
        public static final int SHORTEN = 2;
        public static final int EXTEND = 3;
        public static final int INVINCIBLE = 4;
        public static final int INVISIBLE = 5;
        public static final int SCORE_DOUBLE = 6;
        public static final int BOMB = 7;
    }


}
