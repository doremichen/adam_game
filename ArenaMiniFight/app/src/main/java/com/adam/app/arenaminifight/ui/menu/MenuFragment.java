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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.adam.app.arenaminifight.R;
import com.adam.app.arenaminifight.databinding.FragmentMenuBinding;
import com.adam.app.arenaminifight.utils.GameUtil;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Main Menu Fragment providing navigation hub (Start Game, Setting, Leaderboard, About).
 * Trace: UC-MENU-02, REQ-MENU-02, UI-002
 */
@AndroidEntryPoint
public class MenuFragment extends Fragment {

    private static final String TAG = MenuFragment.class.getSimpleName();
    private FragmentMenuBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GameUtil.log(TAG + ": onCreateView()");
        mBinding = FragmentMenuBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.btnStartGame.setOnClickListener(v -> {
            GameUtil.log("[StartGameFlow][Step 1] MenuFragment: Start Game button clicked -> Navigating to gameFragment");
            Navigation.findNavController(v).navigate(R.id.action_menu_to_game);
        });

        mBinding.btnSetting.setOnClickListener(v -> {
            GameUtil.log(TAG + ": Setting clicked");
            Navigation.findNavController(v).navigate(R.id.action_menu_to_settings);
        });

        mBinding.btnLeaderboard.setOnClickListener(v -> {
            GameUtil.log(TAG + ": Leaderboard clicked");
            Navigation.findNavController(v).navigate(R.id.action_menu_to_leaderboard);
        });

        mBinding.btnAbout.setOnClickListener(v -> {
            GameUtil.log(TAG + ": About clicked");
            Navigation.findNavController(v).navigate(R.id.action_menu_to_about);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
