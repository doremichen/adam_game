/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adam.app.racinggame2d.domain.ILeaderboardRepository;
import com.adam.app.racinggame2d.domain.entity.LeaderboardRecord;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * UseCase for handling leaderboard-related business processes.
 * Adheres to AS-02: Highly cohesive, type-safe Enum-based UseCases.
 */
@Singleton
public final class LeaderboardUseCase {

    private final ILeaderboardRepository mRepository;

    @Inject
    public LeaderboardUseCase(ILeaderboardRepository repository) {
        this.mRepository = repository;
    }

    /**
     * Executes the requested leaderboard action.
     * Generics are used to automatically infer the return type.
     *
     * @param action The action to perform.
     * @param data   Optional data required for the action.
     * @param <T>    The expected return type.
     * @return The result of the action, or null.
     */
    @Nullable
    public <T> T execute(@NonNull Action action, @Nullable Object data) {
        return action.handle(mRepository, data);
    }

    /**
     * Action Enum encapsulating both the action type and its execution logic.
     * Uses a generic handle method to eliminate casting risks in the execute method.
     */
    public enum Action {
        ADD_SCORE {
            @Override
            public <T> T handle(ILeaderboardRepository repository, Object data) {
                if (data instanceof LeaderboardRecord) {
                    repository.addScore((LeaderboardRecord) data);
                }
                return null;
            }
        },
        GET_ALL_SCORES {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T handle(ILeaderboardRepository repository, Object data) {
                if (data instanceof Callback callback) {
                    repository.getAllScores(callback::onResult);
                }
                return null;
            }
        };

        /**
         * Generic internal handler method to be implemented by each action.
         */
        public abstract <T> T handle(ILeaderboardRepository repository, Object data);
    }

    /**
     * Interface for leaderboard action results.
     */
    public interface Callback {
        void onResult(List<LeaderboardRecord> list);
    }
}
