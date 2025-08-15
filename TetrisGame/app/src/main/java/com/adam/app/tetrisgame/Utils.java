package com.adam.app.tetrisgame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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
            String info = String.format("Class: %s, Method: %s, Line: %d",
                    e.getClassName(),
                    e.getMethodName(),
                    e.getLineNumber());
            Log.d("CallStack", info);
        }
    }

    /**
     * Create Intent with context and class
     * @param context Context
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


    // Numbers of columns and rows
    public static interface NUM {
        int COLUMNS = 10;
        int ROWS = 20;
    }

    /**
     * Dialog Button content
     * info: string
     * listener: DialogInterface.OnClickListener
     */
    public static class DialogButton {
        private String mInfo;
        private DialogInterface.OnClickListener mListener;

        public DialogButton(String info, DialogInterface.OnClickListener listener) {
            mInfo = info;
            mListener = listener;
        }

        public String getInfo() {
            return mInfo;
        }

        public DialogInterface.OnClickListener getListener() {
            return mListener;
        }
    }

    // show alert dialog with message
    public static void showAlertDialog(Context context,
                                       String title,
                                       String message,
                                       DialogButton positiveButton,
                                       DialogButton negativeButton) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButton.getInfo(), positiveButton.getListener());
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton.getInfo(), negativeButton.getListener());
        }
        builder.show();
    }

    /**
     * Show toast message
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }
}
