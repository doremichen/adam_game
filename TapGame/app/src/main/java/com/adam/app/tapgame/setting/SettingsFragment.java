/**
 * File: SettingsFragment.java
 * Description: This class is Settings Fragment
 *
 * @author Adam Chen
 * @version 1.0 - 2026/01/14
 */
package com.adam.app.tapgame.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.app.tapgame.R;
import com.adam.app.tapgame.utils.GameUtils;
import com.adam.app.tapgame.utils.SettingsManager;

public class SettingsFragment extends PreferenceFragmentCompat {

    // setting manager
    private SettingsManager mSettingsManager;

    // preferences
    private SwitchPreferenceCompat mEasyModePref;
    private EditTextPreference mIntervalPref;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // create wrapper theme context
        final Context contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.Theme_TapGame_Settings);
        // new layout inflater
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        return super.onCreateView(localInflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set background color and space
        final RecyclerView listView = getListView();
        listView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
        listView.setPadding(0, 16, 0, 16);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        mSettingsManager = SettingsManager.getInstance(requireContext());


        bindPreference();
    }

    private void bindPreference() {
        // find preferences
        mEasyModePref = findPreference(SettingsManager.getKeyEasyMode());
        mIntervalPref = findPreference(SettingsManager.getKeyInterval());

        if (mEasyModePref != null) {
            mEasyModePref.setChecked(mSettingsManager.isEasyMode());
            // set listener
            mEasyModePref.setOnPreferenceChangeListener(this::onPreferenceChange);
        }

        if (mIntervalPref != null) {
            mIntervalPref.setText(String.valueOf(mSettingsManager.getInterval()));
            // set listener
            mIntervalPref.setOnPreferenceChangeListener(this::onPreferenceChange);
        }
    }

    private boolean onPreferenceChange(Preference preference, Object o) {
        if (preference == mEasyModePref) {
            mSettingsManager.setEasyMode((Boolean) o);
        } else if (preference == mIntervalPref) {
            try {
                int newInterval = Integer.parseInt((String) o);

                // simple validation
                if (newInterval < 1 || newInterval > 60) {
                    GameUtils.showToast(requireContext(), R.string.tap_game_interval_invalid);
                    return false;
                }

                mSettingsManager.setInterval(newInterval);
                return true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}