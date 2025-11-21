package com.adam.app.flappybird.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SettingsManager {
    // --- Key ---
    private static final String KEY_SOUND_EFFECT = "sound_effect";
    private static SettingsManager mIstance;
    private SharedPreferences mPreferences;

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
        synchronized (SettingsManager.class) {
            if (mIstance == null) {
                synchronized (SettingsManager.class) {
                    mIstance = new SettingsManager(context);
                }
            }
        }
        return mIstance;
    }

    public boolean isSoundEffect() {
        return mPreferences.getBoolean(KEY_SOUND_EFFECT, false);
    }

    public void setSoundEffect(boolean value) {
        mPreferences.edit().putBoolean(KEY_SOUND_EFFECT, value).apply();
    }

}
