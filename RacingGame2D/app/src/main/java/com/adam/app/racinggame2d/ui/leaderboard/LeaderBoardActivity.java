/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.ui.leaderboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.databinding.ActivityLeaderBoardBinding;
import com.adam.app.racinggame2d.util.Utils;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for the leaderboard.
 */
@AndroidEntryPoint
public final class LeaderBoardActivity extends AppCompatActivity {
    private static final String sTAG = "LeaderBoardActivity";
    private ActivityLeaderBoardBinding mBinding;
    private LeaderBoardAdapter mAdapter;
    private LeaderBoardViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_leader_board);
        mViewModel = new ViewModelProvider(this).get(LeaderBoardViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        initUI();
        initObservers();

        mViewModel.loadScores();
    }

    private void initUI() {
        Utils.logDebug(sTAG, "initUI");
        mAdapter = new LeaderBoardAdapter();
        mBinding.recyclerLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerLeaderboard.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mBinding.recyclerLeaderboard.setAdapter(mAdapter);

        mBinding.btnClose.setOnClickListener(v -> finish());
    }

    private void initObservers() {
        mViewModel.getScoresLiveData().observe(this, records -> {
            if (records != null && !records.isEmpty()) {
                mAdapter.setScores(records);
            }
        });
    }
}
