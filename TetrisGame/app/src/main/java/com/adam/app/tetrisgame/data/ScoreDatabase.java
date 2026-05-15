/*
 * Copyright (c) 2025 Adam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
                            .addCallback(sCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
