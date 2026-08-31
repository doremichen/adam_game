/*
 * Copyright (c) 2026 Adam Game
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
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.databinding.DialogCarInitBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Unified utility class for the RacingGame2D project.
 */
public final class Utils {

    private static final String sTAG = "RacingGame2D";

    private Utils() {
        // Prevent instantiation
    }

    /**
     * CQ-03: Unified Logging
     *
     * @param label   The label/tag for the log.
     * @param message The message to log.
     */
    public static void logDebug(String label, String message) {
        Log.d(sTAG, String.format("[%s]: %s", label, message));
    }

    /**
     * Unified error logging.
     *
     * @param label   The label/tag for the log.
     * @param message The message to log.
     */
    public static void logError(String label, String message) {
        Log.e(sTAG, String.format("[%s]: %s", label, message));
    }

    /**
     * Show toast message, ensuring it runs on the UI thread.
     */
    public static void showToast(Context context, String message) {
        if (context == null || message == null) {
            return;
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(() ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                );
            } else {
                logError("Utils", "Unable to show toast: context is not an Activity");
            }
        }
    }

    /**
     * Show a standard alert dialog.
     */
    public static void showDialog(@NonNull Context context, String title, String message,
                                  DialogButtonContent positiveButton, DialogButtonContent negativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton.getLabel(), positiveButton.getClickListener());
        }
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton.getLabel(), negativeButton.getClickListener());
        }
        builder.show();
    }

    /**
     * Show a dialog with an EditText.
     */
    public static void showEditDialog(@NonNull Context context, String title, String hint, String initialText,
                                      @NonNull DialogButtonContent positiveButton,
                                      @Nullable DialogButtonContent negativeButton) {
        final EditText editText = new EditText(context);
        editText.setHint(hint);
        editText.setText(initialText);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(editText);
        builder.setCancelable(false);

        builder.setPositiveButton(positiveButton.getLabel(), (dialog, which) -> {
            String text = editText.getText().toString();
            if (positiveButton.getOnEditConfirmedListener() != null) {
                positiveButton.getOnEditConfirmedListener().onEditConfirmed(text);
            }
        });

        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton.getLabel(), (dialog, which) -> dialog.dismiss());
        }

        builder.show();
    }

    /**
     * Show a dialog for initializing car and player info with custom racing style.
     */
    public static void showCarInitDialog(@NonNull Context context, String title,
                                         String playerHint, String carIdHint, String carNameHint,
                                         String positiveLabel,
                                         @NonNull OnCarInitConfirmedListener positiveListener,
                                         @Nullable DialogButtonContent negativeButton) {
        DialogCarInitBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.dialog_car_init, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();

        binding.btnDialogOk.setOnClickListener(v -> {
            positiveListener.onConfirmed(
                    binding.etPlayerName.getText().toString(),
                    binding.etCarId.getText().toString(),
                    binding.etCarName.getText().toString()
            );
            dialog.dismiss();
        });

        binding.btnDialogCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    /**
     * Helper to draw debug information on canvas.
     */
    public static void debugDraw(Canvas canvas) {
        Paint debugPaint = new Paint();
        debugPaint.setColor(Color.GREEN);
        debugPaint.setStrokeWidth(2f);
        debugPaint.setTextSize(30f);

        for (int y = 0; y < canvas.getHeight(); y += 200) {
            canvas.drawLine(0, y, canvas.getWidth(), y, debugPaint);
            canvas.drawText("Y=" + y, 10, y + 30, debugPaint);
        }

        for (int x = 0; x < canvas.getWidth(); x += 200) {
            canvas.drawLine(x, 0, x, canvas.getHeight(), debugPaint);
            canvas.drawText("X=" + x, x + 10, 40, debugPaint);
        }

        canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, 10f, debugPaint);
        canvas.drawText("Center", canvas.getWidth() / 2f + 10, canvas.getHeight() / 2f + 10, debugPaint);
    }

    /**
     * Deep copy a list of PointF objects.
     */
    public static List<PointF> deepCopyPoints(List<PointF> source) {
        if (source == null) return new ArrayList<>();
        List<PointF> copy = new ArrayList<>();
        for (PointF p : source) {
            copy.add(new PointF(p.x, p.y));
        }
        return copy;
    }

    /**
     * Dump point list to log for debugging.
     */
    public static void dumpList(String name, List<PointF> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        logDebug("Utils", name + " size: " + list.size());
        logDebug("Utils", "============================================");
        for (PointF pointF : list) {
            logDebug("Utils", "Point: " + pointF.x + ", " + pointF.y);
        }
        logDebug("Utils", "============================================");
    }

    /**
     * Callback for car initialization dialog.
     */
    public interface OnCarInitConfirmedListener {
        void onConfirmed(String playerName, String carId, String carName);
    }

    /**
     * Dialog button wrapper.
     */
    public static class DialogButtonContent {
        private final String mLabel;
        private DialogInterface.OnClickListener mClickListener;
        private OnEditConfirmedListener mOnEditConfirmedListener;

        public DialogButtonContent(String label, DialogInterface.OnClickListener clickListener) {
            mLabel = label;
            mClickListener = clickListener;
        }

        public DialogButtonContent(String label, @NonNull OnEditConfirmedListener listener) {
            mLabel = label;
            mOnEditConfirmedListener = listener;
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

        public interface OnEditConfirmedListener {
            void onEditConfirmed(String text);
        }
    }
}
