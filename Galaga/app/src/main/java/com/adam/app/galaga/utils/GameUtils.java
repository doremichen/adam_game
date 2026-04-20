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

package com.adam.app.galaga.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public final class GameUtils {

    // Game Tag
    private static final String TAG = "Galaga";


    private GameUtils() {
        throw new UnsupportedOperationException("This is Game Utils!!!");
    }

    /**
     * info log
     *
     * @param tag
     * @param msg
     */
    public static void info(String tag, String msg) {
        Log.i(TAG, tag + ": " + msg);
    }


    /**
     * erro log
     *
     * @param tag
     * @param msg
     */
    public static void error(String tag, String msg) {
        Log.e(TAG, tag + ": " + msg);
    }


    /**
     *
     * Show toast message.
     *
     * @param context Context
     * @param msg String
     */
    public static void showToast(Context context, String msg) {
        // check if main thread
        if (Looper.myLooper() != Looper.getMainLooper()) {
            // run in main thread
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
            return;
        }

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * start activity
     * @param context Context
     * @param cls Class
     */
    public static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

}
