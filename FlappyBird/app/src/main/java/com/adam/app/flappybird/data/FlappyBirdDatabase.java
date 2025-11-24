/**
 * This class is the flappy bird game database.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-21
 */
package com.adam.app.flappybird.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FlappyBird.class}, version = 1, exportSchema = false)
public abstract class FlappyBirdDatabase extends RoomDatabase {
    private static volatile FlappyBirdDatabase sInstance;

    public abstract FlappyBirdDao flappyBirdDao();

    public static FlappyBirdDatabase getDatabase(final Context context) {
        if (sInstance == null) {
            synchronized (FlappyBirdDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            FlappyBirdDatabase.class,
                            "flappy_bird_database").build();
                }

            }
        }
        return sInstance;
    }
}
