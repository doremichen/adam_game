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

package com.adam.app.arenaminifight.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.adam.app.arenaminifight.R;

import java.util.List;

public final class GameUtil {

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

    public static void showToast(Context context, String msg) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(() -> executeShow(context, msg));
            return;
        }

        executeShow(context, msg);
    }

    private static void executeShow(Context context, String msg) {
        synchronized (GameUtil.class) {
            context = context.getApplicationContext();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showUnImplementedToast(Context context) {
        showToast(context, context.getString(R.string.arenaminifight_game_unimplemented_info));
    }
}
