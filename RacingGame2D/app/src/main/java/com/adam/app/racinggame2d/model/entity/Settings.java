/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is used to save the settings of the game.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/30
 */
package com.adam.app.racinggame2d.model.entity;

public class Settings {
    // sound
    private boolean mSoundEnable;
    private GameDifficulty mDifficulty;

    /**
     * constructor
     */
    public Settings() {
        mSoundEnable = true;
        mDifficulty = GameDifficulty.NORMAL;
    }

    public boolean isSoundEnable() {
        return mSoundEnable;
    }

    public void setSoundEnable(boolean enable) {
        mSoundEnable = enable;
    }

    public GameDifficulty getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(GameDifficulty difficulty) {
        mDifficulty = difficulty;
    }


    @Override
    public String toString() {
        return "Settings{" +
                "mSoundEnable=" + mSoundEnable +
                ", mDifficulty=" + mDifficulty.name() +
                '}';
    }

    /**
     * game difficulty
     * <p>
     * EASY, NORMAL, HARD
     * <p>
     */
    public enum GameDifficulty {
        EASY(
                0.6f,   // controlSensitivity
                0.5f,   // obstacleSpawnRate
                500f,   // checkpointDistance
                1.2f,   // frictionCoefficient
                0.5f    // obstacleEffectDuration
        ),
        NORMAL(
                1.0f,
                1.0f,
                700f,
                1.0f,
                1.0f
        ),
        HARD(
                1.4f,
                1.5f,
                900f,
                0.8f,
                1.5f
        );

        private final float mCtlSensitivity;
        private final float mObstacleSpawnRate;
        private final float mCheckpointDistance;
        private final float mFrictionCoefficient;
        private final float mObstacleEffectDuration;

        private GameDifficulty(float ctlSensitivity, float obstacleSpawnRate, float checkpointDistance, float frictionCoefficient, float obstacleEffectDuration) {
            mCtlSensitivity = ctlSensitivity;
            mObstacleSpawnRate = obstacleSpawnRate;
            mCheckpointDistance = checkpointDistance;
            mFrictionCoefficient = frictionCoefficient;
            mObstacleEffectDuration = obstacleEffectDuration;
        }

        // --- getter ---
        public float getCtlSensitivity() {
            return mCtlSensitivity;
        }

        public float getObstacleSpawnRate() {
            return mObstacleSpawnRate;
        }

        public float getCheckpointDistance() {
            return mCheckpointDistance;
        }

        public float getFrictionCoefficient() {
            return mFrictionCoefficient;
        }

        public float getObstacleEffectDuration() {
            return mObstacleEffectDuration;
        }

    }



}
