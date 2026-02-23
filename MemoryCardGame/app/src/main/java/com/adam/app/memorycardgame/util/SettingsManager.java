/**
 * Copyright (c) 2026 Adam Chen. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 * <P>
 *     Description: This is the settings manager.
 * </P>
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/02/23
 */
package com.adam.app.memorycardgame.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static SettingsManager sIinstance;
    private SharedPreferences mPrefs;

    // setting: enable sound
    public static final String KEY_ENABLE_SOUND = "key_sound";
    // setting: theme
    public static final String KEY_THEME = "key_theme";

    private SettingsManager (Context ctx) {
        this.mPrefs = ctx.getSharedPreferences("memory_card_game_settings", Context.MODE_PRIVATE);
    }

    /**
     * Singleton
     * @param ctx Context
     * @return SettingsManager
     */
    public static SettingsManager getInstance(Context ctx) {
        if (sIinstance == null) {
            synchronized (SettingsManager.class) {
                if (sIinstance == null) {
                    sIinstance = new SettingsManager(ctx);
                }
            }
        }
        return sIinstance;
    }

    // -- setter --
    public void setSoundEnabled(boolean enable) {
        mPrefs.edit().putBoolean(KEY_ENABLE_SOUND, enable).apply();
    }
    public void setThemeMode(String mode) {
        mPrefs.edit().putString(KEY_THEME, mode).apply();
    }

    // -- getter --
    public boolean isSoundEnabled() {
        return mPrefs.getBoolean(KEY_ENABLE_SOUND, true);
    }
    public String getThemeMode() {
        return mPrefs.getString(KEY_THEME, "system");
    }

}
