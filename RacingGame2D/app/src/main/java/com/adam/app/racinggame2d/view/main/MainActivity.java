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
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.databinding.ActivityMainBinding;
import com.adam.app.racinggame2d.util.Constants;
import com.adam.app.racinggame2d.util.GameUtil;
import com.adam.app.racinggame2d.view.game.GameActivity;
import com.adam.app.racinggame2d.view.setting.SettingsActivity;
import com.adam.app.racinggame2d.viewmodel.MainViewModel;

import java.util.HashMap;
import java.util.Map;

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
        // define event map table
        Map<MainViewModel.NavigateEvent, Runnable> navigationActions = new HashMap<>();
        navigationActions.put(MainViewModel.NavigateEvent.START_GAME, this::showPlayerNameDialog);
        navigationActions.put(MainViewModel.NavigateEvent.LEADER_BOARD, this::showNoImplement);
        navigationActions.put(MainViewModel.NavigateEvent.SETTING, this::gotoSettings);
        navigationActions.put(MainViewModel.NavigateEvent.ABOUT, this::showNoImplement);
        navigationActions.put(MainViewModel.NavigateEvent.EXIT, this::finish);

        // observer
        mViewModel.mNavigateEvent.observe(this, event -> {
            if (navigationActions.containsKey(event)) {
                Runnable action = navigationActions.get(event);
                if (action != null) {
                    action.run();
                }
            }
        });


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


    /**
     * Show edit player name dialog
     */
    private void showPlayerNameDialog() {
        // create positive dialog button for edit dialog
        GameUtil.DialogButtonContent positiveButton = new GameUtil.DialogButtonContent(getString(R.string.racinggame2d_dlg_ok_btn_label),
                text -> {
                    if (TextUtils.isEmpty(text)) {
                        text = getString(R.string.racinggame2d_default_name);
                    }
                    mViewModel.setPlayerName(text);
                    goToGame(text);

                });
        // create negative dialog button for edit dialog
        GameUtil.DialogButtonContent negativeButton = new GameUtil.DialogButtonContent(getString(R.string.racinggame2d_dlg_cancel_btn_label), (DialogInterface.OnClickListener) null);

        // show edit player name dialog
        GameUtil.showEditDialog(this,
                getString(R.string.racinggame2d_dlg_edit_player_name_tilte), getString(R.string.racinggame2d_dlg_edit_player_name_hint),
                getString(R.string.racinggame2d_dlg_edit_player_name_text),
                positiveButton, negativeButton);

    }

    private void goToGame(String text) {
        // go to game activity
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(Constants.PLAYER_NAME, text);
        startActivity(intent);
    }

    /**
     * gotoSettings
     */
    private void gotoSettings() {
        // go to settings activity
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }



    private void showNoImplement() {
        GameUtil.showToast(this, "Not implemented yet!");
    }
}