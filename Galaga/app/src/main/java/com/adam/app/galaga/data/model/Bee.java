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

import java.util.Random;

/**
 * Bee class
 */
public class Bee extends GameObject{

    // TAG
    private static final String TAG = Bee.class.getSimpleName();


    private boolean mIsDiving;

    // angle
    private float mAngle;
    private final Random mRandom = new Random();
    private long mLastTurnTime;
    private static final int TURN_INTERVAL = 1000; // change angle every 1 sec



    public Bee(float x, float y, float speed, int width, int height) {
        super(x, y, speed, width, height);
        // initial angle
        this.mAngle = (float) (mRandom.nextFloat() * Math.PI * 2);
    }

    @Override
    public void update() {
        //GameUtils.info(TAG, "update");
        float dx = 0;
        float dy = 0;

        // is diving
        if (mIsDiving) {
            // move down
            dy = this.mSpeed * 2.5f;
            dx = 0;
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastTurnTime >= TURN_INTERVAL) {
                mAngle += (float) ((mRandom.nextFloat() - 0.5f) * (Math.PI / 2));
                mLastTurnTime = currentTime;
            }
            dx = (float) (Math.cos(mAngle) * mSpeed);
            dy = (float) (Math.sin(mAngle) * mSpeed);
        }


        mPosition.x += dx;
        mPosition.y += dy;

        float worldWidth = GameUtils.getScreenWidth(); //1080f;
        float playAreaHeight = GameUtils.getScreenHeight(); //1000f;

        // boundary check
        if (mPosition.x < 0) {
            mPosition.x = 0;
            mAngle = (float) (Math.PI - mAngle); // 反彈
        } else if (mPosition.x > worldWidth - mWidth) {
            mPosition.x = worldWidth - mWidth;
            mAngle = (float) (Math.PI - mAngle); // 反彈
        }

        // 限制蜜蜂只在螢幕上半部 (0 ~ 1000) 亂飛，否則會飛到控制區
        if (mPosition.y < 0) {
            mPosition.y = 0;
            mAngle = -mAngle; // 反彈
        } else if (mPosition.y > playAreaHeight - mHeight) {
            mPosition.y = playAreaHeight - mHeight;
            if (mIsDiving) {
                mIsDiving = false;
                mAngle = (float) (-Math.PI / 4 - mRandom.nextFloat() * Math.PI / 2);
            } else {
                mAngle = -mAngle; // 反彈
            }


        }

        //mPosition.x += (float) (Math.sin(System.currentTimeMillis() / 500.0) * mSpeed);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.save();

        // rotate
        float centerX = mPosition.x + mWidth / 2f;
        float centerY = mPosition.y + mHeight / 2f;

        float degrees = (float) Math.toDegrees(mAngle);
        canvas.rotate(degrees, centerX, centerY);

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

        canvas.restore();

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
