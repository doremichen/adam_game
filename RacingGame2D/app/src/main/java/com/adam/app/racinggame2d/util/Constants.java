/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is used to define constants.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/30
 */
package com.adam.app.racinggame2d.util;

public final class Constants {

    // === Game logic constants ===
    public static final int CAR_MOVE_STEP = 20;         // Every time the car moves, it moves 20 pixels.
    public static final int UPDATE_INTERVAL_MS = 16;    // about 60 FPS
    public static final int INITIAL_LIVES = 3;          // initial lives
    // === SharedPreferences Keys ===
    public static final String PREF_NAME = "racing_game_prefs";
    public static final String PREF_PLAYER_NAME = "player_name";
    public static final String PREF_HIGH_SCORE = "high_score";
    public static final String PREF_SOUND_ENABLED = "sound_enabled";
    // === sound resource constants ===
    public static final int SOUND_COLLISION = 1;
    public static final int SOUND_ENGINE = 2;
    public static final int SOUND_BUTTON = 3;
    // === Intent keys ===
    public static final String PLAYER_NAME = "key.player.name";


    private Constants() {
        // avoid to be instantiated
    }

}
