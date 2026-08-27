/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.presentation.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.snake.R;
import com.adam.app.snake.databinding.ActivityWelcomeBinding;
import com.adam.app.snake.presentation.ui.dialog.NameInputDialog;
import com.adam.app.snake.presentation.viewmodel.WelcomeViewModel;
import com.adam.app.snake.util.Utils;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Welcome activity - launcher activity refactored with Data Binding and SingleLiveEvent
 */
@AndroidEntryPoint
public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    private ActivityWelcomeBinding mBinding;
    private WelcomeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        // Observe navigation events from ViewModel
        mViewModel.getNavigationEvent().observe(this, this::handleNavigation);
    }

    /**
     * Handle navigation events emitted by the ViewModel
     * @param event NavigationEvent
     */
    private void handleNavigation(WelcomeViewModel.NavigationEvent event) {
        if (event == null) return;

        switch (event) {
            case START_GAME:
                showInputUserNameDialog();
                break;
            case SHOW_LEADERBOARD:
                Utils.logDebug(TAG, "start leader board");
                startActivity(LeaderBoardActivity.class);
                break;
            case SHOW_ABOUT:
                Utils.logDebug(TAG, "start about");
                startActivity(AboutActivity.class);
                break;
        }
    }

    private void showInputUserNameDialog() {
        Utils.logDebug(TAG, "showInputUserNameDialog");
        NameInputDialog dlg = new NameInputDialog();
        dlg.setListener(new NameInputDialog.Listener() {
            @Override
            public void onNameConfirmed(String name) {
                Utils.logDebug(TAG, "onNameConfirmed: " + name);
                mViewModel.saveUserName(name);
                startActivity(GameActivity.class);
            }
            @Override
            public void onNameCanceled() {
                Utils.logDebug(TAG, "onNameCanceled");
            }
        });
        dlg.show(getSupportFragmentManager(), "name_input_dialog");
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
