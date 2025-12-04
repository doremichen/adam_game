/**
 * Copyright 2025 Adam Game
 * <p>
 * This class is used to manage the settings of the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-04
 *
 */
package com.adam.app.whack_a_molejava.controller;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    // --- Key ---
    public static final String KEY_SOUND_ON = "sound_on";
    public static final String KEY_DURATION_TIME = "duration_time";
    public static final String KEY_VIBRATION_ON = "vibration_on";
    public static final String KEY_DIFFICULTY = "difficulty";

    private static SettingsManager sInstance;
    private final SharedPreferences mPreferences;

    private SettingsManager(Context context) {
        mPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public static SettingsManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SettingsManager.class) {
                if (sInstance == null) {
                    sInstance = new SettingsManager(context);
                }
            }
        }
        return sInstance;
    }

    // --- setter/getter ---
    public void setSoundOn(boolean soundOn) {
        mPreferences.edit().putBoolean(KEY_SOUND_ON, soundOn).apply();
    }
    public boolean isSoundOn() {
        return mPreferences.getBoolean(KEY_SOUND_ON, true);
    }

    public void setDurationTime(int durationTime) {
        mPreferences.edit().putInt(KEY_DURATION_TIME, durationTime).apply();
    }
    public int getDurationTime() {
        return mPreferences.getInt(KEY_DURATION_TIME, 30);
    }

    public void setVibrationOn(boolean vibrationOn) {
        mPreferences.edit().putBoolean(KEY_VIBRATION_ON, vibrationOn).apply();
    }
    public boolean isVibrationOn() {
        return mPreferences.getBoolean(KEY_VIBRATION_ON, true);
    }

    public void setDifficulty(int difficulty) {
        mPreferences.edit().putInt(KEY_DIFFICULTY, difficulty).apply();
    }
    public int getDifficulty() {
        return mPreferences.getInt(KEY_DIFFICULTY, 1);
    }
}
