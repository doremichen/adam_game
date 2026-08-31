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

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.racinggame2d.databinding.ItemLeaderboardBinding;
import com.adam.app.racinggame2d.domain.entity.LeaderboardRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the leaderboard RecyclerView.
 */
public final class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {
    private final List<LeaderboardRecord> mRecords = new ArrayList<>();

    public void setScores(List<LeaderboardRecord> records) {
        mRecords.clear();
        if (records != null) {
            mRecords.addAll(records);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLeaderboardBinding binding = ItemLeaderboardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mRecords.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemLeaderboardBinding mBinding;

        ViewHolder(ItemLeaderboardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(LeaderboardRecord record, int rank) {
            mBinding.setRecord(record);
            mBinding.setRank(rank);
            mBinding.executePendingBindings();
        }
    }
}
