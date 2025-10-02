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

import com.adam.app.snake.model.SpecialFood;

import java.util.ArrayList;
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
            // paint
            Paint paint = new Paint();
            switch (food.getType()) {
                case SpecialFood.TYPE.SPEED_UP:
                    paint.setColor(Color.CYAN);
                    break; // cyan
                case SpecialFood.TYPE.SLOW_DOWN:
                    paint.setColor(Color.rgb(192, 192, 192));
                    break; // gray
                case SpecialFood.TYPE.SHORTEN:
                    paint.setColor(Color.MAGENTA);
                    break; // magenta
                case SpecialFood.TYPE.EXTEND:
                    paint.setColor(Color.GREEN);
                    break; // green
                case SpecialFood.TYPE.INVINCIBLE:
                    paint.setColor(Color.YELLOW);
                    break; // yellow
                case SpecialFood.TYPE.INVISIBLE:
                    paint.setColor(Color.LTGRAY);
                    break; // light gray
                case SpecialFood.TYPE.SCORE_DOUBLE:
                    paint.setColor(Color.rgb(255, 165, 0));
                    break; // orange
                case SpecialFood.TYPE.BOMB:
                    paint.setColor(Color.BLACK);
                    break; // black
            }
            canvas.drawRect(food.getX() * CEIL_SIZE, food.getY() * CEIL_SIZE, (food.getX() + 1) * CEIL_SIZE, (food.getY() + 1) * CEIL_SIZE, paint);
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
