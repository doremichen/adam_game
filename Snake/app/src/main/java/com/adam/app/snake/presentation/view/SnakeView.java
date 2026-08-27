/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.presentation.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.adam.app.snake.domain.model.SpecialFood;
import com.adam.app.snake.presentation.view.strategy.FoodPaintStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Snake game view
 */
public class SnakeView extends View {
    // snake size
    public static final int CEIL_SIZE = 50;
    // TAG: SnakeView
    private static final String TAG = "SnakeView";
    // paint snack
    private final Paint mPaintSnake = new Paint();
    // paint food
    private final Paint mPaintFood = new Paint();
    // paint special food
    private final Paint mPaintSpecialFood = new Paint();
    // temp paint for special food
    private final Paint mPaintTemp = new Paint();

    // list of special food
    private final List<SpecialFood> mSpecialFoods = new ArrayList<>();
    // snake: List<Point>
    private List<Point> mSnake;

    // food: Point
    private Point mFood;


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
    }

    /**
     * set snake with List<Point>
     *
     * @param snake List<Point>
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
     * set special foods with foods
     *
     * @param foods List<SpecialFood>
     */
    public void setSpecialFoods(List<SpecialFood> foods) {
        mSpecialFoods.clear();
        if (foods != null) {
            mSpecialFoods.addAll(foods);
        }
        invalidate();
    }

    /**
     * set snake invisible state
     *
     * @param invisible boolean
     *
     */
    public void setSnakeInvisible(boolean invisible) {
        if (invisible) {
            mPaintSnake.setAlpha(0);
        } else {
            mPaintSnake.setAlpha(255);
        }
        invalidate();
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // check if food is not null
        if (mFood != null) {
            // draw food
            canvas.drawRect(mFood.x * CEIL_SIZE, mFood.y * CEIL_SIZE, (mFood.x + 1) * CEIL_SIZE, (mFood.y + 1) * CEIL_SIZE, mPaintFood);
        }


        // draw special foods
        for (SpecialFood food : mSpecialFoods) {
            // get food paint strategy
            FoodPaintStrategy strategy = FoodPaintStrategy.get(food.getType());
            if (strategy != null) {
                strategy.applyPaint(mPaintTemp);
            } else {
                mPaintTemp.setColor(Color.WHITE);
            }

            canvas.drawRect(food.getX() * CEIL_SIZE, food.getY() * CEIL_SIZE, (food.getX() + 1) * CEIL_SIZE, (food.getY() + 1) * CEIL_SIZE, mPaintTemp);
        }


        // check if snake is not null
        if (mSnake != null) {
            // draw snake
            for (Point body : mSnake) {
                canvas.drawRect(body.x * CEIL_SIZE, body.y * CEIL_SIZE, (body.x + 1) * CEIL_SIZE, (body.y + 1) * CEIL_SIZE, mPaintSnake);
            }
        }

    }
}
