/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.adam.app.tetrisgame.R;
import com.adam.app.tetrisgame.databinding.ActivityMainBinding;
import com.adam.app.tetrisgame.presentation.viewmodel.MainViewModel;
import com.adam.app.tetrisgame.util.Utils;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        
        observeViewModel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    private void observeViewModel() {
        mViewModel.getNavigationEvent().observe(this, event -> {
            if (event == null) return;
            switch (event) {
                case START_GAME:
                    startActivity(new Intent(this, GameActivity.class));
                    break;
                case SHOW_LEADERBOARD:
                    startActivity(new Intent(this, LeaderboardActivity.class));
                    break;
                case SHOW_ABOUT:
                    showAboutDialog();
                    break;
                case EXIT:
                    finish();
                    break;
            }
        });
    }

    private void showAboutDialog() {
        Utils.DialogButton okButton = new Utils.DialogButton(getString(R.string.dialog_button_ok), this::dismissDialog);
        Utils.showAlertDialog(MainActivity.this, getString(R.string.dialog_title_instruction), getString(R.string.dialog_message_instruction), okButton, null);
    }

    private void dismissDialog(AlertDialog dialog) {
        dialog.dismiss();
    }
}
