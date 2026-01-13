package com.adam.app.tic_tac_toe.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    public static final String KEY_GAME_MODE_PVE = "game_mode_pve";
    public static final String KEY_AI_STRATEGY_HARD = "ai_strategy_hard";

    private static volatile SettingsManager sInstance;

    private final SharedPreferences mSharedPreferences;

    private SettingsManager(Context context) {
        mSharedPreferences = context.getSharedPreferences("tic_tac_toe_settings", Context.MODE_PRIVATE);
    }

    public static SettingsManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SettingsManager.class) {
                if (sInstance == null) {
                    sInstance = new SettingsManager(context.getApplicationContext());
                }

            }
        }

        return sInstance;

    }

    public boolean isGameModePve() {
        return mSharedPreferences.getBoolean(KEY_GAME_MODE_PVE, false);
    }

    // ---setter/getter ---
    public void setGameModePve(boolean value) {
        mSharedPreferences.edit().putBoolean(KEY_GAME_MODE_PVE, value).apply();
    }

    public boolean isAiStrategyHard() {
        return mSharedPreferences.getBoolean(KEY_AI_STRATEGY_HARD, false);
    }

    public void setAiStrategyHard(boolean value) {
        mSharedPreferences.edit().putBoolean(KEY_AI_STRATEGY_HARD, value).apply();

    }


}
