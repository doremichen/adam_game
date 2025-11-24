/**
 * This class is the flappy bird game entity for database.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-21
 */
package com.adam.app.flappybird.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FlappyBird")
public class FlappyBird {
    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "score")
    private String mScore;
    @ColumnInfo(name = "date")
    private String mDate;

    /**
     * Constructor with parameters.
     *
     */
    public FlappyBird(String score, String date) {
        this.mScore = score;
        this.mDate = date;
    }

    // --- setter/getter ---
    public int getId() {
        return mId;
    }
    public void setId(int id) {
        this.mId = id;
    }
    public String getScore() {
        return mScore;
    }
    public void setScore(String score) {
        this.mScore = score;
    }
    public String getDate() {
        return mDate;
    }
    public void setDate(String date) {
        this.mDate = date;
    }
}
