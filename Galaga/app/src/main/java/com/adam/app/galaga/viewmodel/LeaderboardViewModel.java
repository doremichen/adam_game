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
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.adam.app.galaga.data.local.entities.ScoreRecord;
import com.adam.app.galaga.domain.usecase.GameUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LeaderboardViewModel extends ViewModel {

    private final GameUseCase mGameUseCase;
    private LiveData<List<ScoreRecord>> mTopScores;
    private LiveData<Boolean> mIsEmpty;

    @Inject
    public LeaderboardViewModel(GameUseCase gameUseCase) {
        this.mGameUseCase = gameUseCase;
        loadTopScores();
    }

    public LiveData<List<ScoreRecord>> getTopScores() {
        return mTopScores;
    }

    public LiveData<Boolean> getIsEmpty() {
        return mIsEmpty;
    }

    @SuppressWarnings("unchecked")
    private void loadTopScores() {
        mTopScores = (LiveData<List<ScoreRecord>>) mGameUseCase.execute(
                new GameUseCase.Request(GameUseCase.ActionType.GET_TOP_SCORES, null)
        );
        
        mIsEmpty = Transformations.map(mTopScores, list -> list == null || list.isEmpty());
    }
}
