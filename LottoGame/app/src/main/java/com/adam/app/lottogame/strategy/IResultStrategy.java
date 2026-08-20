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
        return context.getString(R.string.info_you_win_200000_prize);
    }
}

// ThirdPrizeStrategy
class ThirdPrizeStrategy implements IResultStrategy {
    @Override
    public String getResultText(Context context) {
        return context.getString(R.string.info_you_win_300000_prize);
    }

}

// FourthPrizeStrategy
class FourthPrizeStrategy implements IResultStrategy {
    @Override
    public String getResultText(Context context) {
        return context.getString(R.string.info_you_win_400000_prize);
    }
}

// FifthPrizeStrategy
class FifthPrizeStrategy implements IResultStrategy {
    @Override
    public String getResultText(Context context) {
        return context.getString(R.string.info_you_win_500000_prize);
    }
}


// LoseStrategy
class LoseStrategy implements IResultStrategy {
    @Override
    public String getResultText(Context context) {
        return context.getString(R.string.info_you_lose);
    }
}
