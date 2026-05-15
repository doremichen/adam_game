/*
 * Copyright (c) 2025 Adam
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
package com.adam.app.tetrisgame.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SettingsManager {

    public static final String KEY_SOUND_EFFECT = "sound_effect";
    public static final String KEY_SPEED = "speed";
    public static final String KEY_HIGH_SCORE = "high_score";


    private static volatile SettingsManager sManager;
    private SharedPreferences mPreferences;

    private SettingsManager(Context context) {
        // shared preference
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SettingsManager getInstance(Context context) {
        if (sManager == null) {
            synchronized (SettingsManager.class) {
                if (sManager == null) {
                    sManager = new SettingsManager(context);
                }
            }
        }
        return sManager;
    }

    public boolean isSoundEffect() {
        return mPreferences.getBoolean(KEY_SOUND_EFFECT, false);
    }

    public void setSoundEffect(boolean value) {
        mPreferences.edit().putBoolean(KEY_SOUND_EFFECT, value).apply();
    }

    public String getSpeed() {
        return mPreferences.getString(KEY_SPEED, "1");
    }

    public void setSpeed(String value) {
        mPreferences.edit().putString(KEY_SPEED, value).apply();
    }

    public int getHighScore() {
        return mPreferences.getInt(KEY_HIGH_SCORE, 0);
    }

    public void setHighScore(int value) {
        mPreferences.edit().putInt(KEY_HIGH_SCORE, value).apply();
    }
}
