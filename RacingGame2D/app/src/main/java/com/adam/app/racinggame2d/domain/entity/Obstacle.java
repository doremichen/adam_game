/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.domain.entity;

import android.graphics.PointF;

/**
 * Domain entity representing an obstacle.
 */
public final class Obstacle {
    private final Type mType;
    private final int mImageRes;
    private final PointF mPosition;
    private final float mRadius;

    public Obstacle(PointF position, float radius, Type type, int imageRes) {
        this.mPosition = position;
        this.mRadius = radius;
        this.mType = type;
        this.mImageRes = imageRes;
    }

    public PointF getPosition() {
        return mPosition;
    }

    public float getRadius() {
        return mRadius;
    }

    public Type getType() {
        return mType;
    }

    public int getImageRes() {
        return mImageRes;
    }

    public enum Type {
        OIL, ROCK, BOOST, NONE
    }
}
