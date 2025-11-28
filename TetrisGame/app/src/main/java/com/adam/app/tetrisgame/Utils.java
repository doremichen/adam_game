/**
 * Description: This class is the utils class.
 * <p>
 * Author: Adam Chen
 * Date: 2025/08/15
 */
package com.adam.app.tetrisgame;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public final class Utils {
    // TAG string: TerisGame
    private static final String TAG = "TetrisGame";

    // log flag
    private static final boolean LOG_FLAG = true;

    // Logcat with string message
    public static void log(String message) {
        if (!LOG_FLAG) {
            return;
        }
        Log.d(TAG, message);
    }

    // Logcat with string message and exception
    public static void log(String message, Exception e) {
        if (!LOG_FLAG) {
            return;
        }
        Log.e(TAG, message, e);
    }

    // log call stack
    public static void callStack() {
        if (!LOG_FLAG) {
            return;
        }
        StackTraceElement[] list = Thread.currentThread().getStackTrace();
        for (StackTraceElement e : list) {
            String info = String.format("Class: %s, Method: %s, Line: %d", e.getClassName(), e.getMethodName(), e.getLineNumber());
            Log.d("CallStack", info);
        }
    }

    /**
     * Create Intent with context and class
     *
     * @param context  Context
     * @param classRef class reference
     * @return Intent
     */
    public static Intent createIntent(Context context, Class<?> classRef) {
        return new Intent(context, classRef);
    }

    public static void log(String Msg, String subMsg, Exception e) {
        if (!LOG_FLAG) {
            return;
        }

        String message = String.format("%s: %s", Msg, subMsg);

        Log.e(TAG, message, e);
    }

    /**
     * Show alert dialog
     *
     * @param context        Context
     * @param title          Title of dialog
     * @param message        Message of dialog
     * @param positiveButton Positive button
     * @param negativeButton Negative button
     */
    public static void showAlertDialog(Context context, String title, String message, DialogButton positiveButton, DialogButton negativeButton) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_game, null);
        // find view by id
        TextView tvTitle = view.findViewById(R.id.dialog_title);
        TextView tvMessage = view.findViewById(R.id.dialog_message);
        Button btnPositive = view.findViewById(R.id.btn_positive);
        Button btnNegative = view.findViewById(R.id.btn_negative);
        // set text
        tvTitle.setText(title);
        tvMessage.setText(message);
        // create dialog
        AlertDialog dialog = builder.setView(view).setCancelable(false).create();

        // Positive Button
        btnPositive.setText(positiveButton.getInfo());
        btnPositive.setOnClickListener(v -> {
            positiveButton.handleClick(dialog);
        });

        // Negative Button
        if (negativeButton != null) {
            btnNegative.setVisibility(View.VISIBLE);
            btnNegative.setText(negativeButton.getInfo());
            btnNegative.setOnClickListener(v -> negativeButton.handleClick(dialog));
        } else {
            btnNegative.setVisibility(View.GONE);
        }

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    /**
     * Show toast message
     */
    public static void showToast(Context context, String message) {
        // check main looper
        if (android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
            android.os.Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show());
            return;
        }


        Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    /**
     * Dump list
     */
    public static void dumpList(List<?> list) {
        log("===============================================");
        if (list == null) {
            return;
        }
        for (Object o : list) {
            Log.d(TAG, o.toString());
        }
        log("===============================================");
    }

    // Numbers of columns and rows
    public static interface NUM {
        int COLUMNS = 10;
        int ROWS = 20;
    }

    /**
     * Dialog button
     */
    public static class DialogButton {

        private final String mInfo;
        private final OnDialogButtonClickListener mDialogClickListener;

        public DialogButton(String info, OnDialogButtonClickListener listener) {
            mInfo = info;
            mDialogClickListener = listener;
        }

        public String getInfo() {
            return mInfo;
        }

        public void handleClick(AlertDialog dialog) {
            if (mDialogClickListener != null) {
                mDialogClickListener.onClick(dialog);
            }
        }

        @FunctionalInterface
        public interface OnDialogButtonClickListener {
            void onClick(AlertDialog dialog);
        }
    }

}
