/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the snake game model
 *
 * Author: Adam Chen
 * Date: 2025/09/24
 */
package com.adam.app.snake.model;

import com.adam.app.snake.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SnakeGame {

    // TAG SnakeGame
    private static final String TAG = "SnakeGame";

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
        RUNNING, GAME_OVER
    }

    // mNumColums: int
    private final int mNumColumns;
    // mNumRows: int
    private final int mNumRows;

    // mFood[][]: int[1][2]
    private final int[][] mFood = new int[1][2];
    // mSnake: List<int[]>
    private final List<int[]> mSnake = new ArrayList<>();

    // initial Direction is RIGHT
    private Direction mDirection = Direction.RIGHT;
    // initial GameState is RUNNING
    private GameState mGameState = GameState.RUNNING;
    // initial Score is 0
    private int mScore = 0;

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
        mSnake.add(new int[]{5, 5});
        mSnake.add(new int[]{4, 5});
        mSnake.add(new int[]{3, 5});

        generateFood();

        mDirection = Direction.RIGHT;
        mGameState = GameState.RUNNING;
        mScore = 0;
    }

    /**
     * update the game
     */
    public void update() {
        Utils.logDebug(TAG, "update");
        if (mGameState == GameState.GAME_OVER) {
            return;
        }

        // get head of snake
        int[] head = mSnake.get(0);
        // get x and y of head
        int newX = head[0];
        int newY = head[1];

        // update x and y of head according to direction
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

        // check if head is out of bound
        if (newX < 0 || newX >= mNumColumns || newY < 0 || newY >= mNumRows) {
            mGameState = GameState.GAME_OVER;
            return;
        }

        // check if head is in snake
        for (int[] body : mSnake) {
            if (body[0] == newX && body[1] == newY) {
                mGameState = GameState.GAME_OVER;
                return;
            }
        }

        // move snake
        mSnake.add(0, new int[]{newX, newY});
        // check if head is in food
        if (newX == mFood[0][0] && newY == mFood[0][1]) {
            mScore++;
            // generate new food
            generateFood();
        } else {
            // remove tail of snake
            mSnake.remove(mSnake.size() - 1);
        }
        
    }

    /**
     * generate new food
     */
    private void generateFood() {
        Utils.logDebug(TAG, "generateFood");
        // generate random x and y
        int x = (int) (Math.random() * mNumColumns);
        int y = (int) (Math.random() * mNumRows);
        mFood[0][0] = x;
        mFood[0][1] = y;
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
    public int[][] getFood() {
        return mFood;
    }

    /**
     * get snake of snake
     *
     * @return List<int[]>
     */
    public List<int[]> getSnake() {
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

}
