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
 * Bee class
 */
public class Bee extends GameObject{

    // TAG
    private static final String TAG = Bee.class.getSimpleName();


    private boolean mIsDiving;

    public Bee(float x, float y, float speed, int width, int height) {
        super(x, y, speed, width, height);
    }

    @Override
    public void update() {
        GameUtils.info(TAG, "update");
        // is diving
        if (mIsDiving) {
            // move down
            this.mPosition.y += this.mSpeed * 2;
            return;
        }

        mPosition.x += (float) (Math.sin(System.currentTimeMillis() / 500.0) * mSpeed);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        // config paint
        paint.setStyle(Paint.Style.FILL);

        // body
        paint.setColor(Color.YELLOW);
        canvas.drawOval(
                mPosition.x,
                mPosition.y,
                mPosition.x + mWidth,
                mPosition.y + mHeight,
                paint
        );

        // bee
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        canvas.drawLine(mPosition.x + mWidth/3f, mPosition.y, mPosition.x + mWidth/3f, mPosition.y + mHeight, paint);
        canvas.drawLine(mPosition.x + 2*mWidth/3f, mPosition.y, mPosition.x + 2*mWidth/3f, mPosition.y + mHeight, paint);

        // eyes
        paint.setColor(Color.RED);
        canvas.drawCircle(mPosition.x + mWidth * 0.8f, mPosition.y + mHeight * 0.3f, 4, paint);

    }

    /**
     * set Diving
     * @param isDiving
     */
    public void setDiving(boolean isDiving) {
        GameUtils.info(TAG, "setDiving");
        mIsDiving = isDiving;
    }

}
