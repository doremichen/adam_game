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
import com.adam.app.galaga.utils.GameConstants;

/**
 * Base class for all game objects.
 */
public abstract class GameObject {
    protected final PointF mPosition;
    protected final PointF mTargetPosition;
    protected final int mWidth;
    protected final int mHeight;
    protected float mSpeed;
    protected volatile boolean mIsDead = false;

    public GameObject(float x, float y, float speed, int width, int height) {
        mPosition = new PointF(x, y);
        mTargetPosition = new PointF(x, y);
        mSpeed = speed;
        mWidth = width;
        mHeight = height;
    }

    public PointF getPosition() { return mPosition; }
    public PointF getTargetPosition() { return mTargetPosition; }
    public int getWidth() { return mWidth; }
    public int getHeight() { return mHeight; }
    public float getSpeed() { return mSpeed; }
    public void setDead(boolean dead) { mIsDead = dead; }
    public boolean isDead() { return mIsDead; }

    /**
     * Abstracted boundary clamping algorithm.
     * @return True if the object hit or exceeded the bottom boundary.
     */
    public boolean clampToWorld() {
        mPosition.x = Math.max(0, Math.min(GameConstants.GAME_WIDTH - mWidth, mPosition.x));
        float oldY = mPosition.y;
        mPosition.y = Math.max(0, Math.min(GameConstants.GAME_HEIGHT - mHeight, mPosition.y));
        return oldY >= (GameConstants.GAME_HEIGHT - mHeight);
    }

    /**
     * Updates the provided buffer with the object's collision bounds.
     * Prevents allocation in the game loop.
     */
    public void updateCollisionRect(RectF buffer) {
        buffer.set(mPosition.x, mPosition.y, mPosition.x + mWidth, mPosition.y + mHeight);
    }

    public RectF getRectOfCollision() {
        return new RectF(mPosition.x, mPosition.y, mPosition.x + mWidth, mPosition.y + mHeight);
    }

    public abstract void update();
    public abstract void draw(Canvas canvas, Paint paint);
}
