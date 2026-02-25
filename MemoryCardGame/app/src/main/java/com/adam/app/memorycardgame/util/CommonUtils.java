/**
 * Copyright (C) 2017 Adam Chen. All rights reserved.
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/11
 *
 */
package com.adam.app.memorycardgame.util;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.graphics.shapes.Utils;

import com.adam.app.memorycardgame.R;

public abstract class CommonUtils {

    public static final long TOAST_SHOW_TIME = 2000L;
    // TAG
    private static final String TAG = "MemoryCardGame";
    private static String sLastShowMsg;
    private static long sLastShowTime;
    private static Toast sToast;

    private CommonUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * log message
     *
     * @param msg message
     */
    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    public static void error(String s) {
        Log.e(TAG, s);
    }

    /**
     * show toast
     *
     * @param context Context
     * @param msg     message
     */
    public static void showToast(Context context, String msg) {
        // input null check
        if (context == null || msg == null) {
            return;
        }

        // get application context
        Context appContext = context.getApplicationContext();

        // check main looper
        if (Looper.myLooper() != Looper.getMainLooper()) {
            // switch to main loop
            new Handler(Looper.getMainLooper()).post(() -> {
                executeShow(appContext, msg);
            });
            return;
        }

        executeShow(appContext, msg);

    }


    /**
     * execute show toast
     *
     * @param context Context
     * @param msg     message
     */
    private static void executeShow(Context context, String msg) {
        synchronized (Utils.class) {
            long currentTime = System.currentTimeMillis();
            // do not show if the time is less than 2 seconds
            if (msg.equals(sLastShowMsg) && currentTime - sLastShowTime < TOAST_SHOW_TIME) {
                return;
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                if (sToast != null) {
                    sToast.cancel();
                    sToast = null;
                }
            }

            sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            sToast.show();
            sLastShowMsg = msg;
            sLastShowTime = currentTime;
        }
    }

    /**
     * show alert dialog
     */
    public static void showAlertDialog(Context context,
                                       String title,
                                       String message,
                                       DialogButtonContent positiveButton,
                                       DialogButtonContent negativeButton) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false);
        // setup positive button
        if (positiveButton != null) {
            dialogBuilder.setPositiveButton(positiveButton.getLabel(), (dialog, which) -> {
                positiveButton.getListener().onPress();
                dialog.dismiss();
            });
        }

        if (negativeButton != null) {
            dialogBuilder.setNegativeButton(negativeButton.getLabel(), (dialog, which) -> {
                negativeButton.getListener().onPress();
                dialog.dismiss();
            });
        }

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

    }

    /**
     * show unimplemented toast
     *
     * @param context Context
     */
    public static void showUnImplementedToast(Context context) {
        showToast(context, context.getString(R.string.memory_card_game_not_implemented));
    }

    public static class DialogButtonContent {
        private final String mLabel;
        private final onPressListener mListener;

        /**
         * constructor
         */
        public DialogButtonContent(String label, onPressListener listener) {
            mLabel = label;
            mListener = listener;
        }

        public String getLabel() {
            return mLabel;
        }

        public onPressListener getListener() {
            return mListener;
        }

        public interface onPressListener {
            void onPress();
        }

    }

}
