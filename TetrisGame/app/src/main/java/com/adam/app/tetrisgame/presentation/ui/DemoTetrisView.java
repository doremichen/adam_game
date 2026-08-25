/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.presentation.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.adam.app.tetrisgame.domain.model.TetrisBoard;
import java.util.Random;

public class DemoTetrisView extends TetrisView {
    private TetrisBoard mBoard;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Random mRandom = new Random();
    
    private final Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (mBoard != null) {
                // Randomly move or rotate for demo effect
                int action = mRandom.nextInt(10);
                if (action == 0) {
                    mBoard.moveLeft();
                } else if (action == 1) {
                    mBoard.moveRight();
                } else if (action == 2) {
                    mBoard.rotate();
                }
                
                mBoard.update();
                setGrid(mBoard.getDisplayGrid());
                mHandler.postDelayed(this, 500);
            }
        }
    };

    public DemoTetrisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBoard();
    }

    private void initBoard() {
        mBoard = new TetrisBoard(new TetrisBoard.GameListener() {
            @Override
            public void onClearLines() {
                // No score in demo
            }

            @Override
            public void onGameOver() {
                mBoard.reset();
                mBoard.spawnBlock();
            }
        });
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            // When returning to the screen, reset the board and start demo
            if (mBoard != null) {
                mBoard.reset();
                mBoard.spawnBlock();
            }
            mHandler.removeCallbacks(mUpdateRunnable);
            mHandler.post(mUpdateRunnable);
        } else {
            // When leaving the screen, stop demo
            mHandler.removeCallbacks(mUpdateRunnable);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mUpdateRunnable);
    }
}
