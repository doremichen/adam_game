/**
 * This class is the model of the bird
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.model;

import android.graphics.PointF;

public class Bird {
    private final PointF mPosition;
    private float mVelocity;
    private final float mGravity = 0.6f;
    private float mLift = -12f;

    /**
     * Bird constructor
     * @param mPosition - initial position
     */
    public Bird(PointF mPosition) {
        this.mPosition = mPosition;
    }

    /**
     * Update bird position
     */
    public void update() {
        mVelocity += mGravity;
        mPosition.y += mVelocity;
    }

    /**
     * Bird flap
     *
     */
    public void flap() {
        mVelocity = mLift;
    }

    /**
     *  get position
     * @return PointF
     */
    public PointF getPosition() {
        return mPosition;
    }

    /**
     *  get velocity
     * @return float
     */
    public float getVelocity() {
        return mVelocity;
    }
}
