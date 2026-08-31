/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.domain.entity;

import androidx.annotation.NonNull;

/**
 * Domain entity representing game settings.
 */
public final class Settings {
    private boolean mSoundEnable;
    private GameDifficulty mDifficulty;

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

    @NonNull
    @Override
    public String toString() {
        return "Settings{" +
                "mSoundEnable=" + mSoundEnable +
                ", mDifficulty=" + mDifficulty.name() +
                '}';
    }

    /**
     * Enum defining game difficulty levels and their associated parameters.
     */
    public enum GameDifficulty {
        EASY(0.6f, 8, 0.5f, 3, 500f, 1.2f, 3.0f),
        NORMAL(1.0f, 16, 1.0f, 4, 700f, 1.0f, 4.0f),
        HARD(1.4f, 24, 1.5f, 5, 900f, 0.8f, 5.0f);

        private final float mCtlSensitivity;
        private final int mObstacleCount;
        private final float mObstacleSpawnRate;
        private final int mCheckpointCount;
        private final float mCheckpointDistance;
        private final float mFrictionCoefficient;
        private final float mObstacleEffectDuration;

        GameDifficulty(float ctlSensitivity, int obstacleCount, float obstacleSpawnRate,
                       int checkpointCount, float checkpointDistance, float frictionCoefficient,
                       float obstacleEffectDuration) {
            mCtlSensitivity = ctlSensitivity;
            mObstacleCount = obstacleCount;
            mObstacleSpawnRate = obstacleSpawnRate;
            mCheckpointCount = checkpointCount;
            mCheckpointDistance = checkpointDistance;
            mFrictionCoefficient = frictionCoefficient;
            mObstacleEffectDuration = obstacleEffectDuration;
        }

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
