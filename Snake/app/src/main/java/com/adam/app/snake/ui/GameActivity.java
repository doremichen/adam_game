/**
 * Copyright 2015 the Adam Game
 * <p>
 * Description: This class is the snake game activity
 * <p>
 * Author: Adam Chen
 * Date: 2025/09/24
 */
package com.adam.app.snake.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.snake.R;
import com.adam.app.snake.data.file.SharedPreferenceManager;
import com.adam.app.snake.databinding.ActivityGameBinding;
import com.adam.app.snake.model.SnakeGame;
import com.adam.app.snake.util.Utils;
import com.adam.app.snake.view.SnakeView;
import com.adam.app.snake.viewmodel.SnakeViewModel;

public class GameActivity extends AppCompatActivity {
    // TAG GameActivity
    private static final String TAG = "GameActivity";

    // view binding
    private ActivityGameBinding mBinding;

    // snake view model
    private SnakeViewModel mSnakeViewModel;
    //private ExitType mExitType = ExitType.Normal;

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

            mSnakeViewModel.initGame(rows, cols, this);

        });

        // back button click listener
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // stop game
                mSnakeViewModel.stopGame();

                // set Exit type
                //mExitType = ExitType.Normal;

                //showNameDialogAndSaveScore(mSnakeViewModel.getScoreLiveData().getValue());

                showExitDialog(ExitType.Normal);

                // save game score
                //mSnakeViewModel.saveGameScore();

//                // show exit dialog
//                String title = getString(R.string.snake_game_exit_title);
//                String message = getString(R.string.snake_game_dialog_message);
//                showGameDialog(title, message);

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

    }

    @Override
    protected void onResume() {
        Utils.logDebug(TAG, "onResume");
        super.onResume();

        // resume game
        mSnakeViewModel.resumeGame(this);
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
        Utils.logDebug(TAG, "onConfigurationChanged: " + newConfig.toString());
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

            // set Exit type
            //mExitType = ExitType.GameOver;

            // vibration
            vibrateOnGameOver();

            showExitDialog(ExitType.GameOver);

            //showNameDialogAndSaveScore(mSnakeViewModel.getScoreLiveData().getValue());

            // show game over dialog
//            String title = getString(R.string.snake_game_over_title);
//            String message = getString(R.string.snake_game_dialog_message);
//            showGameDialog(title, message);//showGameOverDialog();
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
            String name = SharedPreferenceManager.getInstance(this).getString(SharedPreferenceManager.Keys.USER_NAME, "");
            mSnakeViewModel.saveGameScore(name);

            finish();
        });
        // show dialog
        Utils.showDialog(this, title, message, postButton, negativeButton);
    }

    /**
     * showNameDialogAndSaveScore
     */
//    private void showNameDialogAndSaveScore(final int score) {
//        Utils.logDebug(TAG, "showNameDialogAndSaveScore");
//
//        NameInputDialog dlg = new NameInputDialog();
//        dlg.setListener(new NameInputDialog.Listener() {
//            @Override
//            public void onNameConfirmed(String name) {
//                mSnakeViewModel.saveGameScore(name);
//                Toast.makeText(GameActivity.this, "已儲存分數", Toast.LENGTH_SHORT).show();
//                showExitDialog(mExitType);
//            }
//
//            @Override
//            public void onNameCanceled() {
//                Toast.makeText(GameActivity.this, "已取消儲存分數", Toast.LENGTH_SHORT).show();
//                showExitDialog(mExitType);
//            }
//        });
//
//        dlg.show(getSupportFragmentManager(), "name_input_dialog");
//    }


    /**
     * show exit dialog
     *
     * @param type
     */
    private void showExitDialog(ExitType type) {
        Utils.logDebug(TAG, "showExitDialog");
        String title = "";
        if (type == ExitType.Normal) {
            title = getString(R.string.snake_game_exit_title);

        } else if (type == ExitType.GameOver) {
            title = getString(R.string.snake_game_over_title);
        }

        String message = getString(R.string.snake_game_dialog_message);
        showGameDialog(title, message);
    }

    private enum ExitType {
        Normal,
        GameOver;
    }


}