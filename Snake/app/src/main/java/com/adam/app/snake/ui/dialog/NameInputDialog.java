/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the input user name dialog
 * <p>
 * Author: Adam Chen`
 * Date: 2025/10/09
 */
package com.adam.app.snake.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.adam.app.snake.R;
import com.adam.app.snake.util.Utils;

public class NameInputDialog extends DialogFragment {
    public static final String TAG = "NameInputDialog";
    private Listener mListener;
    private EditText mEtPlayerName;

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, "onCreateDialog");

        // inflate 自訂 layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_input_name, null);
        mEtPlayerName = view.findViewById(R.id.etPlayerName);

        // 建立 AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.snake_game_user_name_dialog_title)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.snake_game_ok, null)
                .setNegativeButton(R.string.snake_game_cancel, null);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Utils.logDebug(TAG, "onStart");

        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog == null) return;

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        // 防止空指標
        if (positiveButton == null || mEtPlayerName == null) {
            Utils.logDebug(TAG, "onStart: button or edittext is null");
            return;
        }

        positiveButton.setOnClickListener(v -> {
            String name = mEtPlayerName.getText().toString().trim();
            if (name.isEmpty()) {
                mEtPlayerName.setError(getString(R.string.snake_game_user_name_error));
                mEtPlayerName.requestFocus();
                return;
            }

            if (mListener != null) {
                mListener.onNameConfirmed(name);
            }
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onNameCanceled();
            }
            dialog.dismiss();
        });
    }

    public interface Listener {
        void onNameConfirmed(String name);

        void onNameCanceled();
    }
}

