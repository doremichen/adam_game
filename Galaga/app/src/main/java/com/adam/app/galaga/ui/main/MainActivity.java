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

package com.adam.app.galaga.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.galaga.databinding.ActivityMainBinding;
import com.adam.app.galaga.ui.about.AboutActivity;
import com.adam.app.galaga.ui.game.GameActivity;
import com.adam.app.galaga.ui.leaderboard.LeaderboardActivity;
import com.adam.app.galaga.ui.settings.SettingsActivity;
import com.adam.app.galaga.utils.GameUtils;
import com.adam.app.galaga.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    //TAG
    private static final String TAG = MainActivity.class.getSimpleName();

    // view binding
    private ActivityMainBinding mBinding;

    // view model
    private MainViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        // init view model
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        // observe event
        mViewModel.getAction().observe(this, this::handleAction);
    }

    private void handleAction(MainViewModel.ActionType action) {
        GameUtils.info(TAG, "handleAction: " + action.name());
        if (action == MainViewModel.ActionType.DO_NOTHING) {
            return;
        }

        switch (action) {
            case START_GAME:
                // start game
                GameUtils.startActivity(this, GameActivity.class);
                break;
            case OPEN_SETTINGS:
                // open settings
                GameUtils.startActivity(this, SettingsActivity.class);
                break;
            case OPEN_LEADER_BOARD:
                // open leader board
                GameUtils.startActivity(this, LeaderboardActivity.class);
                break;
            case OPEN_ABOUT:
                // open about
                GameUtils.startActivity(this, AboutActivity.class);
                break;
            case EXIT:
                // exit
                this.finish();
                break;
        }
        //
        mViewModel.onDoNothing();
    }
}