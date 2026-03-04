/**
 * Copyright (C) 2021 Adam. All rights reserved.
 *
 * Description: This is the game surface view.
 *
 * @author Adam chen
 * @version 1.0.0 - 2026/03/04
 */
package com.adam.app.arenaminifight.ui.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.adam.app.arenaminifight.ui.game.engine.GameLoopManager;
import com.adam.app.arenaminifight.utils.GameUtil;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    // TAG
    private static final String TAG = "GameSurfaceView";

    // Game loop manager
    private GameLoopManager mGameLoopManager;
    // surface holder
    private SurfaceHolder mSurfaceHolder;

    // Paint
    private Paint mPlayerPaint;

    public GameSurfaceView(Context context) {
        super(context);
    }


    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        // init paint
        mPlayerPaint = new Paint();
        mPlayerPaint.setColor(Color.BLUE);

        // init game loop manager
        mGameLoopManager = new GameLoopManager(this);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        GameUtil.log(TAG + ": surfaceCreated");
        // start game loop
        mGameLoopManager.start();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        GameUtil.log(TAG + ": surfaceDestroyed");
        // stop game loop
        mGameLoopManager.stop();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void run() {
        // surface holder valid check
        if (!mSurfaceHolder.getSurface().isValid()) {
            return;
        }

        // draw
        Canvas canvas = null;
        try {
            canvas = mSurfaceHolder.lockCanvas();
            // draw
            if (canvas != null) {
                synchronized (SurfaceHolder.class) {
                    // draw
                    drawPlayers(canvas);
                }
            }
        } finally {
            if (canvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawPlayers(Canvas canvas) {
        GameUtil.log(TAG + ": drawPlayers");
        // 1. 清除畫面 (背景)
        canvas.drawColor(Color.BLACK);
        // TODO: 2. 執行 UC-05/06 繪製
    }
}
