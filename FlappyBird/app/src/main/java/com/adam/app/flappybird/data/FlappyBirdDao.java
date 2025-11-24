/**
 * This class is the flappy bird game dao for database.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-21
 */
package com.adam.app.flappybird.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FlappyBirdDao {
    @Query("SELECT * FROM FlappyBird ORDER BY score DESC")
    List<FlappyBird> getAll();

    @Insert
    void insert(FlappyBird flappyBird);
}
