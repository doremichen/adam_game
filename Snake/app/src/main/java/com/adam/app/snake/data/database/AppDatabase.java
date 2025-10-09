/**
 * Copyright 2015 the Adam Game
 *
 * Description: this class is the database class that is used to store the leaderboard data
 *
 * Author: Adam Chen
 * Date: 2025/10/08
 */
package com.adam.app.snake.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.adam.app.snake.data.dao.LeaderboardDao;
import com.adam.app.snake.data.entity.LeaderboardEntry;

@Database(entities = {LeaderboardEntry.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private volatile static AppDatabase INSTANCE;

    public abstract LeaderboardDao leaderboardDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "snake_game_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
