/**
 * Copyright 2025 - Adam Game. All rights reserved.
 *
 * Description: This class is used to provide utility functions for the game.
 *
 * Author: Adam Game
 * Created Date: 2025/10/28
 */
package com.adam.app.racinggame2d.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class GameUtil {

    public static final String PLAYER_NAME = "key.player.name";

    private GameUtil() {
        // avoid to be instantiated
    }

    // TAG
    public static final String TAG = "RacingGame2D";

    /**
     * Log
     *  log the message
     * @param title
     * @param message
     */
    public static void log(String title, String message) {
        String info = String.format("%s: %s", title, message);
        Log.d(TAG, info);
    }

    /**
     * errorLog
     *  log the error message
     * @param title
     * @param message
     */
    public static void error(String title, String message) {
        String info = String.format("%s: %s", title, message);
        Log.e(TAG, info);
    }


    /**
     * showToast
     *  Show toast message
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        // check if the context or message is null
        if (context == null || message == null) {
            return;
        }

        // check if the main thread
        if (android.os.Process.myTid() == Looper.getMainLooper().getThread().getId()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            // Need to switch to the main thread
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(() ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                );
            } else {
                // Can not switch to the main thread, so skip it or record an error
                GameUtil.error("ToastUtil", "Unable to show toast: context is not an Activity");
            }

        }
    }

    /**
     * class DialogButtonContent
     *  Dialog button content that has label and click listener
     */
    public static class DialogButtonContent {
        /**
         * label
         * label of the button
         */
        private String mLabel;

        /**
         * clickListener
         *  click listener of the button
         */
        private DialogInterface.OnClickListener mClickListener;


        /**
         * interface OnEditConfirmedListener
         */
        public interface OnEditConfirmedListener {
            void onEditConfirmed(String text);
        }

        private OnEditConfirmedListener mOnEditConfirmedListener;

        /**
         * Edit text content
         * Constructor
         * @param label label of the button
         * @param onEditConfirmedListener click listener of the button
         */
        public DialogButtonContent(String label, @NonNull OnEditConfirmedListener onEditConfirmedListener) {
            mLabel = label;
            mOnEditConfirmedListener = onEditConfirmedListener;
        }

        /**
         * Show info
         * Constructor
         * @param label label of the button
         * @param clickListener click listener of the button
         */
        public DialogButtonContent(String label, DialogInterface.OnClickListener clickListener) {
            mLabel = label;
            mClickListener = clickListener;
        }

        public String getLabel() {
            return mLabel;
        }

        public DialogInterface.OnClickListener getClickListener() {
            return mClickListener;
        }

        public OnEditConfirmedListener getOnEditConfirmedListener() {
            return mOnEditConfirmedListener;
        }


    }

    /**
     * showDialog
     *  Show dialog
     * @param context context
     * @param title title of the dialog
     * @param message message of the dialog
     * @param positiveButtonContent positive button content
     * @param negativeButtonContent negative button content
     */
    public static void showDialog(@NonNull Context context, String title, String message, DialogButtonContent positiveButtonContent, DialogButtonContent negativeButtonContent) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        if (positiveButtonContent != null) {
            builder.setPositiveButton(positiveButtonContent.getLabel(), positiveButtonContent.getClickListener());
        }
        if (negativeButtonContent != null) {
            builder.setNegativeButton(negativeButtonContent.getLabel(), negativeButtonContent.getClickListener());
        }
        builder.show();
    }


    /**
     * showEditDialog
     *  Show edit dialog
     * @param context context
     * @param title title of the dialog
     * @param hint hint of the edit text
     * @param initialText initial text of the edit text
     * @param positiveButtonContent positive button content
     * @param negativeButtonContent negative button content
     */
    public static void showEditDialog(@NonNull Context context,
                                      String title,
                                      String hint,
                                      String initialText,
                                      @NonNull DialogButtonContent positiveButtonContent,
                                      @Nullable DialogButtonContent negativeButtonContent) {
        // EditText
        final EditText editText = new EditText(context);
        editText.setHint(hint);
        editText.setText(initialText);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(editText);

        // set positive button
        builder.setPositiveButton(positiveButtonContent.getLabel(), (dialog, which) -> {
            String text = editText.getText().toString();
            positiveButtonContent.getOnEditConfirmedListener().onEditConfirmed(text);
        });

        // set negative button
        if (negativeButtonContent != null) {
            builder.setNegativeButton(negativeButtonContent.getLabel(), (dialog, which) -> dialog.dismiss());
        }

        // show
        builder.show();
    }

}
