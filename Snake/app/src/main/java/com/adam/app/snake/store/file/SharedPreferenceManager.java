/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the shared preference manager
 *
 * Author: Adam Chen
 * Date: 2025/09/26
 */
package com.adam.app.snake.store.file;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    // TAG SharedPreferenceManager
    private static final String TAG = "SharedPreferenceManager";
    private static final String SHARED_PREFERENCES_NAME = "snake_game";
    private static SharedPreferenceManager sInstance;
    private final SharedPreferences prefs;

    /**
     * class Keys
     * WRAPPED_ENABLED: String
     */
    public static class Keys {
        public static final String WRAPPED_ENABLED = "wrapped_enabled";
    }



    /**
     * Constructor with Context
     *
     * @param context Context
     */
    private SharedPreferenceManager(Context context) {
        prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * get instance of SharedPreferenceManager
     */
    public static SharedPreferenceManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SharedPreferenceManager.class) {
                if (sInstance == null) {
                    sInstance = new SharedPreferenceManager(context);
                }
            }
        }
        return sInstance;
    }

    // String
    public void putString(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    // int
    public void putInt(String key, int value) {
        prefs.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    // boolean
    public void putBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    // float
    public void putFloat(String key, float value) {
        prefs.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return prefs.getFloat(key, defaultValue);
    }

    // long
    public void putLong(String key, long value) {
        prefs.edit().putLong(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return prefs.getLong(key, defaultValue);
    }

    // remove key
    public void remove(String key) {
        prefs.edit().remove(key).apply();
    }

    // clear all
    public void clear() {
        prefs.edit().clear().apply();
    }
}
