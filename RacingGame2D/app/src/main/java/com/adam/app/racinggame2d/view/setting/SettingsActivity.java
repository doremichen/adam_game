/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the setting activity of the game.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/28
 */
package com.adam.app.racinggame2d.view.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.databinding.ActivitySettingsBinding;
import com.adam.app.racinggame2d.model.entity.Settings;
import com.adam.app.racinggame2d.util.GameUtil;
import com.adam.app.racinggame2d.viewmodel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {

    // TAG
    private static final String TAG = "SettingsActivity";

    // view binding
    private ActivitySettingsBinding mBinding;

    // settings view model
    private SettingsViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameUtil.log(TAG, "onCreate");
        // view binding
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // init view model
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        // observer
        mViewModel.getSettingsLiveData().observe(this, setting -> {
            updateDifficultyUI(setting.getDifficulty());
            updateSoundSwitchUI(setting.isSoundEnable());
        });

        // init UI
        initUI();

    }



    /**
     * init UI
     */
    private void initUI() {
        GameUtil.log(TAG, "initUI");

        // set sound switch listener
        mBinding.switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            this.mViewModel.setSoundEnable(isChecked);
        });
        
        // init spinner
        initSpinner();

        // save button listener
        mBinding.btnSave.setOnClickListener(v -> {
            this.mViewModel.saveSettings();
            finish();
        });

    }

    private void initSpinner() {
        // items
        String[] items = new String[]{getString(R.string.racinggame2d_difficuty_item_easy),
                getString(R.string.racinggame2d_difficuty_item_medium),
                getString(R.string.racinggame2d_difficuty_item_hard)};
        // adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerDifficulty.setAdapter(adapter);

        // set selected item listener
        mBinding.spinnerDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Settings.GameDifficulty difficulty = Settings.GameDifficulty.values()[position];
                mViewModel.setDifficulty(difficulty);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    /**
     * update sound switch UI
     * @param soundEnable sound enable
     */
    private void updateSoundSwitchUI(boolean soundEnable) {
        mBinding.switchSound.setChecked(soundEnable);
    }


    /**
     * update difficulty UI
     * @param difficulty difficulty
     */
    private void updateDifficultyUI(Settings.GameDifficulty difficulty) {
        mBinding.spinnerDifficulty.setSelection(difficulty.ordinal());
    }
}