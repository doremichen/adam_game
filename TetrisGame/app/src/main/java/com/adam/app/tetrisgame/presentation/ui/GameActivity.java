/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.presentation.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.adam.app.tetrisgame.R;
import com.adam.app.tetrisgame.databinding.ActivityGameBinding;
import com.adam.app.tetrisgame.domain.model.TetrisBoard;
import com.adam.app.tetrisgame.presentation.viewmodel.GameViewModel;
import com.adam.app.tetrisgame.util.Constants;
import com.adam.app.tetrisgame.util.Utils;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GameActivity extends AppCompatActivity {
    private GameViewModel mViewModel;
    private ActivityGameBinding mBinding;
    private TetrisView mTetrisView;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mUpdateRunnable;
    private GameSoundManager mSoundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_game);

        mSoundManager = new GameSoundManager();
        mTetrisView = mBinding.tetrisView;
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        mViewModel.initTetrisBoard(new TetrisBoard.GameListener() {
            @Override
            public void onClearLines() {
                mViewModel.increaseScore(Constants.SCORE_PER_LINE);
                playSound(R.raw.line_clear);
            }

            @Override
            public void onGameOver() {
                mViewModel.setRunning(false);
                mViewModel.saveScore();
                showGameOverDlg();
                playSound(R.raw.game_over);
            }
        });

        mViewModel.getCurrentScore().observe(this, score -> mBinding.tvScoreValue.setText(String.valueOf(score)));
        mViewModel.getHighScore().observe(this, score -> mBinding.tvHighScoreValue.setText(String.valueOf(score)));

        mUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                if (!mViewModel.isRunning()) return;
                mViewModel.updateTetrisBoard();
                mTetrisView.setGrid(mViewModel.getTetrisBoard().getDisplayGrid());
                int speed = mViewModel.getSpeedInt();
                long interval = (speed == 0 ? Constants.SPEED_SLOW : speed == 1 ? Constants.SPEED_NORMAL : Constants.SPEED_FAST);
                mHandler.postDelayed(this, interval);
            }
        };

        setupControls();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mViewModel.setRunning(false);
                showGameExitDlg();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.setRunning(true);
        mHandler.post(mUpdateRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.setRunning(false);
        mHandler.removeCallbacks(mUpdateRunnable);
    }

    private void showGameExitDlg() {
        Utils.DialogButton okButton = new Utils.DialogButton(getString(R.string.dialog_button_ok), this::confirmExit);
        Utils.DialogButton cancelButton = new Utils.DialogButton(getString(R.string.dialog_button_cancel), this::cancelExit);
        Utils.showAlertDialog(this, getString(R.string.dialog_title_exit), getString(R.string.dialog_message_exit), okButton, cancelButton);
    }

    private void cancelExit(AlertDialog alertDialog) {
        alertDialog.dismiss();
        mViewModel.setRunning(true);
        mHandler.post(mUpdateRunnable);
    }

    private void confirmExit(AlertDialog alertDialog) {
        alertDialog.dismiss();
        finish();
    }

    private void showGameOverDlg() {
        Utils.DialogButton okButton = new Utils.DialogButton(getString(R.string.dialog_button_ok), this::playAgain);
        Utils.DialogButton exitButton = new Utils.DialogButton(getString(R.string.dialog_button_exit), this::exitGame);
        Utils.showAlertDialog(this, getString(R.string.dialog_title_gameover), getString(R.string.dialog_message_gameover), okButton, exitButton);
    }

    private void exitGame(AlertDialog alertDialog) {
        alertDialog.dismiss();
        finish();
    }

    private void playAgain(AlertDialog alertDialog) {
        alertDialog.dismiss();
        mViewModel.resetScore();
        mViewModel.setRunning(true);
        mViewModel.reset();
        mHandler.post(mUpdateRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSoundManager != null) {
            mSoundManager.stopMusic();
            mSoundManager.release();
            mSoundManager = null;
        }
        mBinding = null;
        mHandler.removeCallbacks(mUpdateRunnable);
    }

    private void setupControls() {
        mBinding.btnLeft.setOnClickListener(v -> {
            mViewModel.getTetrisBoard().moveLeft();
            playSound(R.raw.move);
        });
        mBinding.btnRight.setOnClickListener(v -> {
            mViewModel.getTetrisBoard().moveRight();
            playSound(R.raw.move);
        });
        mBinding.btnDown.setOnClickListener(v -> {
            mViewModel.getTetrisBoard().moveDown();
            playSound(R.raw.move);
        });
        mBinding.btnRotate.setOnClickListener(v -> {
            mViewModel.getTetrisBoard().rotate();
            playSound(R.raw.rotate);
        });
        mBinding.btnSettings.setOnClickListener(v -> startActivity(Utils.createIntent(this, SettingsActivity.class)));
    }

    public void playSound(int rawId) {
        if (mSoundManager == null || !mViewModel.isSoundEffectEnabled()) return;
        if (!mSoundManager.hasRawResource(this, rawId)) return;
        mSoundManager.playShortSound(this, rawId);
    }
}
