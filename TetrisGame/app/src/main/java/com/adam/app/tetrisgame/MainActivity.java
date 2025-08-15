/**
 * Copyright 2025 Adam
 * Description: MainActivity is the main activity of the application.
 * Author: Adam
 * Date: 2025/06/23
 */
package com.adam.app.tetrisgame;

import android.content.Intent;
import android.os.Bundle;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
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
        mBinding.buttonAbout.setOnClickListener(v -> {
            // start instruction dialog
            String title = getString(R.string.dialog_title_instruction);
            String message = getString(R.string.dialog_message_instruction);
            // Ok button
            Utils.DialogButton okButton = new Utils.DialogButton(getString(R.string.dialog_button_ok), null);
            // show dialog
            Utils.showAlertDialog(MainActivity.this, title, message, okButton, null);
        });
        // set listener for exit button
        mBinding.buttonExit.setOnClickListener(v -> {
            // exit application
            finish();
        });
    }
}