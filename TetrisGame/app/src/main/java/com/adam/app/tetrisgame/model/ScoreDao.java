/**
 * Description: This class is the score dao.
 * Author: Adam Chen
 * Date: 2025/08/15
 */
package com.adam.app.tetrisgame.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {

    @Insert
    void insert(ScoreRecord score);

    @Query("SELECT * FROM score_table ORDER BY score DESC LIMIT 100")
    List<ScoreRecord> getTopScores();

    @Query("SELECT COUNT(*) FROM score_table")
    int getCount();

    @Query("DELETE FROM score_table WHERE id IN (SELECT id FROM score_table ORDER BY score ASC LIMIT :excess)")
    void deleteLowestScores(int excess);
}

