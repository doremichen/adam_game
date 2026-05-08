/*
 * MIT License
 *
 * Copyright (c) 2025 Adam Chen
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
package com.adam.app.whack_a_molejava.ui;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.adam.app.whack_a_molejava.R;
import com.adam.app.whack_a_molejava.controller.SettingsManager;

/**
 * This class is the game settings activity for the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-04
 */
public class GameSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SettingsManager mSettingManager;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            mSettingManager = SettingsManager.getInstance(requireContext().getApplicationContext());
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // vibrator
            SwitchPreference vibPreference = findPreference(SettingsManager.KEY_VIBRATION_ON);
            // sound
            SwitchPreference soundPreference = findPreference(SettingsManager.KEY_SOUND_ON);
            // duration
            EditTextPreference durationPreference = findPreference(SettingsManager.KEY_DURATION_TIME);

            if (vibPreference != null) {
                vibPreference.setChecked(mSettingManager.isVibrationOn());
                vibPreference.setOnPreferenceChangeListener(this::onVibPrefChange);
            }
            if (soundPreference != null) {
                soundPreference.setChecked(mSettingManager.isSoundOn());
                soundPreference.setOnPreferenceChangeListener(this::onSoundPrefChange);
            }
            if (durationPreference != null) {
                durationPreference.setText(String.valueOf(mSettingManager.getDurationTime()));
                durationPreference.setOnPreferenceChangeListener(this::onDurationPrefChange);
            }
        }

        private boolean onDurationPrefChange(Preference preference, Object o) {
            try {
                int duration = Integer.parseInt((String) o);
                mSettingManager.setDurationTime(duration);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        private boolean onSoundPrefChange(Preference preference, Object o) {
            boolean isSoundOn = (boolean) o;
            mSettingManager.setSoundOn(isSoundOn);
            return true;
        }

        private boolean onVibPrefChange(Preference preference, Object o) {
            boolean isVibOn = (boolean) o;
            mSettingManager.setVibrationOn(isVibOn);
            return true;
        }
    }
}
