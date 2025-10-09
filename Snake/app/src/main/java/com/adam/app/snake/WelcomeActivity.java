/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the welcome activity that is the launcher activity of the game
 *
 * Author: Adam Chen
 * Date: 2025/10/08
 */
package com.adam.app.snake;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adam.app.snake.data.file.SharedPreferenceManager;
import com.adam.app.snake.databinding.ActivityWelcomeBinding;
import com.adam.app.snake.ui.GameActivity;
import com.adam.app.snake.ui.LeaderBoardActivity;
import com.adam.app.snake.ui.dialog.NameInputDialog;
import com.adam.app.snake.util.Utils;

public class WelcomeActivity extends AppCompatActivity {

    // TAG
    private static final String TAG = "WelcomeActivity";

    // view binding
    private ActivityWelcomeBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // start game
        mBinding.btnStart.setOnClickListener(v -> {
            // show input user name dialog
            showInputUserNameDialog();
            //startActivity(GameActivity.class);
        });

        // start leader board
        mBinding.btnLeaderboard.setOnClickListener(v -> {
            Utils.logDebug(TAG, "start leader board");
           startActivity(LeaderBoardActivity.class);
        });

        // exit
        mBinding.btnExit.setOnClickListener(v -> {
            finishAffinity();
        });
    }

    private void showInputUserNameDialog() {
        Utils.logDebug(TAG, "showInputUserNameDialog");
        // new
        NameInputDialog dlg = new NameInputDialog();
        dlg.setListener(new NameInputDialog.Listener() {
            @Override
            public void onNameConfirmed(String name) {
                Utils.logDebug(TAG, "onNameConfirmed: " + name);
                // save user name to shared preferences
                SharedPreferenceManager manager = SharedPreferenceManager.getInstance(WelcomeActivity.this);
                manager.putString(SharedPreferenceManager.Keys.USER_NAME, name);
                // start game
                startActivity(GameActivity.class);
            }
            @Override
            public void onNameCanceled() {
                Utils.logDebug(TAG, "onNameCanceled");
            }
        });
        dlg.show(getSupportFragmentManager(), "name_input_dialog");
    }

    /**
     * start activity
     * @param cls
     */
    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}