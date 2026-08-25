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
package com.adam.app.tetrisgame.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.adam.app.tetrisgame.domain.repository.SettingsRepository;
import com.adam.app.tetrisgame.util.Constants;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;

public class SettingsRepositoryImpl implements SettingsRepository {
    private final SharedPreferences mPreferences;

    @Inject
    public SettingsRepositoryImpl(@ApplicationContext Context context) {
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean isSoundEffectEnabled() {
        return mPreferences.getBoolean(Constants.PREF_SOUND_EFFECT, false);
    }

    @Override
    public void setSoundEffectEnabled(boolean enabled) {
        mPreferences.edit().putBoolean(Constants.PREF_SOUND_EFFECT, enabled).apply();
    }

    @Override
    public String getSpeed() {
        return mPreferences.getString(Constants.PREF_SPEED, "1");
    }

    @Override
    public void setSpeed(String speed) {
        mPreferences.edit().putString(Constants.PREF_SPEED, speed).apply();
    }

    @Override
    public int getHighScore() {
        return mPreferences.getInt(Constants.PREF_HIGH_SCORE, 0);
    }

    @Override
    public void setHighScore(int highScore) {
        mPreferences.edit().putInt(Constants.PREF_HIGH_SCORE, highScore).apply();
    }
}
