/**
 * Copyright (C) 2017 Adam Chen. All rights reserved.
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/11
 *
 */
package com.adam.app.memorycardgame.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.adam.app.memorycardgame.R;

public abstract class CommonUtils {

    // TAG
    private static final String TAG = "MemoryCardGame";

    private CommonUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * log message
     * @param msg message
     */
    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    /**
     * show toast
     *
     * @param context Context
     * @param msg message
     */
    public static void showToast(Context context, String msg) {
        // check main thread
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            // switch to main loop
            new Handler(Looper.getMainLooper()).post(() -> {
                // show toast
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            });
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * show unimplemented toast
     * @param context Context
     */
    public static void showUnImplementedToast(Context context) {
        showToast(context, context.getString(R.string.memory_card_game_not_implemented));
    }

}
