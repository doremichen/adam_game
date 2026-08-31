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

package com.adam.app.racinggame2d.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.racinggame2d.application.SettingsUseCase;
import com.adam.app.racinggame2d.util.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the MainActivity.
 * Adheres to AS-02: Interacts only with UseCases.
 */
@HiltViewModel
public final class MainViewModel extends ViewModel {
    private final SettingsUseCase mSettingsUseCase;
    private final SingleLiveEvent<NavigateEvent> mNavigateEvent = new SingleLiveEvent<>();

    @Inject
    public MainViewModel(SettingsUseCase settingsUseCase) {
        this.mSettingsUseCase = settingsUseCase;
    }

    public LiveData<NavigateEvent> getNavigateEvent() {
        return mNavigateEvent;
    }

    public String getPlayerName() {
        return mSettingsUseCase.execute(SettingsUseCase.Action.GET_PLAYER_NAME, null);
    }

    public void setPlayerName(String playerName) {
        mSettingsUseCase.execute(SettingsUseCase.Action.SET_PLAYER_NAME, playerName);
    }

    public void navigateToStartGame() {
        mNavigateEvent.setValue(NavigateEvent.START_GAME);
    }

    public void navigateToSetting() {
        mNavigateEvent.setValue(NavigateEvent.SETTING);
    }

    public void navigateToLeaderBoard() {
        mNavigateEvent.setValue(NavigateEvent.LEADER_BOARD);
    }

    public void navigateToAbout() {
        mNavigateEvent.setValue(NavigateEvent.ABOUT);
    }

    public enum NavigateEvent {
        START_GAME, SETTING, LEADER_BOARD, ABOUT
    }
}
