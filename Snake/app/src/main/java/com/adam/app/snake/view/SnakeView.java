/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the snake game view
 *
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

import java.util.List;

public class SnakeView extends View {
    // paint snack
    private final Paint mPaintSnake = new Paint();
    // paint food
    private final Paint mPaintFood = new Paint();
    // paint text
    private final Paint mPaintText = new Paint();
    // snake size
    private int mSnakeSize = 50;

    // snake: List<int[]>
    private List<int[]> mSnake;
    // food: int[1][2]
    private int[][] mFood;
    //score: int
    private int mScore = 0;
    // gem over text: string
    private String mGameOverText;



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

    /**
     * set score with int
     *
     * @param score int
     */
    public void setScore(int score) {
        mScore = score;
        invalidate();
    }

    /**
     * set game over text with isGameOver
     *
     * @param isGameOver boolean
     */
    public void setGameOverText(boolean isGameOver) {
        mGameOverText = isGameOver ? "Game Over" : "";
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        // check if snake is not null
        if (mSnake != null) {
            // draw snake
            for (int[] body : mSnake) {
                canvas.drawRect(body[0] * mSnakeSize, body[1] * mSnakeSize,
                        (body[0] + 1) * mSnakeSize, (body[1] + 1) * mSnakeSize, mPaintSnake);
            }
        }

        // check if food is not null
        if (mFood != null) {
            // draw food
            canvas.drawRect(mFood[0][0] * mSnakeSize, mFood[0][1] * mSnakeSize,
                    (mFood[0][0] + 1) * mSnakeSize, (mFood[0][1] + 1) * mSnakeSize, mPaintFood);
        }


        // draw score
        canvas.drawText("Score: " + mScore, 50, 100, mPaintText);

        // check if game over text is not empty
        if (!mGameOverText.isEmpty()) {
            // draw game over text
            canvas.drawText(mGameOverText, 100, getHeight() / 2, mPaintText);
        }
    }
}
