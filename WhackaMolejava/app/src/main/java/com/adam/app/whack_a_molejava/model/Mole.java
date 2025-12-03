/**
 * Copyright 2025 Adam Game
 * <p>
 * This class is the mole model
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-02
 *
 */
package com.adam.app.whack_a_molejava.model;

import android.graphics.PointF;

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
