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

import android.graphics.Point;

import com.adam.app.snake.domain.model.strategy.ISpecialFoodEffect;
import com.adam.app.snake.domain.model.strategy.SpecialFoodStrategyFactory;
import com.adam.app.snake.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Snake game model
 */
public final class SnakeGame {

    // TAG SnakeGame
    private static final String TAG = "SnakeGame";
    // Special food lifetime (ms)
    private static final long SPECIAL_FOOD_LIFETIME = 30000L;
    // mNumColums: int
    private final int mNumColumns;
    // mNumRows: int
    private final int mNumRows;
    // mSnake: List<Point>
    private final List<Point> mSnake = new ArrayList<>();
    // Record special food timestamps
    private final Map<SpecialFood, Long> mSpecialFoodTimestamps = new HashMap<>();
    private final Random mRandom = new Random();
    private final List<SpecialFood> mSpecialFoods = new ArrayList<>();
    // mFood: Point
    private Point mFood;
    // initial Direction is RIGHT
    private Direction mDirection = Direction.RIGHT;
    // initial GameState is RUNNING
    private GameState mGameState = GameState.RUNNING;
    // initial Score is 0
    private int mScore = 0;
    // initial normal food eaten is 0
    private int mNormalFoodEaten = 0;
    // check if wrap is enabled
    private boolean mWrapEnabled = false;
    // check if special food is enabled
    private boolean mSpecialFoodEnabled = false;
    // check if multiple special food is enabled
    private boolean mAllowMultiSpecialFood = false;
    // check if the snake is invincible
    private boolean mIsInvincible = false;
    // check if the snake is invisible
    private boolean mIsInvisible = false;

    // Game listener
    private GameListener mGameListener;

    /**
     * Constructor with rows and columns
     *
     * @param rows int
     * @param columns int
     */
    public SnakeGame(int rows, int columns) {
        mNumRows = rows;
        mNumColumns = columns;

        reset();
    }

    /**
     * set game speed listener
     *
     * @param listener GameListener
     */
    public void setGameListener(GameListener listener) {
        mGameListener = listener;
    }


    /**
     * reset the game
     */
    public void reset() {
        // clear the snake
        mSnake.clear();
        // build snake
        mSnake.add(new Point(5, 5));
        mSnake.add(new Point(4, 5));
        mSnake.add(new Point(3, 5));

        generateFood();
        // clear special food
        mSpecialFoods.clear();
        mSpecialFoodTimestamps.clear();
        mGameState = GameState.RUNNING;
        mDirection = Direction.RIGHT;
        mScore = 0;
        mNormalFoodEaten = 0;
        mIsInvincible = false;
        mIsInvisible = false;
    }

    /**
     * start the game
     */
    public void start() {
        mGameState = GameState.RUNNING;
    }


    /**
     * stop the game
     */
    public void stop() {
        mGameState = GameState.STOP;
    }

    /**
     * update the game
     */
    public void update() {
        if (isGameOver()) {
            return;
        }

        removeExpiredSpecialFoods();

        Point newHead = calculateNewHead();

        if (isOutOfBounds(newHead)) {
            mGameState = GameState.GAME_OVER;
            return;
        }

        if (isCollidingWithSelf(newHead)) {
            mGameState = GameState.GAME_OVER;
            return;
        }

        moveSnake(newHead);
        handleFoodCollision(newHead);
    }

    private boolean isGameOver() {
        return mGameState == GameState.GAME_OVER;
    }


    /**
     * removeExpiredSpecialFoods
     * remove expired special foods
     */
    public void removeExpiredSpecialFoods() {
        if (mSpecialFoods.isEmpty()) return;
        
        Iterator<SpecialFood> iterator = mSpecialFoods.iterator();
        while (iterator.hasNext()) {
            SpecialFood food = iterator.next();
            Long timestamp = mSpecialFoodTimestamps.get(food);
            if (timestamp != null && System.currentTimeMillis() - timestamp > SPECIAL_FOOD_LIFETIME) {
                iterator.remove();
                mSpecialFoodTimestamps.remove(food);
            }
        }
    }

    private Point calculateNewHead() {
        Point head = mSnake.get(0);
        int newX = head.x;
        int newY = head.y;

        switch (mDirection) {
            case UP -> newY--;
            case DOWN -> newY++;
            case LEFT -> newX--;
            case RIGHT -> newX++;
        }

        if (mWrapEnabled || mIsInvincible) {
            newX = (newX + mNumColumns) % mNumColumns;
            newY = (newY + mNumRows) % mNumRows;
        }

        return new Point(newX, newY);
    }

    private boolean isOutOfBounds(Point p) {
        if (mWrapEnabled || mIsInvincible) {
            return false;
        }
        return p.x < 0 || p.x >= mNumColumns || p.y < 0 || p.y >= mNumRows;
    }

    private boolean isCollidingWithSelf(Point p) {
        if (mIsInvincible) {
            return false;
        }
        for (Point body : mSnake) {
            if (body.equals(p)) {
                return true;
            }
        }
        return false;
    }

    private void moveSnake(Point newHead) {
        mSnake.add(0, newHead);
    }

    private void handleFoodCollision(Point newHead) {
        if (Objects.equals(newHead, mFood)) {
            mScore++;
            mNormalFoodEaten++;
            generateFood();

            if (mSpecialFoodEnabled && mNormalFoodEaten % 3 == 0) {
                generateSpecialFoods();
            }
        } else if (!isSpecialFood()) {
            mSnake.remove(mSnake.size() - 1);
        }
    }

    /**
     * isSpecialFood
     * check if there is the special food
     *
     * @return boolean
     */
    public boolean isSpecialFood() {
        Iterator<SpecialFood> iterator = mSpecialFoods.iterator();
        while (iterator.hasNext()) {
            SpecialFood food = iterator.next();
            // check if special food is meet the head of snack
            if (mSnake.get(0).equals(new Point(food.getX(), food.getY()))) {
                // apply special food effect
                applySpecialFoodEffect(food);
                // remove special food
                iterator.remove();
                mSpecialFoodTimestamps.remove(food);
                return true;
            }
        }

        return false;
    }


    private void generateSpecialFoods() {
        // check if multiple special food is enabled
        if (!mAllowMultiSpecialFood) {
            mSpecialFoods.clear();
            mSpecialFoodTimestamps.clear();
        }
        int x, y, type;
        do {
            x = mRandom.nextInt(mNumColumns);
            y = mRandom.nextInt(mNumRows);
            type = mRandom.nextInt(8);  // 0~7
        } while (isOnSnake(x, y) || (mFood != null && mFood.equals(new Point(x, y))));
        // new special food
        SpecialFood specialFood = new SpecialFood(x, y, type);
        mSpecialFoods.add(specialFood);
        mSpecialFoodTimestamps.put(specialFood, System.currentTimeMillis());

        Utils.logDebug(TAG, "generateSpecialFoods: x: " + x + ", y: " + y + ", type: " + type);
    }

    /**
     * apply special food effect
     *
     * @param specialFood SpecialFood
     */
    public void applySpecialFoodEffect(SpecialFood specialFood) {
        Utils.logDebug(TAG, "applySpecialFoodEffect: " + specialFood);
        // get strategy
        ISpecialFoodEffect strategy = SpecialFoodStrategyFactory.getStrategy(specialFood.getType());
        if (Utils.isNull(strategy)) {
            Utils.logDebug(TAG, "applySpecialFoodEffect: strategy is null");
            return;
        }

        // tell view model to show special food type
        if (mGameListener != null) {
            mGameListener.onShowSpecialFood(specialFood.toResource());
        }

        strategy.apply(this);

    }


    /**
     * generate new food
     */
    private void generateFood() {
        Utils.logDebug(TAG, "generateFood");

        if (mNumColumns <= 0 || mNumRows <= 0) {
            // avoid to uninitialize snake view
            return;
        }

        int x, y;
        do {
            x = mRandom.nextInt(mNumColumns); // 0 ~ mNumColumns-1
            y = mRandom.nextInt(mNumRows);    // 0 ~ mNumRows-1
            // check if food is in snake
        } while (isOnSnake(x, y));

        // initial mFood
        mFood = new Point(x, y);
    }

    /**
     * check if (x, y) is in snake
     *
     * @param x int
     * @param y int
     * @return boolean
     */
    private boolean isOnSnake(int x, int y) {
        for (Point segment : mSnake) {
            if (segment.x == x && segment.y == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * set direction of snake according to input Direction
     *
     * @param direction Direction
     */
    public void setDirection(Direction direction) {
        Utils.logDebug(TAG, "setDirection: " + direction);
        if (isOppositeDirection(direction)) {
            Utils.logDebug(TAG, "isOppositeDirection: " + direction);
            return;
        }
        mDirection = direction;
    }

    /**
     * check if direction is opposite of current direction
     *
     * @param direction Direction
     * @return boolean
     */
    private boolean isOppositeDirection(Direction direction) {
        return mDirection.isOpposite(direction);
    }

    /**
     * get food of snake
     *
     * @return Point
     */
    public Point getFood() {
        return mFood;
    }

    /**
     * get snake of snake
     *
     * @return List<Point>
     */
    public List<Point> getSnake() {
        return mSnake;
    }

    /**
     * get score of snake
     *
     * @return int
     */
    public int getScore() {
        return mScore;
    }

    /**
     * get state of game
     *
     * @return GameState
     */
    public GameState getGameState() {
        return mGameState;
    }


    /**
     * set Invisible
     *
     * @param invisible boolean
     */
    public void setInvisible(boolean invisible) {
        mIsInvisible = invisible;
    }

    /**
     * set invincible
     *
     * @param invincible boolean
     */
    public void setInvincible(boolean invincible) {
        mIsInvincible = invincible;
    }


    /**
     * get Invisible
     *
     * @return boolean
     */
    public boolean isInvisible() {
        return mIsInvisible;
    }


    /**
     * set wrap enabled
     * @param enabled boolean
     */
    public void setWrapEnabled(boolean enabled) {
        mWrapEnabled = enabled;
    }

    /**
     * set special food enabled
     * @param enabled boolean
     */
    public void setSpecialFoodEnabled(boolean enabled) {
        mSpecialFoodEnabled = enabled;
    }

    /**
     * set multiple special food enabled
     * @param enabled boolean
     */
    public void allowMultiSpecialFood(boolean enabled) {
        mAllowMultiSpecialFood = enabled;
    }

    /**
     * get special foods
     * @return List<SpecialFood>
     */
    public List<SpecialFood> getSpecialFoods() {
        return mSpecialFoods;
    }

    /**
     * Speed up or slow down
     * @param speedUp boolean
     */
    public void speedUp(boolean speedUp) {
        if (Utils.isNull(mGameListener)) {
            return;
        }

        if (speedUp) {
            mGameListener.onGameSpeedUp();
        } else {
            mGameListener.onGameSlowDown();
        }
    }

    /**
     * Shorten snake
     */
    public void shortenSnake() {
        if (mSnake.size() > 3) {
            mSnake.remove(mSnake.size() - 1);
        }
    }

    /**
     * Extend snake
     */
    public void extendSnake() {
        Point last = mSnake.get(mSnake.size() - 1);
        mSnake.add(new Point(last.x, last.y));
    }

    /**
     * Make snake invisible
     */
    public void makeSnakeInvisible() {
        mIsInvisible = true;
        if (Utils.isNull(mGameListener)) {
            return;
        }

        mGameListener.onSnakeInVisible();
    }

    /**
     * Make snake invincible
     */
    public void makeSnakeInvincible() {
        mIsInvincible = true;
        if (Utils.isNull(mGameListener)) {
            return;
        }
        mGameListener.onSnakeInvincible();
    }

    /**
     * Double score
     */
    public void doubleScore() {
        mScore *= 2;
    }

    /**
     * Game over
     */
    public void gameOver() {
        mGameState = GameState.GAME_OVER;
    }


    /**
     * enum Direction
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        /**
         * check if direction is opposite
         * @param other Direction
         * @return boolean
         */
        public boolean isOpposite(Direction other) {
            return (this == UP && other == DOWN) || (this == DOWN && other == UP)
                    || (this == LEFT && other == RIGHT) || (this == RIGHT && other == LEFT);
        }
    }

    /**
     * enum GameState
     */
    public enum GameState {
        RUNNING, STOP, GAME_OVER
    }

    /**
     * interface listener
     */
    public interface GameListener {
        void onGameSpeedUp();

        void onGameSlowDown();

        void onSnakeInVisible();

        void onSnakeInvincible();

        void onShowSpecialFood(int resId);

    }

}
