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

package com.adam.app.tic_tac_toe.application.usecases;

import com.adam.app.tic_tac_toe.application.interfaces.GameRepository;
import com.adam.app.tic_tac_toe.domain.entities.Board;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

/**
 * Encapsulates game logic using an Enum-based action system.
 */
public class GameUseCase {

    private final GameRepository mRepository;

    @Inject
    public GameUseCase(@NonNull GameRepository repository) {
        mRepository = repository;
    }

    /**
     * Enum defining available game actions.
     */
    public enum Action {
        GET_SETTINGS,
        SET_GAME_MODE,
        SET_AI_DIFFICULTY,
        RESET_BOARD,
        CHECK_GAME_OVER
    }

    /**
     * Execute a game action.
     *
     * @param action The action to perform.
     * @param params Optional parameters for the action.
     * @return Result of the action.
     */
    @Nullable
    public Object execute(@NonNull Action action, @NonNull Object... params) {
        switch (action) {
            case GET_SETTINGS:
                return new Settings(mRepository.isGameModePve(), mRepository.isAiStrategyHard());
            case SET_GAME_MODE:
                if (params.length > 0 && params[0] instanceof Boolean) {
                    mRepository.setGameModePve((Boolean) params[0]);
                }
                return null;
            case SET_AI_DIFFICULTY:
                if (params.length > 0 && params[0] instanceof Boolean) {
                    mRepository.setAiStrategyHard((Boolean) params[0]);
                }
                return null;
            case RESET_BOARD:
                if (params.length > 0 && params[0] instanceof Board board) {
                    board.reset();
                }
                return null;
            case CHECK_GAME_OVER:
                if (params.length > 0 && params[0] instanceof Board board) {
                    return board.getWinner() != null || board.isDraw();
                }
                return false;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    /**
     * Data class for game settings.
     */
    public static class Settings {
        private final boolean mIsPve;
        private final boolean mIsHardAi;

        public Settings(boolean isPve, boolean isHardAi) {
            mIsPve = isPve;
            mIsHardAi = isHardAi;
        }

        public boolean isPve() {
            return mIsPve;
        }

        public boolean isHardAi() {
            return mIsHardAi;
        }
    }
}
