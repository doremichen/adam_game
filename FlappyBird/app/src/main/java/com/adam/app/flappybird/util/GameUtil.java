/**
 * This class is used to provide some utility functions.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public final class GameUtil {

    // TAG
    public static final String TAG = "GameUtil";
    public static final String GAME_TAG= "FlappyBird";

    // --- Constants ---
    public static final int COLLISION_RANGE= 40;

    private GameUtil() {}

    /**
     * log message
     * @param title title
     * @param message message
     */
    public static void info(String title, String message) {
        Log.i(TAG, title + " : " + message);
    }

    /**
     * log error
     * @param title title
     * @param message message
     */
    public static void error(String title, String message) {
        Log.e(TAG, title + " : " + message);
    }

    /**
     * class ButtonContent
     * This class is used to provide the content of button.
     *
     */
    public static class ButtonContent {
        private final String mLabel;
        private final DialogInterface.OnClickListener mOnClickListener;

        public ButtonContent(String label, DialogInterface.OnClickListener onClickListener) {
            mLabel = label;
            mOnClickListener = onClickListener;
        }

        // --- getter ---
        public String getLabel() {
            return mLabel;
        }

        public DialogInterface.OnClickListener getOnClickListener() {
            return mOnClickListener;
        }

    }

    /**
     * show dialog
     * @param context context
     * @param title title
     * @param message message
     * @param positiveButton positive button
     * @param negativeButton negative button
     */
    public static void showDialog(Context context,
                                  String title,
                                  String message,
                                  ButtonContent positiveButton,
                                  ButtonContent negativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton.getLabel(), positiveButton.getOnClickListener());
        }
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton.getLabel(), negativeButton.getOnClickListener());
        }
        builder.show();
    }

}
