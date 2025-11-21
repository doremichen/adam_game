/**
 * This class is the about dialog fragment.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-21
 */
package com.adam.app.flappybird.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.adam.app.flappybird.R;

public class AboutDialogFragment extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // View
        View view = requireActivity().getLayoutInflater()
                .inflate(R.layout.about_dialog, null);
        String title = getString(R.string.flappy_bird_about_dialog_title);


        // AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title);
        builder.setView(view);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.flappy_bird_ok_dlg_btn, this::onOkClick);

        return builder.create();

    }

    private void onOkClick(DialogInterface dialogInterface, int i) {
        // dismiss
        if (dialogInterface != null) {
            dialogInterface.dismiss();
        }
    }

}
