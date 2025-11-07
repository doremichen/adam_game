/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is used to help with long press buttons.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/11/07
 */
package com.adam.app.racinggame2d.util;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

/**
 * LongPressButtonHelper
 * ---------------------
 * Used to help with long press buttons.
 * At the same time, the button's ripple animation and click event are preserved.
 *
 * Usage：
 * LongPressButtonHelper.attach(button, 100l, () -> {
 *     // repeat action
 *     viewModel.moveCarLeft();
 * });
 */
public class LongPressButtonHelper {
    // default interval
    private static final long DEFAULT_INTERVAL = 100L;
    // used to do long press action
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    // interval time of loong press button
    private final long mIntervalMillis;
    // long press action
    private final Runnable mRepeatTask;

    private boolean mIsPressed = false;


    // repeatTask runnable
    private final Runnable mRepeatTaskRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mIsPressed) {
                return;
            }
            if (mRepeatTask != null) mRepeatTask.run();
            mHandler.postDelayed(this, mIntervalMillis);
        }
    };


    /**
     * Constructor
     *
     * @param button button
     * @param intervalMillis interval
     * @param repeatTask repeat task
     */
    private LongPressButtonHelper(View button, long intervalMillis, Runnable repeatTask) {
        this.mIntervalMillis = intervalMillis > 0L ? intervalMillis : DEFAULT_INTERVAL;;
        this.mRepeatTask = repeatTask;

        // set touch listener
        button.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mIsPressed = true;
                    mHandler.post(mRepeatTaskRunnable);
                    // perform click event
                    view.performClick();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    stop();
                    break;
            }
            return false;  // Retain the original click effect
        });
    }

    /**
     * attach
     * attach to button
     *
     * @param button button
     * @param intervalMillis interval
     * @param repeatTask repeat task
     * @return LongPressButtonHelper
     */
    public static LongPressButtonHelper attach(View button, long intervalMillis, Runnable repeatTask) {
        return new LongPressButtonHelper(button, intervalMillis, repeatTask);
    }

    /**
     * stop
     * unset isPressed and remove repeat task
     */
    private void stop() {
        mIsPressed = false;
        mHandler.removeCallbacks(mRepeatTaskRunnable);
    }


}
