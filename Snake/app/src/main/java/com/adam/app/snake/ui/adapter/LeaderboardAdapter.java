/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the leaderboard adapter that is used to display the leaderboard
 *
 * Author: Adam Chen
 * Date: 2025/10/08
 */
package com.adam.app.snake.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.snake.R;
import com.adam.app.snake.data.entity.LeaderboardEntry;
import com.adam.app.snake.util.Utils;

public class LeaderboardAdapter  extends ListAdapter<LeaderboardEntry, LeaderboardAdapter.ViewHolder> {

    // TAG
    private static final String TAG = "LeaderboardAdapter";

    public LeaderboardAdapter() {
        super(DIFF_CALLBACK);
    }

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard_entry, parent, false);
        // view holder
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Utils.logDebug(TAG, "onBindViewHolder: position = " + position);
        // get entry
        LeaderboardEntry entry = getItem(position);
        // bind
        holder.bind(entry, position + 1);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    /**
     * class view holder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mRank;
        public TextView mName;
        public TextView mScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRank = itemView.findViewById(R.id.text_rank);
            mName = itemView.findViewById(R.id.text_name);
            mScore = itemView.findViewById(R.id.text_score);
        }

        void bind(LeaderboardEntry entry, int position) {
            Utils.logDebug(TAG, "bind: position = " + position);
            mRank.setText(String.valueOf(position));
            mName.setText(entry.getName());
            mScore.setText(String.valueOf(entry.getScore()));
        }
    }
}
