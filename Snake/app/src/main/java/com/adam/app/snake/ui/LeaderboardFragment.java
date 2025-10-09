/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the leaderboard fragment that is used to display the leaderboard
 *
 * Author: Adam Chen
 * Date: 2025/10/08
 */
package com.adam.app.snake.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.snake.R;
import com.adam.app.snake.data.entity.LeaderboardEntry;
import com.adam.app.snake.ui.adapter.LeaderboardAdapter;
import com.adam.app.snake.util.Utils;
import com.adam.app.snake.viewmodel.LeaderboardViewModel;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {
    private static final String TAG = "LeaderboardFragment";

    private LeaderboardViewModel mViewModel;
    private LeaderboardAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        // initial View
        mRecyclerView = view.findViewById(R.id.recycler_leaderboard);
        mEmptyTextView = view.findViewById(R.id.tvEmpty);

        // initial RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new LeaderboardAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // initial RecyclerView divider
        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_leaderboard));
        mRecyclerView.addItemDecoration(divider);

        // create ViewModel
        mViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        // Observer data change
        mViewModel.getTopScores().observe(getViewLifecycleOwner(), this::updateUI);

        // Exit button click listener
        view.findViewById(R.id.btnExit).setOnClickListener(v -> requireActivity().finish());

        return view;
    }

    /**
     * Refresh leaderboard data
     */
    private void updateUI(List<LeaderboardEntry> leaderboardEntries) {
        Utils.logDebug(TAG, "updateUI: list size = " + leaderboardEntries.size());

        if (leaderboardEntries == null || leaderboardEntries.isEmpty()) {
            showEmptyView();
        } else {
            showListView(leaderboardEntries);
        }
    }

    /**
     * 顯示「無資料」畫面
     */
    private void showEmptyView() {
        Utils.logDebug(TAG, "No leaderboard data available");

        mRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyTextView.setVisibility(View.VISIBLE);
        mEmptyTextView.setAlpha(0f);
        mEmptyTextView.animate().alpha(1f).setDuration(300).start();
        mEmptyTextView.requestLayout();
    }

    /**
     * show leaderboard data
     */
    private void showListView(List<LeaderboardEntry> entries) {
        Utils.logDebug(TAG, "Show leaderboard data");
        dumpList(entries);
        mEmptyTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setAlpha(0f);
        mRecyclerView.animate().alpha(1f).setDuration(300).start();

        // Update list
        mAdapter.submitList(new ArrayList<>(entries));
    }

    /**
     * （可選）Debug 輔助函式
     */
    private static void dumpList(List<LeaderboardEntry> leaderboardEntries) {
        if (leaderboardEntries == null) return;
        StringBuilder sb = new StringBuilder("Leaderboard entries:\n");
        for (LeaderboardEntry entry : leaderboardEntries) {
            sb.append(entry.getName())
                    .append(": ")
                    .append(entry.getScore())
                    .append("\n");
        }
        Utils.logDebug(TAG, sb.toString());
    }
}

