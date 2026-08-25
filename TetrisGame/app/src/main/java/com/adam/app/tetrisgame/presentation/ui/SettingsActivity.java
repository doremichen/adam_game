/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.presentation.ui;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import com.adam.app.tetrisgame.R;
import com.adam.app.tetrisgame.presentation.viewmodel.SettingsViewModel;
import com.adam.app.tetrisgame.util.Constants;
import com.adam.app.tetrisgame.util.Utils;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {
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

    @AndroidEntryPoint
    public static class SettingsFragment extends PreferenceFragmentCompat {
        private SettingsViewModel mViewModel;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
            
            SwitchPreferenceCompat soundEffect = findPreference(Constants.PREF_SOUND_EFFECT);
            ListPreference speed = findPreference(Constants.PREF_SPEED);

            if (soundEffect != null) {
                soundEffect.setChecked(mViewModel.isSoundEffectEnabled());
                soundEffect.setOnPreferenceChangeListener(this::onSoundEffectChanged);
            }

            if (speed != null) {
                speed.setValue(mViewModel.getSpeed());
                speed.setOnPreferenceChangeListener(this::onSpeedChanged);
            }
        }

        private boolean onSpeedChanged(Preference preference, Object o) {
            String value = (String) o;
            mViewModel.setSpeed(value);
            Utils.showToast(requireContext(), getString(R.string.tetris_game_speed_changed, value));
            return true;
        }

        private boolean onSoundEffectChanged(Preference preference, Object o) {
            boolean value = (boolean) o;
            mViewModel.setSoundEffectEnabled(value);
            String message = value ? getString(R.string.tetris_game_sound_effect_on) : getString(R.string.tetris_game_sound_effect_off);
            Utils.showToast(requireContext(), getString(R.string.tetris_game_sound_effect_changed, message));
            return true;
        }
    }
}
