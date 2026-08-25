/*
 * Copyright (c) 2026 Adam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.adam.app.tetrisgame.presentation.ui;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.adam.app.tetrisgame.R;
import com.adam.app.tetrisgame.databinding.ActivityLeaderboardBinding;
import com.adam.app.tetrisgame.presentation.viewmodel.LeaderboardViewModel;
import com.adam.app.tetrisgame.presentation.ui.adapter.ScoreAdapter;
import com.adam.app.tetrisgame.presentation.ui.dialog.ProgressDialog;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LeaderboardActivity extends AppCompatActivity {
    private ActivityLeaderboardBinding mBinding;
    private LeaderboardViewModel mViewModel;
    private ScoreAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_leaderboard);

        mAdapter = new ScoreAdapter();
        mBinding.listViewScores.setAdapter(mAdapter);

        mViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.progress_loading));

        mViewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) progressDialog.show();
            else progressDialog.dismiss();
        });

        mViewModel.getScores().observe(this, scoreList -> {
            mAdapter.setScoreList(scoreList);
            mBinding.textViewEmpty.setVisibility(scoreList.isEmpty() ? View.VISIBLE : View.GONE);
            mBinding.listViewScores.setVisibility(scoreList.isEmpty() ? View.GONE : View.VISIBLE);
        });

        mViewModel.loadScores();
    }
}
