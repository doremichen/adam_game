/**
 * This class is the Board class for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.models;

import android.graphics.Point;

public class Board {

    public static final int BOARD_SIZE = 3;
    private final Cell[][] mCells = new Cell[BOARD_SIZE][BOARD_SIZE];;

    private Player mWinner;

    public void reset() {
        this.mWinner = null;
        // init cells
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                mCells[i][j] = new Cell(null, new Point(i, j));
            }
        }
    }

    /**
     * placeMove
     *
     * @param player
     * @param row
     * @param col
     * @return boolean
     */
    public boolean placeMove(Player player, int row, int col) {
        if (isValidMove(row, col)) {
            mCells[row][col].setPlayer(player).setPosition(new Point(row, col));
            checkWinner(player, row, col);
            return true;
        }
        return false;
    }

    public Player checkForWinner() {
        // column check
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mCells[0][i].getPlayer() == null) {
                continue;
            }
            if (mCells[0][i].getPlayer() == mCells[1][i].getPlayer() && mCells[1][i].getPlayer() == mCells[2][i].getPlayer()) {
                return mCells[0][i].getPlayer();
            }
        }

        // row check
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mCells[i][0].getPlayer() == null) {
                continue;
            }
            if (mCells[i][0].getPlayer() == mCells[i][1].getPlayer() && mCells[i][1].getPlayer() == mCells[i][2].getPlayer()) {
                return mCells[i][0].getPlayer();
            }

        }

        // diagonal check: top-left to bottom-right
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mCells[i][i].getPlayer() == null) {
                continue;
            }
            if (mCells[i][i].getPlayer() == mCells[i + 1][i + 1].getPlayer() && mCells[i + 1][i + 1].getPlayer() == mCells[i + 2][i + 2].getPlayer()) {
                return mCells[i][i].getPlayer();
            }

        }

        // diagonal check: top-right to bottom-left
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mCells[i][BOARD_SIZE - 1 - i].getPlayer() == null) {
                continue;
            }
            if (mCells[i][BOARD_SIZE - 1 - i].getPlayer() == mCells[i + 1][BOARD_SIZE - 2 - i].getPlayer() && mCells[i + 1][BOARD_SIZE - 2 - i].getPlayer() == mCells[i + 2][BOARD_SIZE - 3 - i].getPlayer()) {
                return mCells[i][BOARD_SIZE - 1 - i].getPlayer();
            }
        }

        // no winner
        return null;
    }


    private boolean isValidMove(int row, int col) {
        return mCells[row][col].getPlayer() == null;
    }

    /**
     * checkWinner
     * @param player Player
     * @param row int
     * @param col int
     */
    private void checkWinner(Player player, int row, int col) {
        // row check
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mCells[row][i].getPlayer() != player) {
                break;
            }

            if (i == BOARD_SIZE - 1) {
                mWinner = player;
                return;
            }
        }
        // col check
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mCells[i][col].getPlayer() != player) {
                break;
            }
            if (i == BOARD_SIZE - 1) {
                mWinner = player;
                return;
            }
        }

        // diagonal check: top-left to bottom-right
        if (row == col) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (mCells[i][i].getPlayer() != player) {
                    break;
                }
                if (i == BOARD_SIZE - 1) {
                    mWinner = player;
                    return;
                }
            }
        }

        // diagonal check: top-right to bottom-left
        if (row + col == BOARD_SIZE - 1) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (mCells[i][BOARD_SIZE - 1 - i].getPlayer() != player) {
                    break;
                }
                if (i == BOARD_SIZE - 1) {
                    mWinner = player;
                    return;
                }
            }
        }
    }

    /**
     * isDraw
     * @return boolean
     */
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



    public Player getWinner() {
        return mWinner;
    }

    public Cell[][] getCells() {
        return mCells;
    }

    public boolean hasWinner() {
        return mWinner != null;
    }
}
