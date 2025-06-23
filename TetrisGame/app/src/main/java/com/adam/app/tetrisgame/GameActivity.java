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
        mViewModel.initTetrisBoard(new TetrisBoard.GameOverListener() {
            @Override
            public void onGameOver() {
                // stop game
                mViewModel.setRunning(false);
                // show game over dialog
                showGameOverDlg();
            }
        });


        this.mUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                if (!mViewModel.isRunning()) {
                    return;
                }

                mViewModel.updateTetrisBoard();
                mViewModel.applyToView(mTetrisView);
                mHandler.postDelayed(this, 1000L);
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

    }
}