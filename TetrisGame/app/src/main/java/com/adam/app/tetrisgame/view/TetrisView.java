/*
 * Copyright (c) 2025 Adam
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
package com.adam.app.tetrisgame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.adam.app.tetrisgame.Utils;

public class TetrisView extends View {

    // Paint and Grid
        private Paint mPaint;
        private int[][] mGrid;
        private float mCellWidth;
        private float mCellHeight;

        public TetrisView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            // init
            init();
        }

        private void init() {
            // initial paint and grid
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mGrid = new int[Utils.NUM.ROWS][Utils.NUM.COLUMNS];
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mCellWidth = (float) w / Utils.NUM.COLUMNS;
            mCellHeight = (float) h / Utils.NUM.ROWS;
        }

        // set content of grid
        public void setGrid(int[][] grid) {
            if (grid == null) return;
            mGrid = grid;
            invalidate();
        }

        // override onDraw to draw grid
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // draw grid
            drawGrid(canvas);
        }

        private void drawGrid(Canvas canvas) {
            // Draw filled cells
            mPaint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < Utils.NUM.ROWS; i++) {
                for (int j = 0; j < Utils.NUM.COLUMNS; j++) {
                    int color = mGrid[i][j];
                    if (color != 0) {
                        mPaint.setColor(color);
                        canvas.drawRect(
                                j * mCellWidth,
                                i * mCellHeight,
                                (j + 1) * mCellWidth,
                                (i + 1) * mCellHeight,
                                mPaint);
                    }
                }
            }

            // Draw grid borders
            mPaint.setColor(Color.GRAY);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(1);
            for (int i = 0; i <= Utils.NUM.ROWS; i++) {
                canvas.drawLine(0, i * mCellHeight, getWidth(), i * mCellHeight, mPaint);
            }
            for (int j = 0; j <= Utils.NUM.COLUMNS; j++) {
                canvas.drawLine(j * mCellWidth, 0, j * mCellWidth, getHeight(), mPaint);
            }
        }
    }
