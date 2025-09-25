/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the snake game activity
 *
 * Author: Adam Chen
 * Date: 2025/09/24
 */
package com.adam.app.snake;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.snake.databinding.ActivityMainBinding;
import com.adam.app.snake.model.SnakeGame;
import com.adam.app.snake.view.SnakeView;
import com.adam.app.snake.viewmodel.SnakeViewModel;

public class MainActivity extends AppCompatActivity {
    // TAG MainActivity
    private static final String TAG = "MainActivity";

    // view binding
    private ActivityMainBinding mBinding;

    // snake view model
    private SnakeViewModel mSnakeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.logDebug(TAG, "onCreate");
        // view binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // initial snake view model
        mSnakeViewModel = new ViewModelProvider(this).get(SnakeViewModel.class);

        // get snake view width and height
        mBinding.snakeView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {

                    // Log snake view width and height
                    int width = mBinding.snakeView.getWidth();
                    int height = mBinding.snakeView.getHeight();
                    Utils.logDebug(TAG, "onCreate: width: " + width + ", height: " + height);
                    // initial game screen
                    int cols = width / SnakeView.CEIL_SIZE;
                    int rows = height / SnakeView.CEIL_SIZE;
                    Utils.logDebug(TAG, "onCreate: rows: " + rows + ", cols: " + cols);
                    mSnakeViewModel.initGame(rows, cols);

       });

//        // initial game screen
//        int cols = getResources().getDisplayMetrics().widthPixels / SnakeView.CEIL_SIZE;
//        int rows = getResources().getDisplayMetrics().heightPixels / SnakeView.CEIL_SIZE;
//        Utils.logDebug(TAG, "onCreate: rows: " + rows + ", cols: " + cols);
//        mSnakeViewModel.initGame(rows, cols);

        // back button click listener
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // finish activity
                finish();
            }
        });

        // up button click listener
        mBinding.btnUp.setOnClickListener(v -> mSnakeViewModel.setDirection(SnakeGame.Direction.UP));
        // down button click listener
        mBinding.btnDown.setOnClickListener(v -> mSnakeViewModel.setDirection(SnakeGame.Direction.DOWN));
        // left button click listener
        mBinding.btnLeft.setOnClickListener(v -> mSnakeViewModel.setDirection(SnakeGame.Direction.LEFT));
        // right button click listener
        mBinding.btnRight.setOnClickListener(v -> mSnakeViewModel.setDirection(SnakeGame.Direction.RIGHT));


        // observer live data
        mSnakeViewModel.getGameLiveData().observe(this, mBinding.snakeView::setSnake);
        mSnakeViewModel.getFoodLiveData().observe(this, mBinding.snakeView::setFood);
        mSnakeViewModel.getScoreLiveData().observe(this, mBinding.snakeView::setScore);
        mSnakeViewModel.getGameStateLiveData().observe(this, isGameOver ->
                mBinding.snakeView.setGameOverText(isGameOver == SnakeGame.GameState.GAME_OVER));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.logDebug(TAG, "onDestroy");

        // clear handler
        mSnakeViewModel.getGameLiveData().removeObservers(this);
        mSnakeViewModel.getFoodLiveData().removeObservers(this);
        mSnakeViewModel.getScoreLiveData().removeObservers(this);
        mSnakeViewModel.getGameStateLiveData().removeObservers(this);
        mSnakeViewModel = null;

    }

    /**
     * onTouchEvent
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Utils.logDebug(TAG, "onTouchEvent");
        // touch event: Up
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // get touch x and y
            float x = event.getX();
            float y = event.getY();
            float centerX = getWindowManager().getDefaultDisplay().getWidth() / 2f;
            float centerY = getWindowManager().getDefaultDisplay().getHeight() / 2f;
            // LOG x y centerX centerY
            Utils.logDebug(TAG, "onTouchEvent: x: " + x + ", y: " + y + ", centerX: " + centerX + ", centerY: " + centerY);
            // check x and y
            if (Math.abs(x - centerX) > Math.abs(y - centerY)) {
                Utils.logDebug(TAG, "onTouchEvent: x > centerX");
                if (x > centerX) mSnakeViewModel.setDirection(SnakeGame.Direction.RIGHT);
                else mSnakeViewModel.setDirection(SnakeGame.Direction.LEFT);
            } else {
                Utils.logDebug(TAG, "onTouchEvent: y > centerY");
                if (y > centerY) mSnakeViewModel.setDirection(SnakeGame.Direction.DOWN);
                else mSnakeViewModel.setDirection(SnakeGame.Direction.UP);
            }

        }

        return super.onTouchEvent(event);
    }
}