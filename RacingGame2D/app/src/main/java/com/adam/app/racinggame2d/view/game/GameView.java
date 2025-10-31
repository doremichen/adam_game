/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is used to draw the game.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/29
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
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adam.app.racinggame2d.model.entity.Obstacle;
import com.adam.app.racinggame2d.util.GameUtil;
import com.adam.app.racinggame2d.viewmodel.GameEngine;
import com.adam.app.racinggame2d.viewmodel.GameViewModel;

import com.adam.app.racinggame2d.R;


public class GameView extends View implements GameEngine.GameUpdateListener {
    // TAG
    private static final String TAG = "GameView";
    // Paint
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // GameViewModel
    private GameViewModel mViewModel;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4f);
    }

    /**
     * setViewModel
     *
     * @param viewModel GameViewModel
     */
    public void setViewModel(@NonNull GameViewModel viewModel) {
        GameUtil.log(TAG, "setViewModel");
        mViewModel = viewModel;

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4f);

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        GameUtil.log(TAG, "onDraw");
        // check if game engine is ready
        if (!mViewModel.isReady()) {
            GameUtil.log(TAG, "Game engine is not ready");
            return;
        }

        // draw background
        canvas.drawColor(Color.rgb(30, 30, 30));

        // draw checkpoint
        mPaint.setColor(Color.YELLOW);
        for (PointF point : mViewModel.getCheckpoints()) {
            canvas.drawCircle(point.x, point.y, 20f, mPaint);
        }

        // draw Obstacle
        for (Obstacle obstacle : mViewModel.getObstacles()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), obstacle.getImageRes());
            if (bitmap != null) {
                float left = obstacle.getPosition().x - obstacle.getRadius();
                float top = obstacle.getPosition().y - obstacle.getRadius();
                float right = obstacle.getPosition().x + obstacle.getRadius();
                float bottom = obstacle.getPosition().y + obstacle.getRadius();
                RectF dst = new RectF(left, top, right, bottom);
                canvas.drawBitmap(bitmap, null, dst, null);
            } else {
                GameUtil.log(TAG, "bitmap is null");
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
        }

        // draw car
        // fix car position (not move with background)
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car);
        if (bitmap != null) {
            float left = mViewModel.getCarPosition().x - 30;
            float top = mViewModel.getCarPosition().y - 50;
            float right = mViewModel.getCarPosition().x + 30;
            float bottom = mViewModel.getCarPosition().y + 10;
            RectF dst = new RectF(left, top, right, bottom);
            canvas.drawBitmap(bitmap, null, dst, null);
        } else {
            GameUtil.log(TAG, "bitmap is null");
            PointF carPosition = mViewModel.getCarPosition();
            mPaint.setColor(Color.RED);
            GameUtil.log(TAG, "carPosition: " + carPosition.x + ", " + carPosition.y);
            canvas.drawRect(carPosition.x - 30, carPosition.y - 50, carPosition.x + 30, carPosition.y + 10, mPaint);

        }


        // draw score
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(50f);
        canvas.drawText("Score: " + mViewModel.getScore().getValue(), 50f, 80f, mPaint);

    }


    @Override
    public void onUpdate() {
        GameUtil.log(TAG, "onUpdate");
        // update score
        mViewModel.updateScore();

        // draw view
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
