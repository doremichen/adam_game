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

    public boolean mMarkScored;

    public Pipe(PointF mPosition) {
        this.mPosition = mPosition;
        mMarkScored = false;
    }

    public void update() {
        mPosition.x -= mSpeed;
    }

    // --- setter ---

    /**
     * set mark pipe as scored
     * @param mark true if scored
     */
    public void setMarkScored(boolean mark) {
        this.mMarkScored = mark;
    }

    // --- getter ---
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

    public boolean isMarkScored() {
        return mMarkScored;
    }
}
