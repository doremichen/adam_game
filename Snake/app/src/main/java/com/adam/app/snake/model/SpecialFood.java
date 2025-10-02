/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the Special food model
 *
 * Author: Adam Chen
 * Date: 2025/10/01
 */
package com.adam.app.snake.model;

public class SpecialFood {
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

    // x int
    private final int mX;
    // y int
    private final int mY;
    // type TYPE
    private final int mType;

    /**
     * constructor
     *
     * @param x int
     * @param y int
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

}
