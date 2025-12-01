/**
 * Copyright 2025 Adam
 * <p>
 * Description: GameViewModel is the viewmodel of the game.
 * <p>
 * Author: Adam Chen
 * Date: 2025/08/15
 */
package com.adam.app.tetrisgame.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.tetrisgame.Utils;
import com.adam.app.tetrisgame.data.ScoreDatabase;
import com.adam.app.tetrisgame.manager.SettingsManager;
import com.adam.app.tetrisgame.model.ScoreDao;
import com.adam.app.tetrisgame.model.ScoreRecord;
import com.adam.app.tetrisgame.model.TetrisBoard;
import com.adam.app.tetrisgame.view.TetrisView;

public class GameViewModel extends ViewModel {
    public static final int MAX_NUM = 100;
    public static final int RESERVE_NUM = 99;
    // MutableLiveData currentScore and highScore
    private final MutableLiveData<Integer> mCurrentScore = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> mHighScore = new MutableLiveData<>(0);
    // TetrisBord
    private TetrisBoard mTetrisBoard;
    // running flag
    private boolean mRunning = true;

    private SettingsManager mSettingMgr;

    // init tetrisbord method
    public void initTetrisBoard(TetrisBoard.GameListener listener) {
        // check this.mTetrisBoard null
        if (this.mTetrisBoard != null) {
            throw new ExceptionInInitializerError("this.mTetrisBoard has been initialized!!!");
        }

        this.mTetrisBoard = new TetrisBoard(listener);
    }

    // get bord
    public TetrisBoard getTetrisBoard() {
        return this.mTetrisBoard;
    }

    // update bord
    public void updateTetrisBoard() {
        // check this.mTetrisBoard null
        if (this.mTetrisBoard == null) {
            Utils.log("this.mTetrisBoard is null");
            return;
        }
        if (!mRunning) {
            Utils.log("mRunning is false");
            return;
        }
        this.mTetrisBoard.update();
    }

    // applyToView with TetrisView
    public void applyToView(TetrisView view) {
        // check this.mTetrisBoard null or view is null
        if (this.mTetrisBoard == null || view == null) {
            Utils.log("this.mTetrisBoard or view is null");
            return;
        }
        this.mTetrisBoard.applyToView(view);
    }

    // reset
    public void reset() {
        // check this.mTetrisBoard null
        if (this.mTetrisBoard == null) {
            Utils.log("this.mTetrisBoard is null");
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


    // get integer speed from sharedeferences
    public int getSpeedInt() {
        if (mSettingMgr == null) {
            throw new ExceptionInInitializerError("mSettingMgr is null");
        }

        String speed = mSettingMgr.getSpeed();
        try {
            return Integer.parseInt(speed);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Utils.log("NumberFormatException", e);
        }

        return 0;
    }

    // get sound switch setting from sharedeferences
    public boolean isSoundEffectEnabled(Context context) {
        if (mSettingMgr == null) {
            throw new ExceptionInInitializerError("mSettingMgr is null");
        }

        return mSettingMgr.isSoundEffect();
    }

    public void increaseScore(int value) {
        int newScore = (mCurrentScore.getValue() != null ? mCurrentScore.getValue() : 0) + value;
        mCurrentScore.setValue(newScore);
        // high score
        int highScore = mHighScore.getValue() != null ? mHighScore.getValue() : 0;
        if (newScore > highScore) {
            mHighScore.setValue(newScore);
        }
    }

    // reset score
    public void resetScore() {
        mCurrentScore.setValue(0);
    }

    // load high score
    public void loadHighScore(Context context) {
        if (mSettingMgr == null) {
            throw new ExceptionInInitializerError("mSettingMgr is null");
        }

        int highScore = mSettingMgr.getHighScore();
        mHighScore.setValue(highScore);

//        SharedPreferences prefs = context.getSharedPreferences(GAME_PREFS.fileName, Context.MODE_PRIVATE);
//        int savedHigh = prefs.getInt(GAME_PREFS.highScorekey, 0);
//        mHighScore.setValue(savedHigh);
    }

    /**
     * Save score in database: score_database
     *
     */
    public void saveScore(Context context) {
        // get database dao
        ScoreDao dao = ScoreDatabase.getDatabase(context).scoreDao();
        // delete lowest scores when the count is equal to 100
        int count = dao.getCount();
        if (count == MAX_NUM) {
            dao.deleteLowestScores(count - RESERVE_NUM);
        }
        // insert score to database
        int score = mCurrentScore.getValue() != null ? mCurrentScore.getValue() : 0;
        ScoreRecord record = new ScoreRecord(System.currentTimeMillis(), score);
        dao.insert(record);
    }

    public void saveHighScore(Context context) {
        if (mSettingMgr == null) {
            throw new ExceptionInInitializerError("mSettingMgr is null");
        }

        int highScore = mHighScore.getValue() != null ? mHighScore.getValue() : 0;
        mSettingMgr.setHighScore(highScore);

//        SharedPreferences prefs = context.getSharedPreferences(GAME_PREFS.fileName, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        int saveHigh = mHighScore.getValue() != null ? mHighScore.getValue() : 0;
//        editor.putInt(GAME_PREFS.highScorekey, saveHigh);
//        editor.apply();
    }

    // load speed and sound from shared preferences: game_settings
    public void loadSettings(Context context) {
        // get setting manager
        mSettingMgr = SettingsManager.getInstance(context);
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
