/**
 * This class is the main activity of the Flappy Bird game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.flappybird.databinding.ActivityMainBinding;
import com.adam.app.flappybird.model.GameState;
import com.adam.app.flappybird.util.GameUtil;
import com.adam.app.flappybird.viewmodel.GameViewModel;

public class GameActivity extends AppCompatActivity {

    // view binding
    private ActivityMainBinding mBinding;
    // view model
    private GameViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        // set the view model to the view
        mBinding.gameSurface.setViewModel(mViewModel);

        // set touch listener
        mBinding.gameSurface.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mViewModel.flap();
            }
            return true;
        });

        // observer
        mViewModel.getGameState().observe(this, gameState -> {
                // show game over dialog
                if (gameState == GameState.GAME_OVER) {
                    showGameOverDialog();
                }
        });


        // exit button click listener
        mBinding.btnExit.setOnClickListener(v -> {
            // stop game
            mViewModel.stopGame();
            // finish activity
            finish();
        });
    }

    private void showGameOverDialog() {
        GameUtil.ButtonContent positiveButton = new GameUtil.ButtonContent("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        // show
        GameUtil.showDialog(this, "Game Over", "Game over! Please press ok button to exit this game!", positiveButton, null);

    }
}