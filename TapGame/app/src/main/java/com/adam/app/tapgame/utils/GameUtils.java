/**
 * File: GameUtil.java
 * Description: This class is Game Utils
 *
 * @author Adam Chen
 * @version 1.0 - 2026/01/14
 */
package com.adam.app.tapgame.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.adam.app.tapgame.R;

public final class GameUtils {

    // TAG
    private static final String TAG = "GameUtils";

    private GameUtils() {}

    /**
     * log message
     * @param msg String
     */
    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    public static void showUnImplemented(Context context) {
        showToast(context, R.string.tap_game_not_implement_yet_toast);

    }

    public static void showToast(Context context, int resId) {
        Context ctx = context.getApplicationContext();
        // check if main thread
        if (Thread.currentThread() != context.getMainLooper().getThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx, resId, Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        Toast.makeText(ctx, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * show alert dialog
     * @param context Context
     * @param title String
     * @param message String
     * @param positiveButton DialogButton
     * @param negativeButton DialogButton
     */
    public static void showAlertDialog(Context context,
                                       String title,
                                       String message,
                                       DialogButton positiveButton,
                                       DialogButton negativeButton) {

        // AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);

        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton.getLabel(), (dialog, which) -> {
                positiveButton.getListener().onClick();
                dialog.dismiss();
            });
        }

        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton.getLabel(), (dialog, which) -> {
               negativeButton.getListener().onClick();
               dialog.dismiss();
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();

    }



    public enum NavigationDestination {
        MAIN,
        START_GAME,
        SETTINGS,
        ABOUT,
        EXIT,
        NONE
    }


    public static class DialogButton {
        final String label;
        final DialogButtonListener listener;

        public DialogButton(String label, DialogButtonListener listener) {
            this.label = label;
            this.listener = listener;
        }

        public String getLabel() {
            return label;
        }

        public DialogButtonListener getListener() {
            return listener;
        }

        public interface DialogButtonListener {
            void onClick();
        }
    }

}
