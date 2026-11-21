/*
 * Copyright (c) 2026 Adam Chen
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

package com.adam.app.arenaminifight.ui.leaderboard;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.arenaminifight.data.local.AppDatabase;
import com.adam.app.arenaminifight.data.local.ScoreEntity;
import com.adam.app.arenaminifight.databinding.ActivityLeaderboardBinding;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Leaderboard Activity displaying top survival scores from Room Database.
 * Trace: UC-MENU-04, REQ-MENU-04, UI-004, REQ-NFR-04
 */
@AndroidEntryPoint
public class LeaderboardActivity extends AppCompatActivity {

    private static final String TAG = LeaderboardActivity.class.getSimpleName();

    private ActivityLeaderboardBinding mBinding;
    private LeaderboardAdapter mAdapter;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameUtil.log(TAG + ": onCreate()");

        mBinding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.recyclerScores.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LeaderboardAdapter();
        mBinding.recyclerScores.setAdapter(mAdapter);

        mBinding.btnBack.setOnClickListener(v -> {
            GameUtil.log(TAG + ": Back clicked");
            finish();
        });

        loadScores();
    }

    private void loadScores() {
        mExecutor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                List<ScoreEntity> scores = db.scoreDao().getTopScores();
                runOnUiThread(() -> {
                    if (scores == null || scores.isEmpty()) {
                        mBinding.tvEmpty.setVisibility(View.VISIBLE);
                        mBinding.recyclerScores.setVisibility(View.GONE);
                    } else {
                        mBinding.tvEmpty.setVisibility(View.GONE);
                        mBinding.recyclerScores.setVisibility(View.VISIBLE);
                        mAdapter.setScores(scores);
                    }
                });
            } catch (Exception e) {
                GameUtil.log(TAG + ": Error loading scores: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExecutor.shutdown();
    }
}
