/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.infrastructure;

import com.adam.app.racinggame2d.domain.ILeaderboardRepository;
import com.adam.app.racinggame2d.domain.entity.LeaderboardRecord;
import com.adam.app.racinggame2d.infrastructure.database.LeaderboardDao;
import com.adam.app.racinggame2d.infrastructure.database.LeaderboardEntity;
import com.adam.app.racinggame2d.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Infrastructure implementation of the leaderboard repository.
 */
@Singleton
public final class LeaderboardRepositoryImpl implements ILeaderboardRepository {
    private static final String sTAG = "LeaderboardRepo";
    private final LeaderboardDao mLeaderboardDao;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    @Inject
    public LeaderboardRepositoryImpl(LeaderboardDao dao) {
        this.mLeaderboardDao = dao;
    }

    @Override
    public void addScore(LeaderboardRecord record) {
        Utils.logDebug(sTAG, "addScore: " + record.getPlayerName());
        mExecutor.execute(() -> {
            try {
                long timestamp = Long.parseLong(record.getTimestamp());
                mLeaderboardDao.insert(new LeaderboardEntity(record.getPlayerName(), record.getScore(), timestamp));
            } catch (NumberFormatException e) {
                mLeaderboardDao.insert(new LeaderboardEntity(record.getPlayerName(), record.getScore(), System.currentTimeMillis()));
            }
        });
    }

    @Override
    public void getAllScores(Callback callback) {
        Utils.logDebug(sTAG, "getAllScores");
        if (callback == null) return;
        mExecutor.execute(() -> {
            List<LeaderboardEntity> entities = mLeaderboardDao.getAllScores();
            List<LeaderboardRecord> records = new ArrayList<>();
            for (LeaderboardEntity entity : entities) {
                records.add(new LeaderboardRecord(
                        entity.getId(),
                        entity.getPlayerName(),
                        entity.getScore(),
                        String.valueOf(entity.getTimestamp())
                ));
            }
            callback.onResult(records);
        });
    }
}
