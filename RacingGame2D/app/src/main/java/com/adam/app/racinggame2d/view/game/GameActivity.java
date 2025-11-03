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
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

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

        setupFooterButtons();

    }

    private void setupFooterButtons() {
        // set button click listener
//        mBinding.buttonLeft.setOnClickListener(v -> {
//            // SHOW TOAST MESAGE
//            GameUtil.showToast(this, "Left button clicked");
//        });
//
//        mBinding.buttonLeft.setOnLongClickListener(v -> {
//            // SHOW TOAST MESAGE
//            GameUtil.showToast(this, "Left button long clicked");
//            return true;
//        });
//
//
//        mBinding.buttonRight.setOnClickListener(v -> {
//            // SHOW TOAST MESAGE
//            GameUtil.showToast(this, "right button clicked");
//        });

//        // left button
//        bindButtonRepeatAction(mBinding.buttonLeft, () -> mViewModel.moveLeft(true), 16, () -> mViewModel.moveLeft(false));
//
//        // right button
//        bindButtonRepeatAction(mBinding.buttonRight, () -> mViewModel.moveRight(true), 16, () -> mViewModel.moveRight(false));
//
//        // accelaration button
//        bindButtonRepeatAction(mBinding.buttonSpeedUp, () -> mViewModel.speedUp(true), 100, null);
//
//        // slow down button
//        bindButtonRepeatAction(mBinding.buttonSlowDown, () -> mViewModel.speedUp(false), 100, null);

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

    /**
     * Bind a button to a long press action.
     * @param button The button to bind.
     * @param actionDown The action to perform when the button is pressed.
     * @param intervalMs The interval between each action.
     * @param actionUp The action to perform when the button is released.
     */
    private void bindButtonRepeatAction(View button, Runnable actionDown, long intervalMs, Runnable actionUp) {
        final Runnable repeatRunnable = new Runnable() {
            @Override
            public void run() {
                actionDown.run();
                mHandler.postDelayed(this, intervalMs);
            }
        };

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHandler.post(repeatRunnable);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mHandler.removeCallbacks(repeatRunnable);
                        if (actionUp != null) actionUp.run();
                        break;
                }
                return true;
            }

        });
    }
}