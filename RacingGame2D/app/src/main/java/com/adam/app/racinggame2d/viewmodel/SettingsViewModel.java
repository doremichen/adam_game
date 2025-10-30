/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the view model for the settings activity.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/30
 */
package com.adam.app.racinggame2d.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.racinggame2d.model.entity.Settings;
import com.adam.app.racinggame2d.util.SharedPrefHelper;

public class SettingsViewModel extends AndroidViewModel {
    // sharedPrefHelper
    private SharedPrefHelper mSharedPrefHelper;
    // Live data settings
    private MutableLiveData<Settings> mSettingsLiveDate = new MutableLiveData<>();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        this.mSharedPrefHelper = SharedPrefHelper.getInstance(application);
        // update live data
        Settings currentSettings = this.mSharedPrefHelper.loadSettings();
        if (currentSettings == null) {
            currentSettings = new Settings();
            // save to shared pref
            this.mSharedPrefHelper.saveSettings(currentSettings);
        }
        this.mSettingsLiveDate.setValue(currentSettings);
    }

    /**
     * get settings live data
     *
     */
    public LiveData<Settings> getSettingsLiveData() {
        return mSettingsLiveDate;
    }


    /**
     * saveSettings
     */
    public void saveSettings() {
        // get settings from live data
        Settings currentSettings = this.mSettingsLiveDate.getValue();
        if (currentSettings != null) {
            // save to shared pref
            this.mSharedPrefHelper.saveSettings(currentSettings);
        }
    }

    /**
     * set sound enable
     *
     * @param isChecked boolean
     *  true if sound is enabled
     *  false if sound is disabled
     */
    public void setSoundEnable(boolean isChecked) {
        // get current settings
        Settings currentSettings = this.mSettingsLiveDate.getValue();
        if (currentSettings != null) {
            currentSettings.setSoundEnable(isChecked);
            this.mSettingsLiveDate.setValue(currentSettings);
        }
    }

    /**
     * is sound enable
     *
     * @return boolean
     *  true if sound is enabled
     *  false if sound is disabled
     */
    public boolean isSoundEnable() {
        // get current settings
        Settings currentSettings = this.mSettingsLiveDate.getValue();
        if (currentSettings != null) {
            return currentSettings.isSoundEnable();
        }

        return false;
    }

}
