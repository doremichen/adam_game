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

package com.adam.app.arenaminifight.service.engine;

import com.adam.app.arenaminifight.utils.GameConstants;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EngineLoopManager {

    private static final String TAG = "EngineLoopManager";

    private ScheduledExecutorService mExecutorService;
    private final Runnable mUpdateTask;
    private boolean mIsRunning;

    public EngineLoopManager(Runnable updateTask) {
        mUpdateTask = updateTask;
    }

    public synchronized void start() {
        if (mIsRunning) {
            GameUtil.log(TAG + ": EngineLoopManager is already running");
            return;
        }

        if (mExecutorService == null || mExecutorService.isShutdown()) {
            mExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        mExecutorService.scheduleWithFixedDelay(mUpdateTask, 0, GameConstants.FRAME_DELAY_MS, TimeUnit.MILLISECONDS);
        mIsRunning = true;
    }

    public synchronized void stop() {
        if (!mIsRunning || mExecutorService == null) {
            GameUtil.log(TAG + ": EngineLoopManager is already stopped");
            return;
        }

        mExecutorService.shutdown();
        try {
            if (!mExecutorService.awaitTermination(GameConstants.SERVICE_TERMINATION_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                mExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            mExecutorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        mExecutorService = null;
        mIsRunning = false;
    }

    public boolean isRunning() {
        return mIsRunning;
    }
}
