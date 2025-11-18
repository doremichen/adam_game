/**
 * This class is the model of the bird
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.model;

import android.graphics.PointF;

import com.adam.app.flappybird.util.GameConstants;

public class Bird {
    private final PointF mPosition;
    private float mVelocityY;

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
        mVelocityY += GameConstants.GRAVITY;
        mPosition.y += mVelocityY;
    }

    /**
     * Update bird position
     *
     * @param deltaSec delta time
     */
    public void update(float deltaSec) {
        mVelocityY += GameConstants.GRAVITY * deltaSec;
        mPosition.y += mVelocityY * deltaSec;
    }

    /**
     * Bird flap
     *
     */
    public void flap() {
        mVelocityY = GameConstants.FLAP_VELOCITY;
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
        return mVelocityY;
    }
}
