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
                        long mLastWaveStartTime, long mLastEnemySpawnTime) {
        this.mConfig = config;
        this.mSpawnedCount = spawnedCount;
        this.mCurrentTime = currentTime;
        this.mRemainingInWave = remainingInWave;
        this.mCurrentWaveStrategy = currentWaveStrategy;
        this.mLastWaveStartTime = mLastWaveStartTime;
        this.mLastEnemySpawnTime = mLastEnemySpawnTime;
    }

    public LevelConfig getConfig() { return mConfig; }
    public int getSpawnedCount() { return mSpawnedCount; }
    public long getCurrentTime() { return mCurrentTime; }
    
    public int getRemainingInWave() { return mRemainingInWave; }
    public void setRemainingInWave(int remainingInWave) { this.mRemainingInWave = remainingInWave; }
    
    public EnemyEntryStrategy getCurrentWaveStrategy() { return mCurrentWaveStrategy; }
    public void setCurrentWaveStrategy(EnemyEntryStrategy currentWaveStrategy) { this.mCurrentWaveStrategy = currentWaveStrategy; }
    
    public long getLastWaveStartTime() { return mLastWaveStartTime; }
    public void setLastWaveStartTime(long lastWaveStartTime) { this.mLastWaveStartTime = lastWaveStartTime; }
    
    public long getLastEnemySpawnTime() { return mLastEnemySpawnTime; }
    public void setLastEnemySpawnTime(long lastEnemySpawnTime) { this.mLastEnemySpawnTime = lastEnemySpawnTime; }
}
