/**
 * This class is the game settings activity.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.adam.app.flappybird.R;
import com.adam.app.flappybird.manager.SettingsManager;
import com.adam.app.flappybird.util.GameUtil;

public class SettingsActivity extends AppCompatActivity {

    public static Intent createIntent(Activity activity) {
        return new Intent(activity, SettingsActivity.class);
    }

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

        private SettingsManager mSettingsManager;

        public SettingsFragment(Context context) {
            mSettingsManager = SettingsManager.getInstance(context);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            // get sound effect
            SwitchPreferenceCompat soundEffect = findPreference("sound_effect");
            if (soundEffect != null) {
                // get value from shared preferences
                boolean enableSoundEffect = mSettingsManager.isSoundEffect();
                // update switch button
                soundEffect.setChecked(enableSoundEffect);

                soundEffect.setOnPreferenceChangeListener(this::onSEPreferenceChange);
            }
        }

        private boolean onSEPreferenceChange(Preference preference, Object o) {
            // get value
            boolean value = (boolean) o;
            // save to shared preferences
            mSettingsManager.setSoundEffect(value);

            // show toast
            GameUtil.showToast(getContext(), "Sound Effect: " + (value ? "On" : "Off"));

            return true; // save
        }
    }
}