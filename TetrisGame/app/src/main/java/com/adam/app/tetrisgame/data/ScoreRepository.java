/*
 * Copyright (c) 2025 Adam
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


    /**
     * saveScoreAndPrune
     * @param score score record
     * @param maxNum max num
     * @param reserveNum reserve num
     * @param callback callback
     */
    public void saveScoreAndPrune(ScoreRecord score, int maxNum, int reserveNum, RepositoryCallback<Void> callback) {
        mExecutorService.execute(() -> {
            int count = mScoreDao.getCount();
            if (count >= maxNum) {
                mScoreDao.deleteLowestScores(count - reserveNum + 1);
            }
            mScoreDao.insert(score);
            if (callback != null) {
                mHandler.post(() -> callback.onComplete(null));
            }
        });
    }

    // callback interface
    public interface RepositoryCallback<T> {
        void onComplete(T result);
    }

}
