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
import com.adam.app.snake.setting.GameSettingAdapter;
import com.adam.app.snake.setting.GameSettingItem;
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
        String titleFreq = getString(R.string.snake_game_setting_special_freq);
        String titleVersion = getString(R.string.app_version);


        List<GameSettingItem> settingItems = new ArrayList<>();
        // switch item
        // get value from shared preference
        boolean wrapMode = SharedPreferenceManager.getInstance(this).getBoolean(SharedPreferenceManager.Keys.WRAP_MODE, false);
        boolean specialFood = SharedPreferenceManager.getInstance(this).getBoolean(SharedPreferenceManager.Keys.SPECIAL_FOOD, false);
        boolean multiFoodsShow = SharedPreferenceManager.getInstance(this).getBoolean(SharedPreferenceManager.Keys.MULTI_FOODS_SHOW, false);

        settingItems.add(new GameSettingItem(GameSettingItem.TYPE.SWITCH,
                SharedPreferenceManager.Keys.WRAP_MODE,
                titleWraped,
                SharedPreferenceManager.getInstance(this).getBoolean(SharedPreferenceManager.Keys.WRAP_MODE,
                        wrapMode)));
        settingItems.add(new GameSettingItem(GameSettingItem.TYPE.SWITCH,
                SharedPreferenceManager.Keys.SPECIAL_FOOD,
                titleSpecial,
                SharedPreferenceManager.getInstance(this).getBoolean(SharedPreferenceManager.Keys.SPECIAL_FOOD,
                        specialFood)));
        settingItems.add(new GameSettingItem(GameSettingItem.TYPE.SWITCH,
                SharedPreferenceManager.Keys.MULTI_FOODS_SHOW,
                titleMulti,
                SharedPreferenceManager.getInstance(this).getBoolean(SharedPreferenceManager.Keys.MULTI_FOODS_SHOW,
                        multiFoodsShow)));
        // spinner item
        List<String> freqItems = new ArrayList<>();
        freqItems.add(getString(R.string.snake_game_setting_freq_low));
        freqItems.add(getString(R.string.snake_game_setting_freq_middle));
        freqItems.add(getString(R.string.snake_game_setting_freq_high));
        // get value from shared preference
        int freqIndex = SharedPreferenceManager.getInstance(this).getInt(SharedPreferenceManager.Keys.SPECIAL_FREQ, 0);
        settingItems.add(new GameSettingItem(GameSettingItem.TYPE.SPINNER,
                SharedPreferenceManager.Keys.SPECIAL_FREQ,
                titleFreq,
                freqItems,
                freqIndex));
        // text item
        String textValue = String.valueOf(SharedPreferenceManager.getInstance(this).getFloat(SharedPreferenceManager.Keys.VERSION, 0.01f));
        settingItems.add(new GameSettingItem(GameSettingItem.TYPE.TEXT,
                SharedPreferenceManager.Keys.VERSION,
                titleVersion,
                textValue));

        // recycler view set layout manager
        mBinding.recyclerGameSettings.setLayoutManager(new LinearLayoutManager(this));
        // recycler view set adapter
        final GameSettingAdapter adapter = new GameSettingAdapter(this, settingItems);
        mBinding.recyclerGameSettings.setAdapter(adapter);

        // exit button click listener
        mBinding.btnOk.setOnClickListener(v -> {
            // finish activity
            finish();

        });


    }

}