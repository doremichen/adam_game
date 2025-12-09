/**
 * This class is the Settings fragment for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.views.fragments;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.adam.app.tic_tac_toe.R;
import com.adam.app.tic_tac_toe.manager.SettingsManager;

public class SettingsFragment extends PreferenceFragmentCompat {

    // settings manager
    private SettingsManager mSettingsManager;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        mSettingsManager = SettingsManager.getInstance(requireContext());
        // init
        initUI();
    }

    private void initUI() {
        SwitchPreferenceCompat gameModePve = findPreference("game_mode_pve");
        SwitchPreferenceCompat aiStrategyHard = findPreference("ai_strategy_hard");
        boolean isGameModePve = mSettingsManager.isGameModePve();
        boolean isAiStrategyHard = mSettingsManager.isAiStrategyHard();

        // update UI
        if (gameModePve != null || aiStrategyHard != null) {
            gameModePve.setChecked(isGameModePve);
            aiStrategyHard.setChecked(isAiStrategyHard);

            // value change listener
            gameModePve.setOnPreferenceChangeListener(this::onGameModePveChanged);
            aiStrategyHard.setOnPreferenceChangeListener(this::onAiStrategyHardChanged);
        }
    }

    private boolean onAiStrategyHardChanged(Preference preference, Object o) {
        boolean value = (boolean) o;
        mSettingsManager.setAiStrategyHard(value);

        return true;
    }

    private boolean onGameModePveChanged(Preference preference, Object o) {
        boolean value = (boolean) o;
        mSettingsManager.setGameModePve(value);

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}