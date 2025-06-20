/**
 * Strategy pattern
 * Date: 2025/06/20
 */
package com.adam.app.lottogame.strategy;

import android.content.Context;

import com.adam.app.lottogame.R;

public interface IResultStrategy {
    // getResultText with context
    String getResultText(Context context);
}

// BigPrizeStrategy
class BigPrizeStrategy implements IResultStrategy {
    @Override
    public String getResultText(Context context) {
        return context.getString(R.string.info_you_win_big_prize);
    }
}

// SecondPrizeStrategy
class SecondPrizeStrategy implements IResultStrategy {
    @Override
    public String getResultText(Context context) {
        return context.getString(R.string.info_you_win_300000_prize);
    }
}

// ThirdPrizeStrategy
class ThirdPrizeStrategy implements IResultStrategy {
    @Override
    public String getResultText(Context context) {
        return context.getString(R.string.info_you_win_100000_prize);
    }

}

// FourthPrizeStrategy
class FourthPrizeStrategy implements IResultStrategy {
    @Override
    public String getResultText(Context context) {
        return context.getString(R.string.info_you_win_50000_prize);
    }
}

// LoseStrategy
class LoseStrategy implements IResultStrategy {
    @Override
    public String getResultText(Context context) {
        return context.getString(R.string.info_you_lose);
    }
}
