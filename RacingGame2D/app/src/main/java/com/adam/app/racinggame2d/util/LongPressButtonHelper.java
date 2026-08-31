/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Helper class for handling long press events on Buttons.
 */
public final class LongPressButtonHelper {
    private static final long sDEFAULT_INTERVAL = 100L;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final long mIntervalMillis;
    private final Runnable mRepeatTask;
    private boolean mIsPressed = false;

    private final Runnable mRepeatTaskRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mIsPressed) return;
            if (mRepeatTask != null) mRepeatTask.run();
            mHandler.postDelayed(this, mIntervalMillis);
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    private LongPressButtonHelper(@NonNull View button, long intervalMillis, Runnable repeatTask) {
        this.mIntervalMillis = intervalMillis > 0L ? intervalMillis : sDEFAULT_INTERVAL;
        this.mRepeatTask = repeatTask;

        button.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mIsPressed = true;
                    mHandler.post(mRepeatTaskRunnable);
                    view.performClick();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    stop();
                    break;
            }
            return false;
        });
    }

    public static void attach(@NonNull View button, long intervalMillis, Runnable repeatTask) {
        new LongPressButtonHelper(button, intervalMillis, repeatTask);
    }

    private void stop() {
        mIsPressed = false;
        mHandler.removeCallbacks(mRepeatTaskRunnable);
    }
}
