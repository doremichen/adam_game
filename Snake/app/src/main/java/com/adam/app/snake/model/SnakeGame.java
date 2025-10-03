/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the snake game model
 * <p>
 * Author: Adam Chen
 * Date: 2025/09/24
 */
package com.adam.app.snake.model;

import android.graphics.Point;

import com.adam.app.snake.Utils;
import com.adam.app.snake.model.strategy.ISpecialFoodEffect;
import com.adam.app.snake.model.strategy.SpecialFoodStrategyFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class SnakeGame {

    // TAG SnakeGame
    private static final String TAG = "SnakeGame";// 20% chance to generate special food
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
    Random mRandom = new Random();
    // mFood: Point
    private Point mFood;
    private final List<SpecialFood> mSpecialFoods = new ArrayList<>();
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
     * @param rows
     * @param columns
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
        Utils.logDebug(TAG, "update");

        if (isGameOver()) return;

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
        Iterator<SpecialFood> iterator = mSpecialFoods.iterator();
        while (iterator.hasNext()) {
            SpecialFood food = iterator.next();
            if (System.currentTimeMillis() - mSpecialFoodTimestamps.get(food) > SPECIAL_FOOD_LIFETIME) {
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
            case UP:
                newY--;
                break;
            case DOWN:
                newY++;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
        }

        if (mWrapEnabled || mIsInvincible) {
            newX = (newX + mNumColumns) % mNumColumns;
            newY = (newY + mNumRows) % mNumRows;
        }

        return new Point(newX, newY);
    }

    private boolean isOutOfBounds(Point p) {
        if (mWrapEnabled || mIsInvincible) return false;
        return p.x < 0 || p.x >= mNumColumns || p.y < 0 || p.y >= mNumRows;
    }

    private boolean isCollidingWithSelf(Point p) {
        if (mIsInvincible) return false;
        for (Point body : mSnake) {
            if (body.equals(p)) return true;
        }
        return false;
    }

    private void moveSnake(Point newHead) {
        mSnake.add(0, newHead);
    }

    private void handleFoodCollision(Point newHead) {
        if (newHead.equals(mFood)) {
            mScore++;
            mNormalFoodEaten++;
            generateFood();

            if (mSpecialFoodEnabled && mNormalFoodEaten % 3 == 0) {
                generateSpecialFoods();
            }
        } else if (isSpecialFood()) {
            // do nothing
        } else {
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
            mGameListener.onShowSpecialFood(specialFood.toText());
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
        // log mNumColumns mNumRows
        Utils.logDebug(TAG, "generateFood: mNumColumns: " + mNumColumns + ", mNumRows: " + mNumRows);

        int x, y;
        do {
            x = mRandom.nextInt(mNumColumns); // 0 ~ mNumColumns-1
            y = mRandom.nextInt(mNumRows);    // 0 ~ mNumRows-1
            Utils.logDebug(TAG, "generateFood: x: " + x + ", y: " + y);
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
        Utils.logDebug(TAG, "isOppositeDirection: " + direction);
        Utils.logDebug(TAG, "mDirection: " + mDirection);
        return mDirection.isOpposite(direction);
    }

    /**
     * get food of snake
     *
     * @return int[1][2]
     */
    public Point getFood() {
        return mFood;
    }

    /**
     * get snake of snake
     *
     * @return List<int [ ]>
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
     */
    public void setWrapEnabled(boolean enabled) {
        mWrapEnabled = enabled;
    }

    /**
     * set special food enabled
     */
    public void setSpecialFoodEnabled(boolean enabled) {
        mSpecialFoodEnabled = enabled;
    }

    /**
     * set multiple special food enabled
     */
    public void allowMultiSpecialFood(boolean enabled) {
        mAllowMultiSpecialFood = enabled;
    }

    public List<SpecialFood> getSpecialFoods() {
        return mSpecialFoods;
    }

    public void speedUp(boolean speedUp) {
        // tell view model to speed up
        Utils.logDebug(TAG, "speedUp");
        if (Utils.isNull(mGameListener)) {
            Utils.logDebug(TAG, "mGameSpeedListener is null");
            return;
        }

        if (speedUp) {
            mGameListener.onGameSpeedUp();
        } else {
            mGameListener.onGameSlowDown();
        }
    }

    public void shortenSnake() {
        if (mSnake.size() > 3) {
            mSnake.remove(mSnake.size() - 1);
        }
    }

    public void extendSnake() {
        Point last = mSnake.get(mSnake.size() - 1);
        mSnake.add(new Point(last.x, last.y));
    }

    public void makeSnakeInvisible() {
        mIsInvisible = true;
        if (Utils.isNull(mGameListener)) {
            Utils.logDebug(TAG, "mGameSpeedListener is null");
            return;
        }

        mGameListener.onSnakeInVisible();
    }

    public void makeSnakeInvincible() {
        mIsInvincible = true;
        if (Utils.isNull(mGameListener)) {
            Utils.logDebug(TAG, "mGameSpeedListener is null");
            return;
        }
        mGameListener.onSnakeInvincible();
    }

    public void doubleScore() {
        mScore *= 2;
    }

    public void gameOver() {
        mGameState = GameState.GAME_OVER;
    }


    /**
     * enum Direction
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public boolean isOpposite(Direction other) {
            return (this == UP && other == DOWN) || (this == DOWN && other == UP) || (this == LEFT && other == RIGHT) || (this == RIGHT && other == LEFT);
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
     * onGameSpeedUp
     * onGameSlowDown
     * onSnakeInVisible
     * onSnakeInvincible
     */
    public interface GameListener {
        void onGameSpeedUp();

        void onGameSlowDown();

        void onSnakeInVisible();

        void onSnakeInvincible();

        void onShowSpecialFood(String type);

    }

}
