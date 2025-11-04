/**
 * Copyright (C) 2025 Adam. All Rights Reserved.
 * Description: This class is the Game view.
 *
 * @author Adam Chen
 * @since 2025-11-03
 */
package com.adam.app.racinggame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.adam.app.racinggame.util.GameUtil;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // TAG
    private static final String TAG = "GameSurfaceView";

    // paint
    private Paint mPaint;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        GameUtil.log(TAG, "onDraw");
    }
}
