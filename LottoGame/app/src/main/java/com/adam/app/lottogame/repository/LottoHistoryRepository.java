/**
 * Copyright (C) 2025 Adam. All rights reserved.
 * <p>
 * This class is the converter of lotto history.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-26
 */
package com.adam.app.lottogame.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.adam.app.lottogame.Utils;
import com.adam.app.lottogame.data.dao.ILottoHistoryDao;
import com.adam.app.lottogame.data.database.LottoDatabase;
import com.adam.app.lottogame.data.entity.LottoHistoryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LottoHistoryRepository {

    private static final String TAG = LottoHistoryRepository.class.getSimpleName();

    private final ExecutorService mExecutor;
    private final ILottoHistoryDao mDao;
    private final Handler mHandler;

    public LottoHistoryRepository(Context context) {
        mExecutor = Executors.newSingleThreadExecutor();
        mDao = LottoDatabase.getInstance(context).lottoHistoryDao();
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * shutdown
     */
    public void shutdown() {
        mExecutor.shutdown();
    }

    /**
     * add lotto history to database
     *
     * @param entity entity
     */
    public void add(LottoHistoryEntity entity) {
        mExecutor.execute(() -> mDao.upsert(entity));
    }

    /**
     * add all lotto history to database
     *
     * @param list list
     */
    public void addAll(List<LottoHistoryEntity> list) {
        mExecutor.execute(() -> mDao.insertAll(list));
    }

    /**
     * get latest LottoHistoryEntity
     */
    public void getLatest(ResultCallback<LottoHistoryEntity> callback) {
        mExecutor.execute(() -> {
            try {
                LottoHistoryEntity entity = mDao.getLatest();
                mHandler.post(() -> callback.onResult(entity));
            } catch (Exception e) {
                mHandler.post(() -> callback.onError(e));
            }
        });
    }

    /**
     * get all LottoHistoryEntity
     * List<LottoHistoryEntity>
     */
    public Future<List<LottoHistoryEntity>> getAll() {
        return mExecutor.submit(() -> {
            try {
                List<LottoHistoryEntity> list = mDao.getAll();
                Utils.dumpList(list);
                return list;
            } catch (Exception e) {
                Utils.log(TAG, "getAll: " + e.getMessage());
            }
            return null;
        });
    }

    /**
     * get all numbers
     *
     * @return all numbers
     */
    public Future<List<Integer>> getAllNumbers() {
        return mExecutor.submit(() -> {
            try {
                List<Integer> out = new ArrayList<>();
                List<LottoHistoryEntity> list = mDao.getAll();
                Utils.dumpList(list);
                if (list == null) {
                    Utils.log(TAG, "list is null");
                    return out;
                }
                for (LottoHistoryEntity e : list) {
                    out.addAll(e.getNumbers());
                }
                return out;
            } catch (Exception e) {
                Utils.log(TAG, "getAllNumbers: " + e.getMessage());
            }
            return null;
        });
    }

    /**
     * interface ResultCallback
     * <p>
     * Result callback
     */
    public interface ResultCallback<T> {
        void onResult(T result);
        default void onError(Exception e) {
            //TODO: log error
        }
    }

}
