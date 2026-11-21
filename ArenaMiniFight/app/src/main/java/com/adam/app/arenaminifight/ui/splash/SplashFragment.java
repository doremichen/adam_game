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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OROTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.adam.app.arenaminifight.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.adam.app.arenaminifight.R;
import com.adam.app.arenaminifight.databinding.FragmentSplashBinding;
import com.adam.app.arenaminifight.utils.GameUtil;
import com.adam.app.arenaminifight.data.gateway.GameGatewayImpl;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Splash Screen Fragment displaying logo and navigating to Menu.
 * Also binds the background GameService on startup.
 * Trace: UC-MENU-01, UC-IPC-01, REQ-MENU-01, UI-001
 */
@AndroidEntryPoint
public class SplashFragment extends Fragment {

    private static final String TAG = SplashFragment.class.getSimpleName();
    private static final long SPLASH_DELAY_MS = 2000L;

    private FragmentSplashBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GameUtil.log(TAG + ": onCreateView()");
        mBinding = FragmentSplashBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pre-bind GameService on startup so IPC is ready when entering game
        try {
            GameGatewayImpl.getInstance().bindGameService();
            GameUtil.log(TAG + ": Pre-bound GameService successfully");
        } catch (Exception e) {
            GameUtil.log(TAG + ": Failed to pre-bind GameService: " + e.getMessage());
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (getView() != null) {
                GameUtil.log(TAG + ": Navigating to MenuFragment");
                Navigation.findNavController(view).navigate(R.id.action_splash_to_menu);
            }
        }, SPLASH_DELAY_MS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
