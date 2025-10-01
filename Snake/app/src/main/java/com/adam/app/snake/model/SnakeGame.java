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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame {

    // TAG SnakeGame
    private static final String TAG = "SnakeGame";
    // mNumColums: int
    private final int mNumColumns;
    // mNumRows: int
    private final int mNumRows;
    // mFood: Point
    private Point mFood;
    private Point mSpecialFood;
    private long mSpecialFoodExpireTime = 0L;
    // mSnake: List<Point>
    private final List<Point> mSnake = new ArrayList<>();
    // initial Direction is RIGHT
    private Direction mDirection = Direction.RIGHT;
    // initial GameState is RUNNING
    private GameState mGameState = GameState.RUNNING;
    // initial Score is 0
    private int mScore = 0;

    // check if wrap is enabled
    private boolean mWrapEnabled = false;
    // check if special food is enabled
    private boolean mSpecialFoodEnabled = false;

    private static final int SPECIAL_FOOD_SCORE = 3;
    private static final long SPECIAL_FOOD_DURATION = 5000L; // exist time of special food 5 sec
    private static final double SPECIAL_FOOD_PROB = 0.2f;     // 20% chance to generate special food
    
    Random mRandom = new Random();


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
        mSpecialFood = null;
        mGameState = GameState.RUNNING;
        mDirection = Direction.RIGHT;
        mScore = 0;
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

        removeExpiredSpecialFood();

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

    private void removeExpiredSpecialFood() {
        if (mSpecialFood != null && System.currentTimeMillis() > mSpecialFoodExpireTime) {
            mSpecialFood = null;
        }
    }

    private Point calculateNewHead() {
        Point head = mSnake.get(0);
        int newX = head.x;
        int newY = head.y;

        switch (mDirection) {
            case UP:    newY--; break;
            case DOWN:  newY++; break;
            case LEFT:  newX--; break;
            case RIGHT: newX++; break;
        }

        if (mWrapEnabled) {
            newX = (newX + mNumColumns) % mNumColumns;
            newY = (newY + mNumRows) % mNumRows;
        }

        return new Point(newX, newY);
    }

    private boolean isOutOfBounds(Point p) {
        if (mWrapEnabled) return false;
        return p.x < 0 || p.x >= mNumColumns || p.y < 0 || p.y >= mNumRows;
    }

    private boolean isCollidingWithSelf(Point p) {
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
            generateFood();

            if (mSpecialFoodEnabled && mRandom.nextDouble() < SPECIAL_FOOD_PROB) {
                generateSpecialFood();
            }
        } else if (newHead.equals(mSpecialFood)) {
            mScore += SPECIAL_FOOD_SCORE;
            mSpecialFood = null;
        } else {
            mSnake.remove(mSnake.size() - 1);
        }
    }

    private void generateSpecialFood() {
        int x, y;
        do {
            x = mRandom.nextInt(mNumColumns);
            y = mRandom.nextInt(mNumRows);
        } while (isOnSnake(x, y) || (mFood != null && mFood.equals(new Point(x, y))));

        mSpecialFood = new Point(x, y);
        mSpecialFoodExpireTime = System.currentTimeMillis() + SPECIAL_FOOD_DURATION;
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

    public Point getSpecialFood() {
        return mSpecialFood;
    }

    /**
     * enum Direction
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public boolean isOpposite(Direction other) {
            return (this == UP && other == DOWN) ||
                    (this == DOWN && other == UP) ||
                    (this == LEFT && other == RIGHT) ||
                    (this == RIGHT && other == LEFT);
        }
    }

    /**
     * enum GameState
     */
    public enum GameState {
        RUNNING, STOP, GAME_OVER
    }

}
