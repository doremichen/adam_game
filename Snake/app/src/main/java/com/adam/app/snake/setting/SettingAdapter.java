/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the snake game setting adapter
 *
 * Author: Adam Chen
 * Date: 2025/09/30
 */
package com.adam.app.snake.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.snake.R;
import com.adam.app.snake.store.file.SharedPreferenceManager;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    //TAG: SettingAdapter
    private static final String TAG = "SettingAdapter";



    // list of setting item
    private final List<SettingItem> mSettingItems;

    /**
     * Constructor with list of setting item
     *
     * @param settingItems: List<SettingItem>
     */
    public SettingAdapter(List<SettingItem> settingItems) {
        mSettingItems = settingItems;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_setting_switch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Setting item
        SettingItem item = mSettingItems.get(position);
        // title
        holder.mTitle.setText(item.getTitle());
        // switch option
        holder.mSwitchOption.setChecked(item.isEnable());

        //set on click listener in switch option
        holder.mSwitchOption.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // set item enable in shared preference
            SharedPreferenceManager.getInstance(buttonView.getContext())
                    .putBoolean(item.getTitle(), isChecked);
        });

    }

    @Override
    public int getItemCount() {
        return mSettingItems.size();
    }

    /**
     * static class ViewHolder
     * title: TextView
     * switchOption: Switch
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public Switch mSwitchOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.textView_title);
            mSwitchOption = itemView.findViewById(R.id.switch_option);
        }
    }

}
