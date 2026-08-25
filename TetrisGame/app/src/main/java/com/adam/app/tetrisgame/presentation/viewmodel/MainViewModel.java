/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.presentation.viewmodel;

import androidx.lifecycle.ViewModel;
import com.adam.app.tetrisgame.util.SingleLiveEvent;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@HiltViewModel
public class MainViewModel extends ViewModel {

    public enum Event {
        START_GAME,
        SHOW_LEADERBOARD,
        SHOW_ABOUT,
        EXIT
    }

    private final SingleLiveEvent<Event> mNavigationEvent = new SingleLiveEvent<>();

    @Inject
    public MainViewModel() {
    }

    public SingleLiveEvent<Event> getNavigationEvent() {
        return mNavigationEvent;
    }

    public void onStartClick() {
        mNavigationEvent.setValue(Event.START_GAME);
    }

    public void onLeaderboardClick() {
        mNavigationEvent.setValue(Event.SHOW_LEADERBOARD);
    }

    public void onAboutClick() {
        mNavigationEvent.setValue(Event.SHOW_ABOUT);
    }

    public void onExitClick() {
        mNavigationEvent.setValue(Event.EXIT);
    }
}
