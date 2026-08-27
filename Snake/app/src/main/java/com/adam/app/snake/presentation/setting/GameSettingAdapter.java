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
package com.adam.app.snake.presentation.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.snake.databinding.ItemSettingSpinnerBinding;
import com.adam.app.snake.databinding.ItemSettingSwitchBinding;
import com.adam.app.snake.databinding.ItemSettingTextBinding;
import com.adam.app.snake.domain.usecase.SettingUseCase;
import com.adam.app.snake.util.Utils;

/**
 * Game setting adapter using ListAdapter and DiffUtil
 */
public class GameSettingAdapter extends ListAdapter<GameSettingItem, RecyclerView.ViewHolder> {

    // TAG: GameSettingAdapter
    private static final String TAG = "GameSettingAdapter";

    // listener
    private final OnSettingChangeListener mListener;

    /**
     * Interface for setting change listener
     */
    public interface OnSettingChangeListener {
        /**
         * On setting changed
         * @param key SettingKey
         * @param value Object
         */
        void onSettingChanged(SettingUseCase.SettingKey key, Object value);
    }

    /**
     * DiffUtil Callback
     */
    private static final DiffUtil.ItemCallback<GameSettingItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<GameSettingItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull GameSettingItem oldItem, @NonNull GameSettingItem newItem) {
            return oldItem.getSettingType() == newItem.getSettingType();
        }

        @Override
        public boolean areContentsTheSame(@NonNull GameSettingItem oldItem, @NonNull GameSettingItem newItem) {
            return oldItem.equals(newItem);
        }
    };

    /**
     * Constructor
     *
     * @param context Context (unused but kept for API compatibility if needed)
     * @param listener OnSettingChangeListener
     */
    public GameSettingAdapter(Context context, OnSettingChangeListener listener) {
        super(DIFF_CALLBACK);
        this.mListener = listener;
    }

    /**
     * getItemViewType
     * @param position int
     * @return int
     */
    @Override
    public int getItemViewType(int position) {
        return getItem(position).getSettingType().getUiType().ordinal();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // layout inflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        GameSettingItem.UiType uiType = GameSettingItem.UiType.values()[viewType];
        
        switch (uiType) {
            case SWITCH:
                return new SwitchViewHolder(ItemSettingSwitchBinding.inflate(inflater, parent, false), mListener);
            case SPINNER:
                return new SpinnerViewHolder(ItemSettingSpinnerBinding.inflate(inflater, parent, false), mListener);
            case TEXT:
                return new TextViewHolder(ItemSettingTextBinding.inflate(inflater, parent, false));
            default:
                throw new IllegalArgumentException("Unknown UI type: " + uiType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GameSettingItem item = getItem(position);
        GameSettingItem.UiType uiType = item.getSettingType().getUiType();
        
        switch (uiType) {
            case SWITCH:
                ((SwitchViewHolder) holder).bind(item);
                break;
            case SPINNER:
                ((SpinnerViewHolder) holder).bind(item);
                break;
            case TEXT:
                ((TextViewHolder) holder).bind(item);
                break;
        }
    }

    // == view holder ==
    /**
     * switch view holder
     */
    public static class SwitchViewHolder extends RecyclerView.ViewHolder {
        // view binding
        private final ItemSettingSwitchBinding mBinding;
        // listener
        private final OnSettingChangeListener mListener;

        public SwitchViewHolder(@NonNull ItemSettingSwitchBinding binding, OnSettingChangeListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            this.mListener = listener;
        }

        /**
         * bind data
         * @param item GameSettingItem
         */
        public void bind(GameSettingItem item) {
            // title
            mBinding.tvSettingTitle.setText(item.getSettingType().getTitleResId());
            mBinding.switchSetting.setOnCheckedChangeListener(null);
            // switch option
            if (item.getValue() instanceof Boolean) {
                mBinding.switchSetting.setChecked((Boolean) item.getValue());
            }
            // set on click listener in switch option
            mBinding.switchSetting.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (mListener != null) {
                    mListener.onSettingChanged(item.getSettingType().getKey(), isChecked);
                }
            });
        }

    }

    /**
     * spinner view holder
     */
    public static class SpinnerViewHolder extends RecyclerView.ViewHolder {
        // view binding
        private final ItemSettingSpinnerBinding mBinding;
        // listener
        private final OnSettingChangeListener mListener;

        public SpinnerViewHolder(@NonNull ItemSettingSpinnerBinding binding, OnSettingChangeListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            this.mListener = listener;
        }

        /**
         * bind data
         * @param item GameSettingItem
         */
        public void bind(GameSettingItem item) {
            mBinding.tvSettingTitle.setText(item.getSettingType().getTitleResId());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mBinding.getRoot().getContext(),
                    android.R.layout.simple_spinner_item, item.getSpinnerItems());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mBinding.spinnerSetting.setAdapter(adapter);

            mBinding.spinnerSetting.setOnItemSelectedListener(null);
            if (item.getValue() instanceof Integer) {
                mBinding.spinnerSetting.setSelection((Integer) item.getValue(), false);
            }
            mBinding.spinnerSetting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Utils.logDebug(TAG, "onItemSelected: " + position);
                    if (mListener != null) {
                        mListener.onSettingChanged(item.getSettingType().getKey(), position);
                    }
                }
                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

    }

    /**
     * text view holder
     */
    public static class TextViewHolder extends RecyclerView.ViewHolder {
        // view binding
        private final ItemSettingTextBinding mBinding;

        public TextViewHolder(@NonNull ItemSettingTextBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        /**
         * bind data
         * @param item GameSettingItem
         */
        public void bind(GameSettingItem item) {
            String title = mBinding.getRoot().getContext().getString(item.getSettingType().getTitleResId());
            if (item.getInfo() != null) {
                mBinding.tvSettingTitle.setText(String.format("%s: %s", title, item.getInfo()));
            } else {
                mBinding.tvSettingTitle.setText(title);
            }
        }
    }

}
