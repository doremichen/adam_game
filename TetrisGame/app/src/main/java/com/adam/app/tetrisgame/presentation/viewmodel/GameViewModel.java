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
import com.adam.app.tetrisgame.domain.model.TetrisBoard;
import com.adam.app.tetrisgame.domain.usecase.ScoreUseCase;
import com.adam.app.tetrisgame.domain.usecase.SettingsUseCase;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@HiltViewModel
public class GameViewModel extends ViewModel {
    private final MutableLiveData<Integer> mCurrentScore = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> mHighScore = new MutableLiveData<>(0);
    private TetrisBoard mTetrisBoard;
    private boolean mRunning = true;

    private final ScoreUseCase mScoreUseCase;
    private final SettingsUseCase mSettingsUseCase;

    @Inject
    public GameViewModel(ScoreUseCase scoreUseCase, SettingsUseCase settingsUseCase) {
        this.mScoreUseCase = scoreUseCase;
        this.mSettingsUseCase = settingsUseCase;
        mHighScore.setValue((Integer) settingsUseCase.execute(SettingsUseCase.Action.GET_HIGH_SCORE, null));
    }

    public void initTetrisBoard(TetrisBoard.GameListener listener) {
        if (this.mTetrisBoard != null) {
            throw new IllegalStateException("TetrisBoard already initialized");
        }
        this.mTetrisBoard = new TetrisBoard(listener);
    }

    public TetrisBoard getTetrisBoard() {
        return mTetrisBoard;
    }

    public void updateTetrisBoard() {
        if (mTetrisBoard != null && mRunning) {
            mTetrisBoard.update();
        }
    }

    public void reset() {
        if (mTetrisBoard != null) mTetrisBoard.reset();
    }

    public boolean isRunning() {
        return mRunning;
    }

    public void setRunning(boolean running) {
        this.mRunning = running;
    }

    public LiveData<Integer> getCurrentScore() {
        return mCurrentScore;
    }

    public LiveData<Integer> getHighScore() {
        return mHighScore;
    }

    public int getSpeedInt() {
        return (Integer) mSettingsUseCase.execute(SettingsUseCase.Action.GET_SPEED, null);
    }

    public boolean isSoundEffectEnabled() {
        return (Boolean) mSettingsUseCase.execute(SettingsUseCase.Action.IS_SOUND_ENABLED, null);
    }

    public void increaseScore(int value) {
        int current = mCurrentScore.getValue() != null ? mCurrentScore.getValue() : 0;
        int newScore = current + value;
        mCurrentScore.postValue(newScore);
        
        int highScore = mHighScore.getValue() != null ? mHighScore.getValue() : 0;
        if (newScore > highScore) {
            mHighScore.postValue(newScore);
        }
    }

    public void resetScore() {
        mCurrentScore.setValue(0);
    }

    public void saveScore() {
        int score = mCurrentScore.getValue() != null ? mCurrentScore.getValue() : 0;
        mScoreUseCase.execute(ScoreUseCase.Action.SAVE_SCORE, score, null);
        mSettingsUseCase.execute(SettingsUseCase.Action.SAVE_HIGH_SCORE, score);
    }
}
