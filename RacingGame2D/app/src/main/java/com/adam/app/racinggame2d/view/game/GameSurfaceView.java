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
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.model.entity.Obstacle;
import com.adam.app.racinggame2d.util.GameUtil;
import com.adam.app.racinggame2d.viewmodel.GameEngine;
import com.adam.app.racinggame2d.viewmodel.GameViewModel;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, GameEngine.GameUpdateListener{
    // TAG
    private static final String TAG = "GameSurfaceView";

    // Paint
    private final Paint mPaint;
    // GameViewModel
    private GameViewModel mViewModel;


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
     *   draw game view
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

        // draw line to debug
        GameUtil.debugDraw(canvas);

        // clear background
        canvas.drawColor(0xff000000);

        // draw background
        mPaint.setColor(Color.DKGRAY);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);

        // draw checkpoint
        mPaint.setColor(Color.YELLOW);
        for (PointF point : mViewModel.getCheckpoints()) {
            canvas.drawCircle(point.x, point.y, 20f, mPaint);
        }

        // draw Obstacle
        for (Obstacle obstacle : mViewModel.getObstacles()) {
            switch (obstacle.getType()) {
                case OIL:
                    mPaint.setColor(Color.BLACK);
                    break;
                case ROCK:
                    mPaint.setColor(Color.GRAY);
                    break;
                case BOOST:
                    mPaint.setColor(Color.CYAN);
                    break;
            }
            canvas.drawCircle(obstacle.getPosition().x, obstacle.getPosition().y, 20f, mPaint);

        }

        // draw car
        // fix car position (not move with background)
        PointF carPosition = mViewModel.getCarPosition();
        mPaint.setColor(Color.RED);
        GameUtil.log(TAG, "carPosition: " + carPosition.x + ", " + carPosition.y);
        canvas.drawRect(carPosition.x - 30, carPosition.y - 50, carPosition.x + 30, carPosition.y + 10, mPaint);

        // draw score
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(50f);
        GameUtil.log(TAG, "Score: " + mViewModel.getScore().getValue());
        canvas.drawText("Score: " + mViewModel.getScore().getValue(), 50f, 80f, mPaint);

    }
}
