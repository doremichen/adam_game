/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.data.repository;

import android.os.Handler;
import android.os.Looper;
import com.adam.app.tetrisgame.data.local.ScoreDao;
import com.adam.app.tetrisgame.data.local.ScoreRecord;
import com.adam.app.tetrisgame.domain.repository.ScoreRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;

public class ScoreRepositoryImpl implements ScoreRepository {
    private final ScoreDao mScoreDao;
    private final ExecutorService mExecutorService;
    private final Handler mMainHandler;

    @Inject
    public ScoreRepositoryImpl(ScoreDao scoreDao) {
        this.mScoreDao = scoreDao;
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.mMainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void getTopScores(RepositoryCallback<List<ScoreRecord>> callback) {
        mExecutorService.execute(() -> {
            List<ScoreRecord> scores = mScoreDao.getTopScores();
            mMainHandler.post(() -> callback.onComplete(scores));
        });
    }

    @Override
    public void saveScoreAndPrune(ScoreRecord score, int maxNum, int reserveNum, RepositoryCallback<Void> callback) {
        mExecutorService.execute(() -> {
            int count = mScoreDao.getCount();
            if (count >= maxNum) {
                mScoreDao.deleteLowestScores(count - reserveNum + 1);
            }
            mScoreDao.insert(score);
            if (callback != null) {
                mMainHandler.post(() -> callback.onComplete(null));
            }
        });
    }
}
