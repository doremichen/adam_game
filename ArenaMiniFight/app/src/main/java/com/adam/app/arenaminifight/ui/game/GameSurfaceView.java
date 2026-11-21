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
import com.adam.app.arenaminifight.utils.GameConstants;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GameSurfaceView rendering game entities with relative world-to-screen coordinate mapping.
 * Trace: UI-001, REQ-NFR-01
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "GameSurfaceView";

    private GameLoopManager mGameLoopManager;
    private SurfaceHolder mSurfaceHolder;

    private Paint mPlayerPaint;
    private Paint mTextPaint;

    private final List<Player> mPlayers = Collections.synchronizedList(new ArrayList<>());

    public GameSurfaceView(Context context) {
        super(context);
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        mPlayerPaint = new Paint();
        mTextPaint = new Paint();
        mPlayerPaint.setColor(Color.BLUE);
        mTextPaint.setColor(Color.WHITE);

        mGameLoopManager = new GameLoopManager(this);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        GameUtil.log(TAG + ": surfaceCreated");
        synchronized (mPlayers) {
            mPlayers.clear();
        }
        mGameLoopManager.start();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        GameUtil.log(TAG + ": surfaceDestroyed");
        mGameLoopManager.stop();
        mPlayers.clear();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void run() {
        try {
            if (!mSurfaceHolder.getSurface().isValid()) {
                return;
            }

            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.BLACK);
                    synchronized (SurfaceHolder.class) {
                        drawPlayers(canvas);
                    }
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        } catch (Exception e) {
            GameUtil.log(TAG + ": Loop Error: " + e.getMessage());
        }
    }

    public void updatePlayers(List<Player> players) {
        GameUtil.log(TAG + ": updatePlayers");
        synchronized (mPlayers) {
            mPlayers.clear();
            mPlayers.addAll(players);
        }
    }

    private void drawPlayers(Canvas canvas) {
        GameUtil.log(TAG + ": drawPlayers");

        if (mPlayers == null || mPlayers.isEmpty()) {
            return;
        }

        float canvasWidth = canvas.getWidth();
        float canvasHeight = canvas.getHeight();

        synchronized (mPlayers) {
            for (Player player : mPlayers) {
                if (player == null) continue;

                PointF pos = player.getPosition();
                String name = player.getName();
                if (pos == null) continue;
                if (name == null) name = "Loading...";

                float screenX = mapX(pos.x, canvasWidth);
                float screenY = mapY(pos.y, canvasHeight);

                mPlayerPaint.setColor(Color.BLUE);

                // Draw player circle
                canvas.drawCircle(screenX, screenY, GameConstants.PLAYER_RADIUS, mPlayerPaint);

                // Draw combat attack / skill visual effect ring
                if (player.getAttackType() > 0) {
                    mPlayerPaint.setColor(player.getAttackType() == 2 ? Color.CYAN : Color.YELLOW);
                    mPlayerPaint.setStyle(Paint.Style.STROKE);
                    mPlayerPaint.setStrokeWidth(8f);
                    canvas.drawCircle(screenX, screenY, GameConstants.PLAYER_RADIUS + 24f, mPlayerPaint);
                    mPlayerPaint.setStyle(Paint.Style.FILL);
                }

                // Draw player name
                mTextPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(name, screenX, screenY - GameConstants.NAME_OFFSET_Y, mTextPaint);

                // Blood bar background
                mPlayerPaint.setColor(Color.DKGRAY);
                canvas.drawRect(
                        screenX - (GameConstants.BLOOD_BAR_WIDTH / 2f),
                        screenY + GameConstants.BLOOD_BAR_OFFSET_Y,
                        screenX + (GameConstants.BLOOD_BAR_WIDTH / 2f),
                        screenY + GameConstants.BLOOD_BAR_OFFSET_Y + GameConstants.BLOOD_BAR_HEIGHT,
                        mPlayerPaint
                );

                // Current blood bar
                mPlayerPaint.setColor(Color.RED);
                float hpWidth = (player.getHp() / (float) GameConstants.PLAYER_MAX_HP) * GameConstants.BLOOD_BAR_WIDTH;
                canvas.drawRect(
                        screenX - (GameConstants.BLOOD_BAR_WIDTH / 2f),
                        screenY + GameConstants.BLOOD_BAR_OFFSET_Y,
                        screenX - (GameConstants.BLOOD_BAR_WIDTH / 2f) + hpWidth,
                        screenY + GameConstants.BLOOD_BAR_OFFSET_Y + GameConstants.BLOOD_BAR_HEIGHT,
                        mPlayerPaint
                );

                mPlayerPaint.setColor(Color.BLUE);
            }
        }
    }

    private float mapX(float worldX, float canvasWidth) {
        return (worldX - GameConstants.WORLD_MIN_X) / (GameConstants.WORLD_MAX_X - GameConstants.WORLD_MIN_X) * canvasWidth;
    }

    private float mapY(float worldY, float canvasHeight) {
        return (worldY - GameConstants.WORLD_MIN_Y) / (GameConstants.WORLD_MAX_Y - GameConstants.WORLD_MIN_Y) * canvasHeight;
    }
}
