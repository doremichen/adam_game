/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.presentation.viewmodel;

import android.graphics.Point;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.snake.domain.model.SnakeGame;
import com.adam.app.snake.domain.model.SpecialFood;
import com.adam.app.snake.domain.usecase.LeaderboardUseCase;
import com.adam.app.snake.domain.usecase.SettingUseCase;
import com.adam.app.snake.util.Utils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * Snake game view model - Refactored for better Timer management
 */
@HiltViewModel
public class SnakeViewModel extends ViewModel {

    private static final long INITIAL_UPDATE_INTERVAL = 300L;
    private static final long MIN_UPDATE_INTERVAL = 100L;
    private static final long SPEED_STEP = 20L;
    private static final long INVISIBLE_DURATION = 3000L;
    private static final long INVINCIBLE_DURATION = 8000L;

    private static final String TAG = "SnakeViewModel";
    
    private final MutableLiveData<List<Point>> mGameLiveData = new MutableLiveData<>();
    private final MutableLiveData<Point> mFoodLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<SpecialFood>> mSpecialFoodsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> mScoreLiveData = new MutableLiveData<>();
    private final MutableLiveData<SnakeGame.GameState> mGameStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mSnakeInvisibleLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> mSpecialFoodToastLiveData = new MutableLiveData<>();
    
    private long mUpdateInterval = INITIAL_UPDATE_INTERVAL;
    private int mLastScore = 0;
    
    private SnakeGame mGame;
    
    // Dedicated managed timers to prevent memory leaks and redundant tasks
    private GameLoop mMainGameLoop;
    private GameLoop mInvisibilityTimer;
    private GameLoop mInvincibilityTimer;
    
    private final LeaderboardUseCase mUseCase;
    private final SettingUseCase mSettingUseCase;

    // Game listener
    private SnakeGame.GameListener mGameListener = new SnakeGame.GameListener() {
        @Override
        public void onGameSpeedUp() {
            accelerate();
        }

        @Override
        public void onGameSlowDown() {
            decelerate();
        }

        @Override
        public void onSnakeInVisible() {
            if (mInvisibilityTimer != null) {
                mInvisibilityTimer.start(INVISIBLE_DURATION);
            }
        }

        @Override
        public void onSnakeInvincible() {
            if (mInvincibilityTimer != null) {
                mInvincibilityTimer.start(INVINCIBLE_DURATION);
            }
        }

        @Override
        public void onShowSpecialFood(int resId) {
            mSpecialFoodToastLiveData.setValue(resId);
        }

    };

    @Inject
    public SnakeViewModel(LeaderboardUseCase useCase, SettingUseCase settingUseCase) {
        this.mUseCase = useCase;
        this.mSettingUseCase = settingUseCase;
    }


    /**
     * initial game with rows and columns
     */
    public void initGame(int rows, int columns) {
        Utils.logDebug(TAG, "initGame rows: " + rows + ", columns: " + columns);

        mGame = new SnakeGame(rows, columns);
        mGame.setGameListener(mGameListener);

        updateConfigData();
        initializeTimers();
        
        updateLiveData();
        startGame();
    }

    private void initializeTimers() {
        // Main Game Loop
        mMainGameLoop = new GameLoop(() -> {
            if (mGame != null && mGame.getGameState() == SnakeGame.GameState.RUNNING) {
                mGame.update();
                syncGameProgress();
                updateLiveData();
                mMainGameLoop.start(mUpdateInterval);
            }
        });

        // Invisibility Effect Timer
        mInvisibilityTimer = new GameLoop(() -> {
            if (mGame != null) {
                mGame.setInvisible(false);
                updateLiveData();
            }
        });

        // Invincibility Effect Timer
        mInvincibilityTimer = new GameLoop(() -> {
            if (mGame != null) {
                mGame.setInvincible(false);
                updateLiveData();
            }
        });
    }

    private void startGame() {
        if (mGame != null) {
            mGame.start();
        }
        if (mMainGameLoop != null) {
            mMainGameLoop.start(mUpdateInterval);
        }
    }

    public void resumeGame() {
        if (mGame == null) return;
        updateConfigData();
        mGame.start();
        if (mMainGameLoop != null) {
            mMainGameLoop.start(mUpdateInterval);
        }
    }

    private void updateConfigData() {
        if (mGame == null) return;
        mGame.setWrapEnabled(mSettingUseCase.getSetting(SettingUseCase.SettingKey.WRAP_MODE));
        mGame.setSpecialFoodEnabled(mSettingUseCase.getSetting(SettingUseCase.SettingKey.SPECIAL_FOOD));
        mGame.allowMultiSpecialFood(mSettingUseCase.getSetting(SettingUseCase.SettingKey.MULTI_FOODS_SHOW));

        int speedMode = mSettingUseCase.getSetting(SettingUseCase.SettingKey.SPECIAL_FREQ);
        SpeedLevel speedLevel = SpeedLevel.fromId(speedMode);
        if (speedLevel != null) {
            mUpdateInterval = speedLevel.toValue();
        }
    }

    public void stopGame() {
        if (mGame != null) mGame.stop();
        stopAllTimers();
    }
    
    private void stopAllTimers() {
        if (mMainGameLoop != null) mMainGameLoop.stop();
        if (mInvisibilityTimer != null) mInvisibilityTimer.stop();
        if (mInvincibilityTimer != null) mInvincibilityTimer.stop();
    }

    public void saveGameScore(String name) {
        if (mGame != null) {
            mUseCase.saveScore(name, mGame.getScore());
        }
    }

    public String getUserName() {
        return mSettingUseCase.getSetting(SettingUseCase.SettingKey.USER_NAME);
    }

    private void updateLiveData() {
        if (mGame != null) {
            mGameLiveData.setValue(mGame.getSnake());
            mFoodLiveData.setValue(mGame.getFood());
            mSpecialFoodsLiveData.setValue(mGame.getSpecialFoods());
            mScoreLiveData.setValue(mGame.getScore());
            mGameStateLiveData.setValue(mGame.getGameState());
            mSnakeInvisibleLiveData.setValue(mGame.isInvisible());
        }
    }

    private void syncGameProgress() {
        if (mGame != null) {
            int currentScore = mGame.getScore();
            if (currentScore > mLastScore) {
                accelerate();
                mLastScore = currentScore;
            }
        }
    }

    private void accelerate() {
        mUpdateInterval = Math.max(MIN_UPDATE_INTERVAL, mUpdateInterval - SPEED_STEP);
    }

    private void decelerate() {
        mUpdateInterval = Math.min(INITIAL_UPDATE_INTERVAL, mUpdateInterval + SPEED_STEP);
    }

    public void setDirection(SnakeGame.Direction direction) {
        if (mGame != null) mGame.setDirection(direction);
    }

    public void resetGame() {
        mUpdateInterval = INITIAL_UPDATE_INTERVAL;
        mLastScore = 0;
        if (mGame != null) mGame.reset();
        stopAllTimers();
        startGame();
        updateLiveData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopAllTimers();
        mGame = null;
        mMainGameLoop = null;
        mInvisibilityTimer = null;
        mInvincibilityTimer = null;
        mGameListener = null;
    }

    public LiveData<List<Point>> getGameLiveData() { return mGameLiveData; }
    public LiveData<Point> getFoodLiveData() { return mFoodLiveData; }
    public LiveData<List<SpecialFood>> getSpecialFoodsLiveData() { return mSpecialFoodsLiveData; }
    public LiveData<Integer> getScoreLiveData() { return mScoreLiveData; }
    public LiveData<SnakeGame.GameState> getGameStateLiveData() { return mGameStateLiveData; }
    public LiveData<Boolean> getSnakeInvisibleLiveData() { return mSnakeInvisibleLiveData; }
    public LiveData<Integer> getSpecialFoodToastLiveData() { return mSpecialFoodToastLiveData; }

    private enum SpeedLevel {
        Low(0, 300L), Middle(1, 150L), High(2, 100L);
        private final int mId;
        private final long mValue;
        SpeedLevel(int id, long value) { mId = id; mValue = value; }
        public static SpeedLevel fromId(int id) {
            for (SpeedLevel level : values()) { if (level.mId == id) return level; }
            return null;
        }
        long toValue() { return mValue; }
    }
}
