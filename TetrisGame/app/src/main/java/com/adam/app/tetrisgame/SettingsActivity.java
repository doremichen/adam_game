/**
 * Description: SettingsActivity is the activity of the settings.
 * Author: Adam Chen
 * Date: 2025/11/28
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