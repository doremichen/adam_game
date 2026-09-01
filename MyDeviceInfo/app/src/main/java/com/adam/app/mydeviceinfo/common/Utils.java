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

package com.adam.app.mydeviceinfo.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Utility class for logging and UI notifications.
 */
public final class Utils {

    private Utils() {
        // Prevent instantiation
    }

    /**
     * Logs a debug message.
     * @param label The label for the log.
     * @param message The message to log.
     */
    public static void logDebug(String label, String message) {
        Log.d(Constants.GLOBAL_TAG, "[" + label + "] " + message);
    }

    /**
     * Logs a message (legacy support).
     * @param message The message to log.
     */
    public static void log(String message) {
        Log.d(Constants.GLOBAL_TAG, message);
    }

    /**
     * Shows a short toast message.
     * @param context The context to show the toast in.
     * @param message The message to show.
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
