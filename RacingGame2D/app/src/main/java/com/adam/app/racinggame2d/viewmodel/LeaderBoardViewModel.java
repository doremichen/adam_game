/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the view model of leaderboard Activity.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/29
 */
package com.adam.app.racinggame2d.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.racinggame2d.data.LeaderboardEntity;
import com.adam.app.racinggame2d.model.repository.LeaderboardRepository;
import com.adam.app.racinggame2d.util.GameUtil;

import java.util.List;

public class LeaderBoardViewModel extends AndroidViewModel {
    private static final String TAG = "LeaderBoardViewModel";
    private static final boolean IS_DEBUG = true;

    // repository
    private final LeaderboardRepository mRepository;

    // all scores live data
    private final MutableLiveData<List<LeaderboardEntity>> mScoresLiveData = new MutableLiveData<>();

    public LeaderBoardViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = new LeaderboardRepository(application);
    }

    public MutableLiveData<List<LeaderboardEntity>> getScoresLiveData() {
        return mScoresLiveData;
    }

    /**
     * Get all scores from database
     */
    public void loadScores() {
        info("loadScores");
        // update live data
        mRepository.getAllScores(mScoresLiveData::postValue);
    }

    /**
     * Add score to database
     *
     * @param name
     * @param score
     */
    public void addScore(String name, int score) {
        info("addScore");
        LeaderboardEntity entity = new LeaderboardEntity(name, score, System.currentTimeMillis());
        mRepository.addScore(entity);
    }

    private static void info(String msg) {
        if (IS_DEBUG) {
            GameUtil.log(TAG, msg);
        }
    }
}
