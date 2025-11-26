/**
 * Copyright (C) 2025 Adam. All rights reserved.
 *
 * This class is the database of lotto.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-26
 */
package com.adam.app.lottogame.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.adam.app.lottogame.data.dao.ILottoHistoryDao;
import com.adam.app.lottogame.data.entity.LottoHistoryEntity;
import com.adam.app.lottogame.data.util.Converters;

@Database(entities = {LottoHistoryEntity.class}, version = LottoDatabase.DATABASE_VERSION, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class LottoDatabase extends RoomDatabase {
    public abstract ILottoHistoryDao lottoHistoryDao();
    public static final String DATABASE_NAME = "lotto.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_LOTTO_HISTORY = "lotto_history";

    private static volatile LottoDatabase INSTANCE;

    public static LottoDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LottoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(),
                                    LottoDatabase.class,
                                    DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
