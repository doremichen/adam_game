/*
 * Copyright (c) 2026 Adam Chen
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

package com.adam.app.tic_tac_toe.ui.views.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.adam.app.tic_tac_toe.R;
import com.adam.app.tic_tac_toe.ui.viewmodels.SettingsViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for game settings.
 */
@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsViewModel mViewModel;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        initUI();
    }

    private void initUI() {
        SwitchPreferenceCompat gameModePve = findPreference("game_mode_pve");
        SwitchPreferenceCompat aiStrategyHard = findPreference("ai_strategy_hard");

        if (gameModePve != null) {
            gameModePve.setChecked(mViewModel.isPveMode());
            gameModePve.setOnPreferenceChangeListener((preference, o) -> {
                mViewModel.setPveMode((boolean) o);
                return true;
            });
        }

        if (aiStrategyHard != null) {
            aiStrategyHard.setChecked(mViewModel.isHardAi());
            aiStrategyHard.setOnPreferenceChangeListener((preference, o) -> {
                mViewModel.setHardAi((boolean) o);
                return true;
            });
        }
    }
}
