/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the repository for the leaderboard.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/11/05
 */
package com.adam.app.racinggame2d.model.repository;

import android.content.Context;

import com.adam.app.racinggame2d.data.LeaderboardDao;
import com.adam.app.racinggame2d.data.LeaderboardDatabase;
import com.adam.app.racinggame2d.data.LeaderboardEntity;
import com.adam.app.racinggame2d.util.GameUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LeaderboardRepository {
    // TAG
    private static final String TAG = LeaderboardRepository.class.getSimpleName();

    // leaderboard dao
    private final LeaderboardDao mLeaderboardDao;
    private final ExecutorService mService = Executors.newSingleThreadExecutor();


    /**
     * Constructor
     *
     * @param context Context
     */
    public LeaderboardRepository(Context context) {
        LeaderboardDatabase db = LeaderboardDatabase.getDatabase(context);
        mLeaderboardDao = db.leaderboardDao();
    }

    /**
     * insert
     * insert leaderboard entity
     *
     * @param leaderboard LeaderboardEntity
     */
    public void addScore(LeaderboardEntity leaderboard) {
        GameUtil.log(TAG, "insert");
        mService.execute(() -> mLeaderboardDao.insert(leaderboard));
    }

    /**
     * getAllScores
     * get all scores
     *
     * @param callback Callback
     */
    public void getAllScores(Callback callback) {
        GameUtil.log(TAG, "getAllScores");
        if (callback == null) {
            return;
        }
        mService.execute(() -> {
            List<LeaderboardEntity> list = mLeaderboardDao.getAllScores();
            callback.onResult(list);
        });
    }


    /**
     * interface Callback
     */
    public interface Callback {
        void onResult(List<LeaderboardEntity> list);
    }


}
