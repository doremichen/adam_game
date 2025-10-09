/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the app executors that handle work thread or main thread mechanism
 *
 * Author: Adam Chen
 * Date: 2025/10/08
 */
package com.adam.app.snake.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    // Executor
    private final Executor mDiskIO;
    private final Executor mMainThread;

    // INSTANCE
    private static final AppExecutors INSTANCE = new AppExecutors();
    public static AppExecutors getInstance() {
        return INSTANCE;
    }

    /**
     * enum Type
     * 1. DiskIO
     * 2. MainThread
     */
    public enum Type {
        DiskIO,
        MainThread
    }


    /**
     * constructor
     */
    private AppExecutors() {
        mDiskIO = Executors.newSingleThreadExecutor();
        mMainThread = new MainThreadExecutor();
    }

    /**
     * execute
     *
     * @param type
     * @param runnable
     */
    public void execute(Type type, Runnable runnable) {
        switch (type) {
            case DiskIO:
                mDiskIO.execute(runnable);
                break;
            case MainThread:
                mMainThread.execute(runnable);
                break;
        }
    }


    /**
     * class MainThreadExecutor
     */
    private static class MainThreadExecutor implements Executor {
        private final Handler mHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mHandler.post(command);
        }
    }


}
