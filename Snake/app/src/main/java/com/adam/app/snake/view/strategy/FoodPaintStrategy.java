/**
 * Copyright 2015 the Adam Game
 *
 * Description: This enum is define the food paint strategy
 *
 * Author: Adam Chen
 * Date: 2025/10/03
 */
package com.adam.app.snake.view.strategy;

import android.graphics.Color;
import android.graphics.Paint;

import com.adam.app.snake.model.SpecialFood;

public enum FoodPaintStrategy {
    SPEED_UP(SpecialFood.TYPE.SPEED_UP) {
        @Override
        public void applyPaint(Paint paint) {
            paint.setColor(Color.CYAN);
        }
    },
    SLOW_DOWN(SpecialFood.TYPE.SLOW_DOWN) {
        @Override
        public void applyPaint(Paint paint) {
            paint.setColor(Color.GRAY);
        }
    },
    SHORTEN(SpecialFood.TYPE.SHORTEN) {
        @Override
        public void applyPaint(Paint paint) {
            paint.setColor(Color.MAGENTA);
        }
    },
    EXTEND(SpecialFood.TYPE.EXTEND) {
        @Override
        public void applyPaint(Paint paint) {
            paint.setColor(Color.GREEN);
        }
    },
    INVINCIBLE(SpecialFood.TYPE.INVINCIBLE) {
        @Override
        public void applyPaint(Paint paint) {
            paint.setColor(Color.YELLOW);
        }
    },
    INVISIBLE(SpecialFood.TYPE.INVISIBLE) {
        @Override
        public void applyPaint(Paint paint) {
            paint.setColor(Color.LTGRAY);
        }
    },
    SCORE_DOUBLE(SpecialFood.TYPE.SCORE_DOUBLE) {
        @Override
        public void applyPaint(Paint paint) {
            paint.setColor(Color.rgb(255, 165, 0));
        }
    },
    BOMB(SpecialFood.TYPE.BOMB) {
        @Override
        public void applyPaint(Paint paint) {
            paint.setColor(Color.BLACK);
        }
    };


    // type: int
    private final int mType;

    private FoodPaintStrategy(int type) {
        mType = type;
    }

    /**
     * applyPaint
     *      this is apply paint method for every food type
     * @param paint Paint
     */
    public abstract void applyPaint(Paint paint);


    /**
     * get FoodPaintStrategy with type
     *
     * @param type int
     */
    public static FoodPaintStrategy get(int type) {
        for (FoodPaintStrategy strategy : values()) {
            if (strategy.mType == type) {
                return strategy;
            }
        }
        return null;
    }

}
