/**
 * This class is used to store the constants used in the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-18
 */
package com.adam.app.flappybird.util;

public final class GameConstants {

    // --- Constants ---
    public static final int SCREEN_WIDTH = 1080;
    public static final int SCREEN_HEIGHT = 1920;
    public static final int COLLISION_RANGE = 40;
    public static final float BIRD_RADIUS = 40f;
    public static final float PIPE_WIDTH = 200f;
    public static final float PIPE_GAP = 300f;
    public static final float PIPE_SPEED = 400f; // px / second
    public static final float GRAVITY = 1200f; // px / second^2
    public static final float FLAP_VELOCITY = -450f; // px / second
    public static final int SPAWN_INTERVAL = 900; // px distance between pipe centers (approx)
    public static final float MAX_DELTA = 0.05f; // cap delta time (seconds)
    public static final int SOUND_WING = 1;
    public static final int SOUND_HIT = 2;
    public static final int SOUND_POINT = 3;



    private GameConstants() {
        // avoid to instance
    }
}
