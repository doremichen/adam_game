/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the snake game utils
 * <p>
 * Author: Adam Chen
 * Date: 2025/09/24
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

public final class Utils {
    // DEBUG_TAG: SnakeGame
    public static final String DEBUG_TAG = "SnakeGame";

    private Utils() {
        throw new AssertionError();
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
