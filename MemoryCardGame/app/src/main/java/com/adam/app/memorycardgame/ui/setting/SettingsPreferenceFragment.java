/**
 * Copyright (c) 2026 Adam Chen. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 * <p>
 * Description: This is the settings preference fragment.
 * </p>
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/02/23
 */
package com.adam.app.memorycardgame.ui.setting;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.adam.app.memorycardgame.R;
import com.adam.app.memorycardgame.util.SettingsManager;

public class SettingsPreferenceFragment extends PreferenceFragmentCompat {

    // settings view model
    private SettingViewModel mViewModel;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        // init view model
        mViewModel = new ViewModelProvider(this.requireActivity()).get(SettingViewModel.class);

        // sound switch preference
        SwitchPreferenceCompat soundSwitch = findPreference(SettingsManager.KEY_ENABLE_SOUND);
        // theme list preference
        ListPreference themeList = findPreference(SettingsManager.KEY_THEME);

        // set on change listener
        assert soundSwitch != null;
        soundSwitch.setOnPreferenceChangeListener(this::onSoundPreferenceChange);
        assert themeList != null;
        themeList.setOnPreferenceChangeListener(this::onThemePreferenceChange);

    }

    private boolean onThemePreferenceChange(Preference preference, Object newValue) {
        // update theme
        mViewModel.updateTheme((String)newValue);
        return true;
    }

    private boolean onSoundPreferenceChange(Preference preference, Object newValue) {
        // update sound
        mViewModel.updateSound((Boolean)newValue);
        return true;
    }
}