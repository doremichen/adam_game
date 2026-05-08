/*
 * MIT License
 *
 * Copyright (c) 2025 Adam Chen
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
package com.adam.app.whack_a_molejava.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.adam.app.whack_a_molejava.R;

/**
 * This class provides utility methods for the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-02
 */
public final class GameUtils {

    private static final String TAG = "WHACKAMOLE";
    private static final String TAG_GAME_UTIL = "GameUtil";

    private GameUtils() {
        throw new AssertionError("No instances.");
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
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        // check main looper
        if (android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            });
            return;
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * show unimplemented toast message
     */
    public static void showUnImplementedToast(Context context) {
        showToast(context, context.getString(R.string.whack_a_mole_unimplemented_toast));
    }


    /**
     * start activity
     */

    public static void startActivity(Context src, Class<?> des) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(src, des));

        // check if activity
        if (!(src instanceof android.app.Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        src.startActivity(intent);
    }


}
