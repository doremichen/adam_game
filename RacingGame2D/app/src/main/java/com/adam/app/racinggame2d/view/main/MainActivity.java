/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the main activity of the racing game.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/28
 */
package com.adam.app.racinggame2d.view.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.databinding.ActivityMainBinding;
import com.adam.app.racinggame2d.util.GameUtil;
import com.adam.app.racinggame2d.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
     // TAG
    private static final String TAG = MainActivity.class.getSimpleName();

    // Main view model
    private MainViewModel mViewModel;


    // view binding
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // init view model
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        intView();

        // Observer
        mViewModel.mNavigateEvent.observe(
                this,
                event -> {
                    switch (event) {
                        case START_GAME:
                            showPlayerNameDialog();
                            break;
                        case LEADER_BOARD:
                            showNoImplement();
                            break;
                        case SETTING:
                            showNoImplement();
                            break;
                        case ABOUT:
                            showNoImplement();
                            break;
                        case EXIT:
                            finish();
                            break;
                        default:
                            break;
                    }
                }
        );
    }

    private void intView() {
        // button click listener
        mBinding.btnStart.setOnClickListener(v -> {
            // start game
            mViewModel.navigateToStartGame();
        });

        mBinding.btnLeaderboard.setOnClickListener(v -> {
            // show leaderboard
            mViewModel.navigateToLeaderBoard();
        });

        mBinding.btnSetting.setOnClickListener(v -> {
            // show settings
            mViewModel.navigateToSetting();
        });

        mBinding.btnAbout.setOnClickListener(v -> {
            // show about
            mViewModel.navigateToAbout();
        });


        mBinding.btnExit.setOnClickListener(v -> {
            // exit game
           mViewModel.navigateToExit();
        });
    }


    private void showPlayerNameDialog() {
        // create positive dialog button for edit dialog
        GameUtil.DialogButtonContent positiveButton = new GameUtil.DialogButtonContent(getString(R.string.racinggame2d_dlg_ok_btn_label),
                text -> {
            if (TextUtils.isEmpty(text)) {
                text = "Default name";
            }
            mViewModel.setPlayerName(text);
            goToGame(text);

        });
        // create negative dialog button for edit dialog
        GameUtil.DialogButtonContent negativeButton = new GameUtil.DialogButtonContent(getString(R.string.racinggame2d_dlg_cancel_btn_label), (DialogInterface.OnClickListener) null);

        // show edit player name dialog
        GameUtil.showEditDialog(this,
                "Start Game", getString(R.string.racinggame2d_dlg_edit_player_name_title),
                "Abb",
                positiveButton, negativeButton);

    }

    private void goToGame(String text) {
        //TODO: GO TO GAME ACTIVITY
        showNoImplement();
    }

    private void showNoImplement() {
        GameUtil.showToast(this, "Not implemented yet!");
    }
}