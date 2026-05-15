/*
 * Copyright (c) 2026 Adam Chen
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

package com.adam.app.galaga.engine.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Factory for creating enemy entry strategies using a list for easy extension.
 */
public class EntryStrategyFactory {
    private static final Random mRandom = new Random();
    private static final List<Supplier<EnemyEntryStrategy>> STRATEGY_POOL = new ArrayList<>();

    static {
        // To add a new strategy, simply add it to this list
        STRATEGY_POOL.add(ArcEntryStrategy::new);
        STRATEGY_POOL.add(CircleEntryStrategy::new);
    }

    public static EnemyEntryStrategy createRandomStrategy() {
        if (STRATEGY_POOL.isEmpty()) {
            return null;
        }
        int index = mRandom.nextInt(STRATEGY_POOL.size());
        return STRATEGY_POOL.get(index).get();
    }
}
