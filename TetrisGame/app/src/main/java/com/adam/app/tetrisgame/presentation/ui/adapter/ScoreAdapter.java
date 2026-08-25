/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.presentation.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import com.adam.app.tetrisgame.data.local.ScoreRecord;
import com.adam.app.tetrisgame.databinding.ItemScoreBinding;
import java.util.ArrayList;
import java.util.List;

public class ScoreAdapter extends BaseAdapter {
    private List<ScoreRecord> mScoreList = new ArrayList<>();

    public ScoreAdapter() {
    }

    public void setScoreList(List<ScoreRecord> scoreList) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ScoreDiffCallback(this.mScoreList, scoreList));
        this.mScoreList = new ArrayList<>(scoreList);
        result.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) { notifyDataSetChanged(); }
            @Override
            public void onRemoved(int position, int count) { notifyDataSetChanged(); }
            @Override
            public void onMoved(int fromPosition, int toPosition) { notifyDataSetChanged(); }
            @Override
            public void onChanged(int position, int count, Object payload) { notifyDataSetChanged(); }
        });
    }

    @Override
    public int getCount() {
        return mScoreList.size();
    }

    @Override
    public ScoreRecord getItem(int position) {
        return mScoreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mScoreList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            ItemScoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), com.adam.app.tetrisgame.R.layout.item_score, parent, false);
            view = binding.getRoot();
            holder = new ViewHolder(binding);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ScoreRecord scoreRecord = getItem(position);
        holder.getBinding().setScoreRecord(scoreRecord);
        holder.getBinding().executePendingBindings();

        return view;
    }

    private static class ViewHolder {
        private final ItemScoreBinding mBinding;

        ViewHolder(ItemScoreBinding binding) {
            mBinding = binding;
        }

        public ItemScoreBinding getBinding() {
            return mBinding;
        }
    }

    private static class ScoreDiffCallback extends DiffUtil.Callback {
        private final List<ScoreRecord> mOldList;
        private final List<ScoreRecord> mNewList;

        ScoreDiffCallback(List<ScoreRecord> oldList, List<ScoreRecord> newList) {
            this.mOldList = oldList;
            this.mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return mOldList.size();
        }

        @Override
        public int getNewListSize() {
            return mNewList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldList.get(oldItemPosition).getId() == mNewList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            ScoreRecord oldItem = mOldList.get(oldItemPosition);
            ScoreRecord newItem = mNewList.get(newItemPosition);
            return oldItem.getScore() == newItem.getScore() && oldItem.getTimestamp() == newItem.getTimestamp();
        }
    }
}
