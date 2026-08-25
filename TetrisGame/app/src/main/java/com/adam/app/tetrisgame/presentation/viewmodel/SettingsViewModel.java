/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.presentation.viewmodel;

import androidx.lifecycle.ViewModel;
import com.adam.app.tetrisgame.domain.usecase.SettingsUseCase;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@HiltViewModel
public class SettingsViewModel extends ViewModel {
    private final SettingsUseCase mSettingsUseCase;

    @Inject
    public SettingsViewModel(SettingsUseCase settingsUseCase) {
        this.mSettingsUseCase = settingsUseCase;
    }

    public boolean isSoundEffectEnabled() {
        return (Boolean) mSettingsUseCase.execute(SettingsUseCase.Action.IS_SOUND_ENABLED, null);
    }

    public void setSoundEffectEnabled(boolean enabled) {
        mSettingsUseCase.execute(SettingsUseCase.Action.SET_SOUND_ENABLED, enabled);
    }

    public String getSpeed() {
        // execute returns Object, but for speed preference it might be a String in repository
        // but our UseCase returns Integer for GET_SPEED action currently (based on previous logic)
        // actually looking at my previous edit of SettingsUseCase:
        // case GET_SPEED: try { return Integer.parseInt(mBridge.getSpeed()); }
        // so it returns Integer.
        return String.valueOf(mSettingsUseCase.execute(SettingsUseCase.Action.GET_SPEED, null));
    }

    public void setSpeed(String speed) {
        mSettingsUseCase.execute(SettingsUseCase.Action.SET_SPEED, speed);
    }
}
