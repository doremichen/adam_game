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

        public TetrisView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            // init
            init();
        }

        private void init() {
            // initial paint and grid
            mPaint = new Paint();
            mGrid = new int[Utils.NUM.ROWS][Utils.NUM.COLUMNS];
        }

        // set content of grid
        public void setGrid(int[][] grid) {
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
            Utils.log("drawGrid");
            // set width and height
            int width = getWidth();
            int height = getHeight();

            // set cell width and height
            float cellWidth = (float)width / Utils.NUM.COLUMNS;
            float cellHeight = (float)height / Utils.NUM.ROWS;

            // draw
            for (int i = 0; i < Utils.NUM.ROWS; i++) {
                for (int j = 0; j < Utils.NUM.COLUMNS; j++) {
                    Utils.log("i: " +i + " "+ "j: " + j);
                    // get color of cell
                    int color = mGrid[i][j];
                    if (color != 0) {
                        // set paint color
                        mPaint.setColor(color);
                        // set paint style
                        mPaint.setStyle(Paint.Style.FILL);
                        // draw
                        canvas.drawRect(
                                j * cellWidth,
                                i * cellHeight,
                                (j + 1) * cellWidth,
                                (i + 1) * cellHeight,
                                mPaint);
                    }

                    // draw border
                    mPaint.setColor(Color.GRAY);
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setStrokeWidth(1);
                    canvas.drawRect(
                            j * cellWidth,
                            i * cellHeight,
                            (j + 1) * cellWidth,
                            (i + 1) * cellHeight,
                            mPaint);
                }
            }
        }
    }
