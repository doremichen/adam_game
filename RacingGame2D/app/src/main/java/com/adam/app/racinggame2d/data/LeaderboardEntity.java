/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the leaderboard entity.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/11/05
 */
package com.adam.app.racinggame2d.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "leaderboard")
public class LeaderboardEntity {
    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "player_name")
    private String mPlayerName;
    @ColumnInfo(name = "score")
    private int mScore;
    @ColumnInfo(name = "timestamp")
    private long mTimestamp;

    /**
     * constructor
     * @param playerName
     * @param score
     * @param timestamp
     */
    public LeaderboardEntity(String playerName, int score, long timestamp) {
        mPlayerName = playerName;
        mScore = score;
        mTimestamp = timestamp;
    }

    //Getter/Setter
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public void setPlayerName(String playerName) {
        mPlayerName = playerName;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }
}
