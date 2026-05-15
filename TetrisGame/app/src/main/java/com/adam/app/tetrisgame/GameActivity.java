/*
 * Copyright (c) 2025 Adam
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
package com.adam.app.tetrisgame;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.tetrisgame.databinding.ActivityGameBinding;
import com.adam.app.tetrisgame.model.TetrisBoard;
import com.adam.app.tetrisgame.manager.GameSoundManager;
import com.adam.app.tetrisgame.view.TetrisView;
import com.adam.app.tetrisgame.viewmodel.GameViewModel;

public class GameActivity extends AppCompatActivity {

    // Game View model
    private GameViewModel mViewModel;
    // view binding
    private ActivityGameBinding mBinding;
    // Tetris view
    private TetrisView mTetrisView;
    // Handler
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    // Runnable for game loop
    private Runnable mUpdateRunnable;

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

        mSoundManager = new GameSoundManager();

        mTetrisView = mBinding.tetrisView;

        // get view model and initial tetrisbord
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mViewModel.loadSettings(this);
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
                // save score to database
                mViewModel.saveScore(GameActivity.this);
                // show game over dialog
                showGameOverDlg();

                // play game over sound
                playSound(R.raw.game_over);
            }
        });

        // observe and update score UI
        mViewModel.getCurrentScore().observe(this, score -> 
                mBinding.tvScoreValue.setText(String.valueOf(score)));
        // observe and update high score UI
        mViewModel.getHighScore().observe(this, score -> 
                mBinding.tvHighScoreValue.setText(String.valueOf(score)));


        this.mUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                if (!mViewModel.isRunning()) {
                    return;
                }

                mViewModel.updateTetrisBoard();
                mViewModel.applyToView(mTetrisView);
                int speed = mViewModel.getSpeedInt(); // 0=slow, 1=normal, 2=fast
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
        Utils.DialogButton okButton = new Utils.DialogButton(getString(R.string.dialog_button_ok),
                this::confirmExit);
        // Cancel Button
        Utils.DialogButton cancelButton = new Utils.DialogButton(getString(R.string.dialog_button_cancel),
                this::cancelExit);
        // show exit dialog
        String title = getString(R.string.dialog_title_exit);
        String message = getString(R.string.dialog_message_exit);
        Utils.showAlertDialog(this, title, message, okButton, cancelButton);
    }

    private void cancelExit(AlertDialog alertDialog) {
        // dismiss dialog
        alertDialog.dismiss();
        // restart game
        mViewModel.setRunning(true);
        mHandler.post(mUpdateRunnable);
    }

    private void confirmExit(AlertDialog alertDialog) {
        // dismiss dialog
        alertDialog.dismiss();
        // exit game
        finish();
    }


    private void showGameOverDlg() {
        Utils.log("showGameOverDlg");
        // Ok button
        Utils.DialogButton okButton = new Utils.DialogButton(getString(R.string.dialog_button_ok),
                this::playAgain);
        // Exit button
        Utils.DialogButton exitButton = new Utils.DialogButton(getString(R.string.dialog_button_exit),
                this::exitGame);

        // show game over dialog
        String title = getString(R.string.dialog_title_gameover);
        String message = getString(R.string.dialog_message_gameover);
        Utils.showAlertDialog(this, title, message, okButton, exitButton);
    }

    private void exitGame(AlertDialog alertDialog) {
        // dissmiss dialog
        alertDialog.dismiss();
        // Exit game
        finish();
    }

    private void playAgain(AlertDialog alertDialog) {
        // dismiss dialog
        alertDialog.dismiss();
        // reset score
        mViewModel.resetScore();
        // Restart game
        mViewModel.setRunning(true);
        mViewModel.reset();
        mHandler.post(mUpdateRunnable);

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
            mSoundManager = null;
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
            // change activity to settings activity2
            startActivity(Utils.createIntent(this, SettingsActivity.class));
        });
    }

    /**
     * playSound
     *
     * @param rawId raw id
     */
    public void playSound(int rawId) {
        if (mSoundManager == null) {
            Utils.log("playSound: mSoundManager is null");
            return;
        }

        // check sound setting is enabled
        if (!mViewModel.isSoundEffectEnabled()) {
            return;
        }

        if (!mSoundManager.hasRawResource(this, rawId)) {
            Utils.log("playSound: raw resource not found: " + rawId);
            return;
        }

        mSoundManager.playShortSound(this, rawId);
    }
}