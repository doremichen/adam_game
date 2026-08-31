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

import android.content.Context;
import android.content.SharedPreferences;

import com.adam.app.racinggame2d.domain.entity.Settings;
import com.adam.app.racinggame2d.util.Constants;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * Utility class to handle SharedPreferences operations.
 */
@Singleton
public final class SharedPrefHelper {
    private final SharedPreferences mPrefs;
    private final Gson mGson;

    @Inject
    public SharedPrefHelper(@ApplicationContext Context context) {
        this.mPrefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        this.mGson = new Gson();
    }

    public void saveSettings(Settings settings) {
        mPrefs.edit().putString(Constants.PREF_SETTINGS, mGson.toJson(settings)).apply();
    }

    public Settings loadSettings() {
        String json = mPrefs.getString(Constants.PREF_SETTINGS, "");
        if (json.isEmpty()) {
            return null;
        }
        return mGson.fromJson(json, Settings.class);
    }

    public String getPlayerName() {
        return mPrefs.getString(Constants.PREF_PLAYER_NAME, "");
    }

    public void setPlayerName(String name) {
        mPrefs.edit().putString(Constants.PREF_PLAYER_NAME, name).apply();
    }

    public int getHighScore() {
        return mPrefs.getInt(Constants.PREF_HIGH_SCORE, 0);
    }

    public void setHighScore(int score) {
        mPrefs.edit().putInt(Constants.PREF_HIGH_SCORE, score).apply();
    }

    public void clearAll() {
        mPrefs.edit().clear().apply();
    }
}
