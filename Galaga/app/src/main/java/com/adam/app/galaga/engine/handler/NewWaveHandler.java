/*
 * Copyright (c) 2026 Adam Chen
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
        
        if (mNext != null) {
            return mNext.handle(context);
        }
        return false;
    }
}
