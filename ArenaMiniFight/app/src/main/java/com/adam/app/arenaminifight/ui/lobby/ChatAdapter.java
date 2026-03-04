/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This class is the chat adapter of list view
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/04
 */
package com.adam.app.arenaminifight.ui.lobby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.arenaminifight.databinding.ItemChatMessageBinding;
import com.adam.app.arenaminifight.domain.model.ChatMessage;

public class ChatAdapter extends ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder> {


    private static final DiffUtil.ItemCallback<ChatMessage> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {

        @Override
        public boolean areItemsTheSame(@NonNull ChatMessage oldItem, @NonNull ChatMessage newItem) {
            return oldItem.getTimestamp() == newItem.getTimestamp() &&
                    oldItem.getSenderId().equals(newItem.getSenderId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatMessage oldItem, @NonNull ChatMessage newItem) {
            return oldItem.getMessage().equals(newItem.getMessage());
        }
    };

    protected ChatAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChatMessageBinding binding = ItemChatMessageBinding.inflate(inflater, parent, false);
        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    protected static class ChatViewHolder extends RecyclerView.ViewHolder {
        // view binding
        private final ItemChatMessageBinding mBinding;

        public ChatViewHolder(@NonNull ItemChatMessageBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        public void bind(ChatMessage chatMessage) {
            mBinding.setChatMessage(chatMessage);
            mBinding.executePendingBindings(); // update view
        }

    }

}
