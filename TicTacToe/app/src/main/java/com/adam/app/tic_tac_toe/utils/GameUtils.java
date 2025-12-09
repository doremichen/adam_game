/**
 * This class provides utility methods for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public final class GameUtils {

    private static final String TAG = "TICTACTOE";
    private static final String TAG_UTIL = "GameUtils";

    private GameUtils() {
        // avoid to be instantiated
    }

    /**
     * log
     *
     * @param title
     * @param message
     */
    public static void log(String title, String message) {
        Log.d(TAG, title + ": " + message);
    }

    /**
     * show toast
     *
     * @param context Context
     * @param message String
     */
    public static void showToast(Context context, String message) {
        // check if main looper
        if (android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            });
        } else {
            try {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        }
    }

    public static void unImplemented(Context context) {
        showToast(context, "Not implemented yet");
    }


}
