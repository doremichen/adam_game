/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the game util of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/03
 */
package com.adam.app.arenaminifight.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.adam.app.arenaminifight.R;

import java.util.List;

public final class GameUtil {

    // TAG
    private static final String TAG = "DebugGame";

    private GameUtil() {
        throw new UnsupportedOperationException("Utility class; do not instantiate.");
    }

    public static void log(String msg) {
        Log.d(TAG, msg);
    }


    public static void dumpList(String title, List<?> list) {
        log(title + " size: " + list.size());
        for (Object item : list) {
            log(item.toString());
        }
    }

    /**
     * show toast
     */
    public static void showToast(Context context, String msg) {
        // check main looper
        if (Looper.myLooper() != Looper.getMainLooper()) {
            // switch to main queue
            new Handler(Looper.getMainLooper()).post(() -> executeShow(context, msg));
            return;
        }

        executeShow(context, msg);
    }

    private static void executeShow(Context context, String msg) {
        // avoid race condition
        synchronized (GameUtil.class) {
            // get application context
            context = context.getApplicationContext();
            // show toast
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * show unimplemented toast
     */
    public static void showUnImplementedToast(Context context) {
        showToast(context, context.getString(R.string.arenaminifight_game_unimplemented_info));
    }

}
