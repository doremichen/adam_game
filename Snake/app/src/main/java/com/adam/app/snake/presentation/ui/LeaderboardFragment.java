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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.snake.R;
import com.adam.app.snake.data.entity.LeaderboardEntry;
import com.adam.app.snake.databinding.FragmentLeaderboardBinding;
import com.adam.app.snake.presentation.ui.adapter.LeaderboardAdapter;
import com.adam.app.snake.presentation.viewmodel.LeaderboardViewModel;
import com.adam.app.snake.util.Utils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Leaderboard fragment
 */
@AndroidEntryPoint
public class LeaderboardFragment extends Fragment {
    private static final String TAG = "LeaderboardFragment";

    private LeaderboardViewModel mViewModel;
    private LeaderboardAdapter mAdapter;
    private FragmentLeaderboardBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, "onCreateView");

        mBinding = FragmentLeaderboardBinding.inflate(inflater, container, false);

        // initial RecyclerView
        mBinding.recyclerLeaderboard.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new LeaderboardAdapter();
        mBinding.recyclerLeaderboard.setAdapter(mAdapter);

        // initial RecyclerView divider
        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_leaderboard));
        mBinding.recyclerLeaderboard.addItemDecoration(divider);

        // create ViewModel
        mViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        // Observer data change
        mViewModel.getTopScores().observe(getViewLifecycleOwner(), this::updateUI);

        // Exit button click listener
        mBinding.btnExit.setOnClickListener(v -> requireActivity().finish());

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    /**
     * Refresh leaderboard data
     * @param leaderboardEntries List<LeaderboardEntry>
     */
    private void updateUI(List<LeaderboardEntry> leaderboardEntries) {
        Utils.logDebug(TAG, "updateUI: list size = " + (leaderboardEntries != null ? leaderboardEntries.size() : 0));

        if (leaderboardEntries == null || leaderboardEntries.isEmpty()) {
            showEmptyView();
        } else {
            showListView(leaderboardEntries);
        }
    }

    /**
     * Update empty view
     */
    private void showEmptyView() {
        Utils.logDebug(TAG, "No leaderboard data available");

        if (mBinding != null) {
            mBinding.recyclerLeaderboard.setVisibility(View.INVISIBLE);
            mBinding.tvEmpty.setVisibility(View.VISIBLE);
            mBinding.tvEmpty.setAlpha(0f);
            mBinding.tvEmpty.animate().alpha(1f).setDuration(300).start();
            mBinding.tvEmpty.requestLayout();
        }
    }

    /**
     * show leaderboard data
     * @param entries List<LeaderboardEntry>
     */
    private void showListView(List<LeaderboardEntry> entries) {
        Utils.logDebug(TAG, "Show leaderboard data");
        if (mBinding != null) {
            mBinding.tvEmpty.setVisibility(View.INVISIBLE);
            mBinding.recyclerLeaderboard.setVisibility(View.VISIBLE);
            mBinding.recyclerLeaderboard.setAlpha(0f);
            mBinding.recyclerLeaderboard.animate().alpha(1f).setDuration(300).start();

            // Update list
            mAdapter.submitList(new ArrayList<>(entries));
        }
    }
}
