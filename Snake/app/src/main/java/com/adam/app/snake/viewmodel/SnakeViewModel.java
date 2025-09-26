/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the snake game view model
 * <p>
 * Author: Adam Chen
 * Date: 2025/09/24
 */
package com.adam.app.snake.viewmodel;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.snake.Utils;
import com.adam.app.snake.model.SnakeGame;

import java.util.List;

public class SnakeViewModel extends ViewModel {

    private static final long INITIAL_UPDATE_INTERVAL = 300L;
    private static final long MIN_UPDATE_INTERVAL = 100L; // min update interval
    private static final long SPEED_STEP = 20L; // speed step

    // TAG SnakeViewModel
    private static final String TAG = "SnakeViewModel";

    public long mUpdateInterval = INITIAL_UPDATE_INTERVAL;

    private int mLastScore = 0;

    // Game Handler
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    // live data
    private final MutableLiveData<List<int[]>> mGameLiveData = new MutableLiveData<>();
    private final MutableLiveData<int[][]> mFoodLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> mScoreLiveData = new MutableLiveData<>();
    private final MutableLiveData<SnakeGame.GameState> mGameStateLiveData = new MutableLiveData<>();
    // SnakeGame model: mGame
    private SnakeGame mGame;
    // Runnable
    private Runnable mGameRunnable;

    /**
     * initial game with rows and columns
     *
     * @param rows
     * @param columns
     * @param isWrap
     */
    public void initGame(int rows, int columns, boolean isWrap) {
        Utils.logDebug(TAG, "initGame rows: " + rows + ", columns: " + columns);
        mGame = new SnakeGame(rows, columns);

        // set wrap enabled
        mGame.setWrapEnabled(isWrap);

        mGameRunnable = new Runnable() {
            @Override
            public void run() {
                Utils.logDebug(TAG, "run");
                // check game state is running
                if (mGame.getGameState() != SnakeGame.GameState.RUNNING) {
                    Utils.logDebug(TAG, "run: game is not running");
                    return;
                }


                mGame.update();
                updateLiveData();
                if (mGame.getGameState() == SnakeGame.GameState.RUNNING) {
                    Utils.logDebug(TAG, "run: game is running");
                    mHandler.postDelayed(this, mUpdateInterval);
                }
            }
        };

        // update live data
        updateLiveData();
        // start game
        startGame();

    }

    /**
     * start game
     */
    private void startGame() {
        Utils.logDebug(TAG, "startGame");

        mHandler.postDelayed(mGameRunnable, mUpdateInterval);
    }

    /**
     * stop game
     */
    public void stopGame() {
        Utils.logDebug(TAG, "stopGame");
        mGame.stop();
        mHandler.removeCallbacks(mGameRunnable);
    }

    /**
     * update live data
     */
    private void updateLiveData() {
        mGameLiveData.setValue(mGame.getSnake());
        mFoodLiveData.setValue(mGame.getFood());
        mScoreLiveData.setValue(mGame.getScore());
        mGameStateLiveData.setValue(mGame.getGameState());

        // check if score is changed -> speed up
        int currentScore = mGame.getScore();
        if (currentScore > mLastScore) {
            accelerate();
            mLastScore = currentScore;
        }

    }

    private void accelerate() {
        Utils.logDebug(TAG, "accelerate");
        mUpdateInterval = Math.max(MIN_UPDATE_INTERVAL, mUpdateInterval - SPEED_STEP);
        Utils.logDebug(TAG, "accelerate: mUpdateInterval: " + mUpdateInterval);
    }

    /**
     * set direction of snake
     */
    public void setDirection(SnakeGame.Direction direction) {
        mGame.setDirection(direction);
    }

    /**
     * reset game
     */
    public void resetGame() {
        mGame.reset();
        updateLiveData();
        startGame();
    }

    /**
     * set wrap enabled
     */
    public void setWrapEnabled(boolean enabled) {
        mGame.setWrapEnabled(enabled);
    }

    /**
     * clear handler
     *
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        Utils.logDebug(TAG, "onCleared");
        mHandler.removeCallbacks(mGameRunnable);
    }


    // live data getter
    public MutableLiveData<List<int[]>> getGameLiveData() {
        return mGameLiveData;
    }

    public MutableLiveData<int[][]> getFoodLiveData() {
        return mFoodLiveData;
    }

    public MutableLiveData<Integer> getScoreLiveData() {
        return mScoreLiveData;
    }

    public MutableLiveData<SnakeGame.GameState> getGameStateLiveData() {
        return mGameStateLiveData;
    }

}
