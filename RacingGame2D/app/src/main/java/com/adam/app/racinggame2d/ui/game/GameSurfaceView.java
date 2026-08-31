/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.ui.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.application.GameEngine;
import com.adam.app.racinggame2d.domain.entity.Obstacle;
import com.adam.app.racinggame2d.util.GameImageLoader;
import com.adam.app.racinggame2d.util.Utils;

/**
 * SurfaceView that handles the rendering of the game.
 */
public final class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, GameEngine.GameUpdateListener {
    private static final String sTAG = "GameSurfaceView";

    private final Paint mPaint;
    private GameViewModel mViewModel;
    private Bitmap mCarBitmap;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4f);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Utils.logDebug(sTAG, "surfaceCreated");
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }

    public void setViewModel(@NonNull GameViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @Override
    public void onUpdate() {
        if (mViewModel == null) return;
        mViewModel.updateScore();
        mViewModel.updateHp();

        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            drawGame(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void drawGame(@NonNull Canvas canvas) {
        if (mViewModel == null || !mViewModel.isReady()) return;

        canvas.drawColor(Color.parseColor("#1A1A2E"));
        drawBackground(canvas);

        mPaint.setColor(Color.YELLOW);
        mPaint.setAlpha(255);
        for (PointF point : mViewModel.getCheckpoints()) {
            Bitmap bitmap = GameImageLoader.load(getContext(), "images/flag.png", 80, 80);
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, point.x - bitmap.getWidth() / 2f, point.y - bitmap.getHeight() / 2f, mPaint);
            }
        }

        for (Obstacle obstacle : mViewModel.getObstacles()) {
            PointF pos = obstacle.getPosition();
            String path = "images/" + obstacle.getType().name().toLowerCase() + ".png";
            Bitmap bitmap = GameImageLoader.load(getContext(), path, 80, 80);
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, pos.x - bitmap.getWidth() / 2f, pos.y - bitmap.getHeight() / 2f, mPaint);
            }
        }

        drawCar(canvas);
    }

    private void drawCar(@NonNull Canvas canvas) {
        PointF pos = mViewModel.getCarPosition();
        if (mCarBitmap == null) {
            Bitmap raw = BitmapFactory.decodeResource(getResources(), R.drawable.car);
            if (raw != null) {
                Bitmap scaled = Bitmap.createScaledBitmap(raw, 120, 180, true);
                mCarBitmap = removeWhiteBackground(scaled);
                raw.recycle();
            }
        }

        if (mCarBitmap != null) {
            float angle = mViewModel.getCarRotationAngle();
            canvas.save();
            canvas.translate(pos.x, pos.y);
            canvas.rotate(angle);
            canvas.drawBitmap(mCarBitmap, -mCarBitmap.getWidth() / 2f, -mCarBitmap.getHeight() / 2f, mPaint);
            canvas.restore();
        } else {
            mPaint.setColor(Color.RED);
            canvas.drawRect(pos.x - 30, pos.y - 50, pos.x + 30, pos.y + 10, mPaint);
        }
    }

    /**
     * Helper to make white background transparent.
     */
    private Bitmap removeWhiteBackground(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels.length; i++) {
            int color = pixels[i];
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            // If the pixel is close to white, make it transparent
            if (r > 245 && g > 245 && b > 245) {
                pixels[i] = Color.TRANSPARENT;
            }
        }
        dest.setPixels(pixels, 0, width, 0, 0, width, height);
        return dest;
    }

    private void drawBackground(@NonNull Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int gridSize = 120;

        mPaint.setColor(Color.parseColor("#4FACFE"));
        mPaint.setStrokeWidth(1f);
        mPaint.setAlpha(40);

        for (int x = 0; x <= width; x += gridSize) {
            canvas.drawLine(x, 0, x, height, mPaint);
        }
        for (int y = 0; y <= height; y += gridSize) {
            canvas.drawLine(0, y, width, y, mPaint);
        }

        // Add some glowing highlights to the grid
        mPaint.setStrokeWidth(3f);
        mPaint.setAlpha(20);
        canvas.drawLine(width / 2f, 0, width / 2f, height, mPaint);
    }
}
