/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.presentation.ui.dialog;

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
import com.adam.app.snake.databinding.DialogInputNameBinding;
import com.adam.app.snake.util.Utils;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Input user name dialog
 */
@AndroidEntryPoint
public class NameInputDialog extends DialogFragment {
    public static final String TAG = "NameInputDialog";
    private Listener mListener;
    private DialogInputNameBinding mBinding;

    /**
     * Set listener
     * @param listener Listener
     */
    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, "onCreateDialog");

        // inflate
        mBinding = DialogInputNameBinding.inflate(requireActivity().getLayoutInflater());

        // create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.snake_game_user_name_dialog_title)
                .setView(mBinding.getRoot())
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
        if (dialog == null) {
            return;
        }

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        // null check
        if (positiveButton == null || mBinding == null) {
            Utils.logDebug(TAG, "onStart: button or binding is null");
            return;
        }

        positiveButton.setOnClickListener(v -> {
            String name = mBinding.etPlayerName.getText().toString().trim();
            if (name.isEmpty()) {
                mBinding.etPlayerName.setError(getString(R.string.snake_game_user_name_error));
                mBinding.etPlayerName.requestFocus();
                return;
            }

            if (mListener != null) {
                mListener.onNameConfirmed(name);
            }
            dialog.dismiss();
        });

        if (negativeButton != null) {
            negativeButton.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onNameCanceled();
                }
                dialog.dismiss();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    /**
     * Listener interface
     */
    public interface Listener {
        void onNameConfirmed(String name);

        void onNameCanceled();
    }
}
