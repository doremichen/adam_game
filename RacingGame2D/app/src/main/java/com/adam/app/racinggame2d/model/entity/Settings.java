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
                0.6f,
                8,
                0.5f,
                3,
                500f,
                1.2f,
                3.0f),
        NORMAL(
                1.0f,
                16,
                1.0f,
                4,
                700f,
                1.0f,
                4.0f),
        HARD(
                1.4f,
                24,
                1.5f,
                5,
                900f,
                0.8f,
                5.0f);
        private final float mCtlSensitivity;
        private final int mObstacleCount;
        private final float mObstacleSpawnRate;
        private final int mCheckpointCount;
        private final float mCheckpointDistance;
        private final float mFrictionCoefficient;
        private final float mObstacleEffectDuration;

        private GameDifficulty(float ctlSensitivity, int obstacleCount, float obstacleSpawnRate, int checkpointCount, float checkpointDistance, float frictionCoefficient, float obstacleEffectDuration) {
            mCtlSensitivity = ctlSensitivity;
            mObstacleCount = obstacleCount;
            mObstacleSpawnRate = obstacleSpawnRate;
            mCheckpointCount = checkpointCount;
            mCheckpointDistance = checkpointDistance;
            mFrictionCoefficient = frictionCoefficient;
            mObstacleEffectDuration = obstacleEffectDuration;
        }

        // --- getter ---
        public float getCtlSensitivity() {
            return mCtlSensitivity;
        }

        public int getObstacleCount() {
            return mObstacleCount;
        }

        public float getObstacleSpawnRate() {
            return mObstacleSpawnRate;
        }

        public int getCheckpointCount() {
            return mCheckpointCount;
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
