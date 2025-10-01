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
import android.graphics.Point;
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
    // paint special food
    private final Paint mPaintSpecialFood = new Paint();
    // paint text
    private final Paint mPaintText = new Paint();
    // snake: List<Point>
    private List<Point> mSnake;
    // food: Point
    private Point mFood;
    // special food: Point
    private Point mSpecialFood;


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
        // special food color: yellow
        mPaintSpecialFood.setColor(Color.YELLOW);
        // text color: white
        mPaintText.setColor(Color.YELLOW);
        mPaintText.setTextSize(80f);
    }

    /**
     * set snake with List<int[]>
     *
     * @param snake List<int[]>
     */
    public void setSnake(List<Point> snake) {
        mSnake = snake;
        invalidate();
    }

    /**
     * set food with Point
     *
     * @param food Point
     */
    public void setFood(Point food) {
        mFood = food;
        invalidate();
    }

    /**
     * set special food with Point
     *
     * @param specialFood Point
     */
    public void setSpecialFood(Point specialFood) {
        mSpecialFood = specialFood;
        invalidate();
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // check if food is not null
        if (mFood != null) {
            // draw food
            canvas.drawRect(mFood.x * CEIL_SIZE, mFood.y * CEIL_SIZE,
                    (mFood.x + 1) * CEIL_SIZE, (mFood.y + 1) * CEIL_SIZE, mPaintFood);
        }


        // check if special food is not null
        if (mSpecialFood != null) {
            // draw special food
            canvas.drawRect(mSpecialFood.x * CEIL_SIZE, mSpecialFood.y * CEIL_SIZE,
                    (mSpecialFood.x + 1) * CEIL_SIZE, (mSpecialFood.y + 1) * CEIL_SIZE, mPaintSpecialFood);
        }

        // check if snake is not null
        if (mSnake != null) {
            // draw snake
            for (Point body : mSnake) {
                canvas.drawRect(body.x * CEIL_SIZE, body.y * CEIL_SIZE,
                        (body.x + 1) * CEIL_SIZE, (body.y + 1) * CEIL_SIZE, mPaintSnake);
            }
        }


    }
}
