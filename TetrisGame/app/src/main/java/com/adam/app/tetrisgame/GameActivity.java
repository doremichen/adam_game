/**
 * Copyright 2025 Adam
 * Description: GameActivity is the activity of the game.
 * Author: Adam
 * Date: 2025/06/23
 */
package com.adam.app.tetrisgame;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.tetrisgame.databinding.ActivityGameBinding;
import com.adam.app.tetrisgame.model.TetrisBoard;
import com.adam.app.tetrisgame.sound.GameSoundManager;
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

    // sound manager
    private GameSoundManager mSoundManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.log("GameActivity onCreate");
        Utils.log("API Level: " + Build.VERSION.SDK_INT);
        Utils.log("targetSdkVersion: " + getApplicationInfo().targetSdkVersion);
        // view binding
        mBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mSoundManager = GameSoundManager.getInstance();

        mTetrisView = mBinding.tetrisView;

        // get view model and initial tetrisbord
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // load high score
        mViewModel.loadHighScore(GameActivity.this);

        mViewModel.initTetrisBoard(new TetrisBoard.GameListener() {
            @Override
            public void onClearLines() {
                mViewModel.increaseScore(100);
                // play sound
                playSound(R.raw.line_clear);
            }

            @Override
            public void onGameOver() {
                // stop game
                mViewModel.setRunning(false);
                // save score
                mViewModel.saveHighScore(GameActivity.this);

                // show game over dialog
                showGameOverDlg();

                // play game over sound
                playSound(R.raw.game_over);
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

        // setupControls
        setupControls();

        // add back button callback
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // log back button action
                Utils.log("back button action");
                // stop game
                mViewModel.setRunning(false);

                // show game exit dialog
                showGameExitDlg();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // start game
        mViewModel.setRunning(true);
        mHandler.post(mUpdateRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop game
        mViewModel.setRunning(false);
        mHandler.removeCallbacks(mUpdateRunnable);
    }

    /**
     * show game exit dialog
     */
    private void showGameExitDlg() {
        // Ok Button
        Utils.DialogButton okButton = new Utils.DialogButton(getResources().getString(R.string.dialog_button_ok), (dialog, which) -> {
            // Exit game
            finish();
        });
        // Cancel Button
        Utils.DialogButton cancelButton = new Utils.DialogButton(getResources().getString(R.string.dialog_button_cancel), (dialog, which) -> {
            // restart game
            mViewModel.setRunning(true);
            mHandler.post(mUpdateRunnable);
        });
        // title
        String title = getResources().getString(R.string.dialog_title_exit);
        // message
        String message = getResources().getString(R.string.dialog_message_exit);
        // show dialog
        Utils.showAlertDialog(this, title, message, okButton, cancelButton);

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
        // Log on destroy
        Utils.log("GameActivity onDestroy");


        // release sound manager
        if (mSoundManager != null) {
            mSoundManager.stopMusic();
            mSoundManager.release();
        }
        // view binding
        mBinding = null;
        // stop game
        mHandler.removeCallbacks(mUpdateRunnable);
    }

    private void setupControls() {
        // set control listener
        mBinding.btnLeft.setOnClickListener(v -> {
            mViewModel.getTetrisBoard().moveLeft();
            // play move sound
            playSound(R.raw.move);
        });
        mBinding.btnRight.setOnClickListener(v -> {
            mViewModel.getTetrisBoard().moveRight();
            // play move sound
            playSound(R.raw.move);
        });
        mBinding.btnDown.setOnClickListener(v -> {
            mViewModel.getTetrisBoard().moveDown();
            // play move sound
            playSound(R.raw.move);
        });
        mBinding.btnRotate.setOnClickListener(v -> {
            mViewModel.getTetrisBoard().rotate();
            // play rotate sound
            playSound(R.raw.rotate);
        });
        // setting button
        mBinding.btnSettings.setOnClickListener(v -> {
            startActivity(Utils.createIntent(this, SettingsActivity.class));
        });
    }

    /**
     * playSound
     * @param rawId raw id
     */
    public void playSound(int rawId) {
        Utils.log("playSound");
        // check sound setting is enable?
        boolean sound = mViewModel.isSoundEffectEnabled(this);
        if (!sound) {
            Utils.log("sound is disabled");
            return;
        }

        // check sound manager has sound
        if (mSoundManager == null) {
            throw new ExceptionInInitializerError("mSoundManager is null");
        }

        if (!mSoundManager.hasRawResource(this, rawId)) {
            Utils.log("mSoundManager has no sound");
            return;
        }

        Utils.log("play sound");
        mSoundManager.playShortSound(this, rawId);
    }
}