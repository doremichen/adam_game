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

package com.adam.app.arenaminifight.ui.menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adam.app.arenaminifight.MainActivity;
import com.adam.app.arenaminifight.databinding.ActivityMenuBinding;
import com.adam.app.arenaminifight.ui.about.AboutActivity;
import com.adam.app.arenaminifight.ui.leaderboard.LeaderboardActivity;
import com.adam.app.arenaminifight.ui.settings.SettingsActivity;
import com.adam.app.arenaminifight.utils.GameUtil;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Main Menu Activity providing navigation hub (Start Game, Setting, Leaderboard, About).
 * Trace: UC-MENU-02, REQ-MENU-02, UI-002
 */
@AndroidEntryPoint
public class MenuActivity extends AppCompatActivity {

    private static final String TAG = MenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameUtil.log(TAG + ": onCreate()");

        ActivityMenuBinding binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnStartGame.setOnClickListener(v -> {
            GameUtil.log("[StartGameFlow][Step 1] MenuActivity: Start Game button clicked -> Launching MainActivity");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        binding.btnSetting.setOnClickListener(v -> {
            GameUtil.log(TAG + ": Setting clicked");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        binding.btnLeaderboard.setOnClickListener(v -> {
            GameUtil.log(TAG + ": Leaderboard clicked");
            Intent intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
        });

        binding.btnAbout.setOnClickListener(v -> {
            GameUtil.log(TAG + ": About clicked");
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        });
    }
}
