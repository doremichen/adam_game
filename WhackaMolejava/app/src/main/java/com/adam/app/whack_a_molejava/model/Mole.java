/*
 * MIT License
 *
 * Copyright (c) 2025 Adam Chen
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
package com.adam.app.whack_a_molejava.model;

import android.graphics.PointF;

/**
 * This class is the mole model
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-02
 */
public class Mole {
    // position
    private final PointF mPosition;
    // check visible
    private boolean mIsVisible;
    // time it become visible
    private long mVisibleFrom;
    // time it will hide
    private long mVisibleUntil;
    // mole image index
    private int mImgIndex;

    /**
     * construct
     * @param position
     */
    public Mole(PointF position) {
        mPosition = (position != null) ? position : new PointF(0f, 0f);
        mIsVisible = false;
        mVisibleFrom = 0L;
        mVisibleUntil = 0L;
        mImgIndex = 0;
    }

    // --- setter/getter ---
    public PointF getPosition() {
        return mPosition;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public long getVisibleFrom() {
        return mVisibleFrom;
    }

    public long getVisibleUntil() {
        return mVisibleUntil;
    }

    public int getImgIndex() {
        return mImgIndex;
    }


    public void setVisible(boolean visible) {
        mIsVisible = visible;

        if (visible) {
            mVisibleFrom = System.currentTimeMillis();
        } else {
            mVisibleFrom = 0L;
            mVisibleUntil = 0L;
        }
    }

    public void setVisibleUntil(long visibleUntil) {
        mVisibleUntil = visibleUntil;
    }

    public void setPosition(PointF position) {
        mPosition.set(position);
    }

    public void setImgIndex(int index) {
        mImgIndex = index;
    }
}
