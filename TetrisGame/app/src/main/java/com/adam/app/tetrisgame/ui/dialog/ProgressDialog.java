/**
 * Description: This class is used to show the progress dialog. when the game is loading.
 *
 * Author: Adam Chen
 * Date: 2025/08/18
 */
package com.adam.app.tetrisgame.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.adam.app.tetrisgame.R;

public class ProgressDialog extends AlertDialog {
    // message text view
    private TextView mMessageTextView;

    public ProgressDialog(Context context) {
        super(context);
        init(context);
    }

    /**
     * initial progress dialog
     * @param context context
     */
    private void init(Context context) {
        // Layout inflater
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_progress, null);
        mMessageTextView = view.findViewById(R.id.textMessage);

        setView(view);
        setCancelable(false);
    }

    /**
     * set message
     * @param message message
     */
    public void setMessage(String message) {
        mMessageTextView.setText(message);
    }
}
