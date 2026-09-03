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

package com.adam.app.mydeviceinfo.common.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import com.adam.app.mydeviceinfo.common.Constants;
import com.adam.app.mydeviceinfo.common.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * A lifecycle-aware and power-saving task scheduler using the State Pattern.
 */
@Singleton
public final class TaskScheduler {
    private static final String TAG = "TaskScheduler";

    private final Context mContext;
    private ScheduledExecutorService mExecutor;
    private ScheduledFuture<?> mFuture;
    private Runnable mTask;
    private State mCurrentState = State.STOPPED;

    private final BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Utils.logDebug(TAG, "Screen ON - Resuming task");
                resume();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Utils.logDebug(TAG, "Screen OFF - Pausing task");
                pause();
            }
        }
    };

    /**
     * Constructs the TaskScheduler.
     * @param context Application context.
     */
    @Inject
    public TaskScheduler(@ApplicationContext @NonNull Context context) {
        this.mContext = context;
    }

    /**
     * Starts the scheduler with the provided task.
     * @param task The task to run periodically.
     */
    public synchronized void start(@NonNull Runnable task) {
        this.mTask = task;
        mCurrentState.start(this);
    }

    /**
     * Stops the scheduler and releases resources.
     */
    public synchronized void stop() {
        mCurrentState.stop(this);
    }

    /**
     * Pauses the task execution.
     */
    private synchronized void pause() {
        mCurrentState.pause(this);
    }

    /**
     * Resumes the task execution.
     */
    private synchronized void resume() {
        mCurrentState.resume(this);
    }

    /**
     * Enum representing the states of the scheduler.
     */
    private enum State {
        STOPPED {
            @Override
            void start(TaskScheduler context) {
                context.mExecutor = Executors.newSingleThreadScheduledExecutor();
                context.registerReceiver();
                context.scheduleTask();
                context.mCurrentState = RUNNING;
                Utils.logDebug(TAG, "State changed: STOPPED -> RUNNING");
            }

            @Override
            void stop(TaskScheduler context) {
                // Already stopped
            }

            @Override
            void pause(TaskScheduler context) {
                // Cannot pause if stopped
            }

            @Override
            void resume(TaskScheduler context) {
                // Cannot resume if stopped
            }
        },
        RUNNING {
            @Override
            void start(TaskScheduler context) {
                // Already running
            }

            @Override
            void stop(TaskScheduler context) {
                context.cancelTask();
                context.shutdownExecutor();
                context.unregisterReceiver();
                context.mCurrentState = STOPPED;
                Utils.logDebug(TAG, "State changed: RUNNING -> STOPPED");
            }

            @Override
            void pause(TaskScheduler context) {
                context.cancelTask();
                context.mCurrentState = PAUSED;
                Utils.logDebug(TAG, "State changed: RUNNING -> PAUSED");
            }

            @Override
            void resume(TaskScheduler context) {
                // Already running
            }
        },
        PAUSED {
            @Override
            void start(TaskScheduler context) {
                // Stop first if you want to restart with a new task
                stop(context);
                context.mCurrentState.start(context);
            }

            @Override
            void stop(TaskScheduler context) {
                context.shutdownExecutor();
                context.unregisterReceiver();
                context.mCurrentState = STOPPED;
                Utils.logDebug(TAG, "State changed: PAUSED -> STOPPED");
            }

            @Override
            void pause(TaskScheduler context) {
                // Already paused
            }

            @Override
            void resume(TaskScheduler context) {
                context.scheduleTask();
                context.mCurrentState = RUNNING;
                Utils.logDebug(TAG, "State changed: PAUSED -> RUNNING");
            }
        };

        /**
         * Starts the task execution or transition to RUNNING state.
         * @param context TaskScheduler context.
         */
        abstract void start(TaskScheduler context);

        /**
         * Stops the task execution and releases resources.
         * @param context TaskScheduler context.
         */
        abstract void stop(TaskScheduler context);

        /**
         * Pauses the task execution.
         * @param context TaskScheduler context.
         */
        abstract void pause(TaskScheduler context);

        /**
         * Resumes the task execution.
         * @param context TaskScheduler context.
         */
        abstract void resume(TaskScheduler context);
    }

    /**
     * Schedules the task for periodic execution.
     */
    private void scheduleTask() {
        if (mExecutor != null && mTask != null) {
            mFuture = mExecutor.scheduleWithFixedDelay(
                    mTask,
                    Constants.INITIAL_DELAY_MS,
                    Constants.POLLING_INTERVAL_MS,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    /**
     * Cancels the currently scheduled task.
     */
    private void cancelTask() {
        if (mFuture != null) {
            mFuture.cancel(false);
            mFuture = null;
        }
    }

    /**
     * Shuts down the internal executor service.
     */
    private void shutdownExecutor() {
        if (mExecutor != null) {
            mExecutor.shutdown();
            mExecutor = null;
        }
    }

    /**
     * Registers the screen state broadcast receiver.
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mContext.registerReceiver(mScreenReceiver, filter);
    }

    /**
     * Unregisters the screen state broadcast receiver.
     */
    private void unregisterReceiver() {
        try {
            mContext.unregisterReceiver(mScreenReceiver);
        } catch (IllegalArgumentException e) {
            // Receiver not registered
        }
    }
}
