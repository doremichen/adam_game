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

import com.adam.app.racinggame2d.model.entity.Settings;
import com.google.gson.Gson;

public final class SharedPrefHelper {

    private static SharedPrefHelper sINSTANCE;

    private final SharedPreferences mPrefs;
    // GSON for parsing JSON to save game settings
    private final Gson mGson;


    private SharedPrefHelper(Context context) {
        mPrefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
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

    public void saveSettings(Settings settings) {
        // save to shared preferences
        mPrefs.edit().putString(Constants.PREF_SETTINGS, mGson.toJson(settings)).apply();

    }

    public Settings loadSettings() {
        // load from shared preferences
        String json = mPrefs.getString(Constants.PREF_SETTINGS, "");
        if (json.isEmpty()) {
            return null;
        }
        return mGson.fromJson(json, Settings.class);
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

    public void clearAll() {
        mPrefs.edit().clear().apply();
    }

}
