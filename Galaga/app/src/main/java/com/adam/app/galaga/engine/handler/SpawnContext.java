/*
 * Copyright (c) 2026 Adam Chen
 */

package com.adam.app.galaga.engine.handler;

import com.adam.app.galaga.data.model.LevelConfig;
import com.adam.app.galaga.engine.strategy.EnemyEntryStrategy;

/**
 * Context for the spawning Chain of Responsibility.
 */
public class SpawnContext {
    private final LevelConfig mConfig;
    private final int mSpawnedCount;
    private final long mCurrentTime;
    
    private EnemyEntryStrategy mCurrentWaveStrategy;
    private int mRemainingInWave;
    private long mLastWaveStartTime;
    private long mLastEnemySpawnTime;

    public SpawnContext(LevelConfig config, int spawnedCount, long currentTime, 
                        int remainingInWave, EnemyEntryStrategy currentWaveStrategy,
                        long lastWaveStartTime, long lastEnemySpawnTime) {
        this.mConfig = config;
        this.mSpawnedCount = spawnedCount;
        this.mCurrentTime = currentTime;
        this.mRemainingInWave = remainingInWave;
        this.mCurrentWaveStrategy = currentWaveStrategy;
        this.mLastWaveStartTime = lastWaveStartTime;
        this.mLastEnemySpawnTime = lastEnemySpawnTime;
    }

    public LevelConfig getConfig() { return mConfig; }
    public int getSpawnedCount() { return mSpawnedCount; }
    public long getCurrentTime() { return mCurrentTime; }
    
    public int getRemainingInWave() { return mRemainingInWave; }
    public void setRemainingInWave(int count) { mRemainingInWave = count; }
    
    public EnemyEntryStrategy getCurrentWaveStrategy() { return mCurrentWaveStrategy; }
    public void setCurrentWaveStrategy(EnemyEntryStrategy strategy) { mCurrentWaveStrategy = strategy; }
    
    public long getLastWaveStartTime() { return mLastWaveStartTime; }
    public void setLastWaveStartTime(long time) { mLastWaveStartTime = time; }
    
    public long getLastEnemySpawnTime() { return mLastEnemySpawnTime; }
    public void setLastEnemySpawnTime(long time) { mLastEnemySpawnTime = time; }
}
