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

import android.graphics.PointF;
import android.graphics.RectF;

import com.adam.app.galaga.data.model.Bee;
import com.adam.app.galaga.data.model.Bullet;
import com.adam.app.galaga.data.model.GameObject;
import com.adam.app.galaga.data.model.Plane;

import java.util.Iterator;
import java.util.List;

/**
 * This is used to manage collisions between game objects.
 */
public class CollisionManager {

    // define rect buffer
    private final RectF mRectBufferA = new RectF();
    private final RectF mRectBufferB = new RectF();

    /**
     * Handle collisions bullets with bees.
     *
     * @param bullets List of Bullet
     * @param bees    List of Bee
     * @return total score
     */
    public int handleCollisions(List<Bullet> bullets, List<Bee> bees) {
        int hitCount = 0;
        for (Bullet bullet : bullets) {
            // is dead continue
            if (bullet.isDead()) {
                continue;
            }

            // update buffer
            updateBuffer(mRectBufferA, bullet);

            for (Bee bee : bees) {
                // is dead continue
                if (bee.isDead()) {
                    continue;
                }

                // update buffer
                updateBuffer(mRectBufferB, bee);

                // collision
                if (RectF.intersects(mRectBufferA, mRectBufferB)) {
                    // set dead
                    bullet.setDead(true);
                    bee.setDead(true);
                    hitCount++;
                    break;
                }
            }
        }
        return hitCount;
    }

    /**
     * Handle collisions between plane and bees.
     *
     * @param plane Plane
     * @param bees  List of Bee
     * @return is game over
     */
    public boolean isPlaneHit(Plane plane, List<Bee> bees) {
        // early return
        if (plane == null || bees == null) {
            return false;
        }

        // update buffer
        updateBuffer(mRectBufferA, plane);

        for (Bee bee : bees) {
            // update buffer
            updateBuffer(mRectBufferB, bee);

            // collision
            if (RectF.intersects(mRectBufferA, mRectBufferB)) {
                return true;
            }
        }
        return false;
    }


    /**
     * updateBuffer
     * @param buffer RectF
     * @param object GameObject
     */
    private void updateBuffer(RectF buffer, GameObject object) {
        PointF position = object.getPosition();
        buffer.set(
                position.x,
                position.y,
                position.x + object.getWidth(),
                position.y + object.getHeight()
        );
    }


}
