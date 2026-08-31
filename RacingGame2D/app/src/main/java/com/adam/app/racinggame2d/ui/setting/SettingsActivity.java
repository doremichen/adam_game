/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.ui.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.databinding.ActivitySettingsBinding;
import com.adam.app.racinggame2d.domain.entity.Settings;
import com.adam.app.racinggame2d.util.Utils;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for game settings.
 */
@AndroidEntryPoint
public final class SettingsActivity extends AppCompatActivity {
    private static final String sTAG = "SettingsActivity";
    private ActivitySettingsBinding mBinding;
    private SettingsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        initUI();
        initObservers();
    }

    private void initUI() {
        Utils.logDebug(sTAG, "initUI");
        mBinding.switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> mViewModel.setSoundEnable(isChecked));

        initSpinner();

        mBinding.btnSave.setOnClickListener(v -> {
            mViewModel.saveSettings();
            finish();
        });
    }

    private void initSpinner() {
        String[] items = new String[]{
                getString(R.string.racinggame2d_difficulty_item_easy),
                getString(R.string.racinggame2d_difficulty_item_medium),
                getString(R.string.racinggame2d_difficulty_item_hard)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, items);
        adapter.setDropDownViewResource(R.layout.item_spinner);
        mBinding.spinnerDifficulty.setAdapter(adapter);

        mBinding.spinnerDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mViewModel.setDifficulty(Settings.GameDifficulty.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initObservers() {
        mViewModel.getSettingsLiveData().observe(this, settings -> {
            mBinding.switchSound.setChecked(settings.isSoundEnable());
            mBinding.spinnerDifficulty.setSelection(settings.getDifficulty().ordinal());
        });
    }
}
