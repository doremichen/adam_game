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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = GameSurfaceView.class.getSimpleName();

    private ExecutorService mRenderExecutorService;
    private final SurfaceHolder mHolder;
    private final Paint mPaint = new Paint();
    private List<GameObject> mEntities = new ArrayList<>();
    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        GameUtils.info(TAG, "Construct");
        mHolder = getHolder();
        mHolder.addCallback(this);
        initExecutor();
    }

    private void initExecutor() {
        if (mRenderExecutorService == null || mRenderExecutorService.isShutdown()) {
            mRenderExecutorService = Executors.newSingleThreadExecutor();
        }
    }

    public void updateEntities(List<GameObject> entities) {
        if (entities == null) return;
        this.mEntities = new ArrayList<>(entities);
        initExecutor();
        mRenderExecutorService.execute(this::drawFrame);
    }

    private void drawFrame() {
        Canvas canvas = null;
        try {
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                canvas.save();
                canvas.scale(mScaleX, mScaleY);
                
                for (GameObject entity : mEntities) {
                    entity.draw(canvas, mPaint);
                }
                
                canvas.restore();
            }
        } catch (Exception e) {
            GameUtils.error(TAG, "drawFrame error: " + e.getMessage());
        } finally {
            if (canvas != null) {
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void release() {
        if (mRenderExecutorService != null) {
            mRenderExecutorService.shutdown();
            try {
                if (!mRenderExecutorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    mRenderExecutorService.shutdownNow();
                }
            } catch (InterruptedException ie) {
                mRenderExecutorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
            mRenderExecutorService = null;
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        GameUtils.info(TAG, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        GameUtils.info(TAG, "surfaceChanged: width=" + width + ", height=" + height);
        mScaleX = (float) width / GameConstants.GAME_WIDTH;
        mScaleY = (float) height / GameConstants.GAME_HEIGHT;
        GameUtils.info(TAG, "Scale factors: X=" + mScaleX + ", Y=" + mScaleY);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        GameUtils.info(TAG, "surfaceDestroyed");
        release();
    }
}
