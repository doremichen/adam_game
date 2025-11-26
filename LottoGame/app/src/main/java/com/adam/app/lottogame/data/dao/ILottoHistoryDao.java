/**
 * Copyright (C) 2025 Adam. All rights reserved.
 *
 * This class is the dao of lotto history.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-26
 */
package com.adam.app.lottogame.data.dao;

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

    @Query("SELECT * FROM lotto_history ORDER BY id DESC LIMIT 1")
    LottoHistoryEntity getLatest();

    @Query("SELECT * FROM lotto_history ORDER BY draw_id DESC")
    List<LottoHistoryEntity> getAll();

    @Query("SELECT * FROM lotto_history WHERE draw_id = :drawId LIMIT 1")
    LottoHistoryEntity findByDrawId(int drawId);

    @Query("SELECT COUNT(*) FROM lotto_history")
    int getCount();
}
