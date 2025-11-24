/**
 * This class is the adapter for the leader board.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-21
 */
package com.adam.app.flappybird.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.flappybird.R;
import com.adam.app.flappybird.data.FlappyBird;

public class LeaderBoardAdapter extends ListAdapter<FlappyBird, LeaderBoardAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<FlappyBird> DIFFCALLBACK =
            new DiffUtil.ItemCallback<FlappyBird>() {

                @Override
                public boolean areItemsTheSame(@NonNull FlappyBird oldItem, @NonNull FlappyBird newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull FlappyBird oldItem, @NonNull FlappyBird newItem) {
                    return oldItem.getScore().equals(newItem.getScore());
                }
            };

    public LeaderBoardAdapter() {
        super(DIFFCALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlappyBird flappyBird = getItem(position);
        holder.bind(flappyBird, position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    /**
     * View holder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mId;
        private final TextView mScore;
        private final TextView mDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // find view
            mId = itemView.findViewById(R.id.tvRank);
            mScore = itemView.findViewById(R.id.tvScore);
            mDate = itemView.findViewById(R.id.tvTime);
        }

        public void bind(FlappyBird flappyBird, int position) {
            mId.setText(String.valueOf(position + 1));
            mScore.setText(String.valueOf(flappyBird.getScore()));
            mDate.setText(flappyBird.getDate());
        }
    }


}
