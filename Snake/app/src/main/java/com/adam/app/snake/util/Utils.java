/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.adam.app.snake.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Utils {
    // DEBUG_TAG: SnakeGame
    public static final String DEBUG_TAG = "SnakeGame";

    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());

    private Utils() {
        throw new AssertionError();
    }

    /**
     * format timestamp to readable date string
     *
     * @param timestamp long
     * @return String
     */
    public static String formatDate(long timestamp) {
        return sDateFormat.format(new Date(timestamp));
    }

    /**
     * log debug message
     *
     * @param label String
     * @param message String
     */
    public static void logDebug(String label, String message) {
        Log.d(DEBUG_TAG, label + ": " + message);
    }

    /**
     * show toast message
     *
     * @param context Context
     * @param message String
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * show dialog
     *
     * @param context Context
     * @param title String
     * @param message String
     * @param positiveButton Content DialogButtonContent
     * @param negativeButton Content DialogButtonContent
     */
    public static void showDialog(Context context, String title, String message,
                                  DialogButtonContent positiveButton, DialogButtonContent negativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton.getLabel(), positiveButton.getListener());
        }
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton.getLabel(), negativeButton.getListener());
        }
        builder.show();
    }

    /**
     * Class Dialog button Content
     * label String
     * listener DialogInterface.OnClickListener
     */
    public static class DialogButtonContent {
        private final String mLabel;
        private final DialogInterface.OnClickListener mListener;

        public DialogButtonContent(String label, DialogInterface.OnClickListener listener) {
            this.mLabel = label;
            this.mListener = listener;
        }

        public String getLabel() {
            return mLabel;
        }

        public DialogInterface.OnClickListener getListener() {
            return mListener;
        }

    }

    /**
     * null check with object
     *
     * @param object Object
     * @return boolean
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

}
