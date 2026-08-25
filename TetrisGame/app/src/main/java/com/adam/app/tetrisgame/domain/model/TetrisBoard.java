/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.domain.model;

import com.adam.app.tetrisgame.util.Utils;
import java.util.Random;

public class TetrisBoard {
    private final int[][] mGrid;
    private Tetromino mCurrentBlock;
    private int mCurrentRow;
    private int mCurrentCol;
    private final Random mRandom = new Random();
    private final GameListener mGameListener;

    public interface GameListener {
        void onClearLines();
        void onGameOver();
    }

    public TetrisBoard(GameListener listener) {
        mGrid = new int[Utils.NUM.ROWS][Utils.NUM.COLUMNS];
        this.mGameListener = listener;
        spawnBlock();
    }

    public void reset() {
        for (int i = 0; i < Utils.NUM.ROWS; i++) {
            for (int j = 0; j < Utils.NUM.COLUMNS; j++) {
                mGrid[i][j] = 0;
            }
        }
    }

    public void spawnBlock() {
        TetrominoType type = TetrominoType.values()[mRandom.nextInt(TetrominoType.values().length)];
        mCurrentBlock = new Tetromino(type);
        mCurrentRow = 0;
        mCurrentCol = Utils.NUM.COLUMNS / 2 - mCurrentBlock.getShape()[0].length / 2;
    }

    public void update() {
        if (mCurrentBlock == null) return;
        if (!moveDown()) {
            mergeBlockToGrid();
            clearLines();
            spawnBlock();
            if (isGameOver()) {
                if (mGameListener != null) mGameListener.onGameOver();
            }
        }
    }

    public boolean moveDown() {
        if (canMove(mCurrentRow + 1, mCurrentCol)) {
            mCurrentRow++;
            return true;
        }
        return false;
    }

    public boolean moveLeft() {
        if (canMove(mCurrentRow, mCurrentCol - 1)) {
            mCurrentCol--;
            return true;
        }
        return false;
    }

    public boolean moveRight() {
        if (canMove(mCurrentRow, mCurrentCol + 1)) {
            mCurrentCol++;
            return true;
        }
        return false;
    }

    public void rotate() {
        if (mCurrentBlock == null) return;
        int[][] oldShape = mCurrentBlock.getShape();
        mCurrentBlock.rotate();
        if (!canMove(mCurrentRow, mCurrentCol)) {
            mCurrentBlock.setShape(oldShape);
        }
    }

    public boolean isGameOver() {
        if (mCurrentBlock == null) return false;
        return !canMove(mCurrentRow, mCurrentCol);
    }

    private boolean canMove(int row, int col) {
        if (mCurrentBlock == null) return false;
        int[][] shape = mCurrentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    if (row + i < 0 || row + i >= Utils.NUM.ROWS || col + j < 0 || col + j >= Utils.NUM.COLUMNS) return false;
                    if (mGrid[row + i][col + j] != 0) return false;
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
                for (int k = i; k > 0; k--) {
                    System.arraycopy(mGrid[k - 1], 0, mGrid[k], 0, Utils.NUM.COLUMNS);
                }
                mGrid[0] = new int[Utils.NUM.COLUMNS];
                if (mGameListener != null) mGameListener.onClearLines();
            }
        }
    }

    private void mergeBlockToGrid() {
        int[][] shape = mCurrentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int r = mCurrentRow + i;
                    int c = mCurrentCol + j;
                    if (r >= 0 && r < Utils.NUM.ROWS && c >= 0 && c < Utils.NUM.COLUMNS) {
                        mGrid[r][c] = mCurrentBlock.getColor();
                    }
                }
            }
        }
    }

    public int[][] getDisplayGrid() {
        int[][] displayGrid = new int[Utils.NUM.ROWS][Utils.NUM.COLUMNS];
        for (int i = 0; i < Utils.NUM.ROWS; i++) {
            System.arraycopy(mGrid[i], 0, displayGrid[i], 0, Utils.NUM.COLUMNS);
        }
        if (mCurrentBlock != null) {
            int[][] shape = mCurrentBlock.getShape();
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] != 0) {
                        int r = mCurrentRow + i;
                        int c = mCurrentCol + j;
                        if (r >= 0 && r < Utils.NUM.ROWS && c >= 0 && c < Utils.NUM.COLUMNS) {
                            displayGrid[r][c] = mCurrentBlock.getColor();
                        }
                    }
                }
            }
        }
        return displayGrid;
    }
}
