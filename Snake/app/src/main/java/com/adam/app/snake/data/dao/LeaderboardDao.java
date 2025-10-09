/**
 * Copyright 2015 the Adam Game
 *
 * Description: this class is the leaderboard dao class that is used to access the leaderboard data
 *
 * Author: Adam Chen
 * Date: 2025/10/08
 */
package com.adam.app.snake.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.adam.app.snake.data.entity.LeaderboardEntry;

import java.util.List;

@Dao
public interface LeaderboardDao {

    @Insert
    void insert(LeaderboardEntry entry);

    @Query("SELECT * FROM leaderboard ORDER BY score DESC")
    LiveData<List<LeaderboardEntry>> getTopScores();

    @Query("DELETE FROM leaderboard")
    void clearAll();

    @Delete
    void delete(LeaderboardEntry entry);
}
