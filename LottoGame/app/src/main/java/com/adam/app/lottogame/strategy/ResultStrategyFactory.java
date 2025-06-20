/**
 * Strategy pattern
 * Date: 2025/06/20
 */
package com.adam.app.lottogame.strategy;

import java.util.HashMap;
import java.util.Map;

public class ResultStrategyFactory {
    /**
     * enum match number: 6,5,4,3
     */
    public enum MatchNumber {
        BIG_PRIZE, SECOND_PRIZE, THIRD_PRIZE, FOURTH_PRIZE, LOSE
    }

    // Default strategy
    private static final IResultStrategy DEFAULT_STRATEGY = new LoseStrategy();

    // STRATEGY_MAP: MatchNumber -> IResultStrategy
    private static final Map<MatchNumber, IResultStrategy> STRATEGY_MAP = new HashMap<>() {
        {
            put(MatchNumber.BIG_PRIZE, new BigPrizeStrategy());
            put(MatchNumber.SECOND_PRIZE, new SecondPrizeStrategy());
            put(MatchNumber.THIRD_PRIZE, new ThirdPrizeStrategy());
        }
    };

    // getStrategy
    public static IResultStrategy getStrategy(MatchNumber matchNumber) {
        return STRATEGY_MAP.getOrDefault(matchNumber, DEFAULT_STRATEGY);
    }
}
