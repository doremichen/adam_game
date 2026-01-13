/**
 * This enum is the AIStrategy class for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.models.strategy;

import android.graphics.Point;

import com.adam.app.tic_tac_toe.models.Board;
import com.adam.app.tic_tac_toe.models.Cell;
import com.adam.app.tic_tac_toe.models.Player;
import com.adam.app.tic_tac_toe.utils.GameUtils;
import com.adam.app.tic_tac_toe.utils.WinnerPattern;

import java.util.ArrayList;
import java.util.List;

public enum AIStrategy {

    EasyAIStrategy {

        @Override
        public Point findBestMove(Board board, Player aiPlayer) {
            List<Point> availableMoves = getAvailableMoves(board);
            // check if there are available moves
            if (availableMoves.isEmpty()) {
                return null;
            }

            // choose a random move
            int randomIndex = (int) (Math.random() * availableMoves.size());
            return availableMoves.get(randomIndex);
        }

    },
    HardAIStrategy {
        @Override
        public Point findBestMove(Board board, Player aiPlayer) {
            // get cells
            Cell[][] cells = board.getCells();
            // get opponent
            Player opponent = aiPlayer == Player.X ? Player.O : Player.X;

            // Ai winners?
            Point winningMove = findPatternMove(cells, aiPlayer);
            if (winningMove != null) {
                return winningMove;
            }

            // block opponent
            Point blockMove = findPatternMove(cells, opponent);
            if (blockMove != null) {
                return blockMove;
            }

            // occupied center first
            if (cells[1][1].getPlayer() == null) {
                return new Point(1, 1);
            }

            // random move
            return EasyAIStrategy.findBestMove(board, aiPlayer);
        }

        private Point findPatternMove(Cell[][] cells, Player player) {
            for (List<Cell> pattern : WinnerPattern.WIN_PATTERNS) {
                int playerCount = 0;
                Point emptyPoint = null;
                boolean isPatternBlocked = false;

                for (Cell cell : pattern) {
                    int r = cell.getPosition().x;
                    int c = cell.getPosition().y;

                    Player owner = cells[r][c].getPlayer();
                    if (owner == player) {
                        playerCount++;
                    } else if (owner == null) {
                        emptyPoint = cell.getPosition();
                    } else {
                        isPatternBlocked = true;
                        break;
                    }
                }

                if (!isPatternBlocked && playerCount == 2 && emptyPoint != null) {
                    return emptyPoint;

                }
            }
            return null;
        }

    };

    /**
     * getAvailableMoves
     * @param board Board
     * @return List<Point>
     */
    protected List<Point> getAvailableMoves(Board board) {
        List<Point> moves = new ArrayList<>();
        Cell[][] cells = board.getCells();
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (cells[i][j].getPlayer() == null) {
                    moves.add(new Point(i, j));
                }
            }
        }
        return moves;
    }


    public abstract Point findBestMove(Board board, Player aiPlayer);

}
