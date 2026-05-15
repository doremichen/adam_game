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

import com.adam.app.galaga.R;
import com.adam.app.galaga.data.local.prefs.GameSettings;
import com.adam.app.galaga.data.model.Bee;
import com.adam.app.galaga.data.model.Bullet;
import com.adam.app.galaga.data.model.GameObject;
import com.adam.app.galaga.data.model.LevelConfig;
import com.adam.app.galaga.data.model.Plane;
import com.adam.app.galaga.engine.strategy.EnemyEntryStrategy;
import com.adam.app.galaga.utils.GameConstants;
import com.adam.app.galaga.utils.GameUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameObjectManager {
    // TAG
    private static final String TAG = GameObjectManager.class.getSimpleName();

    // use CopyOnWriteArrayList to avoid concurrent modification exception
    // list of bees
    private final List<Bee> mBees = new CopyOnWriteArrayList<>();
    // list of bullets
    private final List<Bullet> mBullets = new CopyOnWriteArrayList<>();
    // collision manager
    private final CollisionManager mCollisionManager = new CollisionManager();
    // level manager
    private final LevelManager mLevelManager = new LevelManager();
    // Winnings strategy
    private WinningStrategy mWinningStrategy;
    // Pre-allocated list for all entities to reduce GC pressure
    private final List<GameObject> mAllEntities = new java.util.ArrayList<>();
    // level config
    private LevelConfig mLevelConfig;
    // player
    private Plane mPlayerPlane;
    // last spawn time
    private long mLastSpawnTime = 0;
    // spawned count
    private int mSpawnedCount = 0;
    // level start time
    private long mLevelStartTime;
    // last auto fire time
    private long mLastAutoFireTime;

    // Enemy Spawning
    private final EnemySpawner mEnemySpawner = new EnemySpawner();

    private GameObjectManager() {
    }

    /**
     * singleton
     *
     * @return GameObjectManager
     */
    public static GameObjectManager getInstance() {
        return Helper.INSTANCE;
    }

    /**
     * init
     */
    public void init() {
        mBees.clear();
        mBullets.clear();

        initPlayer();

        if (!loadLevel(1)) {
            GameUtils.error(TAG, "no config file!!!");
        }
    }

    /**
     * loadLevel
     *
     * @param levelId int
     */
    public boolean loadLevel(int levelId) {
        // level config
        mLevelConfig = mLevelManager.enterLevel(levelId);
        if (mLevelConfig == null) {
            return false;
        }

        mLevelStartTime = System.currentTimeMillis();

        // winning strategy
        buildWinningStrategy();

        // clear bee/bullet
        resetLevelState();
        GameUtils.info(TAG, "Level " + levelId + " loaded: " + mLevelConfig.getMetadata().getTitle());
        return true;
    }

    /**
     * isLevelCleared
     * @return boolean true if level is cleared
     */
    public boolean isLevelCleared() {
        if (mLevelConfig == null) {
            GameUtils.error(TAG, "Level config is null");
            return false;
        }
        return mWinningStrategy.validate(this);
    }

    /**
     * get level id
     *
     * @return int
     */
    public int getCurrentLevelId() {
        return mLevelManager.getCurrentLevelId();
    }

    /**
     * next level
     */
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


    /**
     * get level start time
     * @return long the time when level start
     */
    public long getLevelStartTime() {
        return mLevelStartTime;
    }

    /**
     * check if bees are live?
     *
     * @return boolean true if all bees are dead
     */
    public boolean areAllBeesDead() {
        return mSpawnedCount >= mLevelConfig.getEnemySettings().getTotalCount() && mBees.isEmpty();
    }


    /**
     * get metadata title
     *
     * @return String
     */
    public String getMetadataTitle() {
        if (mLevelConfig == null) return "";
        return mLevelConfig.getMetadata().getTitle();
    }


    /**
     * get strategy
     *
     * @return WinningStrategy
     */
    public WinningStrategy getStrategy() {
        return (mWinningStrategy == null)? WinningStrategy.ELIMINATE_ALL : mWinningStrategy;
    }

    /**
     * updateAll
     */
    public void updateAll() {
        updatePlayer();
        updateSpawning();
        updateBees();
        updateBullets();
    }

    private void updateBullets() {
        mBullets.removeIf(Bullet::isOutOfBound);
        // update bullets
        for (Bullet bullet : mBullets) {
            bullet.update();
        }
    }

    private void updateBees() {
        for (Bee bee : mBees) {
            bee.update();
        }
    }

    private void updatePlayer() {
        if (mPlayerPlane != null) {
            mPlayerPlane.update();
        }
    }

    /**
     * cleanupEntities
     */
    public void cleanupEntities() {
        mBees.removeIf(Bee::isDead);
        mBullets.removeIf(bullet -> bullet.isDead() || bullet.isOutOfBound());
    }

    /**
     * handleCollisions
     *
     * @return int
     */
    public int handleCollisions() {
        return mCollisionManager.handleCollisions(mBullets, mBees);
    }

    /**
     * isPlayerDead
     *
     * @return boolean
     */
    public boolean isPlayerDead() {
        return mCollisionManager.isPlaneHit(mPlayerPlane, mBees);
    }

    /**
     * movePlayer
     *
     * @param direction Direction
     */
    public void movePlayer(Direction direction) {
        GameUtils.info(TAG, "movePlayer");
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
            default:
                break;
        }
    }

    /**
     * spawnBullet
     */
    public void spawnBullet() {
        GameUtils.info(TAG, "spawnBullet");
        // early return
        if (mPlayerPlane == null) {
            return;
        }

        // shot type
        GameSettings.ShotStyle shotStyle = GameSettings.getInstance().getShotStyle();
        // spawn bullets
        shotStyle.spawn(mBullets, mPlayerPlane);
    }

    /**
     * getAllEntities
     *
     * @return List<GameObject>
     */
    public List<GameObject> getAllEntities() {
        mAllEntities.clear();
        if (mPlayerPlane != null) mAllEntities.add(mPlayerPlane);
        mAllEntities.addAll(mBees);
        mAllEntities.addAll(mBullets);
        return new ArrayList<>(mAllEntities);
    }

    /**
     * clear
     */
    public void clear() {
        resetLevelState();
        mPlayerPlane = null;
    }

    public void handleAutoFiring() {
        boolean isAutoFire = GameSettings.getInstance().isAutoFire();
        if (!isAutoFire) return;

        // handle auto fire logic
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastAutoFireTime >= GameConstants.AUTO_FIRE_INTERVAL) {
            spawnBullet();
            mLastAutoFireTime = currentTime;
        }

    }

    /**
     * direction
     */
    public enum Direction {
        UP(R.id.btnUp),
        DOWN(R.id.btnDown),
        LEFT(R.id.btnLeft),
        RIGHT(R.id.btnRight);

        // Map: id -> Direction
        private static Map<Integer, Direction> sResIdToDirection = new HashMap<>();

        static {
            for (Direction direction : Direction.values()) {
                sResIdToDirection.put(direction.mResId, direction);
            }
        }

        private final int mResId;

        private Direction(int resId) {
            mResId = resId;
        }

        public static Direction fromResId(int resId) {
            return sResIdToDirection.get(resId);
        }

    }

    private void resetLevelState() {
        // clear bee/bullet
        mBees.clear();
        mBullets.clear();
        // reset spawn state
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



    /**
     * build winnings strategy
     */
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
        
        // Survival strategy check
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




    private static class Helper {
        private static final GameObjectManager INSTANCE = new GameObjectManager();
    }
}
