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
package com.adam.app.lottogame.strategy;

import com.adam.app.lottogame.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public enum LottoAIStrategy {
    RANDOM_PICK {
        @Override
        List<Integer> pickNumbers(List<Integer> historyNumbers, int pickCount, int maxNumber) {
            Utils.log(TAG, "RANDOM_PICK");

            if (pickCount > maxNumber) {
                throw new IllegalArgumentException("pickCount cannot > maxNumber");
            }

            Set<Integer> result = new HashSet<>();
            while (result.size() < pickCount) {
                result.add(1 + mRandom.nextInt(maxNumber));
            }
            return new ArrayList<>(result);
        }
    },

    HOT_PICK {
        @Override
        List<Integer> pickNumbers(List<Integer> historyNumbers, int pickCount, int maxNumber) {
            Utils.log(TAG, "HOT_PICK");

            Map<Integer, Integer> freq = countFrequencyFull(historyNumbers, maxNumber);

            return freq.entrySet()
                    .stream()
                    .sorted((a, b) -> b.getValue() - a.getValue())
                    .limit(pickCount)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    },

    COLD_PICK {
        @Override
        List<Integer> pickNumbers(List<Integer> historyNumbers, int pickCount, int maxNumber) {
            Utils.log(TAG, "COLD_PICK");

            Map<Integer, Integer> freq = countFrequencyFull(historyNumbers, maxNumber);

            return freq.entrySet()
                    .stream()
                    .sorted(Comparator.comparingInt(Map.Entry::getValue))
                    .limit(pickCount)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    },

    HOT_WEIGHTED_PICK {
        @Override
        List<Integer> pickNumbers(List<Integer> historyNumbers, int pickCount, int maxNumber) {
            Utils.log(TAG, "HOT_WEIGHTED_PICK");
            Map<Integer, Integer> freq = countFrequencyFull(historyNumbers, maxNumber);

            List<Integer> weightedList = new ArrayList<>();
            for (Map.Entry<Integer, Integer> e : freq.entrySet()) {
                int w = Math.max(1, e.getValue()); // 避免沒有出現過的數字永遠不被選
                for (int i = 0; i < w; i++) {
                    weightedList.add(e.getKey());
                }
            }

            Set<Integer> result = new HashSet<>();
            while (result.size() < pickCount) {
                int idx = mRandom.nextInt(weightedList.size());
                result.add(weightedList.get(idx));
            }

            return new ArrayList<>(result);
        }
    },

    TREND_PICK {
        @Override
        List<Integer> pickNumbers(List<Integer> historyNumbers, int pickCount, int maxNumber) {
            Utils.log(TAG, "TREND_PICK");


            if (historyNumbers.isEmpty()) {
                return RANDOM_PICK.pickNumbers(historyNumbers, pickCount, maxNumber);
            }

            int windowSize = Math.min(20, historyNumbers.size());
            List<Integer> recent = historyNumbers.subList(historyNumbers.size() - windowSize, historyNumbers.size());

            Map<Integer, Integer> freq = countFrequencyFull(recent, maxNumber);

            return freq.entrySet()
                    .stream()
                    .sorted((a, b) -> b.getValue() - a.getValue())
                    .limit(pickCount)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    },

    HYBRID_PICK {
        private static final double HOT_RATE  = 0.4;
        private static final double COLD_RATE = 0.8;

        @Override
        List<Integer> pickNumbers(List<Integer> historyNumbers, int pickCount, int maxNumber) {
            Utils.log(TAG, "HYBRID_PICK");

            double p = mRandom.nextDouble();

            if (p < HOT_RATE) {
                return HOT_PICK.pickNumbers(historyNumbers, pickCount, maxNumber);

            } else if (p < COLD_RATE) {
                return COLD_PICK.pickNumbers(historyNumbers, pickCount, maxNumber);

            } else {
                return RANDOM_PICK.pickNumbers(historyNumbers, pickCount, maxNumber);
            }
        }
    };

    // TAG
    protected static final String TAG = LottoAIStrategy.class.getSimpleName();

    protected final Random mRandom = new Random();

    abstract List<Integer> pickNumbers(List<Integer> historyNumbers, int pickCount, int maxNumber);

    /**
     * freq (Contains all numbers from 1 to maxnumber)
     */
    protected Map<Integer, Integer> countFrequencyFull(List<Integer> historyNumbers, int maxNumber) {
        Map<Integer, Integer> map = new HashMap<>();

        // fill map with 1..maxNumber
        for (int i = 1; i <= maxNumber; i++) {
            map.put(i, 0);
        }

        for (int n : historyNumbers) {
            if (n >= 1 && n <= maxNumber) {
                map.put(n, map.get(n) + 1);
            }
        }

        return map;
    }

    public static List<Integer> pick(LottoAIStrategy type, List<Integer> historyNumbers, int pickCount, int maxNumber) {
        return type.pickNumbers(historyNumbers, pickCount, maxNumber);
    }
}

