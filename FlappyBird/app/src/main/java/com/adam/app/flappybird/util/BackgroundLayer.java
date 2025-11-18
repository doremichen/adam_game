/**
 * This class is used to create a background layer for the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-18
 */
package com.adam.app.flappybird.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BackgroundLayer {
    private final Bitmap mBitmap;
    private final float mSpeed;

    private float mX1;
    private float mX2;

    public BackgroundLayer(Bitmap bitmap, float speed, int screenWidth) {
        mBitmap = bitmap;
        mSpeed = speed;
        mX1 = 0;
        mX2 = screenWidth;
    }

    /**
     * update the background layer
     *
     * @param deltaTime the time between frames
     */
    public void update(float deltaTime) {
        float dx = mSpeed * deltaTime;
        mX1 -= dx;
        mX2 -= dx;

        // round looping
        if (mX1 + mBitmap.getWidth() < 0) {
            mX1 = mX2 + mBitmap.getWidth();
        }
        if (mX2 + mBitmap.getWidth() < 0) {
            mX2 = mX1 + mBitmap.getWidth();
        }
    }

    /**
     * draw
     *
     * @param canvas the canvas to draw on
     * @param paint  the paint to use for drawing
     * @param y      the y position to draw at
     */
    public void draw(Canvas canvas, Paint paint, float y) {
        canvas.drawBitmap(mBitmap, mX1, y, paint);
        canvas.drawBitmap(mBitmap, mX2, y, paint);
    }

}
