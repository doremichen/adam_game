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

import com.adam.app.snake.domain.model.SnakeGame;

/**
 * Strategy interface of special food effect
 */
public interface ISpecialFoodEffect {
    /**
     * Apply special food effect
     * @param snakeGame SnakeGame
     */
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
