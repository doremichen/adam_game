/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.domain.usecase;

import androidx.lifecycle.LiveData;

import com.adam.app.snake.data.entity.LeaderboardEntry;
import com.adam.app.snake.domain.repository.ILeaderboardRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Leaderboard use case - Refactored with Parameter Object for Type Safety
 */
public class LeaderboardUseCase {

    private final ILeaderboardRepository mRepository;

    @Inject
    public LeaderboardUseCase(ILeaderboardRepository repository) {
        this.mRepository = repository;
    }

    /**
     * Leaderboard operations
     */
    public enum Operation {
        GET_TOP_SCORES,
        SAVE_SCORE,
        CLEAR_ALL
    }

    /**
     * Parameter object to encapsulate operation arguments
     */
    public static class Args {
        private final String mPlayerName;
        private final int mScore;

        public Args(String playerName, int score) {
            this.mPlayerName = playerName;
            this.mScore = score;
        }

        public String getPlayerName() {
            return mPlayerName;
        }

        public int getScore() {
            return mScore;
        }
    }

    /**
     * Execute leaderboard operation
     * @param <T> Return type
     * @param operation Operation
     * @param args Optional Parameter object
     * @return Result of the operation
     */
    @SuppressWarnings("unchecked")
    public <T> T execute(Operation operation, Args args) {
        if (operation == null) return null;

        return switch (operation) {
            case GET_TOP_SCORES -> (T) mRepository.getTopScores();
            case SAVE_SCORE -> {
                if (args != null) {
                    mRepository.insert(new LeaderboardEntry(
                            args.getPlayerName(),
                            args.getScore(),
                            System.currentTimeMillis()
                    ));
                }
                yield null;
            }
            case CLEAR_ALL -> {
                mRepository.clearAll();
                yield null;
            }
        };
    }

    /**
     * Type-safe helper to get top scores
     */
    public LiveData<List<LeaderboardEntry>> getTopScores() {
        return execute(Operation.GET_TOP_SCORES, null);
    }

    /**
     * Type-safe helper to save score
     */
    public void saveScore(String name, int score) {
        execute(Operation.SAVE_SCORE, new Args(name, score));
    }

    /**
     * Type-safe helper to clear leaderboard
     */
    public void clearAll() {
        execute(Operation.CLEAR_ALL, null);
    }
}
