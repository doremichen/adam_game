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

import com.adam.app.galaga.data.local.prefs.GameSettings;
import com.adam.app.galaga.data.model.Bee;
import com.adam.app.galaga.data.model.Bullet;
import com.adam.app.galaga.data.model.GameObject;
import com.adam.app.galaga.data.model.LevelConfig;
import com.adam.app.galaga.data.model.Plane;
import com.adam.app.galaga.utils.GameConstants;
import com.adam.app.galaga.utils.GameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameObjectManager {
    private static final String TAG = GameObjectManager.class.getSimpleName();

    private final List<Bee> mBees = new CopyOnWriteArrayList<>();
    private final List<Bullet> mBullets = new CopyOnWriteArrayList<>();
    private final CollisionManager mCollisionManager;
    private final LevelManager mLevelManager;
    private final SoundManager mSoundManager;
    private final GameSettings mGameSettings;
    
    private WinningStrategy mWinningStrategy;
    private LevelConfig mLevelConfig;
    private Plane mPlayerPlane;
    private int mSpawnedCount = 0;
    private long mLevelStartTime;
    private long mLastAutoFireTime;

    private final EnemySpawner mEnemySpawner = new EnemySpawner();

    @Inject
    public GameObjectManager(LevelManager levelManager, 
                             CollisionManager collisionManager, 
                             SoundManager soundManager,
                             GameSettings gameSettings) {
        this.mLevelManager = levelManager;
        this.mCollisionManager = collisionManager;
        this.mSoundManager = soundManager;
        this.mGameSettings = gameSettings;
    }

    public void init() {
        mBees.clear();
        mBullets.clear();
        initPlayer();
        if (!loadLevel(1)) {
            GameUtils.error(TAG, "no config file!!!");
        }
    }

    public boolean loadLevel(int levelId) {
        mLevelConfig = mLevelManager.enterLevel(levelId);
        if (mLevelConfig == null) {
            return false;
        }

        mLevelStartTime = System.currentTimeMillis();
        buildWinningStrategy();
        resetLevelState();
        GameUtils.info(TAG, "Level " + levelId + " loaded: " + mLevelConfig.getMetadata().getTitle());
        return true;
    }

    public boolean isLevelCleared() {
        if (mLevelConfig == null) {
            GameUtils.error(TAG, "Level config is null");
            return false;
        }
        return mWinningStrategy.validate(this);
    }

    public int getCurrentLevelId() {
        return mLevelManager.getCurrentLevelId();
    }

    public void nextLevel() {
        mLevelConfig = mLevelManager.nextLevel();
        if (mLevelConfig == null) {
            throw new RuntimeException("Level config is null");
        }

        mLevelStartTime = System.currentTimeMillis();
        buildWinningStrategy();
        resetLevelState();
        GameUtils.info(TAG, "Level " + mLevelManager.getCurrentLevelId() + " loaded: " + mLevelConfig.getMetadata().getTitle());
    }

    public long getLevelStartTime() {
        return mLevelStartTime;
    }

    public boolean areAllBeesDead() {
        return mSpawnedCount >= mLevelConfig.getEnemySettings().getTotalCount() && mBees.isEmpty();
    }

    public String getMetadataTitle() {
        if (mLevelConfig == null) return "";
        return mLevelConfig.getMetadata().getTitle();
    }

    public WinningStrategy getStrategy() {
        return (mWinningStrategy == null)? WinningStrategy.ELIMINATE_ALL : mWinningStrategy;
    }

    public void updateAll() {
        updatePlayer();
        updateSpawning();
        updateBees();
        updateBullets();
    }

    private void updateBullets() {
        mBullets.removeIf(Bullet::isOutOfBound);
        mBullets.forEach(Bullet::update);
    }

    private void updateBees() {
        mBees.forEach(Bee::update);
    }

    private void updatePlayer() {
        if (mPlayerPlane != null) {
            mPlayerPlane.update();
        }
    }

    public void cleanupEntities() {
        mBees.removeIf(Bee::isDead);
        mBullets.removeIf(bullet -> bullet.isDead() || bullet.isOutOfBound());
    }

    public int handleCollisions() {
        return mCollisionManager.handleCollisions(mBullets, mBees);
    }

    public boolean isPlayerDead() {
        return mCollisionManager.isPlaneHit(mPlayerPlane, mBees);
    }

    public void movePlayer(Direction direction) {
        switch (direction) {
            case UP:
                mPlayerPlane.moveUp();
                break;
            case DOWN:
                mPlayerPlane.moveDown();
                break;
            case LEFT:
                mPlayerPlane.moveLeft();
                break;
            case RIGHT:
                mPlayerPlane.moveRight();
                break;
        }
    }

    public void spawnBullet() {
        if (mPlayerPlane == null) {
            return;
        }

        GameSettings.ShotStyle shotStyle = mGameSettings.getShotStyle();
        shotStyle.spawn(mBullets, mPlayerPlane, mSoundManager);
    }

    public List<GameObject> getAllEntities() {
        List<GameObject> entities = new ArrayList<>();
        if (mPlayerPlane != null) entities.add(mPlayerPlane);
        entities.addAll(mBees);
        entities.addAll(mBullets);
        return entities;
    }

    public void clear() {
        resetLevelState();
        mPlayerPlane = null;
    }

    public void handleAutoFiring() {
        if (!mGameSettings.isAutoFire()) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastAutoFireTime >= GameConstants.AUTO_FIRE_INTERVAL) {
            spawnBullet();
            mLastAutoFireTime = currentTime;
        }
    }

    private void resetLevelState() {
        mBees.clear();
        mBullets.clear();
        mSpawnedCount = 0;
        mEnemySpawner.reset();
        mLevelStartTime = System.currentTimeMillis();
    }

    private void initPlayer() {
        mPlayerPlane = new Plane(
                GameConstants.PLAYER_START_X,
                GameConstants.PLAYER_START_Y,
                GameConstants.PLAYER_SPEED,
                GameConstants.PLAYER_WIDTH,
                GameConstants.PLAYER_HEIGHT
        );
    }

    private void buildWinningStrategy() {
        String type = mLevelConfig.getWinningCondition().getType();
        try {
            mWinningStrategy = WinningStrategy.valueOf(type);
        } catch (IllegalArgumentException | NullPointerException e) {
            mWinningStrategy = WinningStrategy.ELIMINATE_ALL;
        }
    }

    private void updateSpawning() {
        if (mLevelConfig == null) return;
        
        if (mWinningStrategy == WinningStrategy.SURVIVAL) {
            if (System.currentTimeMillis() - mLevelStartTime >= GameConstants.LEVEL_DURATION_MS) {
                return;
            }
        }
        
        Bee bee = mEnemySpawner.spawnNextEnemy(mLevelConfig, mSpawnedCount);
        if (bee != null) {
            mBees.add(bee);
            mSpawnedCount++;
        }
    }
}
