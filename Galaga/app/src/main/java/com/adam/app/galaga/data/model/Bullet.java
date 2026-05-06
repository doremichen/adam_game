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

/**
 * Bullet class
 */
public class Bullet extends GameObject{

    // vector position
    private final float mVx;
    private final float mVy;


    public Bullet(float x, float y, float vx, float vy) {
        super(x, y, 0, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT);
        this.mVx = vx;
        this.mVy = vy;
    }


    public Bullet(float x, float y, float speed) {
        super(x, y, speed, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT);
        this.mVx = 0.0f;
        this.mVy = -speed;
    }

    @Override
    public void update() {
        // move
        this.mPosition.x += this.mVx;
        this.mPosition.y += this.mVy;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        // config paint
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        // body
        canvas.drawRoundRect(
                mPosition.x,
                mPosition.y,
                mPosition.x + mWidth,
                mPosition.y + mHeight,
                5, 5, // radius
                paint
        );

    }

    /**
     * Use to check out of range
     */
    public boolean isOutOfBound() {
        return mPosition.y < -mHeight ||
                mPosition.y > GameConstants.GAME_HEIGHT ||
                mPosition.x < -mWidth ||
                mPosition.x > GameConstants.GAME_WIDTH;
    }

}
