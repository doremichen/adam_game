package com.adam.app.tetrisgame.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.tetrisgame.GameActivity;
import com.adam.app.tetrisgame.Utils;
import com.adam.app.tetrisgame.model.TetrisBoard;
import com.adam.app.tetrisgame.view.TetrisView;

import java.util.Optional;

public class GameViewModel extends ViewModel{
    public static final String GAME_PREFS = "game_prefs";
    public static final String HIGH_SCORE_KEY = "high_score";
    // TetrisBord
    private TetrisBoard mTetrisBoard;
    // running flag
    private boolean mRunning = true;

    // MutableLiveData currentScore and highScore
    private final MutableLiveData<Integer> mCurrentScore = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> mHighScore = new MutableLiveData<>(0);


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

    // set running flag
    public void setRunning(boolean running) {
        this.mRunning = running;
    }

    // is running
    public boolean isRunning() {
        return this.mRunning;
    }

    // get current score
    public MutableLiveData<Integer> getCurrentScore() {
        return mCurrentScore;
    }

    // get high score
    public MutableLiveData<Integer> getHighScore() {
        return mHighScore;
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
        SharedPreferences prefs = context.getSharedPreferences(GAME_PREFS, Context.MODE_PRIVATE);
        int savedHigh = prefs.getInt(HIGH_SCORE_KEY, 0);
        mHighScore.setValue(savedHigh);
    }


    public void saveHighScore(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(GAME_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(HIGH_SCORE_KEY, mHighScore.getValue());
        editor.apply();
    }
}
