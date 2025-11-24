/**
 * This class is the flappy bird game repository for database.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-21
 */
package com.adam.app.flappybird.data.repository;

import android.content.Context;

import com.adam.app.flappybird.data.FlappyBird;
import com.adam.app.flappybird.data.FlappyBirdDao;
import com.adam.app.flappybird.data.FlappyBirdDatabase;
import com.adam.app.flappybird.util.GameUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FlappyBirdRepository {
    // TAG
    private static final String TAG = "FlappyBirdRepository";

    // Dao
    private final FlappyBirdDao mDao;
    // executor service
    private final ExecutorService mExecutorService;
    private Future<?> mFuture;

    // Constructor
    public FlappyBirdRepository(Context context) {
        mDao = FlappyBirdDatabase.getDatabase(context).flappyBirdDao();
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Add score to database.
     * @param score Score to add.
     */
    public void addScore(int score) {
        GameUtil.info(TAG, "addScore: " + score);
        String scoreString = String.valueOf(score);
        FlappyBird flappyBird = new FlappyBird(scoreString, GameUtil.formateDate(System.currentTimeMillis()));
        mFuture = mExecutorService.submit(() -> mDao.insert(flappyBird));
    }

    /**
     * Get scores from database.
     * @param listener Listener for getting scores.
     */
    public void getScores(OnGetScoresListener listener) {
        if (listener == null) {
            return;
        }
        mFuture = mExecutorService.submit(() -> {
            List<FlappyBird> scores = mDao.getAll();
            listener.onGetScores(scores);
        });

    }

    /**
     * Listener for getting scores.
     */
    public interface OnGetScoresListener {
        void onGetScores(List<FlappyBird> scores);
    }

}
