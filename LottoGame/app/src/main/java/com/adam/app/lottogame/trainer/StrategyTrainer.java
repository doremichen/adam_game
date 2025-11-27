/**
 * Copyright (C) 2025 Adam. All rights reserved.
 * <p>
 * This class is used to provide the evaluation strategy
 *
 * @Author: Adam Chen
 * @Date: 2025-11-24
 */
package com.adam.app.lottogame.trainer;

import com.adam.app.lottogame.data.entity.LottoHistoryEntity;
import com.adam.app.lottogame.repository.LottoHistoryRepository;
import com.adam.app.lottogame.strategy.LottoAIStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class StrategyTrainer {

    private final LottoHistoryRepository mRepository;
    private final int mMaxNumber;


    /**
     * constructor
     */
    public StrategyTrainer(LottoHistoryRepository repository, int maxNumber) {
        mRepository = repository;
        mMaxNumber = maxNumber;
    }

    /**
     * Evaluate a single strategy (incremental evaluation).
     *
     * @param strategy the LottoAIStrategy (enum) to evaluate
     * @param pickCount how many numbers strategy picks (eg. 6)
     * @param trialsPerEntry number of repeats per historical draw (for stochastic strategies)
     * @return average hits per draw (double)
     */
    public double evaluateStrategyIncremental(LottoAIStrategy strategy, int pickCount, int trialsPerEntry) {
        // get all lotto game history form database
        Future<List<LottoHistoryEntity>> future = mRepository.getAll();
        List<LottoHistoryEntity> list = null;
        try {
            list = future.get();  // synchronization
        } catch (Exception e) {
            e.printStackTrace();
        }

        // check list is not null and not empty
        if (list == null || list.isEmpty()) {
            return 0.0;
        }

        // totalHits, totalTrials
        long totalHits = 0L;
        long totalTrials = 0L;

        // incremental: for each draw i, let strategy see draws [0..i-1]
        for (int i = 0; i < list.size(); i++) {
            LottoHistoryEntity current = list.get(i);
            List<Integer> groundTruth = current.getNumbers() == null ? Collections.emptyList() : current.getNumbers();

            // priorHistoryNumbers = flatten numbers of previous draws
            List<Integer> prior = flattenHistoryUpTo(list, i); // up to exclude i
            // if prior empty, use entire history as fallback (optional); here we fallback to repository.getAllNumbers()
            if (prior.isEmpty()) {
                Future<List<Integer>> allNumbersFuture = mRepository.getAllNumbers();
                try {
                    prior = allNumbersFuture.get();  // synchronization
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (prior == null || prior.isEmpty()) {
                    return 0.0;
                }
            }

            for (int t = 0; t < Math.max(1, trialsPerEntry); t++) {
                List<Integer> pickNumber = LottoAIStrategy.pick(strategy, prior, pickCount, mMaxNumber);
                int hits = countMatch(pickNumber, groundTruth);
                totalHits += hits;
                totalTrials++;
            }

            if (totalTrials == 0) {
                return 0.0;
            }

            // average hits per draw across all trials
            double retValue = (double) totalHits / (double) (totalTrials);
            return retValue;
        }

        return 0.0;


//        mRepository.getAll(new LottoHistoryRepository.ResultCallback<List<LottoHistoryEntity>>() {
//            @Override
//            public void onResult(List<LottoHistoryEntity> result) {
//                if (result == null || result.isEmpty()) {
//                    callback.onComplete(0.0); // 如果沒有歷史紀錄，直接回傳 0.0
//                    return;
//                }
//
//                long totalHits = 0L;
//                long totalTrials = 0L;
//
//                // incremental: for each draw i, let strategy see draws [0..i-1]
//                for (int i = 0; i < result.size(); i++) {
//                    LottoHistoryEntity current = result.get(i);
//                    List<Integer> groundTruth = current.getNumbers() == null ? Collections.emptyList() : current.getNumbers();
//
//                    // priorHistoryNumbers = flatten numbers of previous draws
//                    List<Integer> prior = flattenHistoryUpTo(result, i); // up to exclude i
//                    // if prior empty, use entire history as fallback (optional); here we fallback to repository.getAllNumbers()
////                    if (prior.isEmpty()) {
////                        // 注意：getAllNumbers() 可能是另一個非同步操作。
////                        // 為了簡單起見，這裡假設它是一個同步方法。
////                        // 如果它也是非同步的，需要更複雜的處理，但對於目前的問題，這樣修改是合理的。
////                        prior = mRepository.getAllNumbersEx();
////                    }
//
//                    for (int t = 0; t < Math.max(1, trialsPerEntry); t++) {
//                        List<Integer> pickNumber = LottoAIStrategy.pick(strategy, prior, pickCount, mMaxNumber);
//                        int hits = countMatch(pickNumber, groundTruth);
//                        totalHits += hits;
//                        totalTrials++;
//                    }
//                }
//
//                if (totalTrials == 0) {
//                    callback.onComplete(0.0);
//                    return;
//                }
//                // average hits per draw across all trials
//                double retValue = (double) totalHits / (double) (totalTrials);
//                callback.onComplete(retValue); // 透過回呼傳遞結果
//            }
//        }); // ascending by draw_id expected
    }

    /**
     * Evaluate all strategies (LottoAIStrategy.values()) and return sorted list (best first).
     */
    public void evaluateAllStrategiesIncremental(int pickCount, int trialsPerEntry, AllStrategiesCallback allStrategiesCallback) {
        List<StrategyEvaluationResult> results = Collections.synchronizedList(new ArrayList<>());
        LottoAIStrategy[] strategies = LottoAIStrategy.values();
        AtomicInteger counter = new AtomicInteger(strategies.length);

        if (strategies.length == 0) {
            allStrategiesCallback.onComplete(new ArrayList<>());
            return;
        }

        for (LottoAIStrategy s : strategies) {
            double hits = evaluateStrategyIncremental(s, pickCount, trialsPerEntry);
            results.add(new StrategyEvaluationResult(s, hits, pickCount, trialsPerEntry));
            // notify callback when all strategies are evaluated
            if (counter.decrementAndGet() == 0) {
                // sort by averageHits desc
                Collections.sort(results, (a, b) -> Double.compare(b.getAverageHits(), a.getAverageHits()));
                allStrategiesCallback.onComplete(results);
            }
        }
    }

    /**
     * helper - flatten history numbers up to index (exclusive)
     */
    private List<Integer> flattenHistoryUpTo(List<LottoHistoryEntity> all, int exclusiveIndex) {
        List<Integer> flat = new ArrayList<>();
        for (int j = 0; j < exclusiveIndex; j++) {
            LottoHistoryEntity e = all.get(j);
            if (e.getNumbers() != null) flat.addAll(e.getNumbers());
        }
        return flat;
    }

    /**
     * helper - count matches between pick and winning numbers
     */
    private int countMatch(List<Integer> pick, List<Integer> winning) {
        if (pick == null || winning == null) return 0;
        int c = 0;
        for (int n : pick) {
            if (winning.contains(n)) c++;
        }
        return c;
    }

    public interface EvaluationCallback {
        void onComplete(double averageHits);
    }

    public interface AllStrategiesCallback {
        void onComplete(List<StrategyEvaluationResult> results);
    }
}
