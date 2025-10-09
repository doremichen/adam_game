/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the leader board activity that is used to display the leader board
 * <p>
 * Author: Adam Chen
 * Date: 2025/10/08
 */
package com.adam.app.snake.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adam.app.snake.R;
import com.adam.app.snake.util.Utils;

public class LeaderBoardActivity extends AppCompatActivity {
    // TAG
    private static final String TAG = "LeaderBoardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.logDebug(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.leaderboard_container, new LeaderboardFragment())
                    .commit();
        }
    }
}