/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the database for the leaderboard.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/11/05
 */
package com.adam.app.racinggame2d.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LeaderboardEntity.class}, version = 1, exportSchema = true)
public abstract class LeaderboardDatabase extends RoomDatabase {
    private static volatile LeaderboardDatabase INSTANCE;

    public static LeaderboardDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (LeaderboardDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            LeaderboardDatabase.class,
                            "leaderboard_database").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract LeaderboardDao leaderboardDao();
}
