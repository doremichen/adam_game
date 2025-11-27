/**
 * Copyright 2025 Adam Chen
 * Description: DemoTetrisView is used to show the tetris game in main page.
 * Author: Adam Chen
 * Date: 2025/11/27
 */
package com.adam.app.tetrisgame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DemoTetrisView extends View implements Runnable{

    // --- constants ---
    private static final int BLOCK_SIZE = 40;
    private static final int FPS = 30;
    private static final int[] COLORS = {
            Color.CYAN, Color.BLUE, Color.rgb(255,165, 0), Color.YELLOW,
            Color.GREEN, Color.MAGENTA, Color.RED
    };

    // --- field ---
    private final List<Block> mBlocks = new ArrayList<>();
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public DemoTetrisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBlocks();
        startDemo();
    }

    private void startDemo() {
        mHandler.post(this);
    }

    private void initBlocks() {
        // random generate blocks
        for (int i = 0; i < 5; i++) {
            int x = (int) (Math.random() * 10);
            int y = (int) (Math.random() * 20);
            Block block = new Block(new PointF(x, y), COLORS[(int) (Math.random() * COLORS.length)]);
            mBlocks.add(block);
        }
    }

    @Override
    public void run() {
        // move blocks down
        for (Block block : mBlocks) {
            block.mPosition.y += 5;
            if (block.mPosition.y >= getHeight()) {
                block.mPosition.y = -BLOCK_SIZE;
                block.mPosition.x = (int) (Math.random() * (getWidth() - BLOCK_SIZE));
                block.color = COLORS[(int) (Math.random() * COLORS.length)];
            }
        }

        // invalidate view
        invalidate();
        // post runnable
        mHandler.postDelayed(this, 1000 / FPS);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // remove runnable
        mHandler.removeCallbacks(this);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        // draw
        for (Block block : mBlocks) {
            mPaint.setColor(block.getColor());
            canvas.drawRect(block.getPosition().x, block.getPosition().y,
                    block.getPosition().x + BLOCK_SIZE, block.getPosition().y + BLOCK_SIZE, mPaint);
        }
    }

    private static class Block {
        private PointF mPosition;
        private int color;

        public Block(PointF position, int color) {
            this.mPosition = position;
            this.color = color;
        }

        public PointF getPosition() {
            return mPosition;
        }

        public int getColor() {
            return color;
        }
    }

}
