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

package com.adam.app.galaga.ui.leaderboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.galaga.data.local.entities.ScoreRecord;
import com.adam.app.galaga.databinding.ActivityLeaderboardBinding;
import com.adam.app.galaga.ui.adapter.LeaderboardAdapter;
import com.adam.app.galaga.viewmodel.LeaderboardViewModel;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    //TAG
    private static final String TAG = LeaderboardActivity.class.getSimpleName();

    // view binding
    private ActivityLeaderboardBinding mBinding;

    // adapter
    private LeaderboardAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        // adapter
        mAdapter = new LeaderboardAdapter();
        // linear manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        mBinding.rvLeaderboard.setLayoutManager(linearLayoutManager);
        mBinding.rvLeaderboard.setAdapter(mAdapter);

        // exit button click listener
        mBinding.btnExit.setOnClickListener(v -> finish());

        // init view model
        LeaderboardViewModel viewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        // observer scores
        viewModel.getTopScores().observe(this, this::updateList);


    }

    private void updateList(List<ScoreRecord> scoreRecords) {
        if (scoreRecords == null)  return;
        mAdapter.submitList(new ArrayList<>(scoreRecords));
    }
}