/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the snake game loop that update the game state
 *
 * Author: Adam Chen
 * Date: 2025/10/01
 */
package com.adam.app.snake.viewmodel;

import android.os.Handler;
import android.os.Looper;

public class GameLoop {
    // Handler
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    // Runnable
    private final Runnable mRunnable;
    // interval
    private long mInterval;

    /**
     * Constructor with interval, runnable
     *
     * @param interval long
     * @param task Runnable
     */
    public GameLoop(long interval, Runnable task) {
        mInterval = interval;
        mRunnable = task;
    }

    /**
     * start the game loop
     */
    public void start() {
        // stop
        stop();
        // post delay interval
        mHandler.postDelayed(mRunnable, mInterval);
    }

    /**
     * stop the game loop
     */
    public void stop() {
        mHandler.removeCallbacks(mRunnable);
    }

    /**
     * set interval
     *
     * @param interval long
     */
    public void setInterval(long interval) {
        mInterval = interval;
    }

}
