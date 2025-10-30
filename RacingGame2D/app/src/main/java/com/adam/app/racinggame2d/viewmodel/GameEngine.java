/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is responsible for managing the game engine.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/27
 */
package com.adam.app.racinggame2d.viewmodel;

import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.adam.app.racinggame2d.model.entity.Car;
import com.adam.app.racinggame2d.model.entity.Obstacle;
import com.adam.app.racinggame2d.model.entity.Player;
import com.adam.app.racinggame2d.model.entity.Track;
import com.adam.app.racinggame2d.util.GameUtil;

import java.util.List;

public class GameEngine {
    // TAG
    private static final String TAG = "GameEngine";
    private static final int UPDATE_INTERVAL_MS = 16;
    // Player
    private final Player mPlayer;
    // Track
    private final Track mTrack;
    // Used to control update at every 60 fps
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    // used to check running
    private boolean mIsRunning;
    // start time
    private long mStartTime;
    // callback when update
    private GameUpdateListener mUpdateCallback;

    // update task
    private final Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            GameUtil.log(TAG, "run!!!");
            if (!mIsRunning) {
                GameUtil.log(TAG, "Game is stopped!!!");
                return;
            }

            update(UPDATE_INTERVAL_MS / 1000f);
            mHandler.postDelayed(this, UPDATE_INTERVAL_MS);
        }
    };


    /**
     * Constructor
     *
     * @param player Player
     * @param track  Track
     */
    public GameEngine(@NonNull Player player, @NonNull Track track) {
        mPlayer = player;
        mTrack = track;
        mIsRunning = false;
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
     * Start the game
     */
    public void start() {
        GameUtil.log(TAG, "Game is started!!!");
        // generate Obstacle by track
        mTrack.generateRandomObstacles(8);
        // start time
        mIsRunning = true;
        mStartTime = System.currentTimeMillis();
        mHandler.post(mUpdateRunnable);
    }

    /**
     * Stop the game
     */
    public void stop() {
        GameUtil.log(TAG, "Game is stopped!!!");
        mIsRunning = false;
        // remove runnable
        mHandler.removeCallbacks(mUpdateRunnable);
        // elapsed time
        long elapsedTime = System.currentTimeMillis() - mStartTime;
        // time convert to add score
        long timeInSeconds = elapsedTime / 1000;
        // add score
        mPlayer.addScore((int) timeInSeconds);

    }

    /**
     * update
     *
     * @param deltaTime flaot
     */
    private void update(float deltaTime) {
        GameUtil.log(TAG, "update");

        // monitor car run forward
        float scrollSpeed = mPlayer.getCar().getSpeed(); // the car speed is convert to background scroll speed
        mTrack.update(deltaTime, scrollSpeed);

        // detect collision: use fixed car position to detect
        Car car = mPlayer.getCar();
        boolean collided = mTrack.checkCollisions(car, () -> mPlayer.addScore(50));
        if (collided) {
            stop();
        }

        if (mUpdateCallback != null) {
            mUpdateCallback.onUpdate();
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
     * setCarPosition
     * set car position
     *
     * @param position car position
     */
    public void setCarPosition(PointF position) {
        Car car = mPlayer.getCar();
        car.setPosition(position);
    }

    /**
     * get score
     */
    public int getScore() {
        return mPlayer.getScore();
    }

    public void moveHorizontally(boolean isLeft) {
        Car car = mPlayer.getCar();
        car.moveHorizontally(isLeft);
    }

    /**
     * interface updateCallback
     * callback when update
     */
    public interface GameUpdateListener {
        void onUpdate();
    }

}
