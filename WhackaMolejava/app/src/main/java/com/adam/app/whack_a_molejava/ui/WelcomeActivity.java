/*
 * MIT License
 *
 * Copyright (c) 2025 Adam Chen
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
package com.adam.app.whack_a_molejava.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.whack_a_molejava.databinding.ActivityWelcomeBinding;
import com.adam.app.whack_a_molejava.util.GameUtils;
import com.adam.app.whack_a_molejava.viewmodels.WelcomeViewModel;

/**
 * This class is the welcome activity for the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-02
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWelcomeBinding binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // init view model
        WelcomeViewModel viewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);

        // set listener
        binding.btnStart.setOnClickListener(v -> viewModel.startGame());
        binding.btnSetting.setOnClickListener(v -> viewModel.setting());
        binding.btnAbout.setOnClickListener(v -> viewModel.about());
        binding.btnExit.setOnClickListener(v -> viewModel.exit());

        // observer event
        viewModel.getEvent().observe(this, this::handleEvent);
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
                GameUtils.startActivity(this, AboutActivity.class);
                break;
            case EXIT:
                finish();
                break;
            default:
                break;
        }
    }

}
