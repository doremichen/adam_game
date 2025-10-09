/**
 * Copyright 2015 the Adam Game
 *
 * Description: this class is the leaderboard repository class that is used to store the leaderboard data
 *
 * Author: Adam Chen
 * Date: 2025/10/08
 */
package com.adam.app.snake.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.adam.app.snake.data.dao.LeaderboardDao;
import com.adam.app.snake.data.database.AppDatabase;
import com.adam.app.snake.data.entity.LeaderboardEntry;
import com.adam.app.snake.util.AppExecutors;

import java.util.List;

public class LeaderboardRepository {
    // Dao
    private final LeaderboardDao mLeaderboardDao;
    // Executors
    private final AppExecutors mExecutors;

    public LeaderboardRepository(Application context) {
        AppDatabase database = AppDatabase.getInstance(context);
        mLeaderboardDao = database.leaderboardDao();
        mExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<LeaderboardEntry>> getTopScores() {
        return mLeaderboardDao.getTopScores();
    }

    public void insert(LeaderboardEntry entry) {
        mExecutors.execute(AppExecutors.Type.DiskIO, () -> mLeaderboardDao.insert(entry));
    }

    public void clearAll() {
        mExecutors.execute(AppExecutors.Type.DiskIO, mLeaderboardDao::clearAll);
    }
}
