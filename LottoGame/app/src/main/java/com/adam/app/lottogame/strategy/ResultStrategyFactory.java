/**
 * Strategy pattern
 * Date: 2025/06/20
 */
package com.adam.app.lottogame.strategy;

import java.util.HashMap;
import java.util.Map;

public class ResultStrategyFactory {

    /**
     * interface mach number containt: 3, 4, 5, 6
     */
    interface IMachNUmber {
        int SECOND_PRIZE = 2;
        int THIRD_PRIZE = 3;
        int FOURTH_PRIZE = 4;
        int FIFTH_PRIZE = 5;
        int BIG_PRIZE = 6;
    }

    /**
     * STRATEGY_MAP: IMachNumber -> IResultStrategy
     */
    private static final Map<Integer, IResultStrategy> STRATEGY_MAP = new HashMap<>() {
        {
            put(IMachNUmber.SECOND_PRIZE, new SecondPrizeStrategy());
            put(IMachNUmber.THIRD_PRIZE, new ThirdPrizeStrategy());
            put(IMachNUmber.FOURTH_PRIZE, new FourthPrizeStrategy());
            put(IMachNUmber.FIFTH_PRIZE, new FifthPrizeStrategy());
            put(IMachNUmber.BIG_PRIZE, new BigPrizeStrategy());

        }
    };


    // Default strategy
    private static final IResultStrategy DEFAULT_STRATEGY = new LoseStrategy();


    // getStrategy
    public static IResultStrategy getStrategy(Integer matchNumber) {
        return STRATEGY_MAP.getOrDefault(matchNumber, DEFAULT_STRATEGY);
    }
}
