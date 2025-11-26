/**
 * Copyright (C) 2025 Adam. All rights reserved.
 *
 * This class is the converter of lotto history.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-26
 */
package com.adam.app.lottogame.repository;

import android.content.Context;

import com.adam.app.lottogame.data.dao.ILottoHistoryDao;
import com.adam.app.lottogame.data.database.LottoDatabase;
import com.adam.app.lottogame.data.entity.LottoHistoryEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LottoHistoryRepository {

    private final Executor mExecutor;
    private final ILottoHistoryDao mDao;

    public LottoHistoryRepository(Context context) {
        mExecutor = Executors.newSingleThreadExecutor();
        mDao = LottoDatabase.getInstance(context).lottoHistoryDao();
    }

    /**
     * add lotto history to database
     * @param entity entity
     */
    public void add(LottoHistoryEntity entity) {
        mExecutor.execute(() -> mDao.upsert(entity));
    }

    /**
     * get latest LottoHistoryEntity
     */
    public LottoHistoryEntity getLatest() {
        return mDao.getLatest();
    }

    /**
     * get all LottoHistoryEntity
     */
    public List<LottoHistoryEntity> getAll() {
        return mDao.getAll();
    }

}
