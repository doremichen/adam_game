/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the game activity.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/29
 */
package com.adam.app.racinggame2d.view.game;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.databinding.ActivityGameBinding;
import com.adam.app.racinggame2d.util.Constants;
import com.adam.app.racinggame2d.util.GameUtil;
import com.adam.app.racinggame2d.util.SharedPrefHelper;
import com.adam.app.racinggame2d.util.LongPressButtonHelper;
import com.adam.app.racinggame2d.viewmodel.GameViewModel;

public class GameActivity extends AppCompatActivity {
    // TAG
    private static final String TAG = "GameActivity";


    // View binding
    private ActivityGameBinding mBinding;

    private GameViewModel mViewModel;

    // Handler for long press button action
    private Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameUtil.log(TAG, "onCreate");
        // View binding
        mBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // get player name by intent
        String playerName = getIntent().getStringExtra(Constants.PLAYER_NAME);
        GameUtil.log(TAG, "player name: " + playerName);
        // save player name to shared preferences
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance(getApplicationContext());
        sharedPrefHelper.setPlayerName(playerName);


        // GameViewModel
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mBinding.gameView.setViewModel(mViewModel);

        mBinding.gameView.post(() -> {
            int width = mBinding.gameView.getWidth();
            int height = mBinding.gameView.getHeight();
            GameUtil.log(TAG, "width: " + width + ", height: " + height);
            mViewModel.prepareGameEngine(width, height);
            mViewModel.setGameUpdateListener(mBinding.gameView);
            // start the game
            mViewModel.startGame();
        });

        //observer
        mViewModel.getIsGameOver().observe(this, isGameOver -> {
            if (isGameOver) {
                // stop the game
                mViewModel.stopGame();
                // save game result
                mViewModel.saveGameResult();
                // show game over dialog
                showGameOverDialog();
            }
        });

        mViewModel.getScore().observe(this, score -> {
                    String scoreText = getString(R.string.racinggame2d_score_tv, String.valueOf(score));
                    mBinding.tvScore.setText(scoreText);
        });

        // progressbar setMax is max car hp
        mBinding.progressHp.setMax(Constants.MAX_CAR_HP);

        mViewModel.getHp().observe(this, hp -> {
            mBinding.progressHp.setProgress(hp);
        });

        setupFooterButtons();

        // register back button listener
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // stop the game
                mViewModel.pauseGame();
                showExitConfirmDialog();
            }
        });
    }

    private void showExitConfirmDialog() {
        GameUtil.DialogButtonContent positive = new GameUtil.DialogButtonContent(getString(R.string.racinggame2d_dlg_ok_btn_label), (dialog, which) -> {
            // save
            mViewModel.saveGameResult();
            // finish the activity
            finish();
        });
        GameUtil.DialogButtonContent negative = new GameUtil.DialogButtonContent(getString(R.string.racinggame2d_dlg_cancel_btn_label), (dialog, which) -> {
            // start game
            mViewModel.resumeGame();
        });
        GameUtil.showDialog(this, getString(R.string.racinggame2d_exit_dlg_title), getString(R.string.racinggame2d_exit_dlg_content), positive, negative);

    }

    private void showGameOverDialog() {
        GameUtil.DialogButtonContent positive = new GameUtil.DialogButtonContent(getString(R.string.racinggame2d_dlg_ok_btn_label), (dialog, which) -> {
            // restart game again
            mViewModel.restartGame();

        });
        GameUtil.DialogButtonContent negative = new GameUtil.DialogButtonContent(getString(R.string.racinggame2d_dlg_cancel_btn_label), (dialog, which) -> {
            // finish the activity
            finish();
        });
        GameUtil.showDialog(this, getString(R.string.racinggame2d_game_over_dlog_title), getString(R.string.racinggame2d_game_over_dlg_content), positive, negative);
    }

    private void setupFooterButtons() {
        // set button click listener
        mBinding.buttonLeft.setOnClickListener(v -> {
            // SHOW TOAST MESAGE
            //GameUtil.showToast(this, "Left button clicked");
            mViewModel.moveHorizontally(true);
        });

        mBinding.buttonRight.setOnClickListener(v -> {
            // SHOW TOAST MESAGE
            //GameUtil.showToast(this, "right button clicked");
            mViewModel.moveHorizontally(false);
        });

        mBinding.buttonSpeedUp.setOnClickListener(v -> {
            mViewModel.speedUp(true);
        });

        mBinding.buttonSlowDown.setOnClickListener(v -> {
            mViewModel.speedUp(false);
        });

        // --- long press button action ---
        LongPressButtonHelper.attach(mBinding.buttonLeft, 100L, () -> {
            mViewModel.moveHorizontally(true);
        });
        LongPressButtonHelper.attach(mBinding.buttonRight, 100L, () -> {
            mViewModel.moveHorizontally(false);
        });
        LongPressButtonHelper.attach(mBinding.buttonSpeedUp, 100L, () -> {
            mViewModel.speedUp(true);
        });
        LongPressButtonHelper.attach(mBinding.buttonSlowDown, 100L, () -> {
            mViewModel.speedUp(false);
        });
  }

    @Override
    protected void onResume() {
        super.onResume();
        GameUtil.log(TAG, "onResume");
        if (!mViewModel.isReady()) {
            GameUtil.log(TAG, "Game engine is not ready!");
            return;
        }
        mViewModel.resumeGame();
    }


    @Override
    protected void onPause() {
        super.onPause();
        GameUtil.log(TAG, "onPause");
        mViewModel.pauseGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameUtil.log(TAG, "onDestroy");
        mViewModel.stopGame();
    }

}