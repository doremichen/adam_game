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

package com.adam.app.tic_tac_toe.ui.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adam.app.tic_tac_toe.R;

/**
 * Unified utility class for the project.
 */
public final class Utils {

    private static final String VERSION_NAME = "1.0";
    private static final String TAG = "TICTACTOE";

    private Utils() {
        // Prevent instantiation
    }

    public static String getVersionName() {
        return VERSION_NAME;
    }

    /**
     * Unified logging method.
     *
     * @param label   The label/tag for the log.
     * @param message The message to log.
     */
    public static void logDebug(@Nullable String label, @Nullable String message) {
        Log.d(TAG, label + ": " + message);
    }

    /**
     * Shows a toast message, ensuring it runs on the UI thread.
     *
     * @param context The context.
     * @param message The message to display.
     */
    public static void showToast(@NonNull Context context, @NonNull String message) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Shows an alert dialog.
     *
     * @param context        The context.
     * @param title          The dialog title.
     * @param message        The dialog message.
     * @param positiveButton Configuration for the positive button.
     * @param negativeButton Configuration for the negative button.
     */
    public static void showAlertDialog(@NonNull Context context,
                                       @NonNull String title,
                                       @NonNull String message,
                                       @Nullable DialogButtonContent positiveButton,
                                       @Nullable DialogButtonContent negativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false);

        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton.getLabel(), (dialog, which) -> {
                positiveButton.getListener().onPress();
                dialog.dismiss();
            });
        }

        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton.getLabel(), (dialog, which) -> {
                negativeButton.getListener().onPress();
                dialog.dismiss();
            });
        }

        builder.create().show();
    }

    /**
     * Shows a "not implemented" toast.
     *
     * @param context The context.
     */
    public static void unImplemented(@NonNull Context context) {
        showToast(context, context.getString(R.string.tic_tac_toe_not_implemented_yet_msg));
    }

    /**
     * Data class for dialog button content.
     */
    public static class DialogButtonContent {
        private final String mLabel;
        private final OnPressListener mListener;

        public DialogButtonContent(String label, OnPressListener listener) {
            this.mLabel = label;
            this.mListener = listener;
        }

        public String getLabel() {
            return mLabel;
        }

        public OnPressListener getListener() {
            return mListener;
        }

        /**
         * Interface for button press events.
         */
        public interface OnPressListener {
            void onPress();
        }
    }
}
