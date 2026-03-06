/**
 * Copyright 2023 Adam Chen. All rights reserved.
 * <p>
 * Description: This is the engine loop manager.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/05
 */
package com.adam.app.arenaminifight.service.engine;

import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class EngineLoopManager {
    // TAG
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

        // check if executor service is null
        if (mExecutorService == null || mExecutorService.isShutdown()) {
            mExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        // schedule by 16 ms
        mExecutorService.scheduleWithFixedDelay(mUpdateTask, 0, 16L, java.util.concurrent.TimeUnit.MILLISECONDS);
        mIsRunning = true;
    }


    public void stop() {
        if (!mIsRunning) {
            GameUtil.log(TAG + ": EngineLoopManager is already stopped");
            return;
        }

        if (mExecutorService == null) {
            GameUtil.log(TAG + ": EngineLoopManager is already stopped");
            return;
        }

        mExecutorService.shutdown();
        try {
            if (!mExecutorService.awaitTermination(500, java.util.concurrent.TimeUnit.SECONDS)) {
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
