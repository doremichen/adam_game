/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the data access object for the leaderboard.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/11/05
 */
package com.adam.app.racinggame2d.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LeaderboardDao {
    @Insert
    void insert(LeaderboardEntity entity);

    @Query("SELECT * FROM leaderboard ORDER BY score DESC")
    List<LeaderboardEntity> getAllScores();
}
