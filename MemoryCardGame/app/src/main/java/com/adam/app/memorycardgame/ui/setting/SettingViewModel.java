/**
 * Copyright (c) 2026 Adam Chen. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 * <p>
 * Description: This is the setting view model.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/02/12
 */
package com.adam.app.memorycardgame.ui.setting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.memorycardgame.util.SettingsManager;

public class SettingViewModel extends AndroidViewModel {

    // Live data: enable sound
    private final MutableLiveData<Boolean> mSoundEnabled = new MutableLiveData<>();
    // Live data: theme mode
    private final MutableLiveData<String> mThemeMode = new MutableLiveData<>();
    private final SettingsManager mSettingsManager;

    public SettingViewModel(@NonNull Application application) {
        super(application);

        this.mSettingsManager = SettingsManager.getInstance(application);

        // init live data
        mSoundEnabled.setValue(mSettingsManager.isSoundEnabled());
        mThemeMode.setValue(mSettingsManager.getThemeMode());
    }

    public LiveData<Boolean> getSoundEnabled() {
        return mSoundEnabled;
    }

    public LiveData<String> getThemeMode() {
        return mThemeMode;
    }

    public void updateSound(boolean enable) {
        mSettingsManager.setSoundEnabled(enable);
        mSoundEnabled.setValue(enable);
    }

    public void updateTheme(String mode) {
        mSettingsManager.setThemeMode(mode);

        switch (mode) {
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }

        mThemeMode.setValue(mode);
    }
}