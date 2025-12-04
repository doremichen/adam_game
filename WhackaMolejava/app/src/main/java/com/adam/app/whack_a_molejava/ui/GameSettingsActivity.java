/**
 * Copyright 2025 Adam Game
 * <p>
 * This class is the game settings activity for the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-04
 *
 */
package com.adam.app.whack_a_molejava.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.adam.app.whack_a_molejava.R;
import com.adam.app.whack_a_molejava.controller.SettingsManager;

public class GameSettingsActivity extends AppCompatActivity {

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

        // settingsManager
        private final SettingsManager mSettingManager;
        private final Context mContext;


        public SettingsFragment(Context context) {
            mContext = context;
            mSettingManager = SettingsManager.getInstance(context);
        }


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            // vibrator
            SwitchPreference vibPreference = findPreference(SettingsManager.KEY_VIBRATION_ON);
            // sound
            SwitchPreference soundPreference = findPreference(SettingsManager.KEY_SOUND_ON);
            // duration
            EditTextPreference durationPreference = findPreference(SettingsManager.KEY_DURATION_TIME);

            if (vibPreference != null) {
                boolean isVibOn = mSettingManager.isVibrationOn();
                vibPreference.setChecked(isVibOn);
                vibPreference.setOnPreferenceChangeListener(this::onVibPrefChange);
            }
            if (soundPreference != null) {
                boolean isSoundOn = mSettingManager.isSoundOn();
                soundPreference.setChecked(isSoundOn);
                soundPreference.setOnPreferenceChangeListener(this::onSoundPrefChange);
            }
            if (durationPreference != null) {
                int duration = mSettingManager.getDurationTime();
                durationPreference.setText(String.valueOf(duration));
                durationPreference.setOnPreferenceChangeListener(this::onDurationPrefChange);
            }

        }

        private boolean onDurationPrefChange(Preference preference, Object o) {
            int duration = Integer.parseInt((String) o);
            mSettingManager.setDurationTime(duration);
            return true;
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