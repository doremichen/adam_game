/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "leaderboard")
public class LeaderboardEntry {

    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "score")
    private int mScore;

    @ColumnInfo(name = "time")
    private long mTimeStamp;

    public LeaderboardEntry(String name, int score, long timeStamp) {
        mName = name;
        mScore = score;
        mTimeStamp = timeStamp;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public int getScore() {
        return mScore;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mScore=" + mScore +
                ", mTimeStamp=" + mTimeStamp +
                '}';
    }

}
