/*
 * Copyright (c) 2025 Adam
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
package com.adam.app.tetrisgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.adam.app.tetrisgame.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // view binding
    private ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.log("MainActivity onCreate");
        // view binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // set listener
        setListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // view binding
        mBinding = null;
    }

    private void setListener() {
        // set listener for start button
        mBinding.buttonStart.setOnClickListener(v -> {
            // start game activity
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });
        // set listener for leaderboard button
        mBinding.buttonLeaderboard.setOnClickListener(v -> {
            // start leaderboard activity
            Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });

        // set listener for about button
        mBinding.buttonAbout.setOnClickListener(this::showAboutDialog);
        // set listener for exit button
        mBinding.buttonExit.setOnClickListener(v -> {
            // exit application
            finish();
        });
    }

    private void showAboutDialog(View view) {
        // Ok button
        Utils.DialogButton okButton = new Utils.DialogButton(getString(R.string.dialog_button_ok),
                this::dismissDialog);

        String title = getString(R.string.dialog_title_instruction);
        String message = getString(R.string.dialog_message_instruction);
        // show dialog
        Utils.showAlertDialog(MainActivity.this, title, message, okButton, null);

    }

    private void dismissDialog(AlertDialog dialog) {
        // dismiss
        dialog.dismiss();
    }


}