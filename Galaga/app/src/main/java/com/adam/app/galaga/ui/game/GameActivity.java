/*
 * Copyright (c) 2026 Adam Chen
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

package com.adam.app.galaga.ui.game;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.galaga.databinding.ActivityGameBinding;
import com.adam.app.galaga.databinding.DialogGameOverBinding;
import com.adam.app.galaga.databinding.DialogPauseBinding;
import com.adam.app.galaga.engine.GameEngine;
import com.adam.app.galaga.utils.GameUtils;
import com.adam.app.galaga.viewmodel.GameViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GameActivity extends AppCompatActivity {
    private static final String TAG = GameActivity.class.getSimpleName();

    private ActivityGameBinding mBinding;
    private GameViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameUtils.info(TAG, "onCreate");
        
        mBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        mViewModel.getEntities().observe(this, mBinding.gameSurfaceView::updateEntities);
        mViewModel.getCurrentState().observe(this, this::onState);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mViewModel.getCurrentState().getValue() == GameEngine.State.RUNNING) {
                    mViewModel.pauseGame();
                    showPauseDialog();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameUtils.info(TAG, "onResume");
        mViewModel.resumeGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GameUtils.info(TAG, "onPause");
        mViewModel.pauseGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameUtils.info(TAG, "onDestroy");
        mBinding.gameSurfaceView.release();
    }

    private void onState(GameEngine.State state) {
        if (state == GameEngine.State.GAME_OVER) {
            showGameOverDialog();
        }
    }

    private void showGameOverDialog() {
        GameUtils.info(TAG, "showGameOverDialog");
        DialogGameOverBinding dialogBinding = DialogGameOverBinding.inflate(getLayoutInflater());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .setCancelable(false)
                .create();

        dialogBinding.tvFinalScore.setText(String.valueOf(mViewModel.getFinalScore()));

        dialogBinding.btnConfirm.setOnClickListener(v -> {
            String name = (dialogBinding.etPlayerName.getText() != null) 
                    ? dialogBinding.etPlayerName.getText().toString() : "";
            mViewModel.saveScore(name);
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private void showPauseDialog() {
        GameUtils.info(TAG, "showPauseDialog");
        DialogPauseBinding binding = DialogPauseBinding.inflate(getLayoutInflater());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(binding.getRoot())
                .setCancelable(false)
                .create();

        binding.btnContinue.setOnClickListener(v -> {
            mViewModel.resumeGame();
            dialog.dismiss();
        });
        
        binding.btnQuit.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }
}
