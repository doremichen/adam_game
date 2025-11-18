/**
 * This class is the model of the pipe
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.model;

import android.graphics.PointF;

import com.adam.app.flappybird.util.GameConstants;

public class Pipe {
    private final PointF mPosition;

    private boolean mMarkScored;

    public Pipe(PointF mPosition) {
        this.mPosition = mPosition;
        mMarkScored = false;
    }

    public void update() {
        mPosition.x -= GameConstants.PIPE_SPEED;
    }

    public void update(float deltaTime) {
        mPosition.x -= GameConstants.PIPE_SPEED * deltaTime;
    }

    // --- getter/setter ---
    public PointF getPosition() {
        return mPosition;
    }

    public float getTopPipeBottomY() {
        return mPosition.y - (GameConstants.PIPE_GAP / 2f);
    }

    public float getBottomPipeTopY() {
        return mPosition.y + (GameConstants.PIPE_GAP / 2f);
    }

    public float getRightX() {
        return mPosition.x + GameConstants.PIPE_WIDTH;
    }

    public boolean isMarkScored() {
        return mMarkScored;
    }

    /**
     * set mark pipe as scored
     *
     * @param mark true if scored
     */
    public void setMarkScored(boolean mark) {
        this.mMarkScored = mark;
    }
}
