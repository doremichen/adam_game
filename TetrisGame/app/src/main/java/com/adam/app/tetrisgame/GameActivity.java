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

import com.adam.app.tetrisgame.databinding.ActivityGameBinding;
import com.adam.app.tetrisgame.model.TetrisBoard;
import com.adam.app.tetrisgame.view.TetrisView;

public class GameActivity extends AppCompatActivity {

    // view binding
    private ActivityGameBinding mBinding;

    // Tetris view
    private TetrisView mTetrisView;
    // Tetris board
    private TetrisBoard mTetrisBoard;

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

        this.mTetrisBoard = new TetrisBoard(new TetrisBoard.GameOverListener() {
            @Override
            public void onGameOver() {
                // stop game
                mRunning = false;
                // show game over dialog
                showGameOverDlg();
            }
        });

        this.mUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                if (!mRunning) {
                    return;
                }

                mTetrisBoard.update();
                mTetrisBoard.applyToView(mTetrisView);
                mHandler.postDelayed(this, 1000);
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
            // Restart game
            mRunning = true;
            mTetrisBoard.reset();
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
        mBinding.btnLeft.setOnClickListener(v -> mTetrisBoard.moveLeft());
        mBinding.btnRight.setOnClickListener(v -> mTetrisBoard.moveRight());
        mBinding.btnDown.setOnClickListener(v -> mTetrisBoard.moveDown());
        mBinding.btnRotate.setOnClickListener(v -> mTetrisBoard.rotate());

    }
}