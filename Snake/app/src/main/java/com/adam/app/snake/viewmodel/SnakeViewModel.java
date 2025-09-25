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
    public static final long UPDATE_GAME_MILLIS = 300L;
    // TAG SnakeViewModel
    private static final String TAG = "SnakeViewModel";
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
     */
    public void initGame(int rows, int columns) {
        Utils.logDebug(TAG, "initGame rows: " + rows + ", columns: " + columns);
        mGame = new SnakeGame(rows, columns);

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
                    mHandler.postDelayed(this, UPDATE_GAME_MILLIS);
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

        mHandler.postDelayed(mGameRunnable, UPDATE_GAME_MILLIS);
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
