/*
 * MIT License
 *
 * Copyright (c) 2025 Adam Chen
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
package com.adam.app.whack_a_molejava.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.adam.app.whack_a_molejava.util.SingleLiveEvent;

/**
 * This class is the view model for the welcome activity.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-02
 */
public class WelcomeViewModel extends AndroidViewModel {

    private SingleLiveEvent<WelcomeEvent> mEvent = new SingleLiveEvent<WelcomeEvent>();

    public WelcomeViewModel(@NonNull Application application) {
        super(application);
    }

    // --- getter ---
    public SingleLiveEvent<WelcomeEvent> getEvent() {
        return mEvent;
    }

    // --- setter ---
    public void startGame() {
        mEvent.setValue(WelcomeEvent.START_GAME);
    }

    public void setting() {
        mEvent.setValue(WelcomeEvent.SETTING);
    }

    public void about() {
        mEvent.setValue(WelcomeEvent.ABOUT);
    }

    public void exit() {
        mEvent.setValue(WelcomeEvent.EXIT);
    }

    public enum WelcomeEvent {
        START_GAME,
        SETTING,
        ABOUT,
        EXIT
    }


}
