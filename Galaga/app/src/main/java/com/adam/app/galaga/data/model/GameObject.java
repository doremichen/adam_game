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

package com.adam.app.galaga.data.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Base class for all game objects.
 */
public abstract class GameObject {
    // TAG
    private static final String TAG = GameObject.class.getSimpleName();

    protected final PointF mPosition;
    // width
    protected final int mWidth;
    // height
    protected final int mHeight;
    protected float mSpeed;
    // mark delete flag
    protected volatile boolean mIsDead = false;

    public GameObject(float x, float y, float speed, int width, int height) {
        mPosition = new PointF(x, y);
        mSpeed = speed;
        mWidth = width;
        mHeight = height;
    }

    /**
     * set speed
     *
     * @param speed speed
     */
    public void setSpeed(float speed) {
        mSpeed = speed;
    }

    /**
     * set dead
     * @param dead boolean
     */
    public void setDead(boolean dead) {
        mIsDead = dead;
    }


    /**
     * is dead
     * @return boolean
     */
    public boolean isDead() {
        return mIsDead;
    }


    /**
     * Gets the position of the object.
     *
     * @return The position of the object.
     */
    public PointF getPosition() {
        return mPosition;
    }


    /**
     * get width
     *
     * @return width
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * get height
     *
     * @return height
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * Gets the rect of collision.
     *
     * @return The rect of collision.
     */
    public RectF getRectOfCollision() {
        return new RectF(
                mPosition.x,
                mPosition.y,
                mPosition.x + mWidth,
                mPosition.y + mHeight
        );
    }

    /**
     * update the object.
     */
    public abstract void update();

    /**
     * draw the object.
     *
     * @param canvas canvas
     * @param paint  paint
     */
    public abstract void draw(Canvas canvas, Paint paint);

}
