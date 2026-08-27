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

package com.adam.app.tic_tac_toe.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.adam.app.tic_tac_toe.application.usecases.GameUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * View model for the Settings screen.
 */
@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final GameUseCase mGameUseCase;

    @Inject
    public SettingsViewModel(@NonNull GameUseCase gameUseCase) {
        this.mGameUseCase = gameUseCase;
    }

    public boolean isPveMode() {
        GameUseCase.Settings settings = (GameUseCase.Settings) mGameUseCase.execute(GameUseCase.Action.GET_SETTINGS);
        return settings != null && settings.isPve();
    }

    public void setPveMode(boolean isPve) {
        mGameUseCase.execute(GameUseCase.Action.SET_GAME_MODE, isPve);
    }

    public boolean isHardAi() {
        GameUseCase.Settings settings = (GameUseCase.Settings) mGameUseCase.execute(GameUseCase.Action.GET_SETTINGS);
        return settings != null && settings.isHardAi();
    }

    public void setHardAi(boolean isHard) {
        mGameUseCase.execute(GameUseCase.Action.SET_AI_DIFFICULTY, isHard);
    }
}
