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
package com.adam.app.snake.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    // Executor
    private final Executor mDiskIO;
    private final Executor mMainThread;

    // sInstance
    private static final AppExecutors sInstance = new AppExecutors();
    public static AppExecutors getInstance() {
        return sInstance;
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
