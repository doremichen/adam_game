/**
 * This class is the Board class for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.models;

public class Board {

    public static final int BOARD_SIZE = 3;
    private final Cell[][] mCells = new Cell[BOARD_SIZE][BOARD_SIZE];;

    private Player mWinner;

    public void reset() {
        this.mWinner = null;
        // init cells
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                mCells[i][j] = new Cell(null);
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
            mCells[row][col].setPlayer(player);
            checkWinner(player, row, col);
            return true;
        }
        return false;
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

}
