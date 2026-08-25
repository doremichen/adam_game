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
package com.adam.app.tetrisgame.domain.usecase;

import com.adam.app.tetrisgame.data.local.ScoreRecord;
import com.adam.app.tetrisgame.domain.bridge.ScoreBridge;
import com.adam.app.tetrisgame.domain.repository.ScoreRepository;
import com.adam.app.tetrisgame.util.Constants;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ScoreUseCase {
    public enum Action {
        GET_TOP_SCORES,
        SAVE_SCORE
    }

    private final ScoreBridge mBridge;

    @Inject
    public ScoreUseCase(ScoreBridge bridge) {
        this.mBridge = bridge;
    }

    @SuppressWarnings("unchecked")
    public void execute(Action action, Object param, ScoreRepository.RepositoryCallback<?> callback) {
        switch (action) {
            case GET_TOP_SCORES:
                mBridge.getTopScores((ScoreRepository.RepositoryCallback<List<ScoreRecord>>) callback);
                break;
            case SAVE_SCORE:
                int score = (int) param;
                ScoreRecord record = new ScoreRecord(System.currentTimeMillis(), score);
                mBridge.saveScoreAndPrune(record, Constants.MAX_SCORE_RECORDS, Constants.RESERVE_SCORE_RECORDS, (ScoreRepository.RepositoryCallback<Void>) callback);
                break;
        }
    }
}
