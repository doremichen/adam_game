/**
 * Description: This class is the listview adapter of the leaderboard.
 * Author: Adam Chen
 * Date: 2025/08/15
 */
package com.adam.app.tetrisgame.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.tetrisgame.R;
import com.adam.app.tetrisgame.model.ScoreRecord;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    // score list
    private final List<ScoreRecord> mScoreList;

    /**
     * Constructor
     * @param scoreList score list
     */
    public ScoreAdapter(List<ScoreRecord> scoreList) {
        mScoreList = scoreList;
    }


    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        // get score record
        ScoreRecord scoreRecord = mScoreList.get(position);
        // set date
        holder.textViewDate.setText(scoreRecord.getDate());
        // set score
        holder.textViewScore.setText(String.valueOf(scoreRecord.getScore()));

    }

    @Override
    public int getItemCount() {
        return mScoreList.size();
    }

    /**
     * Score view holder
     */
    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewDate;
        private final TextView textViewScore;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textView_date);
            textViewScore = itemView.findViewById(R.id.textView_score);
        }
    }

}
