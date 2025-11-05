/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is used to show the leaderboard recycle list view.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/11/05
 */
package com.adam.app.racinggame2d.view.leaderboard;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.racinggame2d.data.LeaderboardEntity;
import com.adam.app.racinggame2d.R;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>{

    private List<LeaderboardEntity> mData;

    /**
     * set data
     *
     * @param list
     */
    public void setScores(List<LeaderboardEntity> list) {
        mData = list;
        // notify data change
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LeaderBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_leaderboard, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardAdapter.ViewHolder holder, int position) {
        // get data
        LeaderboardEntity entity = mData.get(position);
        // set data
        holder.mRank.setText(String.valueOf(position + 1));
        holder.mName.setText(entity.getPlayerName());
        holder.mScore.setText(String.valueOf(entity.getScore()));
        // time format: yyyy/MM/dd HH:mm
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        String time = sdf.format(entity.getTimestamp());
        holder.mTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mRank;
        TextView mName;
        TextView mScore;
        TextView mTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRank = itemView.findViewById(R.id.tvRank);
            mName = itemView.findViewById(R.id.tvName);
            mScore = itemView.findViewById(R.id.tvScore);
            mTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
