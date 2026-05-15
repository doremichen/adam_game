/*
 * Copyright (c) 2026 Adam Chen
 */

package com.adam.app.galaga.engine.handler;

import com.adam.app.galaga.utils.GameConstants;

/**
 * Handles spawning when a wave is already in progress.
 */
public class WaveInProgressHandler extends SpawnHandler {
    @Override
    public boolean handle(SpawnContext context) {
        if (context.getRemainingInWave() > 0) {
            if (context.getCurrentTime() - context.getLastEnemySpawnTime() >= GameConstants.INTER_ENEMY_DELAY_MS) {
                context.setRemainingInWave(context.getRemainingInWave() - 1);
                context.setLastEnemySpawnTime(context.getCurrentTime());
                return true;
            }
            return false;
        }
        
        if (mNext != null) {
            return mNext.handle(context);
        }
        return false;
    }
}
