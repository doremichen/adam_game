/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the snake game view model
 * <p>
 * Author: Adam Chen
 * Date: 2025/09/24
 */
package com.adam.app.snake.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.snake.Utils;
import com.adam.app.snake.model.SnakeGame;
import com.adam.app.snake.model.SpecialFood;
import com.adam.app.snake.store.file.SharedPreferenceManager;

import java.util.List;

public class SnakeViewModel extends ViewModel {

    private static final long INITIAL_UPDATE_INTERVAL = 300L;
    private static final long MIN_UPDATE_INTERVAL = 100L; // min update interval
    private static final long SPEED_STEP = 20L; // speed step

    // TAG SnakeViewModel
    private static final String TAG = "SnakeViewModel";
    // live data
    private final MutableLiveData<List<Point>> mGameLiveData = new MutableLiveData<>();
    private final MutableLiveData<Point> mFoodLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<SpecialFood>> mSpecialFoodsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> mScoreLiveData = new MutableLiveData<>();
    private final MutableLiveData<SnakeGame.GameState> mGameStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mSnakeInvisibleLiveData = new MutableLiveData<>();
    public long mUpdateInterval = INITIAL_UPDATE_INTERVAL;
    private int mLastScore = 0;
    // SnakeGame model: mGame
    private SnakeGame mGame;
    // GameLoop
    private GameLoop mGameLoop;
    // Activity context;
    private Activity mContext;


    // Game listener
    private SnakeGame.GameSpeedListener mGameSpeedListener = new SnakeGame.GameSpeedListener() {
        @Override
        public void onGameSpeedUp() {
            Utils.logDebug(TAG, "onSpeedUp");
            accelerate();
        }

        @Override
        public void onGameSlowDown() {
            Utils.logDebug(TAG, "onSpeedDown");
            decelerate();
        }

        @Override
        public void onSnakeInVisible() {
            // start timer to hide snake
            GameLoop timer = new GameLoop(() -> {
               mGame.setInvisible(false); // show snake
            });
            timer.start(3000L);

        }

        @Override
        public void onSnakeInvincible() {
            // start timer to disable snake invincible
            GameLoop timer = new GameLoop(() -> {
                mGame.setInvincible(false); // disable snake invincible
            });
            timer.start(8000L);
        }

        @Override
        public void onShowSpecialFood(String type) {
            // show toast
            Utils.showToast(mContext, "Special Food: " + type);
        }

    };


    /**
     * initial game with rows and columns
     *
     * @param rows:     int
     * @param columns:  int
     * @param activity: Activity
     */
    public void initGame(int rows, int columns, Activity activity) {
        Utils.logDebug(TAG, "initGame rows: " + rows + ", columns: " + columns);
        mContext = activity;

        mGame = new SnakeGame(rows, columns);

        // set game speed listener
        mGame.setGameSpeedListener(mGameSpeedListener);

        // update config data
        updateConfigData(activity);

        // initial Game loop
        mGameLoop = new GameLoop(() -> {
            if (mGame.getGameState() == SnakeGame.GameState.RUNNING) {
                mGame.update();
                updateLiveData();
                // start game loop again
                mGameLoop.start(mUpdateInterval);
            }
        });

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
        mGame.start();
        mGameLoop.start(mUpdateInterval);
    }

    /**
     * Resume game
     */
    public void resumeGame(Activity activity) {
        Utils.logDebug(TAG, "resumeGame");
        if (Utils.isNull(mGame)) {
            // log error
            Utils.logDebug(TAG, "startGame: game is not initialized yet!!!");
            return;
        }

        updateConfigData(activity);
        mGame.start();
        mGameLoop.start(mUpdateInterval);
    }

    private void updateConfigData(Activity activity) {
        String WrapMode = SharedPreferenceManager.Keys.WRAP_MODE;
        boolean isWrap = SharedPreferenceManager.getInstance(activity).getBoolean(WrapMode, false);
        // set wrap enabled
        mGame.setWrapEnabled(isWrap);

        String SpecialFood = SharedPreferenceManager.Keys.SPECIAL_FOOD;
        boolean isSpecialFood = SharedPreferenceManager.getInstance(activity).getBoolean(SpecialFood, false);
        // set special food enabled
        mGame.setSpecialFoodEnabled(isSpecialFood);

        String MultiSpecialFood = SharedPreferenceManager.Keys.MULTI_FOODS_SHOW;
        boolean isMultiSpecialFood = SharedPreferenceManager.getInstance(activity).getBoolean(MultiSpecialFood, false);
        // set special food enabled
        mGame.allowMultiSpecialFood(isMultiSpecialFood);

        // get speed mode
        int speedMode = SharedPreferenceManager.getInstance(activity).getInt(SharedPreferenceManager.Keys.SPECIAL_FREQ, 0);
        SpeedLevel speedLevel = SpeedLevel.fromId(speedMode);
        if (speedLevel != null) {
            mUpdateInterval = speedLevel.toValue();
        }
    }


    /**
     * stop game
     */
    public void stopGame() {
        Utils.logDebug(TAG, "stopGame");
        mGame.stop();
        mGameLoop.stop();
    }

    /**
     * update live data
     */
    private void updateLiveData() {
        mGameLiveData.setValue(mGame.getSnake());
        mFoodLiveData.setValue(mGame.getFood());
        mSpecialFoodsLiveData.setValue(mGame.getSpecialFoods());
        mScoreLiveData.setValue(mGame.getScore());
        mGameStateLiveData.setValue(mGame.getGameState());
        mSnakeInvisibleLiveData.setValue(mGame.isInvisible());

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

    private void decelerate() {
        Utils.logDebug(TAG, "decelerate");
        mUpdateInterval = Math.min(INITIAL_UPDATE_INTERVAL, mUpdateInterval + SPEED_STEP);
        Utils.logDebug(TAG, "decelerate: mUpdateInterval: " + mUpdateInterval);
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
        // reset interval
        mUpdateInterval = INITIAL_UPDATE_INTERVAL;
        mLastScore = 0;
        mGame.reset();
        startGame();
        updateLiveData();
    }

    /**
     * clear handler
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        Utils.logDebug(TAG, "onCleared");
        mGameLoop.stop();
        mGame = null;
        mGameLoop = null;
        mGameSpeedListener = null;
        mGameLiveData.setValue(null);
        mFoodLiveData.setValue(null);
        mSpecialFoodsLiveData.setValue(null);
        mScoreLiveData.setValue(null);
        mGameStateLiveData.setValue(null);

    }


    // live data getter
    public MutableLiveData<List<Point>> getGameLiveData() {
        return mGameLiveData;
    }

    public MutableLiveData<Point> getFoodLiveData() {
        return mFoodLiveData;
    }


    public MutableLiveData<List<SpecialFood>> getSpecialFoodsLiveData() {
        return mSpecialFoodsLiveData;
    }

    public MutableLiveData<Integer> getScoreLiveData() {
        return mScoreLiveData;
    }

    public MutableLiveData<SnakeGame.GameState> getGameStateLiveData() {
        return mGameStateLiveData;
    }

    public MutableLiveData<Boolean> getSnakeInvisibleLiveData() {
        return mSnakeInvisibleLiveData;
    }

    /**
     * enum SpeedLevel
     * id: int
     * Low, Middle, High
     */
    private static enum SpeedLevel {
        Low(0) {
            @Override
            long toValue() {
                return LOW_VALUE;
            }
        },
        Middle(1) {
            @Override
            long toValue() {
                return MIDDLE_VALUE;
            }
        },
        High(2) {
            @Override
            long toValue() {
                return HIGH_VALUE;
            }
        };

        private static final long LOW_VALUE = 300L;
        private static final long MIDDLE_VALUE = 150L;
        private static final long HIGH_VALUE = 100L;


        private int mId;
        private SpeedLevel(int id) {
            mId = id;
        }

        abstract long toValue();

        /**
         * get id
         *
         * @param id int
         * @return SpeedLevel
         */
        public static SpeedLevel fromId(int id) {
            for (SpeedLevel level : values()) {
                if (level.mId == id) {
                    return level;
                }
            }
            return null;
        }
    }


}
