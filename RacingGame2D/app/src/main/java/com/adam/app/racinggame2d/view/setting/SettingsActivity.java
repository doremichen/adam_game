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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.racinggame2d.databinding.ActivitySettingsBinding;
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

        // save button listener
        mBinding.btnSave.setOnClickListener(v -> {
            this.mViewModel.saveSettings();
            finish();
        });

    }

    /**
     * update sound switch UI
     * @param soundEnable sound enable
     */
    private void updateSoundSwitchUI(boolean soundEnable) {
        mBinding.switchSound.setChecked(soundEnable);
    }
}