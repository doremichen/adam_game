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

package com.adam.app.tic_tac_toe.domain.strategy;

import android.graphics.Point;

import com.adam.app.tic_tac_toe.domain.entities.Board;
import com.adam.app.tic_tac_toe.domain.entities.Cell;
import com.adam.app.tic_tac_toe.domain.entities.Player;
import com.adam.app.tic_tac_toe.domain.utils.WinnerPattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * AI strategy for the Tic Tac Toe game.
 */
public enum AIStrategy {

    EasyAIStrategy {
        @Nullable
        @Override
        public Point findBestMove(@NonNull Board board, @NonNull Player aiPlayer) {
            List<Point> availableMoves = getAvailableMoves(board);
            if (availableMoves.isEmpty()) {
                return null;
            }
            int randomIndex = (int) (Math.random() * availableMoves.size());
            return availableMoves.get(randomIndex);
        }
    },

    HardAIStrategy {
        @Nullable
        @Override
        public Point findBestMove(@NonNull Board board, @NonNull Player aiPlayer) {
            Cell[][] cells = board.getCells();
            Player opponent = (aiPlayer == Player.X) ? Player.O : Player.X;

            Point winningMove = findPatternMove(cells, aiPlayer);
            if (winningMove != null) {
                return winningMove;
            }

            Point blockMove = findPatternMove(cells, opponent);
            if (blockMove != null) {
                return blockMove;
            }

            if (cells[1][1].getPlayer() == null) {
                return new Point(1, 1);
            }

            return EasyAIStrategy.findBestMove(board, aiPlayer);
        }

        @Nullable
        private Point findPatternMove(@NonNull Cell[][] cells, @NonNull Player player) {
            for (List<Cell> pattern : WinnerPattern.getWinPatterns()) {
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

    @NonNull
    protected List<Point> getAvailableMoves(@NonNull Board board) {
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

    @Nullable
    public abstract Point findBestMove(@NonNull Board board, @NonNull Player aiPlayer);
}
