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

package com.adam.app.racinggame2d.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.racinggame2d.application.LeaderboardUseCase;
import com.adam.app.racinggame2d.domain.entity.LeaderboardRecord;
import com.adam.app.racinggame2d.util.Utils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the LeaderBoardActivity.
 */
@HiltViewModel
public final class LeaderBoardViewModel extends ViewModel {
    private static final String sTAG = "LeaderBoardViewModel";
    private final LeaderboardUseCase mUseCase;
    private final MutableLiveData<List<LeaderboardRecord>> mScoresLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mIsEmpty = new MutableLiveData<>(false);

    @Inject
    public LeaderBoardViewModel(LeaderboardUseCase useCase) {
        this.mUseCase = useCase;
    }

    public LiveData<List<LeaderboardRecord>> getScoresLiveData() {
        return mScoresLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }

    public LiveData<Boolean> getIsEmpty() {
        return mIsEmpty;
    }

    public void loadScores() {
        Utils.logDebug(sTAG, "loadScores");
        mIsLoading.setValue(true);
        mUseCase.execute(LeaderboardUseCase.Action.GET_ALL_SCORES, (LeaderboardUseCase.Callback) list -> {
            mScoresLiveData.postValue(list);
            mIsEmpty.postValue(list == null || list.isEmpty());
            mIsLoading.postValue(false);
        });
    }

    public void addScore(String name, int score) {
        Utils.logDebug(sTAG, "addScore");
        mUseCase.execute(LeaderboardUseCase.Action.ADD_SCORE, new LeaderboardRecord(0, name, score, String.valueOf(System.currentTimeMillis())));
    }
}
