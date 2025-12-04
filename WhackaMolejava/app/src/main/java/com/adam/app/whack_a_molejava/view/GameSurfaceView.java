/**
 * Copyright 2025 Adam Game
 * <p>
 * This class is the game surface view that handle the display of the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-03
 *
 */
package com.adam.app.whack_a_molejava.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.adam.app.whack_a_molejava.R;
import com.adam.app.whack_a_molejava.controller.GameEngine;
import com.adam.app.whack_a_molejava.model.Mole;
import com.adam.app.whack_a_molejava.util.GameUtils;
import com.adam.app.whack_a_molejava.viewmodels.GameViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "GameSurfaceView";

    // Game model view
    private GameViewModel mGameViewModel;

    // paint
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // schedule executor service
    private final ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private Future<?> mFuture;

    // Bitmap
    private Bitmap mMoleBitmap;

    // scaled size will be computed per frame
    private final List<PointF> mCellCenters = new ArrayList<>(9);

    // grid size
    private float mCellW, mCellH;
    private boolean mCentersComputed = false;

    // touch radius in px (converted from dp)
    private float mTouchRadiusPx;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);

        // load resource safely
        try {
            mMoleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mole);
        } catch (Exception e) {
            GameUtils.log(TAG, "mole bitmap load failed: " + e.getMessage());
            mMoleBitmap = null;
        }
        // default touch radius 48dp
        mTouchRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, getResources().getDisplayMetrics());

    }

    // Ensure centers recalculated when view size changes (more reliable than lazily at draw time)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCentersComputed = false; // force recompute on next draw
        GameUtils.log(TAG, "onSizeChanged -> force compute centers");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        GameUtils.log(TAG, "surfaceChanged");
        // do nothing
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        GameUtils.log(TAG, "surfaceCreated");
        // schedule run at fixed rate (60ms tick)
        if (mFuture == null || mFuture.isCancelled()) {
            GameUtils.log(TAG, "schedule run");
            mFuture = mScheduledExecutorService.scheduleWithFixedDelay(this, 0L, 60L, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        GameUtils.log(TAG, "surfaceDestroyed");

            release();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGameViewModel == null) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // if not RUN, ignore touches (prevent hitting while paused)
            if (isGameRunning()) {
                // forward touch to view model (or to engine via viewmodel)
                // but convert touch radius usage in engine; here we pass raw coordinates
                mGameViewModel.hitMole(event.getX(), event.getY());
            }
        }
        return true;
    }

    private boolean isGameRunning() {
        return mGameViewModel != null && mGameViewModel.getState() == GameEngine.WAMGameState.RUN;
    }

    @Override
    public void run() {
        GameUtils.log(TAG, "run");
        logState();
        // check game status
        // We always draw the static UI. If game is running we update+animate moles.
        boolean running = isGameRunning();
        if (!running) {
            GameUtils.log(TAG, "game not running");
            return;
        }

        try {
            // guard
            if (Thread.currentThread().isInterrupted()) {
                GameUtils.log(TAG, "thread interrupted");
                return;
            }
            if (!getHolder().getSurface().isValid()) {
                GameUtils.log(TAG, "surface not valid");
                return;
            }

            // 1) update game logic first
            mGameViewModel.update();
            // 2) then draw frame
            Canvas canvas = getHolder().lockCanvas();
            if (canvas == null) {
                GameUtils.log(TAG, "canvas is null");
                return;
            }
            try {
                drawFrame(canvas);
            } finally {
                getHolder().unlockCanvasAndPost(canvas);
            }
        } catch (Exception e) {
            GameUtils.log(TAG, "run exception: " + e.getMessage());
        }

    }

    private void logState() {
        if (mGameViewModel == null) {
            GameUtils.log(TAG, "mGameViewModel is null");
            return;
        }

        GameUtils.log(TAG,"state: " + mGameViewModel.getState());
    }

    private void drawFrame(Canvas canvas) {
        GameUtils.log(TAG, "drawFrame");
        int width = getWidth();
        int height = getHeight();

        GameUtils.log(TAG, "width: " + width + ", height: " + height);


        // background
        canvas.drawColor(Color.BLACK);

        // compute sizes and centers once or when size changes
        float topReserve = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, getResources().getDisplayMetrics());
        float gridTop = topReserve;
        float gridHeight = height - gridTop;
        mCellW = width / 3f;
        mCellH = gridHeight / 3f;

        GameUtils.log(TAG, "mCentersComputed: " + mCentersComputed);

        // recompute centers if needed
        if (!mCentersComputed) {
            computeCellCenters(gridTop);
            mCentersComputed = true;
        }

        // draw grid lines
        GameUtils.log(TAG, "draw grid lines");
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(6f);
        canvas.drawLine(mCellW, gridTop, mCellW, height, mPaint);
        canvas.drawLine(mCellW * 2, gridTop, mCellW * 2, height, mPaint);
        canvas.drawLine(0, gridTop + mCellH, width, gridTop + mCellH, mPaint);
        canvas.drawLine(0, gridTop + mCellH * 2, width, gridTop + mCellH * 2, mPaint);

        // draw holes
        GameUtils.log(TAG, "draw holes");
        mPaint.setColor(Color.rgb(60, 40, 20));
        float holeRadiusX = mCellW * 0.32f;
        float holeRadiusY = mCellH * 0.18f;
        for (PointF center : mCellCenters) {
            float left = center.x - holeRadiusX;
            float top = center.y - holeRadiusY;
            float right = center.x + holeRadiusX;
            float bottom = center.y + holeRadiusY;
            canvas.drawOval(left, top, right, bottom, mPaint);
        }

        // draw moles (use scaling based on visibleFrom/visibleUntil for pop animation)
        if (mGameViewModel == null) {
            GameUtils.log(TAG, "mGameViewModel is null");
            return;
        }
        List<Mole> moles = mGameViewModel.getMoles();
        long now = System.currentTimeMillis();
        GameUtils.log(TAG, "draw moles");
        for (Mole mole : moles) {
            if (!mole.isVisible()) continue;
            PointF pos = mole.getPosition();
            long visibleFrom = mole.getVisibleFrom();
            long visibleUntil = mole.getVisibleUntil();
            long duration = Math.max(1, visibleUntil - visibleFrom);
            long elapsed = now - visibleFrom;
            if (elapsed < 0) elapsed = 0;
            float t = Math.min(1f, (float) elapsed / (float) duration);
            // scale: ease-in-out using sin
            float scale = 0.6f + 0.4f * (float)Math.sin(t * Math.PI);

            if (mMoleBitmap != null && !mMoleBitmap.isRecycled()) {
                // compute target size proportional to cell
                float targetW = mCellW * 0.6f;
                float targetH = mCellH * 0.7f;
                float bmpW = mMoleBitmap.getWidth();
                float bmpH = mMoleBitmap.getHeight();
                float sx = (targetW / bmpW) * scale;
                float sy = (targetH / bmpH) * scale;

                Matrix matrix = new Matrix();
                matrix.postTranslate(-bmpW / 2f, -bmpH / 2f);
                matrix.postScale(sx, sy);
                matrix.postTranslate(pos.x, pos.y - mCellH * 0.08f); // slightly raised visually
                canvas.drawBitmap(mMoleBitmap, matrix, mPaint);
            } else {
                // fallback: circle
                mPaint.setColor(Color.RED);
                float radius = Math.min(mCellW, mCellH) * 0.22f * scale;
                canvas.drawCircle(pos.x, pos.y - mCellH * 0.08f, radius, mPaint);
            }
        }
    }

    private void computeCellCenters(float gridTop) {
        GameUtils.log(TAG, "computeCellCenters");
        mCellCenters.clear();
        float width = getWidth();
        float height = getHeight();
        mCellW = width / 3f;
        float gridHeight = height - gridTop;
        mCellH = gridHeight / 3f;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                float cx = mCellW * col + mCellW / 2f;
                float cy = gridTop + mCellH * row + mCellH / 2f;
                mCellCenters.add(new PointF(cx, cy));
            }
        }

        // push positions to engine via viewModel if possible
        if (mGameViewModel != null) {
            // backup cell positions
            mGameViewModel.backupCellPositions(mCellCenters);
            // set cell positions
            mGameViewModel.setMolePositions(mCellCenters);
        }
    }

    /**
     * bind view model
     */
    public void bind(GameViewModel viewModel) {
        mGameViewModel = viewModel;
        if (mCentersComputed && mGameViewModel != null) {
            List<PointF> copy = new ArrayList<>(mCellCenters);
            mGameViewModel.setMolePositions(copy);
        }
    }

    public void release() {
        if (mFuture != null) mFuture.cancel(true);
        mScheduledExecutorService.shutdownNow();
        if (mMoleBitmap != null && !mMoleBitmap.isRecycled()) {
            mMoleBitmap.recycle();
            mMoleBitmap = null;
        }
    }


}
