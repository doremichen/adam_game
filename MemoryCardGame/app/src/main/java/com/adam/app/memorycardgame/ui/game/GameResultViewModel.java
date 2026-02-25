/**
 * Copyrights © 2020 Adam. All rights reserved.
 * <p>
 * Description: This is Game Result ViewModel
 * </p>
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/24
 */
package com.adam.app.memorycardgame.ui.game;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameResultViewModel extends ViewModel {

    private final MutableLiveData<Long> mPlayTime = new MutableLiveData<>(0L);
    private final MutableLiveData<Integer> mMatchCount = new MutableLiveData<>(0);
    private final MutableLiveData<String> mTheme = new MutableLiveData<>("system");

    public void setData(long playTime, int matchCount, String theme) {
        mPlayTime.setValue(playTime);
        mMatchCount.setValue(matchCount);
        mTheme.setValue(theme);
    }

    // --- get live data ---
    public LiveData<Long> getPlayTime() {
        return mPlayTime;
    }

    public LiveData<Integer> getMatchCount() {
        return mMatchCount;
    }

    public LiveData<String> getTheme() {
        return mTheme;
    }

}