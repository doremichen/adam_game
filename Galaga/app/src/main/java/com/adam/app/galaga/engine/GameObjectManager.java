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
import com.adam.app.galaga.data.model.Bee;
import com.adam.app.galaga.data.model.Bullet;
import com.adam.app.galaga.data.model.GameObject;
import com.adam.app.galaga.data.model.Plane;
import com.adam.app.galaga.utils.GameConstants;
import com.adam.app.galaga.utils.GameUtils;

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
    // player
    private Plane mPlayerPlane;

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

        mPlayerPlane = new Plane(
                GameConstants.PLAYER_START_X,
                GameConstants.PLAYER_START_Y,
                GameConstants.PLAYER_SPEED,
                GameConstants.PLAYER_WIDTH,
                GameConstants.PLAYER_HEIGHT
        );


        // init bees
        for (int row = 0; row < GameConstants.BEE_ROWS; row++) {
            for (int col = 0; col < GameConstants.BEE_COLS; col++) {
                float x = GameConstants.BEE_INITIAL_OFFSET_X + col * (GameConstants.BEE_WIDTH + GameConstants.BEE_SPACING);
                float y = GameConstants.BEE_INITIAL_OFFSET_Y + row * (GameConstants.BEE_HEIGHT + GameConstants.BEE_SPACING);
                mBees.add(new Bee(x, y, GameConstants.BEE_SPEED_BASE, GameConstants.BEE_WIDTH, GameConstants.BEE_HEIGHT));
            }
        }
    }

    /**
     * updateAll
     */
    public void updateAll() {
        // update player
        if (mPlayerPlane != null) {
            mPlayerPlane.update();
        }

        // update bees
        for (Bee bee : mBees) {

            if (Math.random() < 0.005) {
                bee.setDiving(true);
            }

            bee.update();
        }
        mBullets.removeIf(Bullet::isOutOfBound);
        // update bullets
        for (Bullet bullet : mBullets) {
            bullet.update();
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
     * @param direction float
     */
    public void movePlayer(float direction) {
        GameUtils.info(TAG, "movePlayer");
        // early return
        if (mPlayerPlane == null) {
            return;
        }

        // left
        if (direction < 0) {
            mPlayerPlane.moveLeft();
            return;
        }

        // right
        mPlayerPlane.moveRight();
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

        float bulletX = mPlayerPlane.getPosition().x + mPlayerPlane.getRectOfCollision().width() / 2f;
        float bulletY = mPlayerPlane.getPosition().y;
        mBullets.add(new Bullet(bulletX, bulletY, GameConstants.BULLET_SPEED));
    }

    /**
     * getAllEntities
     *
     * @return List<GameObject>
     */
    public List<GameObject> getAllEntities() {
        List<GameObject> entities = new CopyOnWriteArrayList<>();
        if (mPlayerPlane != null) entities.add(mPlayerPlane);
        entities.addAll(mBees);
        entities.addAll(mBullets);
        return entities;
    }

    /**
     * clear
     */
    public void clear() {
        mBees.clear();
        mBullets.clear();
        mPlayerPlane = null;
    }

    /**
     * direction
     */
    public enum Direction {
        UP(R.id.btnUp),
        DOWN(R.id.btnDown),
        LEFT(R.id.btnLeft),
        RIGHT(R.id.btnRight);

        private final int mResId;

        // Map: id -> Direction
        private static Map<Integer, Direction> sResIdToDirection = new HashMap<>();
        static {
            for (Direction direction : Direction.values()) {
                sResIdToDirection.put(direction.mResId, direction);
            }
        }
        private Direction(int resId) {
            mResId = resId;
        }

        public static Direction fromResId(int resId) {
            return sResIdToDirection.get(resId);
        }

    }

    private static class Helper {
        private static final GameObjectManager INSTANCE = new GameObjectManager();
    }
}
