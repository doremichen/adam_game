/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the snake game setting activity
 * <p>
 * Author: Adam Chen
 * Date: 2025/09/30
 */
package com.adam.app.snake;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.snake.databinding.ActivitySettingBinding;
import com.adam.app.snake.setting.SettingAdapter;
import com.adam.app.snake.setting.SettingItem;
import com.adam.app.snake.store.file.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    // view binding
    private ActivitySettingBinding mBinding;



    public static Intent createIntent(MainActivity mainActivity) {
        return new Intent(mainActivity, SettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        String titleWraped = getString(R.string.snake_game_setting_wrap_mode);
        String titleSpecial = getString(R.string.snake_game_setting_special_food);
        String titleMulti = getString(R.string.snake_game_setting_multi_foods_show);


        List<SettingItem> settingItems = new ArrayList<>();
        settingItems.add(new SettingItem(titleWraped,
                SharedPreferenceManager.getInstance(this).getBoolean(titleWraped, false)));
        settingItems.add(new SettingItem(titleSpecial,
                SharedPreferenceManager.getInstance(this).getBoolean(titleSpecial, false)));
        settingItems.add(new SettingItem(titleMulti,
                SharedPreferenceManager.getInstance(this).getBoolean(titleMulti, false)));


        // recycler view set layout manager
        mBinding.recyclerGameSettings.setLayoutManager(new LinearLayoutManager(this));
        // recycler view set adapter
        final SettingAdapter adapter = new SettingAdapter(settingItems);
        mBinding.recyclerGameSettings.setAdapter(adapter);

        // exit button click listener
        mBinding.btnOk.setOnClickListener(v -> {
           // finish activity
            finish();

        });


    }

    private void onChanged(Boolean isEnable) {
        // Show toast
        String message = getString(R.string.snake_game_setting_toast_message,
                isEnable ? "On" : "Off");
        Utils.showToast(this, message);
    }
}