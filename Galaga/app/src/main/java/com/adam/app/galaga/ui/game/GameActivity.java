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

public class GameActivity extends AppCompatActivity {

    // TAG
    private static final String TAG = GameActivity.class.getSimpleName();

    // view binding
    private ActivityGameBinding mBinding;
    // view model
    private GameViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        // view model
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // data binding
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        // observer live data
        mViewModel.getEntities().observe(this, entities -> {
            mBinding.gameSurfaceView.updateEntities(entities);
        });
        mViewModel.getCurrentState().observe(this, this::onState);

        // add back press dispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // check running state
                if (mViewModel.getCurrentState().getValue() == GameEngine.State.RUNNING) {
                    // pause game
                    mViewModel.pauseGame();
                    // show pause dialog
                    showPauseDialog();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.pauseGame();

    }

    private void onState(GameEngine.State state) {
        // Game over state
        if (state == GameEngine.State.GAME_OVER) {
            showGameOverDialog();
        }
    }

    /**
     * show game over dialog
     */
    private void showGameOverDialog() {
        GameUtils.info(TAG, "showGameOverDialog");
        // view binding
        DialogGameOverBinding dialogBinding = DialogGameOverBinding.inflate(getLayoutInflater());

        // alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        // set score
        int finalScore = mViewModel.getFinalScore();
        dialogBinding.tvFinalScore.setText(String.valueOf(finalScore));

        // set confirm button click listener
        dialogBinding.btnConfirm.setOnClickListener(
                v -> {
                    // player name
                    String name = dialogBinding.etPlayerName.getText().toString();
                    // save score
                    mViewModel.saveScore(name);
                    // dismiss dialog
                    dialog.dismiss();
                    // return to main activity
                    finish();
                }
        );

        // show
        dialog.show();
    }


    /**
     * showPauseDialog
     */
    private void showPauseDialog() {
        GameUtils.info(TAG, "showPauseDialog");
        // view binding
        DialogPauseBinding binding = DialogPauseBinding.inflate(getLayoutInflater());

        // alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        // continue
        binding.btnContinue.setOnClickListener(
                v -> {
                    mViewModel.resumeGame();
                    dialog.dismiss();
                }
        );
        // quit
        binding.btnQuit.setOnClickListener(
                v -> {
                    dialog.dismiss();
                    finish();
                }
        );

        // show
        dialog.show();
    }
}