/**
 * Copyright (C) 2025 Adam. All rights reserved.
 *
 * This class is used to evaluate the strategy.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-24
 */
package com.adam.app.lottogame.trainer;

import com.adam.app.lottogame.strategy.LottoAIStrategy;

public class StrategyEvaluationResult {

    private final LottoAIStrategy mStrategy;
    private final double mAverageHits;
    private final int mPickCount;
    private final int mTrailsEntry;

    public StrategyEvaluationResult(LottoAIStrategy strategy, double averageHits, int pickCount, int trailsEntry) {
        mStrategy = strategy;
        mAverageHits = averageHits;
        mPickCount = pickCount;
        mTrailsEntry = trailsEntry;
    }

    public LottoAIStrategy getStrategy() {
        return mStrategy;
    }

    public double getAverageHits() {
        return mAverageHits;
    }

    public int getPickCount() {
        return mPickCount;
    }

    public int getTrailsEntry() {
        return mTrailsEntry;
    }
}
