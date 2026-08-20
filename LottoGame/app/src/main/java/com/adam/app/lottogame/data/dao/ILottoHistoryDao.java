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
package com.adam.app.lottogame.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.adam.app.lottogame.data.entity.LottoHistoryEntity;

import java.util.List;

@Dao
public interface ILottoHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(LottoHistoryEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LottoHistoryEntity> list);

    @Query("SELECT * FROM lotto_history ORDER BY id DESC LIMIT 1")
    LottoHistoryEntity getLatest();

    @Query("SELECT * FROM lotto_history ORDER BY draw_id DESC")
    List<LottoHistoryEntity> getAll();

    @Query("SELECT * FROM lotto_history WHERE draw_id = :drawId LIMIT 1")
    LottoHistoryEntity findByDrawId(int drawId);

    @Query("SELECT COUNT(*) FROM lotto_history")
    int getCount();
}
