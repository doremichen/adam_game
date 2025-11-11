/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the game surface view
 * <p>
 * Author: Adam Game
 * Created Date: 2025/11/03
 */
package com.adam.app.racinggame2d.view.game;

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
import com.adam.app.racinggame2d.model.entity.Obstacle;
import com.adam.app.racinggame2d.util.GameImageLoader;
import com.adam.app.racinggame2d.util.GameUtil;
import com.adam.app.racinggame2d.viewmodel.GameEngine;
import com.adam.app.racinggame2d.viewmodel.GameViewModel;


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, GameEngine.GameUpdateListener {
    // TAG
    private static final String TAG = "GameSurfaceView";

    // Paint
    private final Paint mPaint;
    // GameViewModel
    private GameViewModel mViewModel;
    // car bitmap
    private Bitmap mCarBitmap;


    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // log
        GameUtil.log(TAG, "GameSurfaceView");
        // set callback
        getHolder().addCallback(this);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4f);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // log
        GameUtil.log(TAG, "surfaceCreated");
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    /**
     * setViewModel
     *
     * @param viewModel GameViewModel
     */
    public void setViewModel(@NonNull GameViewModel viewModel) {
        GameUtil.log(TAG, "setViewModel");
        mViewModel = viewModel;

    }

    @Override
    public void onUpdate() {
        GameUtil.log(TAG, "onUpdate");
        // update score
        mViewModel.updateScore();
        // update car hp
        mViewModel.updateHp();

        // draw view
        Canvas canvas = getHolder().lockCanvas();
        if (canvas == null) {
            GameUtil.log(TAG, "canvas is null");
            return;
        }

        drawGame(canvas);
        getHolder().unlockCanvasAndPost(canvas);
    }

    /**
     * drawGame
     * draw game view
     *
     * @param canvas
     */
    private void drawGame(@NonNull Canvas canvas) {
        GameUtil.log(TAG, "drawGame");
        GameUtil.log(TAG, "Canvas size: " + canvas.getWidth() + "x" + canvas.getHeight());
        // check if game engine is ready
        if (!mViewModel.isReady()) {
            GameUtil.log(TAG, "Game engine is not ready");
            return;
        }

        // clear background
        canvas.drawColor(Color.WHITE);

        // draw background
        drawBackground(canvas);

        // draw checkpoint
        mPaint.setColor(Color.YELLOW);
        for (PointF point : mViewModel.getCheckpoints()) {
            String assetPath = "images/flag.png";
            Bitmap bitmap = GameImageLoader.load(getContext(), assetPath, 80, 80);

            if (bitmap != null) {
                canvas.drawBitmap(bitmap, point.x - bitmap.getWidth() / 2f, point.y - bitmap.getHeight() / 2f, mPaint);
            }
        }

        // draw Obstacle
        for (Obstacle obstacle : mViewModel.getObstacles()) {
            PointF pos = obstacle.getPosition();
            String assetPath = "images/" + obstacle.getType().name().toLowerCase() + ".png"; // Default png
            Bitmap bitmap = GameImageLoader.load(getContext(), assetPath, 80, 80);

            if (bitmap != null) {
                canvas.drawBitmap(bitmap, pos.x - bitmap.getWidth() / 2f, pos.y - bitmap.getHeight() / 2f, mPaint);
            }
        }

        // draw car
        // fix car position (not move with background)
        drawCar(canvas);

    }

    private void drawCar(@NonNull Canvas canvas) {
        PointF carPosition = mViewModel.getCarPosition();
        if (mCarBitmap == null) {
            Bitmap raw = BitmapFactory.decodeResource(getResources(), R.drawable.car);
            if (raw != null) {
                int targetWidth = 120;
                int targetHeight = 180;
                mCarBitmap = Bitmap.createScaledBitmap(raw, targetWidth, targetHeight, true);
                raw.recycle();
            }
        }

        if (mCarBitmap != null) {
            float x = carPosition.x - mCarBitmap.getWidth() / 2f;
            float y = carPosition.y - mCarBitmap.getHeight() / 2f;
            canvas.drawBitmap(mCarBitmap, x, y, mPaint);
        } else {
            // fallback
            mPaint.setColor(Color.RED);
            canvas.drawRect(carPosition.x - 30, carPosition.y - 50,
                    carPosition.x + 30, carPosition.y + 10, mPaint);
        }
    }

    private void drawBackground(@NonNull Canvas canvas) {
        GameUtil.log(TAG, "drawBackground");
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int gridSize = 100;  // size of block

        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(2f);
        mPaint.setAlpha(80);

        // vertical line
        for (int x = 0; x <= width; x += gridSize) {
            canvas.drawLine(x, 0, x, height, mPaint);
        }

        // horizontal line
        for (int y = 0; y <= height; y += gridSize) {
            canvas.drawLine(0, y, width, y, mPaint);
        }
    }

}
