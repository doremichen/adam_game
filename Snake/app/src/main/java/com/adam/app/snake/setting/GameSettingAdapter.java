/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the snake game setting adapter that support multi-type
 *
 * Author: Adam Chen
 * Date: 2025/10/01
 */
package com.adam.app.snake.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.snake.R;
import com.adam.app.snake.Utils;
import com.adam.app.snake.store.file.SharedPreferenceManager;

import java.util.List;

public class GameSettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // TAG: GameSettingAdapter
    private static final String TAG = "GameSettingAdapter";

    // Context
    private final Context mContext;
    // setting items
    private final List<GameSettingItem> mSettingItems;

    /**
     * Constructor
     *
     * @param context Context
     * @param settingItems List<GameSettingItem>
     */
    public GameSettingAdapter(Context context, List<GameSettingItem> settingItems) {
        mContext = context;
        mSettingItems = settingItems;
    }

    /**
     * getItemViewType
     */
    @Override
    public int getItemViewType(int position) {
        return mSettingItems.get(position).getType();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // layout inflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case GameSettingItem.TYPE.SWITCH:
                return new SwitchViewHolder(inflater.inflate(R.layout.item_setting_switch, parent, false));
            case GameSettingItem.TYPE.SPINNER:
                return new SpinnerViewHolder(inflater.inflate(R.layout.item_setting_spinner, parent, false));
            case GameSettingItem.TYPE.TEXT:
                return new TextViewHolder(inflater.inflate(R.layout.item_setting_text, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GameSettingItem item = mSettingItems.get(position);
        switch (item.getType()) {
            case GameSettingItem.TYPE.SWITCH:
                ((SwitchViewHolder) holder).bind(item);
                break;
            case GameSettingItem.TYPE.SPINNER:
                ((SpinnerViewHolder) holder).bind(item);
                break;
            case GameSettingItem.TYPE.TEXT:
                ((TextViewHolder) holder).bind(item);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mSettingItems.size();
    }

    // == view holder ==
    /**
     * switch view holder
     * title: TextView
     * switchOption: Switch
     */
    public static class SwitchViewHolder extends RecyclerView.ViewHolder {
        // title
        final TextView mTitle;
        // switch option
        final Switch mSwitchOption;

        public SwitchViewHolder(@NonNull View itemView) {
            super(itemView);

            // title
            mTitle = itemView.findViewById(R.id.tvSettingTitle);
            // switch option
            mSwitchOption = itemView.findViewById(R.id.switchSetting);
        }

        public void bind(GameSettingItem item) {
            // title
            mTitle.setText(item.getTitle());
            mSwitchOption.setOnCheckedChangeListener(null);
            // switch option
            mSwitchOption.setChecked(item.isEnable());
            // set on click listener in switch option
            mSwitchOption.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // set item enable in shared preference
                SharedPreferenceManager.getInstance(buttonView.getContext())
                        .putBoolean(item.getKey(), isChecked);
            });
        }

    }

    /**
     * spinner view holder
     * title: TextView
     * spinnerOption: Spinner
     */
    public static class SpinnerViewHolder extends RecyclerView.ViewHolder {
        // title
        final TextView mTitle;
        // spinner option
        final Spinner mSpinnerOption;
        public SpinnerViewHolder(@NonNull View itemView) {
            super(itemView);
            // title
            mTitle = itemView.findViewById(R.id.tvSettingTitle);
            // spinner option
            mSpinnerOption = itemView.findViewById(R.id.spinnerSetting);
        }
        public void bind(GameSettingItem item) {
            mTitle.setText(item.getTitle());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_item, item.getSpinnerItems());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerOption.setAdapter(adapter);

            mSpinnerOption.setOnItemSelectedListener(null);
            mSpinnerOption.setSelection(item.getSpinnerIndex(), false);
            mSpinnerOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // log
                    Utils.logDebug(TAG, "onItemSelected: " + position);
                    item.setSpinnerIndex(position);
                    Utils.logDebug(TAG, "Save value to shared preference: "+"key: " + item.getKey() + ", value: " + position );
                    SharedPreferenceManager.getInstance(parent.getContext())
                            .putInt(item.getKey(), position);
                }
                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

    }

    /**
     * text view holder
     * title: TextView
     */
    public static class TextViewHolder extends RecyclerView.ViewHolder {
        // title
        final TextView mTitle;
        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            // title
            mTitle = itemView.findViewById(R.id.tvSettingTitle);
        }
        public void bind(GameSettingItem item) {
            if (item.getTextValue() != null) {
                mTitle.setText(String.format("%s: %s", item.getTitle(), item.getTextValue()));
            } else {
                mTitle.setText(item.getTitle());
            }
        }
    }

}
