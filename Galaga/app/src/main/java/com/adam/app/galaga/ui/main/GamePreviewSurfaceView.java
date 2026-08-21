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

package com.adam.app.galaga.ui.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.adam.app.galaga.utils.GameConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GamePreviewSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final int ENEMY_COUNT = 15;
    private static final long FRAME_DELAY = 16L;

    private ScheduledExecutorService mExecutor;
    private final Paint mPaint = new Paint();
    private final List<Enemy> mEnemies = new ArrayList<>();
    private final List<Bullet> mBullets = new ArrayList<>();
    private final Random mRandom = new Random();
    private Player mPlayer;

    public GamePreviewSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    private void startAnimation() {
        if (mExecutor != null && !mExecutor.isShutdown()) {
            return;
        }

        mExecutor = Executors.newSingleThreadScheduledExecutor();
        initObjects();

        mExecutor.scheduleWithFixedDelay(this::updateAndDraw, 0, FRAME_DELAY, TimeUnit.MILLISECONDS);
    }

    private void stopAnimation() {
        if (mExecutor != null) {
            mExecutor.shutdown();
            try {
                mExecutor.awaitTermination(1L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                mExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            mExecutor = null;
        }
    }

    private void initObjects() {
        mEnemies.clear();
        mBullets.clear();
        for (int i = 0; i < ENEMY_COUNT; i++) {
            mEnemies.add(new Enemy());
        }
        mPlayer = new Player();
    }

    private void updateAndDraw() {
        update();
        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            try {
                drawFrame(canvas);
            } finally {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private void update() {
        for (Enemy enemy : mEnemies) {
            enemy.update();
        }

        mPlayer.update();

        if (mRandom.nextInt(100) < 5) {
            mBullets.add(new Bullet(mPlayer.x + 40, mPlayer.y));
        }

        Iterator<Bullet> bIter = mBullets.iterator();
        while (bIter.hasNext()) {
            Bullet bullet = bIter.next();
            bullet.update();
            if (bullet.y < 0) {
                bIter.remove();
            }
        }
    }

    private void drawFrame(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);

        mPaint.setColor(Color.RED);
        for (Enemy enemy : mEnemies) {
            canvas.drawRect(enemy.x, enemy.y, enemy.x + 40, enemy.y + 40, mPaint);
        }

        mPaint.setColor(Color.BLUE);
        canvas.drawRect(mPlayer.x, mPlayer.y, mPlayer.x + 80, mPlayer.y + 80, mPaint);

        mPaint.setColor(Color.YELLOW);
        for (Bullet bullet : mEnemies.isEmpty() ? new ArrayList<Bullet>() : mBullets) {
            canvas.drawRect(bullet.x, bullet.y, bullet.x + 10, bullet.y + 20, mPaint);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        startAnimation();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        stopAnimation();
    }

    private class Enemy {
        float x, y, speed;

        Enemy() {
            reset();
        }

        void reset() {
            x = mRandom.nextInt(Math.max(1, getWidth() - 40));
            y = -mRandom.nextInt(500);
            speed = 2 + mRandom.nextFloat() * 3;
        }

        void update() {
            y += speed;
            if (y > getHeight()) {
                reset();
            }
        }
    }

    private class Player {
        float x, y, targetX;

        Player() {
            y = GameConstants.GAME_HEIGHT - 200f; // Simplified
            x = 500;
            targetX = x;
        }

        void update() {
            if (Math.abs(x - targetX) < 5) {
                targetX = mRandom.nextInt(Math.max(1, getWidth() - 80));
            }
            x += (targetX - x) * 0.1f;
            
            // Adjust Y based on actual height
            if (getHeight() > 0) {
                y = getHeight() - 100;
            }
        }
    }

    private static class Bullet {
        float x, y;

        Bullet(float x, float y) {
            this.x = x;
            this.y = y;
        }

        void update() {
            y -= 15;
        }
    }
}
