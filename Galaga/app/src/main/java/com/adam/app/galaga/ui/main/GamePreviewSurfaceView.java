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

import com.adam.app.galaga.data.model.GameObject;
import com.adam.app.galaga.utils.GameConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GamePreviewSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // TAG
    private static final String TAG = "GamePreviewSurfaceView";
    // Random
    private final Random mRandom = new Random();
    // --- Game Entities ---
    private Fighter mFighter;
    private final List<Bee> mEnemies = new ArrayList<>();
    private final List<Bullet> mBullets = new ArrayList<>();
    // ScheduledExecutorService
    private volatile ScheduledExecutorService mExecutor;
    // Paint
    private final Paint mPaint = new Paint();


    public GamePreviewSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // add Callback
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // init game
        initGame();
        // init executor service
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        // start schedule
        this.mExecutor.scheduleWithFixedDelay(this::updateAndRender, 0, 16L, TimeUnit.MILLISECONDS);

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        // shutdown executor
        if (this.mExecutor == null) return;

        try {
            mExecutor.shutdown();
            if (mExecutor.awaitTermination(3L, TimeUnit.SECONDS)) {
                mExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void initGame() {
        // init plan
        this.mFighter = new Fighter(this.getWidth() / 2f, this.getHeight() - 150f, getWidth(), getHeight());
        spawnEnemies();
    }

    private void spawnEnemies() {
        // clear
        this.mEnemies.clear();
        // build list of enemy
        final int ENEMY_COUNT = 5;
        for (int i = 0; i < ENEMY_COUNT; i++) {
            this.mEnemies.add(new Bee(
                    mRandom.nextInt(this.getWidth()),
                    mRandom.nextInt(getHeight() / 2),
                    getWidth(),
                    getHeight()));
        }
    }

    private void updateAndRender() {
        SurfaceHolder holder = getHolder();
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) return;
        try {
            // update background
            canvas.drawColor(Color.BLACK);
            // plan
            this.mFighter.update();
            this.mFighter.draw(canvas, this.mPaint);
            // fire
            if (this.mFighter.shouldFire()) { // 每 500ms 左右發射
                mBullets.add(new Bullet(mFighter.getPosition().x, mFighter.getPosition().y));
            }

            Iterator<Bullet> bIter = mBullets.iterator();
            while (bIter.hasNext()) {
                Bullet b = bIter.next();
                b.update();
                b.draw(canvas, mPaint);
                if (b.getPosition().y < 0) bIter.remove(); // when out of y bound the bullet is removed
            }

            // update bee
            Iterator<Bee> eIter = mEnemies.iterator();
            while (eIter.hasNext()) {
                Bee bee = eIter.next();
                bee.update();
                bee.draw(canvas, mPaint);

                // detect collision
                for (Bullet b: mBullets) {
                    if (Math.abs(b.getPosition().x - bee.getPosition().x) < 40 && Math.abs(b.getPosition().y - bee.getPosition().y) < 40) {
                        bee.setDead(true);
                        break;
                    }
                }
                // when bee is dead that would be removed
                if (bee.isDead()) {
                    eIter.remove();
                }
            }

            // reset when the enemies is empty
            if (mEnemies.isEmpty()) {
                spawnEnemies();
            }

        } finally {
            holder.unlockCanvasAndPost(canvas);
        }
    }

    // Game entity
    private static class Fighter extends GameObject {

        private long mLastFireTime;

        public Fighter(float x, float y, int width, int height) {
            super(x, y, 5f, width, height);
            this.mLastFireTime = System.currentTimeMillis();
        }


        public boolean shouldFire() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastFireTime >= GameConstants.AUTO_FIRE_INTERVAL) {
                mLastFireTime = currentTime;
                return  true;
            }
            return  false;
        }



        @Override
        public void update() {
            this.mPosition.x += this.mSpeed;
            if (this.mPosition.x < 50 ||
                    this.mPosition.x > this.mWidth - 50) {
                // change speed direction
                this.mSpeed *= -1;
            }
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            paint.setColor(Color.WHITE);
            // body
            canvas.drawRect(
                    this.mPosition.x - 30,
                    this.mPosition.y,
                    this.mPosition.x + 30,
                    this.mPosition.y + 40,
                    paint
            );
            // head
            paint.setColor(Color.RED);
            canvas.drawRect(
                    this.mPosition.x - 5,
                    this.mPosition.y - 10,
                    this.mPosition.x + 5,
                    this.mPosition.y,
                    paint
            );

        }
    }

    private static class Bullet extends GameObject {

        public Bullet(float x, float y) {
            super(x, y, 15f, 0, 0);
        }

        @Override
        public void update() {
            // update position
            this.mPosition.y -= this.mSpeed;
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(this.mPosition.x, this.mPosition.y, 8f, paint);
        }
    }

    private static class Bee extends GameObject {
        private boolean mIsDead = false;
        private float mVx;
        private float mVy;

        public Bee(float x, float y, int width, int height) {
            super(x, y, 0f, width, height);
            this.mVx = (new Random().nextFloat() - 0.5f) * 10;
            this.mVy = (new Random().nextFloat() - 0.5f) * 10;
        }

        @Override
        public void update() {
            // update position
            this.mPosition.x += this.mVx;
            this.mPosition.y += this.mVy;
            // boundary check
            if (this.mPosition.x < 0 || this.mPosition.x > this.mWidth) {
                this.mVx *= -1f;
            }
            if (this.mPosition.y < 0 || this.mPosition.y > this.mHeight / 2f) {
                this.mVy *= -1f;
            }
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            paint.setColor(Color.MAGENTA);
            canvas.drawCircle(this.mPosition.x, this.mPosition.y, 25f, paint);
        }

        @Override
        public boolean isDead() {
            return mIsDead;
        }

        @Override
        public void setDead(boolean dead) {
            mIsDead = dead;
        }
    }


}
