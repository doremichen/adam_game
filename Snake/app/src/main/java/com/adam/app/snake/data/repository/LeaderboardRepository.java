/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.adam.app.snake.data.dao.LeaderboardDao;
import com.adam.app.snake.data.database.AppDatabase;
import com.adam.app.snake.data.entity.LeaderboardEntry;
import com.adam.app.snake.domain.repository.ILeaderboardRepository;
import com.adam.app.snake.util.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * LeaderboardRepository implementation
 */
@Singleton
public class LeaderboardRepository implements ILeaderboardRepository {
    // Dao
    private final LeaderboardDao mLeaderboardDao;
    // Executors
    private final AppExecutors mExecutors;

    @Inject
    public LeaderboardRepository(@ApplicationContext Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        mLeaderboardDao = database.leaderboardDao();
        mExecutors = AppExecutors.getInstance();
    }

    @Override
    public LiveData<List<LeaderboardEntry>> getTopScores() {
        return mLeaderboardDao.getTopScores();
    }

    @Override
    public void insert(LeaderboardEntry entry) {
        mExecutors.execute(AppExecutors.Type.DiskIO, () -> mLeaderboardDao.insert(entry));
    }

    @Override
    public void clearAll() {
        mExecutors.execute(AppExecutors.Type.DiskIO, mLeaderboardDao::clearAll);
    }
}
