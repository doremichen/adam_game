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

package com.adam.app.tic_tac_toe.ui.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.adam.app.tic_tac_toe.R;
import com.adam.app.tic_tac_toe.databinding.FragmentMainBinding;
import com.adam.app.tic_tac_toe.ui.viewmodels.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Main menu fragment for the game.
 */
@AndroidEntryPoint
public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private FragmentMainBinding mBinding;
    private NavController mNavCtl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMainBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mBinding.setViewModel(mViewModel);
        mNavCtl = NavHostFragment.findNavController(this);

        setupNavigationObserver();
    }

    private void setupNavigationObserver() {
        mViewModel.getNavigateTo().observe(getViewLifecycleOwner(), destination -> {
            if (destination == null || destination == MainViewModel.NavigationDestination.NONE) {
                return;
            }
            switch (destination) {
                case START_GAME -> mNavCtl.navigate(R.id.action_mainFragment_to_gameFragment);
                case SETTINGS -> mNavCtl.navigate(R.id.action_mainFragment_to_settingsFragment);
                case ABOUT -> mNavCtl.navigate(R.id.action_mainFragment_to_aboutFragment);
                case EXIT -> requireActivity().finish();
            }
            mViewModel.onNavigationDone();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
