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

package com.adam.app.racinggame2d.infrastructure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adam.app.racinggame2d.domain.ISettingsRepository;
import com.adam.app.racinggame2d.domain.entity.Settings;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Infrastructure implementation of settings repository using SharedPrefHelper.
 */
@Singleton
public final class SettingsRepositoryImpl implements ISettingsRepository {
    private final SharedPrefHelper mSharedPrefHelper;

    @Inject
    public SettingsRepositoryImpl(SharedPrefHelper sharedPrefHelper) {
        this.mSharedPrefHelper = sharedPrefHelper;
    }

    @Override
    public void saveSettings(@NonNull Settings settings) {
        mSharedPrefHelper.saveSettings(settings);
    }

    @Override
    @Nullable
    public Settings loadSettings() {
        return mSharedPrefHelper.loadSettings();
    }

    @Override
    @NonNull
    public String getPlayerName() {
        return mSharedPrefHelper.getPlayerName();
    }

    @Override
    public void setPlayerName(@NonNull String name) {
        mSharedPrefHelper.setPlayerName(name);
    }

    @Override
    public int getHighScore() {
        return mSharedPrefHelper.getHighScore();
    }

    @Override
    public void setHighScore(int score) {
        mSharedPrefHelper.setHighScore(score);
    }
}
