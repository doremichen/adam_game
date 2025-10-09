/**
 * Copyright 2015 the Adam Game
 *
 * Description: this class is the leaderboard entry class that is used to store the leaderboard data
 *
 * Author: Adam Chen
 * Date: 2025/10/08
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
