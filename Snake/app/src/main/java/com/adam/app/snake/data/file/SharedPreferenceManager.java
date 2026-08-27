/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.data.file;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class SharedPreferenceManager {
    // TAG SharedPreferenceManager
    private static final String TAG = "SharedPreferenceManager";
    private static final String SHARED_PREFERENCES_NAME = "snake_game";
    private final SharedPreferences mPrefs;

    /**
     * Keys class
     */
    public static class Keys {
        public static final String USER_NAME = "key_user_name";
        public static final String WRAP_MODE = "key_wrap_mode";
        public static final String SPECIAL_FOOD = "key_special_food";
        public static final String MULTI_FOODS_SHOW = "key_multi_foods_show";
        public static final String SPECIAL_FREQ = "key_special_freq";
        public static final String VERSION = "key_version";
    }


    /**
     * Constructor with Context
     *
     * @param context Context
     */
    @Inject
    public SharedPreferenceManager(@ApplicationContext Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // String
    public void putString(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return mPrefs.getString(key, defaultValue);
    }

    // int
    public void putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return mPrefs.getInt(key, defaultValue);
    }

    // boolean
    public void putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPrefs.getBoolean(key, defaultValue);
    }

    // float
    public void putFloat(String key, float value) {
        mPrefs.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return mPrefs.getFloat(key, defaultValue);
    }

    // long
    public void putLong(String key, long value) {
        mPrefs.edit().putLong(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return mPrefs.getLong(key, defaultValue);
    }

    // remove key
    public void remove(String key) {
        mPrefs.edit().remove(key).apply();
    }

    // clear all
    public void clear() {
        mPrefs.edit().clear().apply();
    }
}
