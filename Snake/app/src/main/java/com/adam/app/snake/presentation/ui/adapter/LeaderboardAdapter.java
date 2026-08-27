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
package com.adam.app.snake.presentation.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.snake.databinding.ItemLeaderboardEntryBinding;
import com.adam.app.snake.data.entity.LeaderboardEntry;
import com.adam.app.snake.util.Utils;

/**
 * Leaderboard adapter
 */
public class LeaderboardAdapter extends ListAdapter<LeaderboardEntry, LeaderboardAdapter.ViewHolder> {

    // TAG
    private static final String TAG = "LeaderboardAdapter";

    private static final DiffUtil.ItemCallback<LeaderboardEntry> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<LeaderboardEntry>() {
                @Override
                public boolean areItemsTheSame(@NonNull LeaderboardEntry oldItem, @NonNull LeaderboardEntry newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull LeaderboardEntry oldItem, @NonNull LeaderboardEntry newItem) {
                    return oldItem.getScore() == newItem.getScore() &&
                            oldItem.getName().equals(newItem.getName());
                }
            };

    public LeaderboardAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // view binding
        ItemLeaderboardEntryBinding binding = ItemLeaderboardEntryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        // view holder
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Utils.logDebug(TAG, "onBindViewHolder: position = " + position);
        // get entry
        LeaderboardEntry entry = getItem(position);
        // bind
        holder.bind(entry, position + 1);
    }

    /**
     * view holder for leaderboard entry
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemLeaderboardEntryBinding mBinding;

        public ViewHolder(@NonNull ItemLeaderboardEntryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(LeaderboardEntry entry, int position) {
            Utils.logDebug(TAG, "bind: position = " + position);
            mBinding.textRank.setText(String.valueOf(position));
            mBinding.textName.setText(entry.getName());
            mBinding.textScore.setText(String.valueOf(entry.getScore()));
        }
    }
}
