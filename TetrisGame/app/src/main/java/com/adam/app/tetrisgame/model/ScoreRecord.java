/**
 * Description: This class is Table that is used to record the score of the game.
 * Author: Adam Chen
 * Date: 2025/08/15
 */
package com.adam.app.tetrisgame.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Entity(tableName = "score_table")
public class ScoreRecord {
    // primary key
    @PrimaryKey(autoGenerate = true)
    public int id;

    // timestamp
    @ColumnInfo(name = "timestamp")
    public long mTimestamp;
    // score
    @ColumnInfo(name = "score")
    public int mScore;

    /**
     * Constructor
     */
    public ScoreRecord(long timestamp, int score) {
        this.mTimestamp = timestamp;
        this.mScore = score;
    }

    public String getDate() {
        // timestamp to date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(mTimestamp);
    }

    public char[] getScore() {
        return String.valueOf(mScore).toCharArray();
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        return "ScoreRecord{" +
                "id=" + id +
                ", timestamp=" + mTimestamp +
                ", score=" + mScore +
                '}';
    }
}
