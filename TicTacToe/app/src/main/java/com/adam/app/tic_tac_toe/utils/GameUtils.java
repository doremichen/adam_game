/**
 * This class provides utility methods for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.adam.app.tic_tac_toe.R;

public final class GameUtils {

    private static final String TAG = "TICTACTOE";
    private static final String TAG_UTIL = "GameUtils";

    public static final String VERSION_NAME = "1.0";

    private GameUtils() {
        // avoid to be instantiated
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
     * @param context Context
     * @param message String
     */
    public static void showToast(Context context, String message) {
        // check if main looper
        if (android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            });
        } else {
            try {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
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


    public static void unImplemented(Context context) {
        showToast(context, context.getString(R.string.tic_tac_toe_not_implemented_yet_msg));
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

        public static interface onPressListener {
            void onPress();
        }

    }


}
