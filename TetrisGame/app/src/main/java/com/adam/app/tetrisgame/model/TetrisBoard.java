/**
 * Copyright 2025 Adam
 * Description: TetrisBoard is the board of the game.
 * Author: Adam
 * Date: 2025/06/23
 */
package com.adam.app.tetrisgame.model;


import com.adam.app.tetrisgame.Utils;
import com.adam.app.tetrisgame.view.TetrisView;

import java.util.Random;

public class TetrisBoard {

    // new two dimensional grid array of integers
    private int[][] mGrid;

    // tetromino
    private Tetromino mCurrentBlock;
    // current position
    private int mCurrentRow;
    private int mCurrentCol;

    // Random
    private Random mRandom = new Random();

    // get grid
    public int[][] getGrid() {
        return mGrid;
    }

    /**
     * GameOverListener
     * onGameOver: void
     */
    public interface GameOverListener {
        void onGameOver();
    }

    private GameOverListener mGameOverListener;

    // construct
    public TetrisBoard(GameOverListener listener) {
        mGrid = new int[Utils.NUM.ROWS][Utils.NUM.COLUMNS];
        this.mGameOverListener = listener;
        // spawn block
        spawnBlock();
    }

    // reset
    public void reset() {
        // mGrid
        for (int i = 0; i < Utils.NUM.ROWS; i++) {
            for (int j = 0; j < Utils.NUM.COLUMNS; j++) {
                mGrid[i][j] = 0;
            }
        }
    }


    // spawnBlock
    public void spawnBlock() {
        // get random tetromino type
        TetrominoType type = TetrominoType.values()[mRandom.nextInt(TetrominoType.values().length)];
        // create new tetromino
        mCurrentBlock = new Tetromino(type);
        // set current position
        mCurrentRow = 0;
        mCurrentCol = Utils.NUM.COLUMNS / 2 - mCurrentBlock.getShape()[0].length/2;
    }

    // update
    public void update() {
        // check if current block can move down
        if (!moveDown()) {
            mergeBlockToGrid();
            clearLines();
            spawnBlock();

            // check if game over
            if (isGameOver()) {
                // tell activity by listener
                if (mGameOverListener == null) {
                    throw new NullPointerException("mGameOverListener is null");
                }

                mGameOverListener.onGameOver();
            }
        }
    }

    // moveDown
    public boolean moveDown() {
        // check if current block can move
        if (canMove(mCurrentRow+1, mCurrentCol)) {
            mCurrentRow++;
            return true;
        }
        return false;
    }

    // moveLeft
    public boolean moveLeft() {
        // check if current block can move
        if (canMove(mCurrentRow, mCurrentCol-1)) {
            mCurrentCol--;
            return true;
        }
        return false;
    }

    // moveRight
    public boolean moveRight() {
        // check if current block can move
        if (canMove(mCurrentRow, mCurrentCol+1)) {
            mCurrentCol++;
            return true;
        }
        return false;
    }

    public void rotate() {
        // check mCurrentBlock null
        if (mCurrentBlock == null) {
            throw new NullPointerException("mCurrentBlock is null");
        }
        // get shape of current block
        int[][] oldShape  = mCurrentBlock.getShape();
        // rotate
        mCurrentBlock.rotate();
        // check if current block can move
        if (!canMove(mCurrentRow, mCurrentCol)) {
            // restore old shape
            mCurrentBlock.setShape(oldShape);
        }
    }

    // isGameOver
    public boolean isGameOver() {
        // check mCurrentBlock null
        if (mCurrentBlock == null) {
            throw new NullPointerException("mCurrentBlock is null");
        }

        return !canMove(mCurrentRow, mCurrentCol);
    }


    private boolean canMove(int row, int col) {
        // check mCurrentBlock null
        if (mCurrentBlock == null) {
            throw new NullPointerException("mCurrentBlock is null");
        }
        // get shape of current block
        int[][] shape = mCurrentBlock.getShape();
        // loop
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                // check if cell is not empty
                if (shape[i][j] != 0) {
                    // check if cell is out of bounds
                    if (row + i < 0 || row + i >= Utils.NUM.ROWS || col + j < 0 || col + j >= Utils.NUM.COLUMNS) {
                        return false;
                    }
                    // check if cell is occupied
                    if (mGrid[row + i][col + j] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void clearLines() {
        for (int i = 0; i < Utils.NUM.ROWS; i++) {
            boolean full = true;
            for (int j = 0; j < Utils.NUM.COLUMNS; j++) {
                if (mGrid[i][j] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                // clear this line and move others down
                for (int k = i; k > 0; k--) {
                    System.arraycopy(mGrid[k - 1], 0, mGrid[k], 0, Utils.NUM.COLUMNS);
                }
                mGrid[0] = new int[Utils.NUM.COLUMNS];
            }
        }
    }

    private void mergeBlockToGrid() {
        // get shape of current block
        int[][] shape = mCurrentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                // check if cell is not empty
                if (shape[i][j] != 0) {
                    int r = mCurrentRow + i;
                    int c = mCurrentCol + j;
                    // check if cell is out of bounds
                    if (r < 0 || r >= Utils.NUM.ROWS || c < 0 || c >= Utils.NUM.COLUMNS) {
                        continue;
                    }
                    mGrid[r][c] = mCurrentBlock.getColor();
                }
            }
        }

    }

    // applyToView
    public void applyToView(TetrisView view) {
        // check view null
        if (view == null) {
            throw new NullPointerException("view is null");
        }

        // copy first
        int[][] copy = new int[Utils.NUM.ROWS][Utils.NUM.COLUMNS];
        for (int i = 0; i < Utils.NUM.ROWS; i++) {
            System.arraycopy(mGrid[i], 0, copy[i], 0, Utils.NUM.COLUMNS);
        }

        // Overrlay current block
        int[][] shape = mCurrentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int r = mCurrentRow + i;
                    int c = mCurrentCol + j;
                    if (r >= 0 && r < Utils.NUM.ROWS && c >= 0 && c < Utils.NUM.COLUMNS) {
                        copy[r][c] = mCurrentBlock.getColor();
                    }
                }
            }
        }
        // set grid
        view.setGrid(copy);
    }
}
