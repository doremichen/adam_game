/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the leaderboard view model that is bridget between the view and the data
 *
 * Author: Adam Chen
 * Date: 2025/10/08
 */
package com.adam.app.snake.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.adam.app.snake.data.entity.LeaderboardEntry;
import com.adam.app.snake.data.repository.LeaderboardRepository;

import java.util.List;

public class LeaderboardViewModel extends AndroidViewModel {
    // repository
    private final LeaderboardRepository mRepository;
    // list of top leaderboard entries live data
    private final LiveData<List<LeaderboardEntry>> mTopScores;

    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
        mRepository = new LeaderboardRepository(application);
        mTopScores = mRepository.getTopScores();
    }

    public LiveData<List<LeaderboardEntry>> getTopScores() {
        return mTopScores;
    }

    /**
     * addScore
     *     add score to database
     * @param name
     * @param score
     */
    public void addScore(String name, int score) {
        LeaderboardEntry entry = new LeaderboardEntry(name, score, System.currentTimeMillis());
        mRepository.insert(entry);
    }

    /**
     * clearAll
     *     clear all scores from database
     */
    public void clearAll() {
        mRepository.clearAll();
    }
}
