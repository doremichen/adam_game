/**
 * Description: This class is the repository that handle the data accessed and
 * background thread execution.
 *
 * Author: Adam Chen
 * Date: 2025/08/18
 */
package com.adam.app.tetrisgame.data;

import android.content.Context;
import android.os.Handler;

import com.adam.app.tetrisgame.model.ScoreDao;
import com.adam.app.tetrisgame.model.ScoreRecord;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScoreRepository {
    public static final long DELAY_DEAFULT_TIME = 1000L;
    // score dao
    private final ScoreDao mScoreDao;
    // Executor service
    private final ExecutorService mExecutorService;
    // Handler
    private final Handler mHandler;

    /**
     * Constructor
     */
    public ScoreRepository(Context context) {
        final ScoreDatabase db = ScoreDatabase.getDatabase(context);
        mScoreDao = db.scoreDao();
        mExecutorService = Executors.newSingleThreadExecutor();
        mHandler = new Handler(context.getMainLooper());
    }

    /**
     * getTopScores
     */
    public void getTopScores(RepositoryCallback<List<ScoreRecord>> callback) {
        getTopScores(DELAY_DEAFULT_TIME, callback);
    }

    /**
     * getTopScores
     * @param delayTime delay time
     * @param callback callback
     */
    public void getTopScores(long delayTime, RepositoryCallback<List<ScoreRecord>> callback) {
        mExecutorService.execute(() -> {
            final List<ScoreRecord> scoreList = mScoreDao.getTopScores();
            mHandler.postDelayed(() -> callback.onComplete(scoreList), delayTime);
        });
    }




    /**
     * insert score
     * @param score score record
     * @param callback callback
     */
    public void insertScore(ScoreRecord score, RepositoryCallback<Void> callback) {
        mExecutorService.execute(() -> {
            mScoreDao.insert(score);
            mHandler.post(() -> callback.onComplete(null));
        });
    }

    /**
     * getCount
     * @param callback callback
     */
    public void getCount(RepositoryCallback<Integer> callback) {
        mExecutorService.execute(() -> {
            final int count = mScoreDao.getCount();
            mHandler.post(() -> callback.onComplete(count));
        });
    }

    /**
     * deleteLowestScores
     * @param excess excess
     * @param callback callback
     */
    public void deleteLowestScores(int excess, RepositoryCallback<Void> callback) {
        mExecutorService.execute(() -> {
            mScoreDao.deleteLowestScores(excess);
            mHandler.post(() -> callback.onComplete(null));
        });
    }


    // callback interface
    public interface RepositoryCallback<T> {
        void onComplete(T result);
    }

}
