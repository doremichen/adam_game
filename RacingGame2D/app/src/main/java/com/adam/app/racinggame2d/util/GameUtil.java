/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is used to provide utility functions for the game.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/28
 */
package com.adam.app.racinggame2d.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class GameUtil {

    public static final String UTil_TAG = "GameUtil";
    // TAG
    public static final String TAG = "RacingGame2D";

    private GameUtil() {
        // avoid to be instantiated
    }

    /**
     * Log
     * log the message
     *
     * @param title
     * @param message
     */
    public static void log(String title, String message) {
        String info = String.format("%s: %s", title, message);
        Log.d(TAG, info);
    }

    /**
     * errorLog
     * log the error message
     *
     * @param title
     * @param message
     */
    public static void error(String title, String message) {
        String info = String.format("%s: %s", title, message);
        Log.e(TAG, info);
    }


    /**
     * showToast
     * Show toast message
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
     * showDialog
     * Show dialog
     *
     * @param context               context
     * @param title                 title of the dialog
     * @param message               message of the dialog
     * @param positiveButtonContent positive button content
     * @param negativeButtonContent negative button content
     */
    public static void showDialog(@NonNull Context context, String title, String message, DialogButtonContent positiveButtonContent, DialogButtonContent negativeButtonContent) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
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
     * Show edit dialog
     *
     * @param context               context
     * @param title                 title of the dialog
     * @param hint                  hint of the edit text
     * @param initialText           initial text of the edit text
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
        builder.setCancelable(false);

        // set positive button
        builder.setPositiveButton(positiveButtonContent.getLabel(), (dialog, which) -> {
            // get the text from the edit text
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

    /**
     * debugDraw
     * Draw debug lines on the canvas
     *
     * @param canvas canvas to draw on
     */
    public static void debugDraw(Canvas canvas) {
        log(UTil_TAG, "debugDraw");
        // draw debug grid lines
        Paint debugPaint = new Paint();
        debugPaint.setColor(Color.GREEN);
        debugPaint.setStrokeWidth(2f);
        debugPaint.setTextSize(30f);

        // draw debug grid lines on the canvas (every 20px)
        for (int y = 0; y < canvas.getHeight(); y += 200) {
            canvas.drawLine(0, y, canvas.getWidth(), y, debugPaint);
            canvas.drawText("Y=" + y, 10, y + 30, debugPaint);
        }

        // draw debug grid lines on the canvas (every 20px)
        for (int x = 0; x < canvas.getWidth(); x += 200) {
            canvas.drawLine(x, 0, x, canvas.getHeight(), debugPaint);
            canvas.drawText("X=" + x, x + 10, 40, debugPaint);
        }

        // draw debug center line
        canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, 10f, debugPaint);
        canvas.drawText("Center", canvas.getWidth() / 2f + 10, canvas.getHeight() / 2f + 10, debugPaint);
    }

    /**
     * class DialogButtonContent
     * Dialog button content that has label and click listener
     */
    public static class DialogButtonContent {
        /**
         * label
         * label of the button
         */
        private String mLabel;

        /**
         * clickListener
         * click listener of the button
         */
        private DialogInterface.OnClickListener mClickListener;
        private OnEditConfirmedListener mOnEditConfirmedListener;

        /**
         * Edit text content
         * Constructor
         *
         * @param label                   label of the button
         * @param onEditConfirmedListener click listener of the button
         */
        public DialogButtonContent(String label, @NonNull OnEditConfirmedListener onEditConfirmedListener) {
            mLabel = label;
            mOnEditConfirmedListener = onEditConfirmedListener;
        }

        /**
         * Show info
         * Constructor
         *
         * @param label         label of the button
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

        /**
         * interface OnEditConfirmedListener
         */
        public interface OnEditConfirmedListener {
            void onEditConfirmed(String text);
        }


    }

    /**
     * dumpList
     * Dump the list to the log
     * @param list list to dump
     */
    public static void dumpList(String Name, List<PointF> list) {
        if (list == null && list.isEmpty()) {
            return;
        }
        log(UTil_TAG, Name + " size: " + list.size());
        log(UTil_TAG, "============================================");
        for (PointF pointF : list) {
            log(UTil_TAG, "dumpList: " + pointF.x + ", " + pointF.y);
        }
        log(UTil_TAG, "============================================");
    }

    /**
     * deepCopyPoints
     * Deep copy the list of points
     * @param source source list
     * @return copy list
     */
    public static List<PointF> deepCopyPoints(List<PointF> source) {
        List<PointF> copy = new ArrayList<>();
        for (PointF p : source) {
            copy.add(new PointF(p.x, p.y)); // deep copy
        }
        return copy;
    }



}
