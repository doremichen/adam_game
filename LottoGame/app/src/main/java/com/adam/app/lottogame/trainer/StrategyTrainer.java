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

    public StrategyTrainer(LottoHistoryRepository repository, int maxNumber) {
        mRepository = repository;
        mMaxNumber = maxNumber;
    }

    /**
     * Evaluate a single strategy (incremental evaluation).
     *
     * @param strategy       the LottoAIStrategy (enum) to evaluate
     * @param pickCount      how many numbers strategy picks (eg. 6)
     * @param trialsPerEntry number of repeats per historical draw (for stochastic strategies)
     * @return average hits per draw (double)
     */
    public double evaluateStrategyIncremental(LottoAIStrategy strategy, int pickCount, int trialsPerEntry) {
        Future<List<LottoHistoryEntity>> future = mRepository.getAll();
        List<LottoHistoryEntity> list = null;
        try {
            list = future.get();  // synchronization
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (list == null || list.isEmpty()) {
            return 0.0;
        }

        long totalHits = 0L;
        long totalTrials = 0L;

        for (int i = 0; i < list.size(); i++) {
            LottoHistoryEntity current = list.get(i);
            List<Integer> groundTruth = current.getNumbers() == null ? Collections.emptyList() : current.getNumbers();

            List<Integer> prior = flattenHistoryUpTo(list, i);
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
        }

        if (totalTrials == 0) {
            return 0.0;
        }

        return (double) totalHits / (double) (totalTrials);
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
            if (counter.decrementAndGet() == 0) {
                Collections.sort(results, (a, b) -> Double.compare(b.getAverageHits(), a.getAverageHits()));
                allStrategiesCallback.onComplete(results);
            }
        }
    }

    private List<Integer> flattenHistoryUpTo(List<LottoHistoryEntity> all, int exclusiveIndex) {
        List<Integer> flat = new ArrayList<>();
        for (int j = 0; j < exclusiveIndex; j++) {
            LottoHistoryEntity e = all.get(j);
            if (e.getNumbers() != null) {
                flat.addAll(e.getNumbers());
            }
        }
        return flat;
    }

    private int countMatch(List<Integer> pick, List<Integer> winning) {
        if (pick == null || winning == null) {
            return 0;
        }
        int c = 0;
        for (int n : pick) {
            if (winning.contains(n)) {
                c++;
            }
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
