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

package com.adam.app.galaga.ui.game;

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
import com.adam.app.galaga.utils.GameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // TAG
    private static final String TAG = GameSurfaceView.class.getSimpleName();

    // Render Executor service
    private ScheduledExecutorService mRenderExecutorService;
    private Future<?> mRenderFuture;

    private final Paint mCommonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private volatile List<GameObject> mEntities = new ArrayList<>();

    // Scale x, y
    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;


    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        GameUtils.info(TAG, "Construct");
        getHolder().addCallback(this);
        initExecutor();
    }


    /**
     * update Entities
     */
    public void updateEntities(List<GameObject> entities) {
        GameUtils.info(TAG, "updateEntities");
        mEntities = entities;
    }


    private void initExecutor() {
        if (mRenderExecutorService == null || mRenderExecutorService.isShutdown()) {
            mRenderExecutorService = Executors.newSingleThreadScheduledExecutor();
        }
    }


    private void startRender() {
        GameUtils.info(TAG, "startRender");
        initExecutor(); // assure executor service is live
        if (mRenderFuture != null && !mRenderFuture.isCancelled()) {
            GameUtils.info(TAG, "Render is already running");
            return;
        }

        mRenderFuture = mRenderExecutorService.scheduleWithFixedDelay(
                this::renderLoop,
                0,
                GameConstants.FRAME_PERIOD_MS,
                TimeUnit.MILLISECONDS
        );
    }

    private void renderLoop() {
        // draw
        SurfaceHolder holder = getHolder();
        if (!holder.getSurface().isValid()) {
            GameUtils.info(TAG, "Surface is not valid");
            return;
        }
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas();
            if (canvas == null) {
                GameUtils.info(TAG, "Canvas is null");
                return;
            }
            // save canvas
            canvas.save();

            // clear canvas
            canvas.drawColor(Color.BLACK);

            // scale canvas
            canvas.scale(mScaleX, mScaleY);

            // draw object
            List<GameObject> snapshot = mEntities;
            for (GameObject obj : snapshot) {
                obj.draw(canvas, mCommonPaint);
            }
        } catch (Exception e) {
            GameUtils.error(TAG, "renderLoop error");
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void stopRender() {
        GameUtils.info(TAG, "stopRender");
        if (mRenderFuture == null) {
            GameUtils.info(TAG, "Render is not running");
            return;
        }

        if (mRenderFuture.isCancelled()) {
            GameUtils.info(TAG, "Render is already stopped");
            return;
        }

        mRenderFuture.cancel(true);
        mRenderFuture = null;
    }

    public void release() {
        GameUtils.info(TAG, "release");
        stopRender();
        mRenderExecutorService.shutdownNow();

        try {
            if (!mRenderExecutorService.awaitTermination(1000, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                // shutdown now
                mRenderExecutorService.shutdownNow();
                GameUtils.info(TAG, "render service is  shutdown!!");
            }
        } catch (InterruptedException ie) {
            mRenderExecutorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }


    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        GameUtils.info(TAG, "surfaceChanged: ");
        GameUtils.info(TAG, "width=" + width + ", height=" + height);
        GameUtils.setScreenSize(width, height);
        mScaleX = (float) width / GameConstants.GAME_WIDTH;
        mScaleY = (float) height / GameConstants.GAME_HEIGHT;

        GameUtils.info(TAG, "Scale factors: X=" + mScaleX + ", Y=" + mScaleY);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        GameUtils.info(TAG, "surfaceCreated");
        // start render
        startRender();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        GameUtils.info(TAG, "surfaceDestroyed");
        stopRender();
    }
}
