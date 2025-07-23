/**
 * Copyright 2025 Adam
 * Description: SettingsActivity is the activity of the settings.
 * Author: Adam
 * Date: 2025/06/24
 */
package com.adam.app.tetrisgame;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.tetrisgame.databinding.ActivitySettingsBinding;
import com.adam.app.tetrisgame.viewmodel.GameViewModel;

public class SettingsActivity extends AppCompatActivity {

    // game viewModel
    private GameViewModel mGameViewModel;

    // view binding
    private ActivitySettingsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // init game viewModel
        mGameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mGameViewModel.loadSettings(this);

        // setup speed option
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.speed_array,
                R.layout.spinner_item_left);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_left);
        mBinding.spinnerSpeed.setAdapter(adapter);

        // observer and update speed
        mGameViewModel.getSpeed().observe(this, speed -> {
            mBinding.spinnerSpeed.setSelection(speed);
        });

        // observer and update sound
        mGameViewModel.getSound().observe(this, sound -> {
            mBinding.switchSound.setChecked(sound);
        });

        // set control listener
        mBinding.spinnerSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGameViewModel.setSpeed(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mBinding.switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mGameViewModel.setSound(isChecked);
        });

        // save settings
        mBinding.btnSaveSettings.setOnClickListener(v -> {
            mGameViewModel.saveSettings(this);
            finish();
        });

    }
}