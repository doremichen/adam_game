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

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.arenaminifight.data.local.ScoreEntity;
import com.adam.app.arenaminifight.databinding.ItemScoreBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ScoreViewHolder> {

    private List<ScoreEntity> mScores = new ArrayList<>();

    public void setScores(List<ScoreEntity> scores) {
        mScores = scores != null ? scores : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemScoreBinding binding = ItemScoreBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ScoreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        ScoreEntity score = mScores.get(position);
        holder.bind(score, position + 1);
    }

    @Override
    public int getItemCount() {
        return mScores.size();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        private final ItemScoreBinding mBinding;

        public ScoreViewHolder(@NonNull ItemScoreBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ScoreEntity score, int rank) {
            mBinding.tvRank.setText(String.format(Locale.getDefault(), "#%d", rank));
            mBinding.tvPlayerName.setText(score.getPlayerName() != null ? score.getPlayerName() : "Hero");
            
            long mins = score.getSurvivalTime() / 60;
            long secs = score.getSurvivalTime() % 60;
            mBinding.tvSurvivalTime.setText(String.format(Locale.getDefault(), "%02d:%02d", mins, secs));
            mBinding.tvKills.setText(String.format(Locale.getDefault(), "Kills: %d", score.getKillCount()));
        }
    }
}
