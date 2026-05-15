/*
 * Copyright (c) 2025 Adam
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
package com.adam.app.tetrisgame.model;


import com.adam.app.tetrisgame.Utils;
import com.adam.app.tetrisgame.view.TetrisView;

import java.util.Random;

public class TetrisBoard {

    // new two dimensional grid array of integers
    private final int[][] mGrid;

    // tetromino
    private Tetromino mCurrentBlock;
    // current position
    private int mCurrentRow;
    private int mCurrentCol;

    // Random
    private final Random mRandom = new Random();

    /**
     * GameListener
     * onGameOver: void
     */
    public interface GameListener {
        void onClearLines();
        void onGameOver();
    }

    private final GameListener mGameListener;
    private final int[][] mBufferGrid;

    // construct
    public TetrisBoard(GameListener listener) {
        mGrid = new int[Utils.NUM.ROWS][Utils.NUM.COLUMNS];
        mBufferGrid = new int[Utils.NUM.ROWS][Utils.NUM.COLUMNS];
        this.mGameListener = listener;
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
        if (mCurrentBlock == null) return;

        // check if current block can move down
        if (!moveDown()) {
            mergeBlockToGrid();
            clearLines();
            spawnBlock();

            // check if game over
            if (isGameOver()) {
                // tell activity by listener
                if (mGameListener != null) {
                    mGameListener.onGameOver();
                }
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
        if (mCurrentBlock == null) {
            return;
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
        if (mCurrentBlock == null) {
            return false;
        }

        return !canMove(mCurrentRow, mCurrentCol);
    }


    private boolean canMove(int row, int col) {
        if (mCurrentBlock == null) {
            return false;
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
                // tell activity by listener
                if (mGameListener != null) {
                    mGameListener.onClearLines();
                }
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
        if (view == null || mCurrentBlock == null) {
            return;
        }

        // Use buffer to avoid frequent allocations
        for (int i = 0; i < Utils.NUM.ROWS; i++) {
            System.arraycopy(mGrid[i], 0, mBufferGrid[i], 0, Utils.NUM.COLUMNS);
        }

        // Overlay current block
        int[][] shape = mCurrentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int r = mCurrentRow + i;
                    int c = mCurrentCol + j;
                    if (r >= 0 && r < Utils.NUM.ROWS && c >= 0 && c < Utils.NUM.COLUMNS) {
                        mBufferGrid[r][c] = mCurrentBlock.getColor();
                    }
                }
            }
        }
        // set grid
        view.setGrid(mBufferGrid);
    }
}
