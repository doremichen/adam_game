/*
 * Copyright (c) 2025 Adam
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
package com.adam.app.tetrisgame;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.adam.app.tetrisgame.manager.SettingsManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment(getApplicationContext()))
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SettingsManager mSettingMgr;
        private Context mContext;

        public SettingsFragment(Context context) {
            mSettingMgr = SettingsManager.getInstance(context);
            mContext = context;
        }


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            // get setting item
            SwitchPreferenceCompat soundEffect = findPreference(SettingsManager.KEY_SOUND_EFFECT);
            ListPreference speed = findPreference(SettingsManager.KEY_SPEED);

            if (soundEffect != null) {
                boolean isSoundEffect = mSettingMgr.isSoundEffect();
                soundEffect.setChecked(isSoundEffect);
                // set listener
                soundEffect.setOnPreferenceChangeListener(this::onSoundEffectChanged);
            }

            if (speed != null) {
                String speedValue = mSettingMgr.getSpeed();
                speed.setValue(String.valueOf(speedValue));
                // set listener
                speed.setOnPreferenceChangeListener(this::onSpeedChanged);
            }
        }

        private boolean onSpeedChanged(Preference preference, Object o) {
            // get value
            String value = (String) o;
            // set value
            mSettingMgr.setSpeed(value);
            String message = mContext.getString(R.string.tetris_game_speed_changed, value);
            Utils.showToast(mContext, message);
            return true; // save setting
        }

        private boolean onSoundEffectChanged(Preference preference, Object o) {
            // get value
            boolean value = (boolean) o;
            // set value
            mSettingMgr.setSoundEffect(value);
            String message = value ?
                    mContext.getString(R.string.tetris_game_sound_effect_on) : mContext.getString(R.string.tetris_game_sound_effect_off);
            Utils.showToast(mContext, mContext.getString(R.string.tetris_game_sound_effect_changed, message));
            // save setting
            return true; // save setting
        }
    }

}