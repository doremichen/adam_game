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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * View model for the Main screen.
 */
@HiltViewModel
public class MainViewModel extends ViewModel {

    private final MutableLiveData<NavigationDestination> mNavigateTo = new MutableLiveData<>(NavigationDestination.NONE);

    @Inject
    public MainViewModel() {
    }

    @NonNull
    public LiveData<NavigationDestination> getNavigateTo() {
        return mNavigateTo;
    }

    public void onStartGameClicked() {
        mNavigateTo.setValue(NavigationDestination.START_GAME);
    }

    public void onSettingsClicked() {
        mNavigateTo.setValue(NavigationDestination.SETTINGS);
    }

    public void onAboutClicked() {
        mNavigateTo.setValue(NavigationDestination.ABOUT);
    }

    public void onExitClicked() {
        mNavigateTo.setValue(NavigationDestination.EXIT);
    }

    public void onNavigationDone() {
        mNavigateTo.setValue(NavigationDestination.NONE);
    }

    /**
     * Navigation destinations.
     */
    public enum NavigationDestination {
        START_GAME,
        SETTINGS,
        ABOUT,
        EXIT,
        NONE
    }
}
