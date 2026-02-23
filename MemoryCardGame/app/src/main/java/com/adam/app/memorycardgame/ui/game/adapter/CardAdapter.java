/**
 * Copyright (C) 2020 Adam Chen. All rights reserved.
 * <p>
 * Description: This is the adapter for the card view.
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/12
 */
package com.adam.app.memorycardgame.ui.game.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.memorycardgame.data.model.Card;
import com.adam.app.memorycardgame.databinding.ItemCardBinding;
import com.adam.app.memorycardgame.ui.game.CardClickListener;

public class CardAdapter extends ListAdapter<Card, CardAdapter.CardViewHolder> {

    private static final DiffUtil.ItemCallback<Card> DIFF_CALLBACK = new DiffUtil.ItemCallback<Card>() {

        @Override
        public boolean areItemsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
            return oldItem.equals(newItem);
        }
    };
    // card click listener
    private final CardClickListener mListener;


    // constructor
    public CardAdapter(CardClickListener listener) {
        super(DIFF_CALLBACK);
        mListener = listener;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardBinding binding = ItemCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(getItem(position), mListener);
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        // item card view binding
        ItemCardBinding mBinding;


        public CardViewHolder(@NonNull ItemCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Card card, CardClickListener listener) {
            mBinding.setCard(card);
            mBinding.setListener(listener);
            mBinding.executePendingBindings();
        }
    }
}
