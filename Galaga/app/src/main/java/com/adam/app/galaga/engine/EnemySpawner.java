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

package com.adam.app.galaga.engine;

import com.adam.app.galaga.data.model.Bee;
import com.adam.app.galaga.data.model.LevelConfig;
import com.adam.app.galaga.engine.handler.NewWaveHandler;
import com.adam.app.galaga.engine.handler.SpawnContext;
import com.adam.app.galaga.engine.handler.SpawnHandler;
import com.adam.app.galaga.engine.handler.WaveInProgressHandler;
import com.adam.app.galaga.engine.strategy.EnemyEntryStrategy;
import com.adam.app.galaga.utils.GameConstants;

/**
 * Manages enemy spawning using a Chain of Responsibility.
 * Renamed from WaveManager for better clarity.
 */
public class EnemySpawner {
    private long mLastWaveStartTime = 0;
    private long mLastEnemySpawnTime = 0;
    private int mRemainingInWave = 0;
    private EnemyEntryStrategy mCurrentWaveStrategy;

    private final SpawnHandler mSpawnChain;

    public EnemySpawner() {
        // Setup Chain of Responsibility
        SpawnHandler waveInProgress = new WaveInProgressHandler();
        SpawnHandler newWave = new NewWaveHandler();
        
        waveInProgress.setNext(newWave);
        this.mSpawnChain = waveInProgress;
    }

    public void reset() {
        mRemainingInWave = 0;
        mLastWaveStartTime = 0;
        mLastEnemySpawnTime = 0;
        mCurrentWaveStrategy = null;
    }

    /**
     * Attempts to spawn the next enemy.
     */
    public Bee spawnNextEnemy(LevelConfig config, int spawnedCount) {
        if (config == null) return null;

        SpawnContext context = new SpawnContext(
            config, spawnedCount, System.currentTimeMillis(),
            mRemainingInWave, mCurrentWaveStrategy,
            mLastWaveStartTime, mLastEnemySpawnTime
        );

        if (mSpawnChain.handle(context)) {
            // Update state from context
            this.mRemainingInWave = context.getRemainingInWave();
            this.mCurrentWaveStrategy = context.getCurrentWaveStrategy();
            this.mLastWaveStartTime = context.getLastWaveStartTime();
            this.mLastEnemySpawnTime = context.getLastEnemySpawnTime();

            return createBee(config, spawnedCount, mCurrentWaveStrategy);
        }
        
        return null;
    }

    private Bee createBee(LevelConfig config, int index, EnemyEntryStrategy strategy) {
        int cols = GameConstants.DEFAULT_BEES_COLS;
        int row = index / cols;
        int col = index % cols;

        float x = GameConstants.BEE_INITIAL_OFFSET_X + col * (GameConstants.BEE_WIDTH + GameConstants.BEE_SPACING);
        float y = GameConstants.BEE_INITIAL_OFFSET_Y + row * (GameConstants.BEE_HEIGHT + GameConstants.BEE_SPACING);

        float speed = config.getEnemySettings().getBaseSpeed();
        float difficulty = config.getMetadata().getDifficultyMultiplier();
        
        Bee bee = new Bee(x, y, speed * difficulty, GameConstants.BEE_WIDTH, GameConstants.BEE_HEIGHT);
        bee.setEntryStrategy(strategy);
        return bee;
    }
}
