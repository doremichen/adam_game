/**
 * Copyright (C) 2025 Adam. All rights reserved.
 *
 * This class is the strategy of Ai pick number.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-26
 */
package com.adam.app.lottogame.strategy;

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

