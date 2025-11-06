/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the leader board activity.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/11/05
 */
package com.adam.app.racinggame2d.view.leaderboard;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.racinggame2d.databinding.ActivityLeaderBoardBinding;
import com.adam.app.racinggame2d.util.GameUtil;
import com.adam.app.racinggame2d.viewmodel.LeaderBoardViewModel;

public class LeaderBoardActivity extends AppCompatActivity {
    //TAG
    private static final String TAG = "LeaderBoardActivity";
    private static final boolean IS_DEBUG= true;

    //view binding
    private ActivityLeaderBoardBinding mBinding;
    // adapter
    private LeaderBoardAdapter mAdapter;
    // viewmodel
    private LeaderBoardViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info("onCreate");

        // view binding
        mBinding = ActivityLeaderBoardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // init adapter
        mAdapter = new LeaderBoardAdapter();
        // init view model
        mViewModel = new ViewModelProvider(this).get(LeaderBoardViewModel.class);

        // setup recycle view
        setupRecycleView();

        //observer list of leaderboard entity
        mViewModel.getScoresLiveData().observe(this, scores -> {
            if (scores == null || scores.isEmpty()) {
                // show empty view
                showEmptyView(true);
                return;
            }

            // show list view
            showEmptyView(false);
            // set scores
            mAdapter.setScores(scores);
        });

        // Close button listener
        mBinding.btnClose.setOnClickListener(v -> finish());


        // load scores
        mViewModel.loadScores();
    }

    private void showEmptyView(boolean show) {
        mBinding.recyclerLeaderboard.setVisibility(show? View.GONE: View.VISIBLE);
        mBinding.tvEmpty.setVisibility(show? View.VISIBLE: View.GONE);
    }



    private void setupRecycleView() {
        info("setupRecycleView");
        // set layout manager
        mBinding.recyclerLeaderboard.setLayoutManager(new LinearLayoutManager(this));

        // initial RecyclerView divider
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mBinding.recyclerLeaderboard.addItemDecoration(divider);



        // set adapter
        mBinding.recyclerLeaderboard.setAdapter(mAdapter);
    }

    private void info(String msg) {
        if (IS_DEBUG) {
            GameUtil.log(TAG, msg);
        }
    }
}