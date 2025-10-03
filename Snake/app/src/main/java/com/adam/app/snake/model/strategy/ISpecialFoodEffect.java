/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is define strategy interface of special food effect
 *
 * Author: Adam Chen
 * Date: 2025/10/03
 */
package com.adam.app.snake.model.strategy;

import com.adam.app.snake.model.SnakeGame;

public interface ISpecialFoodEffect {
    // apply special food effect
    void apply(SnakeGame snakeGame);
}

/**
 * SpeedUpFoodEffectStrategy
 */
class SpeedUpFoodEffectStrategy implements ISpecialFoodEffect {
    @Override
    public void apply(SnakeGame snakeGame) {
        // speed up snake
        snakeGame.speedUp(true);
    }
}

/**
 * SlowDownFoodEffectStrategy
 */
class SlowDownFoodEffectStrategy implements ISpecialFoodEffect {
    @Override
    public void apply(SnakeGame snakeGame) {
        // slow down snake
        snakeGame.speedUp(false);

    }
}

/**
 * ShortenFoodEffectStrategy
 */
class ShortenFoodEffectStrategy implements ISpecialFoodEffect {
    @Override
    public void apply(SnakeGame snakeGame) {
        // shorten snake size
        snakeGame.shortenSnake();
    }
}

/**
 * ExtendFoodEffectStrategy
 */
class ExtendFoodEffectStrategy implements ISpecialFoodEffect {
    @Override
    public void apply(SnakeGame snakeGame) {
        // extend snake size
        snakeGame.extendSnake();

    }
}

/**
 * InvisibleFoodEffectStrategy
 */
class InvisibleFoodEffectStrategy implements ISpecialFoodEffect {
    @Override
    public void apply(SnakeGame snakeGame) {
        // make snake invisible
        snakeGame.makeSnakeInvisible();
    }
}

/**
 * InvincibleFoodEffectStrategy
 */
class InvincibleFoodEffectStrategy implements ISpecialFoodEffect {
    @Override
    public void apply(SnakeGame snakeGame) {
        // make snake invincible
        snakeGame.makeSnakeInvincible();
    }
}

/**
 * ScoreDoubleFoodEffectStrategy
 */
class ScoreDoubleFoodEffectStrategy implements ISpecialFoodEffect {
    @Override
    public void apply(SnakeGame snakeGame) {
        // double score
        snakeGame.doubleScore();

    }
}

/**
 * BombFoodEffectStrategy
 */
class BombFoodEffectStrategy implements ISpecialFoodEffect {
    @Override
    public void apply(SnakeGame snakeGame) {
        // game over
        snakeGame.gameOver();
    }
}

