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

package com.adam.app.galaga.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.galaga.utils.GameUtils;

public class MainViewModel extends ViewModel {
    // TAG
    private static final String TAG = MainViewModel.class.getSimpleName();

    // event live data
    private final MutableLiveData<ActionType> mAction = new MutableLiveData<>(ActionType.DO_NOTHING);
    public LiveData<ActionType> getAction() {
        return mAction;
    }


    public void onStartGame() {
        GameUtils.info(TAG, "onStartGame");
        mAction.setValue(ActionType.START_GAME);
    }

    public void onOpenSettings() {
        GameUtils.info(TAG, "onOpenSettings");
        mAction.setValue(ActionType.OPEN_SETTINGS);
    }

    public void onOpenLeaderBoard() {
        GameUtils.info(TAG, "onOpenLeaderBoard");
        mAction.setValue(ActionType.OPEN_LEADER_BOARD);
    }

    public void onOpenAbout() {
        GameUtils.info(TAG, "onOpenAbout");
        mAction.setValue(ActionType.OPEN_ABOUT);
    }

    public void onExit() {
        GameUtils.info(TAG, "onExit");
        mAction.setValue(ActionType.EXIT);
    }

    public void onDoNothing() {
        GameUtils.info(TAG, "onDoNothing");
        mAction.setValue(ActionType.DO_NOTHING);
    }


    public enum ActionType {
        START_GAME,
        OPEN_SETTINGS,
        OPEN_LEADER_BOARD,
        OPEN_ABOUT,
        EXIT,
        DO_NOTHING
    }


}
