/**
 * Copyright (C) 2025 Adam. All Rights Reserved.
 * Description: This class is a util class for the racing game.
 *
 * @author Adam Chen
 * @since 2025-11-03
 */
package com.adam.app.racinggame.util;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public final class GameUtil {

    // TAG
    public static final String TAG = "RacingGame";

    public static final String GameUtil_TAG = "GameUtil";


    private GameUtil() {}

    /**
     * Log
     *  log message
     * @param title
     * @param message
     */
    public static void log(String title, String message) {
        String info = String.format("%s: %s", title, message);
        Log.d(TAG, info);
    }

    /**
     * error
     *  log error message
     * @param title
     * @param message
     */
    public static void error(String title, String message) {
        String info = String.format("%s: %s", title, message);
        Log.e(TAG, info);
    }

    /**
     * showToast
     * show toast
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        if (context == null || message == null) {
            error(GameUtil_TAG, "context or message is null");
            return;
        }

        // check if main thread
        if (android.os.Process.myTid() == Looper.getMainLooper().getThread().getId()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            Activity activity = (Activity) context;
            activity.runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
        }
    }


}
