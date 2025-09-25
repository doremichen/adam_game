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

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
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


        // back button click listener
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // stop game
                mSnakeViewModel.stopGame();

                // show exit dialog
                String title = getString(R.string.snake_game_exit_title);
                String message = getString(R.string.snake_game_dialog_message);
                showGameDialog(title, message);

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
        mSnakeViewModel.getScoreLiveData().observe(this, this::onChanged);
        mSnakeViewModel.getGameStateLiveData().observe(this, this::onChanged);

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


    private void onChanged(Integer score) {
        int scoreValue = score == null ? 0 : score;
        String scoreText = getString(R.string.snake_game_core, scoreValue);
        // set score text
        mBinding.coreTextView.setText(scoreText);
    }

    private void onChanged(SnakeGame.GameState isGameOver) {

        if (isGameOver == SnakeGame.GameState.GAME_OVER) {
            // show game over dialog
            String title = getString(R.string.snake_game_over_title);
            String message = getString(R.string.snake_game_dialog_message);
            showGameDialog(title, message);//showGameOverDialog();
        }
    }


    /**
     * ShowGameDialog with title and message
     *
     * @param title String
     * @param message String
     */
    private void showGameDialog(String title, String message) {
        // post dialog button content
        Utils.DialogButtonContent postButton = new Utils.DialogButtonContent(getString(R.string.snake_game_restart),
                (dialog, which) -> {
                    mSnakeViewModel.resetGame();
                });
        // negative dialog button content
        Utils.DialogButtonContent negativeButton = new Utils.DialogButtonContent(getString(R.string.snake_game_exit),
                (dialog, which) -> {
                    finish();
                });
        // show dialog
        Utils.showDialog(this, title, message, postButton, negativeButton);
    }


}