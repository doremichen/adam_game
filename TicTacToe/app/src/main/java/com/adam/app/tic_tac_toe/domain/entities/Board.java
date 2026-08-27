/*
 * Copyright (c) 2026 Adam Chen
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

package com.adam.app.tic_tac_toe.domain.entities;

import android.graphics.Point;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the Tic Tac Toe board.
 */
public class Board {

    public static final int BOARD_SIZE = 3;
    private final Cell[][] mCells = new Cell[BOARD_SIZE][BOARD_SIZE];
    private List<Point> mWinningPoints = new ArrayList<>();
    private Player mWinner;

    public Board() {
        reset();
    }

    public void reset() {
        mWinner = null;
        mWinningPoints.clear();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                mCells[i][j] = new Cell(null, new Point(i, j));
            }
        }
    }

    public boolean placeMove(@NonNull Player player, int row, int col) {
        if (isValidMove(row, col)) {
            mCells[row][col].setPlayer(player);
            checkWinner(player, row, col);
            return true;
        }
        return false;
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE 
                && mCells[row][col].getPlayer() == null;
    }

    private void checkWinner(@NonNull Player player, int row, int col) {
        // Row check
        List<Point> rowPoints = Arrays.asList(new Point(row, 0), new Point(row, 1), new Point(row, 2));
        if (isWinningLine(player, rowPoints)) {
            return;
        }

        // Column check
        List<Point> colPoints = Arrays.asList(new Point(0, col), new Point(1, col), new Point(2, col));
        if (isWinningLine(player, colPoints)) {
            return;
        }

        // Diagonal check
        if (row == col) {
            List<Point> diagonalPoints = Arrays.asList(new Point(0, 0), new Point(1, 1), new Point(2, 2));
            if (isWinningLine(player, diagonalPoints)) {
                return;
            }
        }

        // Anti-diagonal check
        if (row + col == BOARD_SIZE - 1) {
            List<Point> antiDiagonalPoints = Arrays.asList(new Point(0, 2), new Point(1, 1), new Point(2, 0));
            isWinningLine(player, antiDiagonalPoints);
        }
    }

    private boolean isWinningLine(@NonNull Player player, @NonNull List<Point> points) {
        for (Point point : points) {
            if (mCells[point.x][point.y].getPlayer() != player) {
                return false;
            }
        }
        mWinner = player;
        mWinningPoints = new ArrayList<>(points);
        return true;
    }

    @NonNull
    public List<Point> getWinningPoints() {
        return mWinningPoints;
    }

    public boolean isDraw() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (mCells[i][j].getPlayer() == null) {
                    return false;
                }
            }
        }
        return mWinner == null;
    }

    @Nullable
    public Player getWinner() {
        return mWinner;
    }

    @NonNull
    public Cell[][] getCells() {
        return mCells;
    }
}
