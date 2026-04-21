/*
 * Copyright (c) 2026 Adam Chen
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

package com.adam.app.galaga.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * LevelConfig
 */
public class LevelConfig {

    @SerializedName("level_id")
    private int mLevelId;
    @SerializedName("metadata")
    private Metadata mMetadata;
    @SerializedName("enemy_settings")
    private EnemySettings mEnemySettings;
    @SerializedName("winning_condition")
    private WinningCondition mWinningCondition;

    // --- getter ---
    public int getLevelId() {
        return mLevelId;
    }

    public Metadata getMetadata() {
        return mMetadata;
    }

    public EnemySettings getEnemySettings() {
        return mEnemySettings;
    }

    public WinningCondition getWinningCondition() {
        return mWinningCondition;
    }


    public static class Metadata {
        @SerializedName("title")
        private String mTitle;
        @SerializedName("difficulty_multiplier")
        private float mDifficultyMultiplier;

        // --- getter ---
        public String getTitle() {
            return mTitle;
        }

        public float getDifficultyMultiplier() {
            return mDifficultyMultiplier;
        }
    }

    public static class EnemySettings {
        @SerializedName("total_count")
        private int mTotalCount;
        @SerializedName("base_speed")
        private float mBaseSpeed;
        @SerializedName("spawn_interval_ms")
        private long mSpawnIntervalMs;

        // --- getter ---
        public int getTotalCount() {
            return mTotalCount;
        }

        public float getBaseSpeed() {
            return mBaseSpeed;
        }

        public long getSpawnIntervalMs() {
            return mSpawnIntervalMs;
        }
    }

    public static class WinningCondition {
        @SerializedName("type")
        private String mType;

        // --- getter ---
        public String getType() {
            return mType;
        }

    }
}
