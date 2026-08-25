/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import com.adam.app.tetrisgame.R;
import com.adam.app.tetrisgame.databinding.DialogGameBinding;

public final class Utils {
    private static final String TAG = "TetrisGame";
    private static final boolean LOG_FLAG = true;

    private Utils() {
        // Private constructor to prevent instantiation
    }

    public static void log(String message) {
        if (LOG_FLAG) Log.d(TAG, message);
    }

    public static void log(String message, Exception e) {
        if (LOG_FLAG) Log.e(TAG, message, e);
    }

    public static Intent createIntent(Context context, Class<?> classRef) {
        return new Intent(context, classRef);
    }

    public static void showAlertDialog(Context context, String title, String message, DialogButton positiveButton, DialogButton negativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogGameBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_game, null, false);
        
        binding.dialogTitle.setText(title);
        binding.dialogMessage.setText(message);
        AlertDialog dialog = builder.setView(binding.getRoot()).setCancelable(false).create();

        binding.btnPositive.setText(positiveButton.getInfo());
        binding.btnPositive.setOnClickListener(v -> positiveButton.handleClick(dialog));

        if (negativeButton != null) {
            binding.btnNegative.setVisibility(View.VISIBLE);
            binding.btnNegative.setText(negativeButton.getInfo());
            binding.btnNegative.setOnClickListener(v -> negativeButton.handleClick(dialog));
        } else {
            binding.btnNegative.setVisibility(View.GONE);
        }

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
    }

    public static void showToast(Context context, String message) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public interface NUM {
        int COLUMNS = 10;
        int ROWS = 20;
    }

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
            if (mDialogClickListener != null) mDialogClickListener.onClick(dialog);
        }

        @FunctionalInterface
        public interface OnDialogButtonClickListener {
            void onClick(AlertDialog dialog);
        }
    }
}
