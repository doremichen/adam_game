/*
 * Copyright (c) 2026 Adam Chen
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

package com.adam.app.galaga.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.adam.app.galaga.data.local.AppDatabase;
import com.adam.app.galaga.data.local.dao.ScoreDao;
import com.adam.app.galaga.data.local.entities.ScoreRecord;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameRepository {
    // TAG
    private static final String TAG = GameRepository.class.getSimpleName();
    // Executor service
    private final ExecutorService mExecutorService = Executors.newFixedThreadPool(2);
    // Dao
    private final ScoreDao mScoreDao;

    private static volatile GameRepository sInstance;

    private GameRepository(Application app) {
        AppDatabase database = AppDatabase.getInstance(app);
        mScoreDao = database.scoreDao();
    }

    /**
     * Singleton
     */
    public static GameRepository getInstance(Application app) {
        if (sInstance == null) {
            synchronized (GameRepository.class) {
                if (sInstance == null) {
                    sInstance = new GameRepository(app);
                }
            }
        }

        return sInstance;
    }




    /**
     * insertScore
     * @param record record
     */
    public void insertScore(ScoreRecord record) {
        mExecutorService.execute(() -> mScoreDao.insertScore(record));
    }


    /**
     * getTop10Scores
     * @return top 10 scores
     */
    public LiveData<List<ScoreRecord>> getTop10Scores() {
        return mScoreDao.getTop10Scores();
    }

}
