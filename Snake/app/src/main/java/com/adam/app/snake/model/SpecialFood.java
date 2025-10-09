/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the Special food model
 * <p>
 * Author: Adam Chen
 * Date: 2025/10/01
 */
package com.adam.app.snake.model;

import com.adam.app.snake.R;

import java.util.HashMap;
import java.util.Map;

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
     * @param type TYPE
     */
    public SpecialFood(int x, int y, int type) {
        this.mX = x;
        this.mY = y;
        this.mType = type;
    }

    // getter
    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getType() {
        return mType;
    }


    /**
     * toString
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
        return mResourceMap.get(mType);
    }


    /**
     * class TYPE
     * SPEED_UP
     * SLOW_DOWN
     * SHORTEN
     * EXTEND
     * INVINCIBLE
     * INVISIBLE
     * SCORE_DOUBLE
     * BOMB
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
