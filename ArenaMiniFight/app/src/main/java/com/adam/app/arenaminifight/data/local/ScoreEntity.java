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

package com.adam.app.arenaminifight.data.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room Database Entity for Leaderboard scores.
 * Trace: UC-MENU-04, REQ-MENU-04, REQ-NFR-04
 */
@Entity(tableName = "scores")
public class ScoreEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "player_name")
    private String mPlayerName;

    @ColumnInfo(name = "survival_time")
    private long mSurvivalTime;

    @ColumnInfo(name = "kill_count")
    private int mKillCount;

    @ColumnInfo(name = "level")
    private int mLevel;

    @ColumnInfo(name = "timestamp")
    private long mTimestamp;

    public ScoreEntity(String playerName, long survivalTime, int killCount, int level, long timestamp) {
        mPlayerName = playerName;
        mSurvivalTime = survivalTime;
        mKillCount = killCount;
        mLevel = level;
        mTimestamp = timestamp;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public long getSurvivalTime() {
        return mSurvivalTime;
    }

    public int getKillCount() {
        return mKillCount;
    }

    public int getLevel() {
        return mLevel;
    }

    public long getTimestamp() {
        return mTimestamp;
    }
}
