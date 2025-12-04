/**
 * Copyright 2025 Adam Game
 * <p>
 * This class is the welcome activity for the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-02
 *
 */
package com.adam.app.whack_a_molejava.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.whack_a_molejava.databinding.ActivityWelcomeBinding;
import com.adam.app.whack_a_molejava.util.GameUtils;
import com.adam.app.whack_a_molejava.viewmodels.WelcomeViewModel;

public class WelcomeActivity extends AppCompatActivity {

    // view binding
    private ActivityWelcomeBinding mBinding;

    // view model
    private WelcomeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        // init view model
        mViewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);

        // set listener
        mBinding.btnStart.setOnClickListener(v -> mViewModel.startGame());
        mBinding.btnSetting.setOnClickListener(v -> mViewModel.setting());
        mBinding.btnAbout.setOnClickListener(v -> mViewModel.about());
        mBinding.btnExit.setOnClickListener(v -> mViewModel.exit());

        // observer event
        mViewModel.getEvent().observe(this, this::handleEvent);
    }

    private void handleEvent(WelcomeViewModel.WelcomeEvent event) {
        switch (event) {
            case START_GAME:
                GameUtils.startActivity(this, GameActivity.class);
                break;
            case SETTING:
                GameUtils.startActivity(this, GameSettingsActivity.class);
                break;
            case ABOUT:
                GameUtils.showUnImplementedToast(this);
                break;
            case EXIT:
                finish();
                break;
            default:
                break;
        }
    }

}