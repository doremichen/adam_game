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
package com.adam.app.snake.presentation.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adam.app.snake.R;
import com.adam.app.snake.databinding.ActivityLeaderBoardBinding;
import com.adam.app.snake.util.Utils;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Leader board activity
 */
@AndroidEntryPoint
public class LeaderBoardActivity extends AppCompatActivity {
    // TAG
    private static final String TAG = "LeaderBoardActivity";

    // view binding
    private ActivityLeaderBoardBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.logDebug(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mBinding = ActivityLeaderBoardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.leaderboard_container, new LeaderboardFragment())
                    .commit();
        }
    }
}
