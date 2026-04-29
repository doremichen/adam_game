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

package com.adam.app.galaga.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

import com.adam.app.galaga.R;
import com.adam.app.galaga.data.local.prefs.GameSettings;
import com.adam.app.galaga.databinding.ActivitySettungsBinding;
import com.adam.app.galaga.utils.GameConstants;
import com.adam.app.galaga.utils.GameUtils;
import com.adam.app.galaga.viewmodel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {
    // TAG
    private static final String TAG = SettingsActivity.class.getSimpleName();

    // view binding
    private ActivitySettungsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivitySettungsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(mBinding.settings.getId(), new SettingsFragment())
                    .commit();
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements SharedPreferences.OnSharedPreferenceChangeListener{

        // view model
        private SettingsViewModel mViewMode;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            // init view model
            mViewMode = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        
            // observer exception info
            mViewMode.getExceptionInfo().observe(this.requireActivity(), this::onException);
        
        }

        private void onException(String info) {
            if (info == null) return;

            if (info.equals(GameConstants.NOT_SUPPORT_INFO)) {
                // show toast
                GameUtils.showToast(this.getActivity(), this.getString(R.string.galaga_show_not_support_toast));
                // clear event
                mViewMode.clearExceptionInfo();
            }
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            // set transparent
            getListView().setBackgroundColor(android.graphics.Color.TRANSPARENT);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceManager().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sPf, @Nullable String key) {
            // change item setting
            SettingsItem.changeSharedPreference(mViewMode, sPf, key);
        }

        private enum SettingsItem {

            AutoFire(GameSettings.KEY_AUTO_FIRE) {
                @Override
                protected void handle(SettingsViewModel viewModel, SharedPreferences sPf) {
                    // get setting
                    boolean isAutoFire = sPf.getBoolean(GameSettings.KEY_AUTO_FIRE, false);
                    viewModel.updateAutoFire(isAutoFire);

                }
            },
            ShotType(GameSettings.KEY_SHOT_TYPE) {
                @Override
                protected void handle(SettingsViewModel viewModel, SharedPreferences sPf) {
                    // get setting
                    String style = sPf.getString(GameSettings.KEY_SHOT_TYPE, GameSettings.ShotStyle.STRAIGHT.name());
                    viewModel.updateShotStyle(style);
                }
            };

            private final String mKey;

            private SettingsItem(String key) {
                this.mKey = key;
            }

            static void changeSharedPreference(SettingsViewModel viewModel, SharedPreferences sPf, String key) {
                for (SettingsItem item: SettingsItem.values()) {
                    if (item.mKey.equals(key)) {
                        item.handle(viewModel, sPf);
                        break;
                    }
                }
            }
            protected abstract void handle(SettingsViewModel viewModel, SharedPreferences sPf);
        }


    }
}