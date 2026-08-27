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
package com.adam.app.snake.presentation.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.snake.R;
import com.adam.app.snake.databinding.ActivityGameBinding;
import com.adam.app.snake.domain.model.SnakeGame;
import com.adam.app.snake.presentation.view.SnakeView;
import com.adam.app.snake.presentation.viewmodel.SnakeViewModel;
import com.adam.app.snake.util.Utils;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Snake game activity
 */
@AndroidEntryPoint
public class GameActivity extends AppCompatActivity {
    // TAG GameActivity
    private static final String TAG = "GameActivity";

    // view binding
    private ActivityGameBinding mBinding;

    // snake view model
    private SnakeViewModel mSnakeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.logDebug(TAG, "onCreate");
        // view binding
        mBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // set soft input mode
        getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // keep on screen
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


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
                // show game exit dialog
                showGameExitDialog(ExitType.NORMAL);

            }
        });

        // set settings button click listener
        mBinding.btnSetting.setOnClickListener(v -> {
            startActivity(SettingActivity.createIntent(this));
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
        mSnakeViewModel.getSpecialFoodsLiveData().observe(this, mBinding.snakeView::setSpecialFoods);
        mSnakeViewModel.getScoreLiveData().observe(this, this::onChanged);
        mSnakeViewModel.getGameStateLiveData().observe(this, this::onChanged);
        mSnakeViewModel.getSnakeInvisibleLiveData().observe(this, mBinding.snakeView::setSnakeInvisible);
        mSnakeViewModel.getSpecialFoodToastLiveData().observe(this, resId -> {
            String type = getResources().getString(resId);
            Utils.showToast(this, getString(R.string.snake_game_special_food_toast, type));
        });

    }

    @Override
    protected void onResume() {
        Utils.logDebug(TAG, "onResume");
        super.onResume();

        // resume game
        mSnakeViewModel.resumeGame();
    }

    @Override
    protected void onPause() {
        Utils.logDebug(TAG, "onPause");
        super.onPause();
        // stop game
        mSnakeViewModel.stopGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.logDebug(TAG, "onDestroy");

        // clear handler
        mSnakeViewModel.getGameLiveData().removeObservers(this);
        mSnakeViewModel.getFoodLiveData().removeObservers(this);
        mSnakeViewModel.getSpecialFoodsLiveData().removeObservers(this);
        mSnakeViewModel.getScoreLiveData().removeObservers(this);
        mSnakeViewModel.getGameStateLiveData().removeObservers(this);
        mSnakeViewModel.getSnakeInvisibleLiveData().removeObservers(this);
        mSnakeViewModel = null;

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        Utils.logDebug(TAG, "onConfigurationChanged: " + newConfig);
        super.onConfigurationChanged(newConfig);
    }

    private void onChanged(Integer score) {
        int scoreValue = score == null ? 0 : score;
        String scoreText = getString(R.string.snake_game_core, scoreValue);
        // set score text
        mBinding.coreTextView.setText(scoreText);
    }

    private void onChanged(SnakeGame.GameState GameState) {

        if (GameState == SnakeGame.GameState.GAME_OVER) {
            // log
            Utils.logDebug(TAG, "onChanged: GAME_OVER");

            // stop game
            mSnakeViewModel.stopGame();

            // vibration
            vibrateOnGameOver();

            // show game exit dialog
            showGameExitDialog(ExitType.GAME_OVER);

        }
    }

    /**
     * Vibrate on game over
     */
    private void vibrateOnGameOver() {
        // Vibrate on game over
        final Vibrator vibrator = getSystemService(Vibrator.class);
        // check if vibrator is supported
        if (vibrator == null) {
            // log error
            Utils.logDebug(TAG, "vibrateOnGameOver: vibrator is not supported");
            return;
        }

        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    /**
     * ShowGameDialog with title and message
     *
     * @param title   String
     * @param message String
     */
    private void showGameDialog(String title, String message) {
        // post dialog button content
        Utils.DialogButtonContent postButton = new Utils.DialogButtonContent(getString(R.string.snake_game_restart), (dialog, which) -> {
            mSnakeViewModel.resetGame();
            // dismiss dialog
            dialog.dismiss();
        });
        // negative dialog button content
        Utils.DialogButtonContent negativeButton = new Utils.DialogButtonContent(getString(R.string.snake_game_exit), (dialog, which) -> {
            // save game score to database
            String name = mSnakeViewModel.getUserName();
            mSnakeViewModel.saveGameScore(name);

            finish();
        });
        // show dialog
        Utils.showDialog(this, title, message, postButton, negativeButton);
    }


    /**
     * show game exit dialog
     *
     * @param type ExitType
     */
    private void showGameExitDialog(ExitType type) {
        Utils.logDebug(TAG, "showExitDialog");
        String title = switch (type) {
            case NORMAL -> getString(R.string.snake_game_exit_title);
            case GAME_OVER -> getString(R.string.snake_game_over_title);
        };

        String message = getString(R.string.snake_game_dialog_message);
        showGameDialog(title, message);
    }

    private enum ExitType {
        NORMAL, GAME_OVER
    }


}
