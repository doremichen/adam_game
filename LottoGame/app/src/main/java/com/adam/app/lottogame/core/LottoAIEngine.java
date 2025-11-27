/**
 * Copyright (C) 2025 Adam. All rights reserved.
 * <p>
 * This class is the lotto ai engine
 *
 * @Author: Adam Chen
 * @Date: 2025-11-26
 */
package com.adam.app.lottogame.core;

import com.adam.app.lottogame.repository.LottoHistoryRepository;
import com.adam.app.lottogame.strategy.LottoAIStrategy;
import com.adam.app.lottogame.trainer.StrategyEvaluationResult;
import com.adam.app.lottogame.trainer.StrategyTrainer;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LottoAIEngine {
    // trainer
    private final StrategyTrainer mTrainer;
    // repository
    private final LottoHistoryRepository mRepository;
    // max number
    private final int mMaxNumber;
    // strategy
    private LottoAIStrategy mCurrentStrategy;

    public LottoAIEngine(LottoHistoryRepository repository, int maxNumber) {
        this.mRepository = repository;
        this.mMaxNumber = maxNumber;
        this.mTrainer = new StrategyTrainer(repository, maxNumber);
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
