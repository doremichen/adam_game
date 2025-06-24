/**
 * Copyright 2025 Adam
 * Description: GameActivity is the activity of the game.
 * Author: Adam
 * Date: 2025/06/23
 */
package com.adam.app.tetrisgame;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.tetrisgame.databinding.ActivityGameBinding;
import com.adam.app.tetrisgame.model.TetrisBoard;
import com.adam.app.tetrisgame.view.TetrisView;
import com.adam.app.tetrisgame.viewmodel.GameViewModel;

public class GameActivity extends AppCompatActivity {

    // view binding
    private ActivityGameBinding mBinding;

    // Tetris view
    private TetrisView mTetrisView;

    // Game View model
    GameViewModel mViewModel;


    // Hanlder
    private Handler mHandler = new Handler(Looper.getMainLooper());;
    // Runnable
    private Runnable mUpdateRunnable;
    // Runnig flag
    private boolean mRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.log("GameActivity onCreate");
        // view binding
        mBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mTetrisView = mBinding.tetrisView;

        // get view model and initial tetrisbord
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // load high score
        mViewModel.loadHighScore(GameActivity.this);

        mViewModel.initTetrisBoard(new TetrisBoard.GameListener() {
            @Override
            public void onClearLines() {
                mViewModel.increaseScore(100);
            }

            @Override
            public void onGameOver() {
                // stop game
                mViewModel.setRunning(false);
                // save score
                mViewModel.saveHighScore(GameActivity.this);

                // show game over dialog
                showGameOverDlg();
            }
        });

        // observe and update score UI
        mViewModel.getCurrentScore().observe(this, score -> {
            mBinding.tvScoreValue.setText(String.valueOf(score));
        });
        // observe and update high score UI
        mViewModel.getHighScore().observe(this, score -> {
            mBinding.tvHighScoreValue.setText(String.valueOf(score));
        });


        this.mUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                if (!mViewModel.isRunning()) {
                    return;
                }

                mViewModel.updateTetrisBoard();
                mViewModel.applyToView(mTetrisView);
                int speed = mViewModel.getSpeedInt(GameActivity.this); // 0=slow, 1=normal, 2=fast
                long interval = (speed == 0 ? 1000L : speed == 1 ? 700L : 400L);
                mHandler.postDelayed(this, interval);
            }
        };

        // start game
        mHandler.post(mUpdateRunnable);

        // setupControls
        setupControls();
    }

    private void showGameOverDlg() {
        Utils.log("showGameOverDlg");

        // Ok Button
        Utils.DialogButton restartButton = new Utils.DialogButton(getResources().getString(R.string.dialog_button_restart), (dialog, which) -> {
            // reset score
            mViewModel.resetScore();
            // Restart game
            mViewModel.setRunning(true);
            mViewModel.reset();
            mHandler.post(mUpdateRunnable);
        });
        // Cancel Button
        Utils.DialogButton exitButton = new Utils.DialogButton(getResources().getString(R.string.dialog_button_exit), (dialog, which) -> {
            // Exit game
            finish();
        });
        // title
        String title = getResources().getString(R.string.dialog_title_gameover);
        // message
        String message = getResources().getString(R.string.dialog_message_gameover);
        // show dialog
        Utils.showAlertDialog(this, title, message, restartButton, exitButton);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // view binding
        mBinding = null;
        // stop game
        mHandler.removeCallbacks(mUpdateRunnable);
    }

    private void setupControls() {
        // set control listener
        mBinding.btnLeft.setOnClickListener(v -> mViewModel.getTetrisBoard().moveLeft());
        mBinding.btnRight.setOnClickListener(v -> mViewModel.getTetrisBoard().moveRight());
        mBinding.btnDown.setOnClickListener(v -> mViewModel.getTetrisBoard().moveDown());
        mBinding.btnRotate.setOnClickListener(v -> mViewModel.getTetrisBoard().rotate());
        // setting button
        mBinding.btnSettings.setOnClickListener(v -> {
            startActivity(Utils.createIntent(this, SettingsActivity.class));
        });


    }
}