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

package com.adam.app.galaga.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.galaga.data.local.prefs.GameSettings;
import com.adam.app.galaga.utils.GameConstants;

public class SettingsViewModel extends ViewModel {
    // TAG
    private static final String TAG = SettingsViewModel.class.getSimpleName();

    // live data
    private final MutableLiveData<String> mExeceptionStr = new MutableLiveData<>(null);
    public LiveData<String> getExceptionInfo() {
        return mExeceptionStr;
    }


    // game setting
    private GameSettings mSettings = GameSettings.getInstance();

    public void updateAutoFire(boolean enable) {
        mSettings.setAutoFire(enable);
    }

    public void updateShotStyle(String styleStr) {
        try {
            GameSettings.ShotStyle style = GameSettings.ShotStyle.valueOf(styleStr);
            mSettings.setShotStyle(style);
        } catch (IllegalArgumentException | NullPointerException e) {
            mExeceptionStr.setValue(GameConstants.NOT_SUPPORT_INFO);
        }
    }

    public void clearExceptionInfo() {
        mExeceptionStr.setValue(null);
    }

}
