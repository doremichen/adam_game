/**
 * Copyright 2025 Adam Game
 * <p>
 * This class provides the dialog for the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-02
 *
 */
package com.adam.app.whack_a_molejava.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.adam.app.whack_a_molejava.R;

public final class GameDialog {

    private GameDialog() {
        throw new AssertionError("No instances.");
    }

    /**
     * show dialog
     * @param context context
     * @param title title
     * @param message message
     * @param positive positive
     * @param negative negative
     */
    public static void showDialog(Context context,
                                  String title,
                                  String message,
                                  DialogButtonContent positive,
                                  DialogButtonContent negative) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_game, null);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        Button btnPositive = view.findViewById(R.id.btnPositive);
        Button btnNegative = view.findViewById(R.id.btnNegative);

        // set information
        tvTitle.setText(title);
        tvMessage.setText(message);

        // AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(false);
        // Alert dialog
        AlertDialog dialog = builder.create();
        // set button label
        btnPositive.setText(positive.getLabel());
        btnPositive.setOnClickListener(v -> {
            dialog.dismiss();
            positive.handleClick();
        });

        // Negative Button
        if (negative != null) {
            btnNegative.setVisibility(View.VISIBLE);
            btnNegative.setText(negative.getLabel());
            btnNegative.setOnClickListener(v -> {
                dialog.dismiss();
                negative.handleClick();
            });
        } else {
            btnNegative.setVisibility(View.GONE);
        }

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

    }

    /**
     * Dialog button
     */
    public static class DialogButtonContent {

        private final String mLabel;
        private final OnDialogButtonClickListener mDialogClickListener;

        public DialogButtonContent(String label, OnDialogButtonClickListener listener) {
            mLabel = label;
            mDialogClickListener = listener;
        }

        public String getLabel() {
            return mLabel;
        }

        public void handleClick() {
            if (mDialogClickListener != null) {
                mDialogClickListener.onClick();
            }
        }

        @FunctionalInterface
        public interface OnDialogButtonClickListener {
            void onClick();
        }
    }


}
