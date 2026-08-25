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

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.adam.app.tetrisgame.data.local.ScoreRecord;
import com.adam.app.tetrisgame.domain.usecase.ScoreUseCase;
import dagger.hilt.android.lifecycle.HiltViewModel;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class LeaderboardViewModel extends ViewModel {
    private final MutableLiveData<List<ScoreRecord>> mScores = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);
    private final ScoreUseCase mScoreUseCase;

    @Inject
    public LeaderboardViewModel(ScoreUseCase scoreUseCase) {
        this.mScoreUseCase = scoreUseCase;
    }

    public LiveData<List<ScoreRecord>> getScores() {
        return mScores;
    }

    public LiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    public void loadScores() {
        mIsLoading.setValue(true);
        mScoreUseCase.execute(ScoreUseCase.Action.GET_TOP_SCORES, null, result -> {
            @SuppressWarnings("unchecked")
            List<ScoreRecord> scores = (List<ScoreRecord>) result;
            mScores.postValue(scores);
            mIsLoading.postValue(false);
        });
    }
}
