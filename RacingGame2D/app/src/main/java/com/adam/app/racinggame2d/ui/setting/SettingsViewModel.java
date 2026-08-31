/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.ui.setting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.racinggame2d.application.SettingsUseCase;
import com.adam.app.racinggame2d.domain.entity.Settings;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the SettingsActivity.
 * Adheres to AS-02: Interacts only with UseCases.
 */
@HiltViewModel
public final class SettingsViewModel extends ViewModel {
    private final SettingsUseCase mSettingsUseCase;
    private final MutableLiveData<Settings> mSettingsLiveData = new MutableLiveData<>();

    @Inject
    public SettingsViewModel(SettingsUseCase settingsUseCase) {
        this.mSettingsUseCase = settingsUseCase;
        Settings current = mSettingsUseCase.execute(SettingsUseCase.Action.LOAD_SETTINGS, null);
        if (current == null) {
            current = new Settings();
            mSettingsUseCase.execute(SettingsUseCase.Action.SAVE_SETTINGS, current);
        }
        mSettingsLiveData.setValue(current);
    }

    public LiveData<Settings> getSettingsLiveData() {
        return mSettingsLiveData;
    }

    public void saveSettings() {
        Settings current = mSettingsLiveData.getValue();
        if (current != null) {
            mSettingsUseCase.execute(SettingsUseCase.Action.SAVE_SETTINGS, current);
        }
    }

    public boolean isSoundEnable() {
        Settings current = mSettingsLiveData.getValue();
        return current != null && current.isSoundEnable();
    }

    public void setSoundEnable(boolean isChecked) {
        Settings current = mSettingsLiveData.getValue();
        if (current != null) {
            current.setSoundEnable(isChecked);
            mSettingsLiveData.setValue(current);
        }
    }

    public void setDifficulty(Settings.GameDifficulty difficulty) {
        Settings current = mSettingsLiveData.getValue();
        if (current != null) {
            current.setDifficulty(difficulty);
            mSettingsLiveData.setValue(current);
        }
    }
}
