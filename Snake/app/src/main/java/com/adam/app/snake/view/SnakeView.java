/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the snake game view
 * <p>
 * Author: Adam Chen
 * Date: 2025/09/24
 */
package com.adam.app.snake.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.adam.app.snake.Utils;

import java.util.List;

public class SnakeView extends View {
    // snake size
    public final static int CEIL_SIZE = 50;
    // TAG: SnakeView
    private static final String TAG = "SnakeView";
    // paint snack
    private final Paint mPaintSnake = new Paint();
    // paint food
    private final Paint mPaintFood = new Paint();
    // paint text
    private final Paint mPaintText = new Paint();
    // snake: List<int[]>
    private List<int[]> mSnake;
    // food: int[1][2]
    private int[][] mFood;


    public SnakeView(Context context) {
        super(context);
        init();
    }

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // snake color: green
        mPaintSnake.setColor(Color.GREEN);
        // food color: red
        mPaintFood.setColor(Color.RED);
        // text color: white
        mPaintText.setColor(Color.YELLOW);
        mPaintText.setTextSize(80f);
    }

    /**
     * set snake with List<int[]>
     *
     * @param snake List<int[]>
     */
    public void setSnake(List<int[]> snake) {
        mSnake = snake;
        invalidate();
    }

    /**
     * set food with int[1][2]
     *
     * @param food int[1][2]
     */
    public void setFood(int[][] food) {
        mFood = food;
        invalidate();
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // check if food is not null
        if (mFood != null) {
            // log food
            Utils.logDebug(TAG, "onDraw: food: " + mFood[0][0] + ", " + mFood[0][1]);
            // draw food
            canvas.drawRect(mFood[0][0] * CEIL_SIZE, mFood[0][1] * CEIL_SIZE,
                    (mFood[0][0] + 1) * CEIL_SIZE, (mFood[0][1] + 1) * CEIL_SIZE, mPaintFood);
        }

        // check if snake is not null
        if (mSnake != null) {
            // draw snake
            for (int[] body : mSnake) {
                canvas.drawRect(body[0] * CEIL_SIZE, body[1] * CEIL_SIZE,
                        (body[0] + 1) * CEIL_SIZE, (body[1] + 1) * CEIL_SIZE, mPaintSnake);
            }
        }


    }
}
