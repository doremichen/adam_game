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

package com.adam.app.galaga.engine.handler;

import com.adam.app.galaga.engine.strategy.EntryStrategyFactory;
import com.adam.app.galaga.utils.GameConstants;

/**
 * Handles starting a new wave.
 */
public class NewWaveHandler extends SpawnHandler {
    @Override
    public boolean handle(SpawnContext context) {
        if (context.getSpawnedCount() < context.getConfig().getEnemySettings().getTotalCount()) {
            if (context.getCurrentTime() - context.getLastWaveStartTime() >= GameConstants.WAVE_DELAY_MS) {
                int count = Math.min(GameConstants.WAVE_SIZE, 
                                     context.getConfig().getEnemySettings().getTotalCount() - context.getSpawnedCount());
                
                context.setRemainingInWave(count);
                context.setCurrentWaveStrategy(EntryStrategyFactory.createRandomStrategy());
                context.setLastWaveStartTime(context.getCurrentTime());
                
                // Spawn first enemy of new wave
                context.setRemainingInWave(context.getRemainingInWave() - 1);
                context.setLastEnemySpawnTime(context.getCurrentTime());
                return true;
            }
        }
        
        if (getNext() != null) {
            return getNext().handle(context);
        }
        return false;
    }
}
