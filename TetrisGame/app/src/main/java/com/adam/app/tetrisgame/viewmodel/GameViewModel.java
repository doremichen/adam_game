/*
 * Copyright (c) 2025 Adam
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
package com.adam.app.tetrisgame.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.tetrisgame.Utils;
import com.adam.app.tetrisgame.data.ScoreRepository;
import com.adam.app.tetrisgame.manager.SettingsManager;
import com.adam.app.tetrisgame.model.ScoreRecord;
import com.adam.app.tetrisgame.model.TetrisBoard;
import com.adam.app.tetrisgame.view.TetrisView;

public class GameViewModel extends ViewModel {
    public static final int MAX_NUM = 100;
    public static final int RESERVE_NUM = 99;
    // MutableLiveData currentScore and highScore
    private final MutableLiveData<Integer> mCurrentScore = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> mHighScore = new MutableLiveData<>(0);
    // TetrisBoard
    private TetrisBoard mTetrisBoard;
    // running flag
    private boolean mRunning = true;

    private SettingsManager mSettingMgr;
    private ScoreRepository mScoreRepository;

    // init tetris board method
    public void initTetrisBoard(TetrisBoard.GameListener listener) {
        if (this.mTetrisBoard != null) {
            throw new IllegalStateException("TetrisBoard has already been initialized!");
        }

        this.mTetrisBoard = new TetrisBoard(listener);
    }

    // get board
    public TetrisBoard getTetrisBoard() {
        return this.mTetrisBoard;
    }

    // update board
    public void updateTetrisBoard() {
        if (this.mTetrisBoard == null) {
            Utils.log("TetrisBoard is null");
            return;
        }
        if (!mRunning) {
            return;
        }
        this.mTetrisBoard.update();
    }

    // applyToView with TetrisView
    public void applyToView(TetrisView view) {
        if (this.mTetrisBoard == null || view == null) {
            return;
        }
        this.mTetrisBoard.applyToView(view);
    }

    // reset
    public void reset() {
        if (this.mTetrisBoard == null) {
            return;
        }

        this.mTetrisBoard.reset();
    }

    // is running
    public boolean isRunning() {
        return this.mRunning;
    }

    // set running flag
    public void setRunning(boolean running) {
        this.mRunning = running;
    }

    // get current score
    public MutableLiveData<Integer> getCurrentScore() {
        return mCurrentScore;
    }

    // get high score
    public MutableLiveData<Integer> getHighScore() {
        return mHighScore;
    }


    // get integer speed from shared preferences
    public int getSpeedInt() {
        if (mSettingMgr == null) {
            return 1; // Default speed
        }

        String speed = mSettingMgr.getSpeed();
        try {
            return Integer.parseInt(speed);
        } catch (NumberFormatException e) {
            Utils.log("NumberFormatException in getSpeedInt", e);
        }

        return 1;
    }

    // get sound switch setting from shared preferences
    public boolean isSoundEffectEnabled() {
        if (mSettingMgr == null) {
            return true; // Default enabled
        }

        return mSettingMgr.isSoundEffect();
    }

    public void increaseScore(int value) {
        int current = mCurrentScore.getValue() != null ? mCurrentScore.getValue() : 0;
        int newScore = current + value;
        mCurrentScore.postValue(newScore);
        
        // high score
        int highScore = mHighScore.getValue() != null ? mHighScore.getValue() : 0;
        if (newScore > highScore) {
            mHighScore.postValue(newScore);
        }
    }

    // reset score
    public void resetScore() {
        mCurrentScore.setValue(0);
    }

    // load high score
    public void loadHighScore(Context context) {
        if (mSettingMgr == null) {
            return;
        }

        int highScore = mSettingMgr.getHighScore();
        mHighScore.setValue(highScore);
    }

    /**
     * Save score in database
     */
    public void saveScore(Context context) {
        if (mScoreRepository == null) {
            mScoreRepository = new ScoreRepository(context);
        }
        
        int score = mCurrentScore.getValue() != null ? mCurrentScore.getValue() : 0;
        ScoreRecord record = new ScoreRecord(System.currentTimeMillis(), score);
        
        mScoreRepository.saveScoreAndPrune(record, MAX_NUM, RESERVE_NUM, null);
    }

    public void saveHighScore(Context context) {
        if (mSettingMgr == null) {
            return;
        }

        int highScore = mHighScore.getValue() != null ? mHighScore.getValue() : 0;
        mSettingMgr.setHighScore(highScore);
    }

    // load speed and sound from shared preferences
    public void loadSettings(Context context) {
        mSettingMgr = SettingsManager.getInstance(context);
        mScoreRepository = new ScoreRepository(context);
    }

//    /**
//     * interface GAME_PREFS
//     * fileName: string
//     * highScorekey: string
//     */
//    private interface GAME_PREFS {
//        String fileName = "game_prefs";
//        String highScorekey = "high_score";
//    }

}
