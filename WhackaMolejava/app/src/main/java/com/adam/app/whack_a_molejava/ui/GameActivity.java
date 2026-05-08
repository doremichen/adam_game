/*
 * MIT License
 *
 * Copyright (c) 2025 Adam Chen
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
package com.adam.app.whack_a_molejava.ui;

import android.os.Bundle;
import android.window.OnBackInvokedDispatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.whack_a_molejava.R;
import com.adam.app.whack_a_molejava.databinding.ActivityGameBinding;
import com.adam.app.whack_a_molejava.util.GameDialog;
import com.adam.app.whack_a_molejava.util.GameUtils;
import com.adam.app.whack_a_molejava.viewmodels.GameViewModel;

/**
 * This class is the game activity for the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-02
 */
public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    // view binding
    private ActivityGameBinding mBinding;

    // view model
    private GameViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameUtils.log(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        // init view model
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // surface view bind view model
        GameUtils.log(TAG, "game surface bind view model");
        mBinding.gameSurface.bind(mViewModel);
        // observer event
        mViewModel.getGameOver().observe(this, this::handleGameOver);
        mViewModel.getRemainingTime().observe(this, this::handleTime);
        mViewModel.getScore().observe(this, this::handleScore);

        // back button listener
        getOnBackInvokedDispatcher().registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                () -> {
                    mViewModel.pauseGame();
                    // show exit dialog
                    showExitDialog();
                });

    }

    @Override
    protected void onResume() {
        GameUtils.log(TAG, "onResume");
        super.onResume();
        mViewModel.startGame();

    }

    @Override
    protected void onPause() {
        GameUtils.log(TAG, "onPause");
        super.onPause();
        mViewModel.pauseGame();
    }

    private void showExitDialog() {
        String title = getString(R.string.whack_a_mole_exit_title);
        String message = getString(R.string.whack_a_mole_exit_message);
        GameDialog.DialogButtonContent positiveBtn = new GameDialog.DialogButtonContent(getString(R.string.whack_a_mole_exit_ok_btn), this::finish);
        GameDialog.DialogButtonContent negativeBtn = new GameDialog.DialogButtonContent(getString(R.string.whack_a_mole_exit_cancel_btn), mViewModel::startGame);
        GameDialog.showDialog(this, title, message, positiveBtn, negativeBtn);
    }

    private void handleScore(Integer score) {
        String scoreStr = String.format(java.util.Locale.getDefault(), "%03d", score);
        String scoreFormat = getResources().getString(R.string.whack_a_mole_score_tv, scoreStr);
        mBinding.tvScore.setText(scoreFormat);
    }

    private void handleTime(Integer time) {
        String timeFormat = getResources().getString(R.string.whack_a_mole_time_tv, time);
        mBinding.tvTime.setText(timeFormat);
    }

    private void handleGameOver(Boolean isGameOver) {
        if (!isGameOver) return;

        // Show game over dialog
        String title = getString(R.string.whack_a_mole_game_over_title);
        String message = getString(R.string.whack_a_mole_game_over_message);
        GameDialog.DialogButtonContent positiveBtn = new GameDialog.DialogButtonContent(getString(R.string.whack_a_mole_game_over_restart_btn), mViewModel::restartGame);
        GameDialog.DialogButtonContent negativeBtn = new GameDialog.DialogButtonContent(getString(R.string.whack_a_mole_exit_cancel_btn), this::finish);
        GameDialog.showDialog(this, title, message, positiveBtn, negativeBtn);

    }
}