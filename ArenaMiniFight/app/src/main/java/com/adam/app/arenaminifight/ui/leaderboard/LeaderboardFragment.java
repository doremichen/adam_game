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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.arenaminifight.data.local.AppDatabase;
import com.adam.app.arenaminifight.data.local.ScoreEntity;
import com.adam.app.arenaminifight.databinding.FragmentLeaderboardBinding;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Leaderboard Fragment displaying top survival scores from Room Database.
 * Trace: UC-MENU-04, REQ-MENU-04, UI-004, REQ-NFR-04
 */
@AndroidEntryPoint
public class LeaderboardFragment extends Fragment {

    private static final String TAG = LeaderboardFragment.class.getSimpleName();

    private FragmentLeaderboardBinding mBinding;
    private LeaderboardAdapter mAdapter;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GameUtil.log(TAG + ": onCreateView()");
        mBinding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.recyclerScores.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new LeaderboardAdapter();
        mBinding.recyclerScores.setAdapter(mAdapter);

        mBinding.btnBack.setOnClickListener(v -> {
            GameUtil.log(TAG + ": Back clicked");
            Navigation.findNavController(v).navigateUp();
        });

        loadScores();
    }

    private void loadScores() {
        mExecutor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(requireContext().getApplicationContext());
                List<ScoreEntity> scores = db.scoreDao().getTopScores();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (scores == null || scores.isEmpty()) {
                            mBinding.tvEmpty.setVisibility(View.VISIBLE);
                            mBinding.recyclerScores.setVisibility(View.GONE);
                        } else {
                            mBinding.tvEmpty.setVisibility(View.GONE);
                            mBinding.recyclerScores.setVisibility(View.VISIBLE);
                            mAdapter.setScores(scores);
                        }
                    });
                }
            } catch (Exception e) {
                GameUtil.log(TAG + ": Error loading scores: " + e.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mExecutor.shutdown();
        mBinding = null;
    }
}
