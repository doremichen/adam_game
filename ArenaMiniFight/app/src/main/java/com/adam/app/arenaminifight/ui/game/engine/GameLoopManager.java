/*
 * Copyright (c) 2026 Adam Chen
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

package com.adam.app.arenaminifight.ui.game.engine;

import com.adam.app.arenaminifight.utils.GameConstants;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameLoopManager {

    private static final String TAG = "GameLoopManager";
    private static final int FRAME_PERIOD_MS = 1000 / 60;

    private final Runnable mGameTask;
    private ScheduledExecutorService mExecutor;
    private boolean mIsRunning;

    public GameLoopManager(Runnable gameTask) {
        mGameTask = gameTask;
    }

    public synchronized void start() {
        GameUtil.log(TAG + ": start");
        if (mIsRunning) {
            return;
        }

        if (mExecutor == null || mExecutor.isShutdown()) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        }

        mExecutor.scheduleWithFixedDelay(mGameTask, 0, FRAME_PERIOD_MS, TimeUnit.MILLISECONDS);
        mIsRunning = true;
    }

    public synchronized void stop() {
        GameUtil.log(TAG + ": stop");
        if (!mIsRunning) {
            return;
        }
        mIsRunning = false;
        if (mExecutor != null) {
            mExecutor.shutdown();
            try {
                if (!mExecutor.awaitTermination(GameConstants.SERVICE_TERMINATION_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                    mExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                mExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            mExecutor = null;
        }
    }

    public boolean isRunning() {
        return mIsRunning;
    }
}
