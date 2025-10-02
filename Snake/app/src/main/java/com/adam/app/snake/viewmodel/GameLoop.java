/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the snake game loop that update the game state
 * <p>
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

    /**
     * Constructor with interval, runnable
     *
     * @param task Runnable
     */
    public GameLoop(Runnable task) {
        mRunnable = task;
    }

    /**
     * start the game loop
     *
     * @param interval long
     */
    public void start(long interval) {
        // stop
        stop();
        // post delay interval
        mHandler.postDelayed(mRunnable, interval);
    }

    /**
     * stop the game loop
     */
    public void stop() {
        mHandler.removeCallbacks(mRunnable);
    }


}
