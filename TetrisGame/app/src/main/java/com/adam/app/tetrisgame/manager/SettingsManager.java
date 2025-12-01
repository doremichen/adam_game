/**
 * copyright 2025 Adam Chen
 * Description: SettingsManager is the manager of the settings.
 * Author: Adam Chen
 * Date: 2025/12/01
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
