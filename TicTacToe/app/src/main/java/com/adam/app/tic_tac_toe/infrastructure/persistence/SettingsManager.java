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

package com.adam.app.tic_tac_toe.infrastructure.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * Manages game settings using SharedPreferences.
 */
@Singleton
public class SettingsManager {
    private static final String KEY_GAME_MODE_PVE = "game_mode_pve";
    private static final String KEY_AI_STRATEGY_HARD = "ai_strategy_hard";

    private final SharedPreferences mSharedPreferences;

    @Inject
    public SettingsManager(@ApplicationContext @NonNull Context context) {
        mSharedPreferences = context.getSharedPreferences("tic_tac_toe_settings", Context.MODE_PRIVATE);
    }

    public boolean isGameModePve() {
        return mSharedPreferences.getBoolean(KEY_GAME_MODE_PVE, false);
    }

    public void setGameModePve(boolean value) {
        mSharedPreferences.edit().putBoolean(KEY_GAME_MODE_PVE, value).apply();
    }

    public boolean isAiStrategyHard() {
        return mSharedPreferences.getBoolean(KEY_AI_STRATEGY_HARD, true);
    }

    public void setAiStrategyHard(boolean value) {
        mSharedPreferences.edit().putBoolean(KEY_AI_STRATEGY_HARD, value).apply();
    }
}
