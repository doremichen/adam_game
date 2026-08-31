/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.databinding.ActivityMainBinding;
import com.adam.app.racinggame2d.ui.about.AboutActivity;
import com.adam.app.racinggame2d.ui.game.GameActivity;
import com.adam.app.racinggame2d.ui.leaderboard.LeaderBoardActivity;
import com.adam.app.racinggame2d.ui.setting.SettingsActivity;
import com.adam.app.racinggame2d.util.Constants;
import com.adam.app.racinggame2d.util.Utils;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Main activity showing the game menu.
 */
@AndroidEntryPoint
public final class MainActivity extends AppCompatActivity {
    private MainViewModel mViewModel;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        initObservers();
    }

    private void initObservers() {
        mViewModel.getNavigateEvent().observe(this, event -> {
            switch (event) {
                case START_GAME:
                    showPlayerNameDialog();
                    break;
                case LEADER_BOARD:
                    gotoLeaderBoard();
                    break;
                case SETTING:
                    gotoSettings();
                    break;
                case ABOUT:
                    gotoAbout();
                    break;
            }
        });
    }

    private void showPlayerNameDialog() {
        Utils.DialogButtonContent negative = new Utils.DialogButtonContent(
                getString(R.string.racinggame2d_dlg_cancel_btn_label),
                (dialog, which) -> dialog.dismiss());

        Utils.showCarInitDialog(this,
                getString(R.string.racinggame2d_dlg_edit_player_name_tilte),
                getString(R.string.racinggame2d_dlg_edit_player_name_hint),
                getString(R.string.racinggame2d_dlg_edit_car_id_hint),
                getString(R.string.racinggame2d_dlg_edit_car_name_hint),
                getString(R.string.racinggame2d_dlg_ok_btn_label),
                (playerName, carId, carName) -> {
                    String pName = TextUtils.isEmpty(playerName) ? getString(R.string.racinggame2d_default_name) : playerName;
                    String cId = TextUtils.isEmpty(carId) ? "BXP1234" : carId;
                    String cName = TextUtils.isEmpty(carName) ? "car1" : carName;

                    mViewModel.setPlayerName(pName);
                    goToGame(pName, cId, cName);
                }, negative);
    }

    private void goToGame(String pName, String cId, String cName) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(Constants.PLAYER_NAME, pName);
        intent.putExtra(Constants.CAR_ID, cId);
        intent.putExtra(Constants.CAR_NAME, cName);
        startActivity(intent);
    }

    private void gotoSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void gotoAbout() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    private void gotoLeaderBoard() {
        startActivity(new Intent(this, LeaderBoardActivity.class));
    }
}
