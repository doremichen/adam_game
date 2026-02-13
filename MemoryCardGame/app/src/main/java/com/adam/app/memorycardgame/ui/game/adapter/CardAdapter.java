/**
 * Copyright (C) 2020 Adam Chen. All rights reserved.
 *
 * Description: This is the adapter for the card view.
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/12
 */
package com.adam.app.memorycardgame.ui.game.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.memorycardgame.data.model.Card;
import com.adam.app.memorycardgame.databinding.ItemCardBinding;
import com.adam.app.memorycardgame.ui.game.CardClickListener;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    // list of cards
    private final List<Card> mCards;

    // card click listener
    private final CardClickListener mListener;

    // constructor
    public CardAdapter(List<Card> cards, CardClickListener listener) {
        mCards = cards;
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
        holder.bind(mCards.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mCards.size();
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
