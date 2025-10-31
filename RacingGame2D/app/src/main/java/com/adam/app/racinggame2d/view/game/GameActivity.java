/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the game activity.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/29
 */
package com.adam.app.racinggame2d.view.game;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.racinggame2d.databinding.ActivityGameBinding;
import com.adam.app.racinggame2d.util.Constants;
import com.adam.app.racinggame2d.util.GameUtil;
import com.adam.app.racinggame2d.util.SharedPrefHelper;
import com.adam.app.racinggame2d.viewmodel.GameViewModel;

public class GameActivity extends AppCompatActivity {
    // TAG
    private static final String TAG = "GameActivity";


    // View binding
    private ActivityGameBinding mBinding;

    private GameViewModel mViewModel;


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

        setupFooterButtons();

    }

    private void setupFooterButtons() {
        // set button click listener
        mBinding.buttonLeft.setOnClickListener(v -> {
            mViewModel.moveHorizontally(true);
        });

        mBinding.buttonRight.setOnClickListener(v -> {
            mViewModel.moveHorizontally(false);
        });

        mBinding.buttonSpeedUp.setOnClickListener(v -> {
            mViewModel.speedUp(true);
        });

        mBinding.buttonSlowDown.setOnClickListener(v -> {
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