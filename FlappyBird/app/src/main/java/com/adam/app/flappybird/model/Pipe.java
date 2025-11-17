/**
 * This class is the model of the pipe
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.model;

import android.graphics.PointF;

public class Pipe {
    private final PointF mPosition;
    private static final float PIPE_WIDTH = 200f;
    private static final float PIPE_HEIGHT = 300f;

    public final float mSpeed = 6f;

    public Pipe(PointF mPosition) {
        this.mPosition = mPosition;
    }

    public void update() {
        mPosition.x -= mSpeed;
    }

    public PointF getPosition() {
        return mPosition;
    }

    public float getTopPipeBottomY() {
        return mPosition.y;
    }

    public float getBottomPipeTopY() {
        return mPosition.y + PIPE_HEIGHT;
    }

    public float getPipeWidth() {
        return PIPE_WIDTH;
    }
}
