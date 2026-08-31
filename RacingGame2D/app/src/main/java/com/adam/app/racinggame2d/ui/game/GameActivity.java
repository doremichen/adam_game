/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.ui.game;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.databinding.ActivityGameBinding;
import com.adam.app.racinggame2d.util.Constants;
import com.adam.app.racinggame2d.util.LongPressButtonHelper;
import com.adam.app.racinggame2d.util.Utils;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for the racing game.
 */
@AndroidEntryPoint
public final class GameActivity extends AppCompatActivity {
    private static final String sTAG = "GameActivity";
    private ActivityGameBinding mBinding;
    private GameViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        mBinding.gameView.setViewModel(mViewModel);

        mBinding.gameView.post(() -> {
            int width = mBinding.gameView.getWidth();
            int height = mBinding.gameView.getHeight();
            String carId = getIntent().getStringExtra(Constants.CAR_ID);
            String carName = getIntent().getStringExtra(Constants.CAR_NAME);
            mViewModel.prepareGameEngine(width, height, carId, carName);
            mViewModel.setGameUpdateListener(mBinding.gameView);
            mViewModel.startGame();
        });

        initObservers();
        setupControls();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mViewModel.pauseGame();
                showExitConfirmDialog();
            }
        });
    }

    private void initObservers() {
        mViewModel.getIsGameOver().observe(this, isGameOver -> {
            if (isGameOver) {
                mViewModel.stopGame();
                mViewModel.saveGameResult();
                showGameOverDialog();
            }
        });

        mViewModel.getScore().observe(this, score -> {
            mBinding.tvScore.setText(getString(R.string.racinggame2d_score_tv, String.valueOf(score)));
        });

        mViewModel.getHp().observe(this, hp -> mBinding.progressHp.setProgress(hp));

        mViewModel.getGameRestartEvent().observe(this, unused -> {
            // Any specific UI reset needed on restart can go here
        });
    }

    private void setupControls() {
        mBinding.buttonLeft.setOnClickListener(v -> mViewModel.moveHorizontally(true));
        mBinding.buttonRight.setOnClickListener(v -> mViewModel.moveHorizontally(false));
        mBinding.buttonSpeedUp.setOnClickListener(v -> mViewModel.speedUp(true));
        mBinding.buttonSlowDown.setOnClickListener(v -> mViewModel.speedUp(false));

        LongPressButtonHelper.attach(mBinding.buttonLeft, 100L, () -> mViewModel.moveHorizontally(true));
        LongPressButtonHelper.attach(mBinding.buttonRight, 100L, () -> mViewModel.moveHorizontally(false));
        LongPressButtonHelper.attach(mBinding.buttonSpeedUp, 100L, () -> mViewModel.speedUp(true));
        LongPressButtonHelper.attach(mBinding.buttonSlowDown, 100L, () -> mViewModel.speedUp(false));
    }

    private void showExitConfirmDialog() {
        Utils.DialogButtonContent positive = new Utils.DialogButtonContent(
                getString(R.string.racinggame2d_dlg_ok_btn_label),
                (dialog, which) -> {
                    mViewModel.saveGameResult();
                    finish();
                });
        Utils.DialogButtonContent negative = new Utils.DialogButtonContent(
                getString(R.string.racinggame2d_dlg_cancel_btn_label),
                (dialog, which) -> mViewModel.resumeGame());

        Utils.showDialog(this,
                getString(R.string.racinggame2d_exit_dlg_title),
                getString(R.string.racinggame2d_exit_dlg_content),
                positive, negative);
    }

    private void showGameOverDialog() {
        Utils.DialogButtonContent positive = new Utils.DialogButtonContent(
                getString(R.string.racinggame2d_dlg_ok_btn_label),
                (dialog, which) -> mViewModel.restartGame());
        Utils.DialogButtonContent negative = new Utils.DialogButtonContent(
                getString(R.string.racinggame2d_dlg_cancel_btn_label),
                (dialog, which) -> finish());

        Utils.showDialog(this,
                getString(R.string.racinggame2d_game_over_dlog_title),
                getString(R.string.racinggame2d_game_over_dlg_content),
                positive, negative);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewModel.isReady()) {
            mViewModel.resumeGame();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.pauseGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.stopGame();
    }
}
