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
package com.adam.app.snake.domain.model.strategy;

import com.adam.app.snake.domain.model.SpecialFood;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory of special food strategy
 */
public final class SpecialFoodStrategyFactory {

    // Map<Type, ISpecialFoodStrategy>
    private static final Map<Integer, ISpecialFoodEffect> sStrategies = new HashMap<>();

    static {
        sStrategies.put(SpecialFood.TYPE.SPEED_UP, new SpeedUpFoodEffectStrategy());
        sStrategies.put(SpecialFood.TYPE.SLOW_DOWN, new SlowDownFoodEffectStrategy());
        sStrategies.put(SpecialFood.TYPE.SHORTEN, new ShortenFoodEffectStrategy());
        sStrategies.put(SpecialFood.TYPE.EXTEND, new ExtendFoodEffectStrategy());
        sStrategies.put(SpecialFood.TYPE.INVINCIBLE, new InvincibleFoodEffectStrategy());
        sStrategies.put(SpecialFood.TYPE.INVISIBLE, new InvisibleFoodEffectStrategy());
        sStrategies.put(SpecialFood.TYPE.SCORE_DOUBLE, new ScoreDoubleFoodEffectStrategy());
        sStrategies.put(SpecialFood.TYPE.BOMB, new BombFoodEffectStrategy());
    }

    /**
     * private constructor
     */
    private SpecialFoodStrategyFactory() {
    }

    /**
     * getStrategy
     *    get strategy by type
     *
     * @param type int
     * @return ISpecialFoodEffect
     */
    public static ISpecialFoodEffect getStrategy(int type) {
        return sStrategies.get(type);
    }
}
