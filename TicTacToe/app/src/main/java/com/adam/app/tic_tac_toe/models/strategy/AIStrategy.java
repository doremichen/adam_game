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

import java.util.ArrayList;
import java.util.List;

public enum AIStrategy {

    EasyAIStrategy {

        @Override
        public Point findBestMove(Board board, Player aiPlayer) {
            List<Point> availableMoves = new ArrayList<>();
            Cell[][] cells = board.getCells();

            for (int i = 0; i < Board.BOARD_SIZE; i++) {
                for (int j = 0; j < Board.BOARD_SIZE; j++) {
                    if (cells[i][j].getPlayer() == null) {
                        availableMoves.add(new Point(i, j));
                    }
                }
            }

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
            int bestScore = Integer.MIN_VALUE;
            Point bestMove = null;

            Cell[][] cells = board.getCells();

            for (int i = 0; i < Board.BOARD_SIZE; i++) {
                for (int j = 0; j < Board.BOARD_SIZE; j++) {
                    if (cells[i][j].getPlayer() == null) {
                        cells[i][j].setPlayer(aiPlayer);
                        // 開始遞迴，從深度 0 開始，下一步是輪到對手 (isMaximizing = false)
                        int score = minimax(board, 0, false, aiPlayer);
                        cells[i][j].setPlayer(null); // 恢復棋盤狀態

                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = new Point(i, j);
                        }
                    }
                }
            }
            return bestMove;
        }

        private int minimax(Board board, int depth, boolean isMaximizing, Player aiPlayer) {
            Player opponent = (aiPlayer == Player.X) ? Player.O : Player.X;

            // 終止條件：檢查是否有贏家或平局
            if (board.hasWinner()) {
                if (board.getWinner() == aiPlayer) {
                    return 10 - depth; // AI 獲勝，分數越高越好，且越快贏分數越高
                } else {
                    return depth - 10; // 對手獲勝，分數越低越好，且越晚輸分數越高
                }
            }
            if (board.isDraw()) {
                return 0; // 平局
            }


            if (isMaximizing) { // AI 的回合 (最大化分數)
                int bestScore = Integer.MIN_VALUE;
                for (int i = 0; i < Board.BOARD_SIZE; i++) {
                    for (int j = 0; j < Board.BOARD_SIZE; j++) {
                        if (board.getCells()[i][j].getPlayer() == null) {
                            board.getCells()[i][j].setPlayer(aiPlayer);
                            int score = minimax(board, depth + 1, false, aiPlayer);
                            board.getCells()[i][j].setPlayer(null);
                            bestScore = Math.max(score, bestScore);
                        }
                    }
                }
                return bestScore;
            } else { // 對手的回合 (最小化分數)
                int bestScore = Integer.MAX_VALUE;
                for (int i = 0; i < Board.BOARD_SIZE; i++) {
                    for (int j = 0; j < Board.BOARD_SIZE; j++) {
                        if (board.getCells()[i][j].getPlayer() == null) {
                            board.getCells()[i][j].setPlayer(opponent);
                            int score = minimax(board, depth + 1, true, aiPlayer);
                            board.getCells()[i][j].setPlayer(null);
                            bestScore = Math.min(score, bestScore);
                        }
                    }
                }
                return bestScore;
            }
        }
    };


    public abstract Point findBestMove(Board board, Player aiPlayer);

}
