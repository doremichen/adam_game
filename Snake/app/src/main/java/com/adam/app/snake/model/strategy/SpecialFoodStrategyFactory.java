/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the factory of special food strategy
 *
 * Author: Adam Chen
 * Date: 2025/10/03
 */
package com.adam.app.snake.model.strategy;

import com.adam.app.snake.model.SpecialFood;

import java.util.HashMap;
import java.util.Map;

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
     */
    public static ISpecialFoodEffect getStrategy(int type) {
        return sStrategies.get(type);
    }
}
