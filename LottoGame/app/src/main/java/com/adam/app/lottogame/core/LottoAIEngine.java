/**
 * Copyright (c) 2026 LottoGame
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
package com.adam.app.lottogame.core;

import com.adam.app.lottogame.repository.LottoHistoryRepository;
import com.adam.app.lottogame.strategy.LottoAIStrategy;
import com.adam.app.lottogame.trainer.StrategyEvaluationResult;
import com.adam.app.lottogame.trainer.StrategyTrainer;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LottoAIEngine {
    // trainer
    private final StrategyTrainer mTrainer;
    // repository
    private final LottoHistoryRepository mRepository;
    // max number
    private final int mMaxNumber = 49;
    // strategy
    private LottoAIStrategy mCurrentStrategy;

    @Inject
    public LottoAIEngine(LottoHistoryRepository repository) {
        this.mRepository = repository;
        this.mTrainer = new StrategyTrainer(repository, mMaxNumber);
        this.mCurrentStrategy = LottoAIStrategy.RANDOM_PICK;
    }


    /**
     * set strategy
     */
    public void setStrategy(LottoAIStrategy strategy) {
        mCurrentStrategy = strategy;
    }

    /**
     * trainBestStrategy
     * Automated training & finding the best AI
     * @return StrategyEvaluationResult
     */
    public void trainBestStrategy(int pickCount, int trialsPerEntry) {
        // list of all strategies
        mTrainer.evaluateAllStrategiesIncremental(pickCount, trialsPerEntry, new StrategyTrainer.AllStrategiesCallback() {
            @Override
            public void onComplete(List<StrategyEvaluationResult> results) {
                if (!results.isEmpty()) {
                    mCurrentStrategy = results.get(0).getStrategy();
                }
            }
        });
    }

    /**
     * select numbers
     * @param pickCount pick count
     * @return selected numbers
     */
    public List<Integer> selectNumbers(int pickCount) {
        Future<List<Integer>> future = mRepository.getAllNumbers();
        List<Integer> history = null;
        try {
            history = future.get();  // synchronization
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return LottoAIStrategy.pick(
                mCurrentStrategy,
                (history==null)?List.of():history,
                pickCount,
                mMaxNumber
        );
    }

    /**
     * get current strategy
     * @return current strategy
     */
    public LottoAIStrategy getCurrentStrategy() {
        return mCurrentStrategy;
    }


}
