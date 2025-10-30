/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is used to store the shared preferences.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/30
 */
package com.adam.app.racinggame2d.util;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPrefHelper {

    private static SharedPrefHelper sINSTANCE;

    private final SharedPreferences mPrefs;

    private SharedPrefHelper(Context context) {
        mPrefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefHelper getInstance(Context context) {
        if (sINSTANCE == null) {
            synchronized (SharedPrefHelper.class) {
                if (sINSTANCE == null) {
                    sINSTANCE = new SharedPrefHelper(context);
                }
            }
        }
        return sINSTANCE;
    }

    public void setPlayerName(String name) {
        mPrefs.edit().putString(Constants.PREF_PLAYER_NAME, name).apply();
    }

    public String getPlayerName() {
        return mPrefs.getString(Constants.PREF_PLAYER_NAME, "");
    }

    public void setHighScore(int score) {
        mPrefs.edit().putInt(Constants.PREF_HIGH_SCORE, score).apply();
    }

    public int getHighScore() {
        return mPrefs.getInt(Constants.PREF_HIGH_SCORE, 0);
    }

    public void setSoundEnabled(boolean enabled) {
        mPrefs.edit().putBoolean(Constants.PREF_SOUND_ENABLED, enabled).apply();
    }

    public boolean isSoundEnabled() {
        return mPrefs.getBoolean(Constants.PREF_SOUND_ENABLED, true);
    }

}
