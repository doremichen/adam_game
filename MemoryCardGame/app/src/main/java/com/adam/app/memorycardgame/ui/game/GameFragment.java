/**
 * Copyright (c) 2021 Adam Chen. All rights reserved.
 *
 * Description: This is the game fragment for the memory card game.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/02/12
 */
package com.adam.app.memorycardgame.ui.game;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adam.app.memorycardgame.R;
import com.adam.app.memorycardgame.databinding.FragmentGameBinding;

public class GameFragment extends Fragment {

    private GameViewModel mViewModel;

    // view binding
    private FragmentGameBinding mBinding;


    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentGameBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init view model
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // set view model
        mBinding.setVm(mViewModel);
    }
}