/**
 * Description: This is the score database.
 * Author: Adam Chen
 * Date: 2025/08/15
 */
package com.adam.app.tetrisgame.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.adam.app.tetrisgame.Utils;
import com.adam.app.tetrisgame.model.ScoreDao;
import com.adam.app.tetrisgame.model.ScoreRecord;


@Database(entities = {ScoreRecord.class}, version = 1, exportSchema = false)
public abstract class ScoreDatabase extends RoomDatabase {

    private static Callback sCallback = new Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Log
            Utils.log("ScoreDatabase onCreate");
        }
    };

    // abstract Score dao
    public abstract ScoreDao scoreDao();

    // singleton
    private static volatile ScoreDatabase INSTANCE;
    public static ScoreDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ScoreDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ScoreDatabase.class, "score_database")
                            .allowMainThreadQueries()
                            .addCallback(sCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
