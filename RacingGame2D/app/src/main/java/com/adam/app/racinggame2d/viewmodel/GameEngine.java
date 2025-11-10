/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is responsible for managing the game engine.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/27
 */
package com.adam.app.racinggame2d.viewmodel;

import android.content.Context;
import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.model.entity.Car;
import com.adam.app.racinggame2d.model.entity.Obstacle;
import com.adam.app.racinggame2d.model.entity.Player;
import com.adam.app.racinggame2d.model.entity.Settings;
import com.adam.app.racinggame2d.model.entity.Track;
import com.adam.app.racinggame2d.util.Constants;
import com.adam.app.racinggame2d.util.GameUtil;
import com.adam.app.racinggame2d.util.SharedPrefHelper;
import com.adam.app.racinggame2d.util.SoundPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameEngine {
    // TAG
    private static final String TAG = "GameEngine";
    // Player
    private final Player mPlayer;
    // Track
    private final Track mTrack;

    // sound
    private final SoundPlayer mSoundPlayer;

    // Used to control update at every 60 fps
    private final ScheduledExecutorService mService;
    private Future<?> mUpdateController;
    // used to check running
    private volatile boolean mIsRunning;
    // start time
    private long mStartTime;
    // callback when update
    private GameUpdateListener mUpdateCallback;
    // callback when game over
    private onGameOverListener mGameOverCallback;

    // update task
    private final Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            GameUtil.log(TAG, "run!!!");
            if (!mIsRunning) {
                GameUtil.log(TAG, "Game is stopped!!!");
                return;
            }

            updateGameFrame(Constants.DELTA_TIME);
        }
    };
    private boolean mIsAccelate = false;

    /**
     * Constructor
     *
     * @param context Application context
     * @param player  Player
     * @param track   Track
     */
    public GameEngine(@NonNull Context context, @NonNull Player player, @NonNull Track track) {
        mPlayer = player;
        mTrack = track;
        mIsRunning = false;
        // initial executor for update view
        mService = Executors.newSingleThreadScheduledExecutor();

        // load settings from shared preferences
        Settings currentSettings = SharedPrefHelper.getInstance(context).loadSettings();
        boolean isSoundEnable = currentSettings != null && currentSettings.isSoundEnable();
        Settings.GameDifficulty difficultySetting = currentSettings != null ? currentSettings.getDifficulty() : Settings.GameDifficulty.EASY;
        // apply to car
        mPlayer.getCar().applyTo(difficultySetting);
        // apply to track
        mTrack.applyTo(difficultySetting);



        // init sound
        mSoundPlayer = new SoundPlayer(context, isSoundEnable);
    }

    /**
     * setUpdateCallback
     * set callback when update
     *
     * @param updateCallback GameUpdateListener
     */
    public void setGameUpdateListener(GameUpdateListener updateCallback) {
        mUpdateCallback = updateCallback;
    }

    /**
     * setGameOverListener
     * set callback when game over
     *
     * @param gameOverCallback onGameOverListener
     */
    public void setGameOverListener(onGameOverListener gameOverCallback) {
        mGameOverCallback = gameOverCallback;
    }

    /**
     * Start the game
     */
    public void start() {
        GameUtil.log(TAG, "Game is started!!!");

        // cancel previous task
        if (mUpdateController != null) {
            mUpdateController.cancel(true);
        }

        // generate Obstacle by track
        mTrack.generateRandomObstacles();
        // start time
        mIsRunning = true;
        mStartTime = System.currentTimeMillis();

        // start to update view by thread
        mUpdateController = mService.scheduleWithFixedDelay(mUpdateRunnable,
                0,
                Constants.UPDATE_INTERVAL_MS,
                TimeUnit.MICROSECONDS);

        // play start short sound effect
        mSoundPlayer.playShortSound(Constants.SOUND_BUTTON, false);

        // play background music
        mSoundPlayer.playBgm(R.raw.background_music, true);

    }

    /**
     * Stop the game
     */
    public void stop() {
        GameUtil.log(TAG, "Game is stopped!!!");
        mIsRunning = false;
        // remove runnable
        if (mUpdateController != null) {
            mUpdateController.cancel(true);
        }

        // stop play background music
        mSoundPlayer.stopBgm();
        // play end short sound effect
        mSoundPlayer.playShortSound(Constants.SOUND_BUTTON, false);

    }

    /**
     * update
     *
     * @param deltaTime flaot
     */
    private void updateGameFrame(float deltaTime) {
        GameUtil.log(TAG, "update");
        if (mPlayer == null || mTrack == null) {
            GameUtil.error(TAG, "player or track is null");
            return;
        }

        Car car = mPlayer.getCar();
        // --- update game ---
        updateTrackScroll(deltaTime, car);
        updateCarState(deltaTime, car);
        // update car  x position
        clampCarXPosition(car);

        // --- detected boundary ---
        if (mTrack.checkBoundary(car)) {
            handleGameOver();
            return;
        }

        // --- detect collision: use fixed car position to detect ---
        if (mTrack.checkCollisions(car, () -> {
            mPlayer.addScore(Constants.COLLISION_SCORE);
            // play collision sound effect
            mSoundPlayer.playShortSound(Constants.SOUND_COLLISION, false);
        })) {
            handleObstacleEffect(car);
        } else {
            car.unsetRock();
        }

        // --- update game view ---
        if (mUpdateCallback != null) {
            mUpdateCallback.onUpdate();
        }
    }

    /**
     * updateCarState
     * update car state
     *
     * @param deltaTime float
     * @param car       Car
     */
    private void updateCarState(float deltaTime, Car car) {
        // check if slipped
        car.updateSlip(deltaTime);
        // check if boost
        car.updateBoost(deltaTime);
        // check if rock
        if (car.updateRock()) {
            handleGameOver();
        }
    }

    /**
     * updateTrackScroll
     * update track scroll
     *
     * @param deltaTime float
     * @param car       Car
     */
    private void updateTrackScroll(float deltaTime, Car car) {
        float scrollSpeed = car.getSpeed();
        // the car speed is convert to background scroll speed
        mTrack.update(deltaTime, scrollSpeed);
    }

    /**
     * handleObstacleEffect
     * handle obstacle effect
     *
     * @param car Car
     */
    private void handleObstacleEffect(Car car) {
        Obstacle.Type type = mTrack.getObstacleType();
        GameUtil.log(TAG, "collided: " + type.name());
        ObstacleEffectStrategy.handle(type, car);
    }

    /**
     * clampCarXPosition
     * clamp car x position
     *
     * @param car Car
     */
    private void clampCarXPosition(Car car) {
        // update car position (width = 1080)
        float xPosition = car.getPosition().x;
        float yPosition = car.getPosition().y;
        xPosition = Math.max(Constants.BOUNDARY_VALUE, Math.min(mTrack.getWidth() - Constants.BOUNDARY_VALUE, xPosition));
        car.setPosition(new PointF(xPosition, yPosition));
    }

    /**
     * gameOver
     * game over
     */
    private void handleGameOver() {
        stop();

        // notify game over
        if (mGameOverCallback != null) {
            mGameOverCallback.onGameOver();
        }
    }

    /**
     * getCheckPoints
     * get all checkpoint
     *
     * @return list of checkpoint
     */
    public List<PointF> getCheckPoints() {
        return mTrack.getCheckPoints();
    }

    /**
     * getObstacles
     * get all obstacles
     *
     * @return list of obstacles
     */
    public List<Obstacle> getObstacles() {
        return mTrack.getObstacles();
    }

    /**
     * getCarPosition
     * get car position
     *
     * @return car position
     */
    public PointF getCarPosition() {
        Car car = mPlayer.getCar();
        return car.getPosition();
    }

    /**
     * get score
     */
    public int getScore() {
        GameUtil.log(TAG, "getScore: " + mPlayer.getScore());
        return mPlayer.getScore();
    }

    /**
     * reset
     * reset game
     */
    public void reset() {
        GameUtil.log(TAG, "reset");
        mPlayer.reset();
        mTrack.reset();
    }

    /**
     * moveHorizontally
     * move car horizontally
     *
     * @param isLeft boolean
     */
    public void moveHorizontally(boolean isLeft) {
        Car car = mPlayer.getCar();
        car.moveHorizontally(isLeft);
    }

    /**
     * speedUp
     * speed up car
     *
     * @param isSpeedUp boolean
     */
    public void speedUp(boolean isSpeedUp) {
        mIsAccelate = isSpeedUp;
        // car
        Car car = mPlayer.getCar();
        car.updateSpeed(isSpeedUp);
    }

    /**
     * getCarHP
     * get car hp
     *
     * @return car hp
     */
    public int getCarHP() {
        return mPlayer.getCar().getCarHP();
    }

    /**
     * ObstacleEffectStrategy
     * handle obstacle effect
     */
    private enum ObstacleEffectStrategy {
        OIL() {
            @Override
            void applyTo(Car car) {
                car.startSlip();
            }

        },
        ROCK() {
            @Override
            void applyTo(Car car) {
                car.startRock();
            }

        },
        BOOST() {
            @Override
            void applyTo(Car car) {
                car.startBoost();
            }
        };

        private static final Map<Obstacle.Type, ObstacleEffectStrategy> MAP = new HashMap<>() {
            {
                put(Obstacle.Type.OIL, OIL);
                put(Obstacle.Type.ROCK, ROCK);
                put(Obstacle.Type.BOOST, BOOST);
            }
        };


        private ObstacleEffectStrategy() {
        }

        public static void handle(Obstacle.Type type, Car car) {
            ObstacleEffectStrategy strategy = MAP.get(type);
            if (strategy != null) {
                strategy.applyTo(car);
            } else {
                throw new IllegalStateException("Unexpected value: " + type);
            }
        }

        abstract void applyTo(Car car);
    }


    /**
     * interface updateCallback
     * callback when update
     */
    public interface GameUpdateListener {
        void onUpdate();
    }

    /**
     * interface gameOverListener
     * callback when game over
     */
    public interface onGameOverListener {
        void onGameOver();
    }

}
