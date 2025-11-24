/**
 * This class is the Flappy Bird leader board activity.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-21
 */
package com.adam.app.flappybird.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.flappybird.databinding.ActivityLeaderBoardBinding;
import com.adam.app.flappybird.ui.adapter.LeaderBoardAdapter;
import com.adam.app.flappybird.viewmodel.LeaderBoardViewModel;

public class LeaderBoardActivity extends AppCompatActivity {

    // view binding
    private ActivityLeaderBoardBinding mBinding;
    // view model
    private LeaderBoardViewModel mViewModel;
    // adapter
    private LeaderBoardAdapter mAdapter;

    public static Intent createIntent(AppCompatActivity activity) {
        return new Intent(activity, LeaderBoardActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLeaderBoardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        // init view model
        mViewModel = new ViewModelProvider(this).get(LeaderBoardViewModel.class);
        // init adapter
        mAdapter = new LeaderBoardAdapter();
        mBinding.recyclerLeaderboard.setAdapter(mAdapter);
        mBinding.recyclerLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        // get data from view model
        mViewModel.getAllFlappyBirds().observe(this, scores -> {
            if (scores == null || scores.isEmpty()) {
                showEmptyView(true);
                return;
            }

            showEmptyView(false);
            mAdapter.submitList(scores);
        });

        mBinding.btnClose.setOnClickListener(v -> finish());
        // load data
        mViewModel.loadScores();
    }

    private void showEmptyView(boolean show) {
        mBinding.recyclerLeaderboard.setVisibility(show? View.GONE: View.VISIBLE);
        mBinding.tvEmpty.setVisibility(show? View.VISIBLE: View.GONE);
    }
}