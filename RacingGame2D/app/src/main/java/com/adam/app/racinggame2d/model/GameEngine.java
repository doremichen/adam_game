/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is responsible for managing the game engine.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/27
 */
package com.adam.app.racinggame2d.model;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.adam.app.racinggame2d.model.entity.Car;
import com.adam.app.racinggame2d.model.entity.Player;
import com.adam.app.racinggame2d.model.entity.Track;
import com.adam.app.racinggame2d.util.GameUtil;

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


    // Runnable to update
    private final Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
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
     * Start the game
     */
    public void start() {
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
        // get car
        Car car = mPlayer.getCar();
        // move
        car.move(deltaTime);

        boolean collided = mTrack.checkCollisions(car, () -> mPlayer.addScore(50));
        if (collided) {
            stop();
        }
    }

    //-- get/set ---
    public Player getPlayer() {
        return mPlayer;
    }

    public Track getTrack() {
        return mTrack;
    }

}
