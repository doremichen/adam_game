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
import android.graphics.LinearGradient;
import android.graphics.Shader;

import com.adam.app.galaga.utils.GameConstants;
import java.util.Random;

/**
 * Bullet class - Supports different styles including Piercing Laser with Builder rendering.
 */
public class Bullet extends GameObject {

    private final float mVx;
    private final float mVy;
    private boolean mIsLaser = false;
    private final Random mRandom = new Random();

    public Bullet(float x, float y, float vx, float vy) {
        this(x, y, vx, vy, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT);
    }

    public Bullet(float x, float y, float vx, float vy, int width, int height) {
        super(x, y, 0, width, height);
        this.mVx = vx;
        this.mVy = vy;
    }

    public void setLaser(boolean isLaser) {
        this.mIsLaser = isLaser;
    }

    public boolean isPiercing() {
        return mIsLaser;
    }

    @Override
    public void update() {
        this.mPosition.x += this.mVx;
        this.mPosition.y += this.mVy;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (mIsLaser) {
            drawLaser(canvas, paint);
            return;
        }

        drawNormalBullet(canvas, paint);
    }

    private void drawNormalBullet(Canvas canvas, Paint paint) {
        paint.setShader(null);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(
                mPosition.x, mPosition.y,
                mPosition.x + mWidth, mPosition.y + mHeight,
                5, 5, paint
        );
    }

    private void drawLaser(Canvas canvas, Paint paint) {
        float flicker = mRandom.nextFloat() * 2f;
        
        // Using Builder Pattern to construct the laser's visual layers
        new LaserRenderBuilder(canvas, paint)
                .setBasePosition(mPosition.x, mPosition.y)
                .setDimensions(mWidth + flicker, (float) mHeight)
                .addGlowLayer(6f + flicker)
                .addCoreLayer(Color.CYAN)
                .addCenterBeam(Color.WHITE)
                .addMotionTail(40)
                .render();
    }

    /**
     * Inner Builder for Laser Rendering.
     */
    private static class LaserRenderBuilder {
        private final Canvas mCanvas;
        private final Paint mPaint;
        private float mX, mY, mWidth, mHeight;

        public LaserRenderBuilder(Canvas canvas, Paint paint) {
            this.mCanvas = canvas;
            this.mPaint = paint;
        }

        public LaserRenderBuilder setBasePosition(float x, float y) {
            this.mX = x;
            this.mY = y;
            return this;
        }

        public LaserRenderBuilder setDimensions(float width, float height) {
            this.mWidth = width;
            this.mHeight = height;
            return this;
        }

        public LaserRenderBuilder addGlowLayer(float size) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setShader(new LinearGradient(mX, mY, mX + mWidth, mY,
                    new int[]{Color.TRANSPARENT, Color.parseColor("#6000FFFF"), Color.TRANSPARENT},
                    null, Shader.TileMode.CLAMP));
            mCanvas.drawRect(mX - size, mY, mX + mWidth + size, mY + mHeight, mPaint);
            return this;
        }

        public LaserRenderBuilder addCoreLayer(int color) {
            mPaint.setShader(null);
            mPaint.setColor(color);
            mCanvas.drawRect(mX, mY, mX + mWidth, mY + mHeight, mPaint);
            return this;
        }

        public LaserRenderBuilder addCenterBeam(int color) {
            mPaint.setColor(color);
            float beamWidth = mWidth / 3f;
            float offset = (mWidth - beamWidth) / 2f;
            mCanvas.drawRect(mX + offset, mY, mX + offset + beamWidth, mY + mHeight, mPaint);
            return this;
        }

        public LaserRenderBuilder addMotionTail(float tailLength) {
            mPaint.setColor(Color.parseColor("#3000FFFF"));
            mCanvas.drawRect(mX + 1, mY + mHeight, mX + mWidth - 1, mY + mHeight + tailLength, mPaint);
            return this;
        }

        public void render() {
            // Placeholder for any final processing if needed
        }
    }

    public boolean isOutOfBound() {
        return mPosition.y < -mHeight ||
                mPosition.y > GameConstants.GAME_HEIGHT ||
                mPosition.x < -mWidth ||
                mPosition.x > GameConstants.GAME_WIDTH;
    }
}
