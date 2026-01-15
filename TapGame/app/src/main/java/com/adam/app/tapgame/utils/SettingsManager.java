/**
 * File: SettingsManager.java
 * Description: This class is Settings Manager
 *
 * @author Adam Chen
 * @version 1.0 - 2026/01/14
 */
package com.adam.app.tapgame.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    // Key
    private static final String KEY_EASY_MODE = "easy_mode";
    private static final String KEY_INTERVAL = "interval";


    private static volatile SettingsManager sInstance;

    private SharedPreferences mPref;

    private SettingsManager(Context ctx) {
        mPref = ctx.getSharedPreferences("tap_game_settings", Context.MODE_PRIVATE);
    }

    public static SettingsManager getInstance(Context ctx) {
        if (sInstance == null) {
            synchronized (SettingsManager.class) {
                if (sInstance == null) {
                    sInstance = new SettingsManager(ctx);
                }
            }
        }
        return sInstance;
    }

    // --- export key ---
    public static String getKeyEasyMode() {
        return KEY_EASY_MODE;
    }
    public static String getKeyInterval() {
        return KEY_INTERVAL;
    }


    /**
     * set easy mode
     * @param easyMode easy mode
     */
    public void setEasyMode(boolean easyMode) {
        mPref.edit().putBoolean(KEY_EASY_MODE, easyMode).apply();
    }

    /**
     * is easy mode
     * @return easy mode
     */
    public boolean isEasyMode() {
        return mPref.getBoolean(KEY_EASY_MODE, false);
    }

    /**
     * set interval
     * @param interval interval
     */
    public void setInterval(int interval) {
        mPref.edit().putInt(KEY_INTERVAL, interval).apply();
    }

    /**
     * get interval
     * @return interval
     */
    public int getInterval() {
        return mPref.getInt(KEY_INTERVAL, 10);
    }

}
