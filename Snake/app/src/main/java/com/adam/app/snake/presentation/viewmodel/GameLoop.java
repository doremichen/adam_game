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

import android.os.Handler;
import android.os.Looper;

/**
 * Game loop for updating game state
 */
public class GameLoop {
    // Handler
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    // Runnable
    private final Runnable mRunnable;

    /**
     * Constructor with task
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
