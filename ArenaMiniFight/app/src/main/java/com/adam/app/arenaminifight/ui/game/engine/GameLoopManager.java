/**
 * Copyright (C) 2021 Adam. All rights reserved.
 * <p>
 * Description: This class handles the game loop.
 *
 * @author Adam chen
 * @version 1.0.0 - 2026/03/04
 */
package com.adam.app.arenaminifight.ui.game.engine;

import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameLoopManager {
    // TAG
    private static final String TAG = "GameLoopManager";

    private static final int FRAME_PERIOD_MS = 1000 / 60;
    private final Runnable mGameTask;
    private ScheduledExecutorService mExecutor;
    private boolean mIsRunning;

    // constructor
    public GameLoopManager(Runnable gameTask) {
        mExecutor = Executors.newSingleThreadScheduledExecutor();
        mGameTask = gameTask;
    }

    public synchronized void start() {
        GameUtil.log(TAG + ": start");
        if (mIsRunning) {
            return;
        }
        // schedule the game loop
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
            // stop the game loop
            mExecutor.shutdown();
            try {
                if (!mExecutor.awaitTermination(100, TimeUnit.SECONDS)) {
                    mExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                mExecutor.shutdownNow();
            }
            mExecutor = null;
        }

    }

    public boolean isRunning() {
        return mIsRunning;
    }

}
