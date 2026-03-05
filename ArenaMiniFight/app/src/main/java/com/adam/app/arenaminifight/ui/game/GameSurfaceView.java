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
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.ui.game.engine.GameLoopManager;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    // TAG
    private static final String TAG = "GameSurfaceView";

    // Game loop manager
    private GameLoopManager mGameLoopManager;
    // surface holder
    private SurfaceHolder mSurfaceHolder;

    // Paint
    private Paint mPlayerPaint;
    private Paint mTextPaint;


    // Players
    private List<Player> mPlayers = new ArrayList<>();

    public GameSurfaceView(Context context) {
        super(context);
    }


    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        // init paint
        mPlayerPaint = new Paint();
        mTextPaint = new Paint();
        mPlayerPaint.setColor(Color.BLUE);
        mTextPaint.setColor(Color.WHITE);

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

    public void updatePlayers(List<Player> players) {
        mPlayers = players;
    }

    private void drawPlayers(Canvas canvas) {
        GameUtil.log(TAG + ": drawPlayers");

        if (mPlayers == null || mPlayers.isEmpty()) {
            return;
        }

        // draw all player
        for (Player player : mPlayers) {
            PointF pos = player.getPosition();
            if (pos == null) continue;

            // player paint
            canvas.drawCircle(pos.x, pos.y, 30f, mPlayerPaint);
            // player name
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(player.getName(), pos.x, pos.y - 50f, mTextPaint);
            // blood color
            mPlayerPaint.setColor(Color.DKGRAY);
            canvas.drawRect(pos.x - 40, pos.y + 40, pos.x + 40, pos.y + 50, mPlayerPaint);
            // current blood color
            mPlayerPaint.setColor(Color.RED);
            float hpWidth = (player.getHp() / 100f) * 80f; // set full 100
            canvas.drawRect(pos.x - 40, pos.y + 40, pos.x - 40 + hpWidth, pos.y + 50, mPlayerPaint);
            // reset for next
            mPlayerPaint.setColor(Color.BLUE);
        }
    }
}
