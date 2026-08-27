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
package com.adam.app.snake.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.snake.domain.usecase.SettingUseCase;
import com.adam.app.snake.util.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for Welcome screen handling navigation events.
 * Removed EXIT event as per modern UI practices.
 */
@HiltViewModel
public class WelcomeViewModel extends ViewModel {

    /**
     * Navigation events for WelcomeActivity
     */
    public enum NavigationEvent {
        START_GAME,
        SHOW_LEADERBOARD,
        SHOW_ABOUT
    }

    private final SettingUseCase mSettingUseCase;
    private final SingleLiveEvent<NavigationEvent> mNavigationEvent = new SingleLiveEvent<>();

    @Inject
    public WelcomeViewModel(SettingUseCase settingUseCase) {
        this.mSettingUseCase = settingUseCase;
    }

    /**
     * Get navigation event LiveData
     * @return LiveData<NavigationEvent>
     */
    public LiveData<NavigationEvent> getNavigationEvent() {
        return mNavigationEvent;
    }

    /**
     * Handle Start Game click
     */
    public void onStartClick() {
        mNavigationEvent.setValue(NavigationEvent.START_GAME);
    }

    /**
     * Handle Leaderboard click
     */
    public void onLeaderboardClick() {
        mNavigationEvent.setValue(NavigationEvent.SHOW_LEADERBOARD);
    }

    /**
     * Handle About click
     */
    public void onAboutClick() {
        mNavigationEvent.setValue(NavigationEvent.SHOW_ABOUT);
    }

    /**
     * Save user name
     * @param name String
     */
    public void saveUserName(String name) {
        mSettingUseCase.saveSetting(SettingUseCase.SettingKey.USER_NAME, name);
    }
}
