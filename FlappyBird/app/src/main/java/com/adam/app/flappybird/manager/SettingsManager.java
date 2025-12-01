/**
 * This class is the settings manager.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SettingsManager {
    // --- Key ---
    private static final String KEY_SOUND_EFFECT = "sound_effect";
    private static volatile SettingsManager sInstance;
    private final SharedPreferences mPreferences;

    private SettingsManager(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    /**
     * get settings manager instance
     *
     * @param context context
     * @return SettingsManager
     */
    public static SettingsManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SettingsManager.class) {
                if (sInstance == null)
                    sInstance = new SettingsManager(context);
            }
        }
        return sInstance;
    }

    public boolean isSoundEffect() {
        return mPreferences.getBoolean(KEY_SOUND_EFFECT, false);
    }

    public void setSoundEffect(boolean value) {
        mPreferences.edit().putBoolean(KEY_SOUND_EFFECT, value).apply();
    }

}
