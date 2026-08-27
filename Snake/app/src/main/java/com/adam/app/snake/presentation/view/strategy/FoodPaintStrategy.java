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
package com.adam.app.snake.presentation.view.strategy;

import android.graphics.Color;
import android.graphics.Paint;

import com.adam.app.snake.domain.model.SpecialFood;

/**
 * Food paint strategy
 */
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

    FoodPaintStrategy(int type) {
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
     * @return FoodPaintStrategy
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
