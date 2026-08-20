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

import java.util.HashMap;
import java.util.Map;

public class ResultStrategyFactory {

    /**
     * Interface for match number constants.
     */
    interface MatchNumber {
        int SECOND_PRIZE = 2;
        int THIRD_PRIZE = 3;
        int FOURTH_PRIZE = 4;
        int FIFTH_PRIZE = 5;
        int BIG_PRIZE = 6;
    }

    /**
     * STRATEGY_MAP: MatchNumber -> IResultStrategy
     */
    private static final Map<Integer, IResultStrategy> STRATEGY_MAP = new HashMap<>() {
        {
            put(MatchNumber.SECOND_PRIZE, new SecondPrizeStrategy());
            put(MatchNumber.THIRD_PRIZE, new ThirdPrizeStrategy());
            put(MatchNumber.FOURTH_PRIZE, new FourthPrizeStrategy());
            put(MatchNumber.FIFTH_PRIZE, new FifthPrizeStrategy());
            put(MatchNumber.BIG_PRIZE, new BigPrizeStrategy());
        }
    };


    // Default strategy
    private static final IResultStrategy DEFAULT_STRATEGY = new LoseStrategy();


    // getStrategy
    public static IResultStrategy getStrategy(Integer matchNumber) {
        return STRATEGY_MAP.getOrDefault(matchNumber, DEFAULT_STRATEGY);
    }
}
