/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the demo snake game surface view
 * <p>
 * Author: Adam Chen
 * Date: 2025/12/01
 */
package com.adam.app.snake.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.adam.app.snake.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DemoSnakeSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG_DEMO_SNAKE_SURFACE_VIEW = "DemoSnakeSurfaceView";

    // cell size
    private static final float CELL_SIZE = 32f;

    // paint
    private final Paint mSnakePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mFoodPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mHeadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // position of snake
    private final List<PointF> mPosition = new ArrayList<>();
    // direction
    private final Point mDirection = new Point(1, 0);
    // position of food
    private final PointF mFoodPosition = new PointF(0f, 0f);
    // draw service
    private DrawTask mDrawTask;
    // time tracking ( 1 min reset)
    private long mDemoStartTime = 0L;


    public DemoSnakeSurfaceView(Context context) {
        super(context);
        init();
    }

    public DemoSnakeSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DemoSnakeSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // config paint
        mSnakePaint.setColor(Color.GREEN);
        mSnakePaint.setStyle(Paint.Style.FILL);
        mFoodPaint.setColor(Color.RED);
        mFoodPaint.setStyle(Paint.Style.FILL);
        mHeadPaint.setColor(Color.rgb(0, 150, 0));
        mHeadPaint.setStyle(Paint.Style.FILL);

        // set callback
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        // assure the food in the center
        clampFoodInside(width, height);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // init snake and food
        resetSnake();
        randomFood();

        // start to schedule draw
        mDrawTask = new DrawTask(surfaceHolder);
        mDrawTask.start(this::onDrawView);

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        // stop
        mDrawTask.stop();
        mDrawTask.release();
    }

    private void resetSnake() {
        // clear snake
        mPosition.clear();
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        // random head position
        float startX = (float)((int)(Math.random() * (w / CELL_SIZE)) * CELL_SIZE);
        float startY = (float)((int)(Math.random() * (h / CELL_SIZE)) * CELL_SIZE);

        // add 3 cells
        mPosition.add(new PointF(startX, startY));
        mPosition.add(new PointF(startX - CELL_SIZE, startY));
        mPosition.add(new PointF(startX - CELL_SIZE * 2, startY));

        // initial direction -> move toward food later
        mDirection.set(1, 0);

        mDemoStartTime = System.currentTimeMillis();
    }

    private void randomFood() {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        int cols = (int) (w / CELL_SIZE);
        int rows = (int) (h / CELL_SIZE);

        int gx = (int) (Math.random() * cols);
        int gy = (int) (Math.random() * rows);

        mFoodPosition.set(gx * CELL_SIZE, gy * CELL_SIZE);
    }


    private void clampFoodInside(int width, int height) {
        int maxX = Math.max(1, (int) (width / CELL_SIZE));
        int maxY = Math.max(1, (int) (height / CELL_SIZE));

        int gx = (int) Math.floor(mFoodPosition.x / CELL_SIZE);
        int gy = (int) Math.floor(mFoodPosition.y / CELL_SIZE);
        gx = Math.min(Math.max(gx, 0), maxX - 1);
        gy = Math.min(Math.max(gy, 0), maxY - 1);
        mFoodPosition.set(gx * CELL_SIZE, gy * CELL_SIZE);
    }

    private void onDrawView(Canvas canvas) {
        // updateSnake
        updateSnake();
        // draw background
        canvas.drawColor(Color.BLACK);
        // draw food
        canvas.drawRect(
                mFoodPosition.x,
                mFoodPosition.y,
                mFoodPosition.x + CELL_SIZE,
                mFoodPosition.y + CELL_SIZE,
                mFoodPaint
        );
        // draw snake
        for (int i = 0; i < mPosition.size(); i++) {
            PointF p = mPosition.get(i);
            Paint paint = (i == 0) ? mHeadPaint : mSnakePaint;
            float cellSize = CELL_SIZE;
            canvas.drawRect(p.x, p.y, p.x + cellSize, p.y + cellSize, paint);
        }
    }

    private void updateSnake() {
        Utils.logDebug(TAG_DEMO_SNAKE_SURFACE_VIEW, "updateSnake");

        // 1 min auto reset
        if (System.currentTimeMillis() - mDemoStartTime > 60_000L) {
            resetSnake();
            randomFood();
            return;
        }

        if (mPosition.isEmpty()) {
            Utils.logDebug(TAG_DEMO_SNAKE_SURFACE_VIEW, "mPosition is empty so can not update snake!!!");
            return;
        }

        final float width = getWidth();
        final float height = getHeight();
        //Utils.logDebug(TAG_DEMO_SNAKE_SURFACE_VIEW, "width: " + width + ", height: " + height);


        PointF head = mPosition.get(0);

        // --- 1) Decide direction toward food (horizontal or vertical only)
        float dx = mFoodPosition.x - head.x;
        float dy = mFoodPosition.y - head.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            // move horizontally
            mDirection.x = dx > 0 ? 1 : -1;
            mDirection.y = 0;
        } else {
            // move vertically
            mDirection.y = dy > 0 ? 1 : -1;
            mDirection.x = 0;
        }

        // --- 2) Boundary check → change moving axis
        float nextX = head.x + mDirection.x * CELL_SIZE;
        float nextY = head.y + mDirection.y * CELL_SIZE;
        Utils.logDebug(TAG_DEMO_SNAKE_SURFACE_VIEW, "nextX: " + nextX + ", nextY: " + nextY + ", dx: " + dx + ", dy: " + dy);

        if (nextX < 0 || nextX + CELL_SIZE > width) {
            Utils.logDebug(TAG_DEMO_SNAKE_SURFACE_VIEW, "nextX < 0 || nextX + CELL_SIZE > width");
            // hit x boundary → switch to vertical
            mDirection.x = 0;
            mDirection.y = dy > 0 ? 1 : -1;
            nextX = head.x;
            nextY = head.y + mDirection.y * CELL_SIZE;
        }

        if (nextY < 0 || nextY + CELL_SIZE > height) {
            Utils.logDebug(TAG_DEMO_SNAKE_SURFACE_VIEW, "nextY < 0 || nextY + CELL_SIZE > height");
            // hit y boundary → switch to horizontal
            mDirection.y = 0;
            mDirection.x = dx > 0 ? 1 : -1;
            nextX = head.x + mDirection.x * CELL_SIZE;
            nextY = head.y;
        }

        // --- 3) Move snake
        PointF nextHead = new PointF(nextX, nextY);
        mPosition.add(0, nextHead);
        mPosition.remove(mPosition.size() - 1);

        // --- 4) Eat food
        if (sameCell(nextHead, mFoodPosition)) {
            grow();
            randomFood();
        }
    }


    private void grow() {
        // copy last segment
        PointF last = mPosition.get(mPosition.size() - 1);
        // add new segment
        mPosition.add(new PointF(last.x, last.y));
    }

    private boolean sameCell(PointF aPosition, PointF bPosition) {
        int gax = (int) Math.floor(aPosition.x / CELL_SIZE);
        int gay = (int) Math.floor(aPosition.y / CELL_SIZE);
        int gbx = (int) Math.floor(bPosition.x / CELL_SIZE);
        int gby = (int) Math.floor(bPosition.y / CELL_SIZE);
        return gax == gbx && gay == gby;
    }


    private static class DrawTask {

        private static final String TAG_DRAW_TASK = "DrawTask";

        // interval of update frame
        private static final long UPDATE_INTERVAL = 120L;
        private final SurfaceHolder mHolder;
        private final ScheduledExecutorService mDrawService = Executors.newSingleThreadScheduledExecutor();
        private Future<?> mFuture;

        private DrawCallback mDrawCallback;

        public DrawTask(SurfaceHolder holder) {
            mHolder = holder;
        }

        public void start(DrawCallback callback) {
            Utils.logDebug(TAG_DRAW_TASK, "start");
            mDrawCallback = callback;
            stop();
            mFuture = mDrawService.scheduleWithFixedDelay(this::drawView, 0L, UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
        }

        public void stop() {
            Utils.logDebug(TAG_DRAW_TASK, "stop");
            if (mFuture == null) {
                Utils.logDebug(TAG_DRAW_TASK, "mFuture is null");
                return;
            }
            mFuture.cancel(true);

            // shutdown
            mDrawService.shutdown();

        }

        public void release() {
            // clear
            mFuture = null;
            mDrawCallback = null;
        }

        private void drawView() {
            // check interrupt flag
            if (Thread.currentThread().isInterrupted()) {
                Utils.logDebug(TAG_DRAW_TASK, "Thread is interrupted");
                return;
            }

            // canvas
            Canvas canvas = null;
            try {
                canvas = mHolder.lockCanvas();
                if (canvas != null) {
                    mDrawCallback.draw(canvas);
                }
            } finally {
                if (canvas != null) {
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }

        }

        /**
         * draw callback
         */
        @FunctionalInterface
        private interface DrawCallback {
            void draw(Canvas canvas);
        }

    }

}
