/**
 * File: GameUtil.java
 * Description: This class is Game Utils
 *
 * @author Adam Chen
 * @version 1.0 - 2026/01/14
 */
package com.adam.app.tapgame.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.adam.app.tapgame.R;

public final class GameUtils {

    // TAG
    private static final String TAG = "GameUtils";

    private GameUtils() {}

    /**
     * log message
     * @param msg String
     */
    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    public static void showUnImplemented(Context context) {
        Context ctx = context.getApplicationContext();
        // check if main thread
        if (Thread.currentThread() != context.getMainLooper().getThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx, R.string.tap_game_not_implement_yet_toast, Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        Toast.makeText(ctx, R.string.tap_game_not_implement_yet_toast, Toast.LENGTH_SHORT).show();

    }



}
