/**
 * Copyright (c) 2026 LottoGame
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
