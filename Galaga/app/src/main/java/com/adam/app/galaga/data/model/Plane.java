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
import android.graphics.Color;
import android.graphics.Paint;

import com.adam.app.galaga.utils.GameConstants;
import com.adam.app.galaga.utils.GameUtils;

/**
 * Plane class
 */
public class Plane extends GameObject{

    // TAG
    private static final String TAG = Plane.class.getSimpleName();

    private int mAnimationFrame;
    private int mFrameCounter;


    public Plane(float x, float y, float speed, int width, int height) {
        super(x, y, speed, width, height);

        mAnimationFrame = 0;
        mFrameCounter = 0;
    }

    @Override
    public void update() {
        GameUtils.info(TAG, "update");
        updateAnimation();
        clampPosition();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        // config paint
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        // plane body
        canvas.drawRect(
                mPosition.x,
                mPosition.y,
                mPosition.x + mWidth,
                mPosition.y + mHeight,
                paint
        );

        // plane wing
        paint.setColor(Color.CYAN);
        canvas.drawRect(
                mPosition.x + mWidth / 4f,
                mPosition.y - 10,
                mPosition.x + 3 * mWidth / 4f,
                mPosition.y,
                paint
        );

    }

    private void updateAnimation() {
        //GameUtils.info(TAG, "updateAnimation");
        // update animation
        mFrameCounter++;
        if (mFrameCounter >= GameConstants.ANIM_FRAME_DELAY) {
            mAnimationFrame = (mAnimationFrame + 1) % GameConstants.MAX_ANIM_FRAMES;
            mFrameCounter = 0;
        }

    }

    private void clampPosition() {
        if (mPosition.x < 0) mPosition.x = 0;
        if (mPosition.x > GameConstants.REFERENCE_SCREEN_WIDTH - mWidth) {
            mPosition.x = GameConstants.REFERENCE_SCREEN_WIDTH - mWidth;
        }
    }

    /**
     * move left
     */
    public void moveLeft() {
        //GameUtils.info(TAG, "moveLeft");
        this.mPosition.x -= this.mSpeed;
        // boundary check
        if (this.mPosition.x < 0) {
            this.mPosition.x = 0;
        }
    }

    /**
     * move right
     */
    public void moveRight() {
        //GameUtils.info(TAG, "moveRight");
        this.mPosition.x += this.mSpeed;
        // boundary check
        if (this.mPosition.x > GameConstants.REFERENCE_SCREEN_WIDTH - this.mWidth) {
            this.mPosition.x = GameConstants.REFERENCE_SCREEN_WIDTH - this.mWidth;
        }
    }

    /**
     * move up
     */
    public void moveUp() {
        //GameUtils.info(TAG, "moveUp");
        this.mPosition.y -= this.mSpeed;
        // boundary check
        if (this.mPosition.y < 0) {
            this.mPosition.y = 0;
        }
    }

    /**
     * move down
     */
    public void moveDown() {
        //GameUtils.info(TAG, "moveDown");
        this.mPosition.y += this.mSpeed;
        // boundary check
        if (this.mPosition.y > GameConstants.REFERENCE_SCREEN_HEIGHT - this.mHeight) {
            this.mPosition.y = GameConstants.REFERENCE_SCREEN_HEIGHT - this.mHeight;
        }
    }

}
