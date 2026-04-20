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

import com.adam.app.galaga.utils.GameUtils;

/**
 * Bullet class
 */
public class Bullet extends GameObject{

    // TAG
    private static final String TAG = Bullet.class.getSimpleName();

    public Bullet(float x, float y, float speed) {
        super(x, y, speed, 10, 20);
    }

    @Override
    public void update() {
        GameUtils.info(TAG, "update");
        // move up
        this.mPosition.y -= this.mSpeed;
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
        GameUtils.info(TAG, "isOutOfBound");
        return this.mPosition.y < -this.mHeight;
    }

}
