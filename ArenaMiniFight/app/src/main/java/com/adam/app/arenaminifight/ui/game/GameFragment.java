/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the game fragment of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/02
 */
package com.adam.app.arenaminifight.ui.game;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.adam.app.arenaminifight.R;
import com.adam.app.arenaminifight.databinding.FragmentGameBinding;
import com.adam.app.arenaminifight.utils.GameUtil;

public class GameFragment extends Fragment {

    // TAG
    private static final String TAG = "GameFragment";

    // view biding
    private FragmentGameBinding mBinding;

    // view model
    private GameViewModel mViewModel;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        GameUtil.log(TAG + ": onCreateView");
        mBinding = FragmentGameBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GameUtil.log(TAG + ": onViewCreated");

//        // 讓 Fragment 內容延伸到系統狀態欄下方
//        WindowInsetsControllerCompat windowInsetsController =
//                WindowCompat.getInsetsController(requireActivity().getWindow(), view);
//        windowInsetsController.setSystemBarsBehavior(
//                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
//        // 可選：隱藏狀態欄以達到全螢幕遊戲效果
//        // windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        // init view model
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // data binding
        mBinding.setVm(mViewModel);
        mBinding.setLifecycleOwner(this);

        // set touch listener
        mBinding.gameSurfaceView.setOnTouchListener(this::onTouch);

        // observer exit event
        mViewModel.getExitEvent().observe(getViewLifecycleOwner(), this::onExit);

    }

    private void onExit(Boolean isExit) {
        if (isExit) {
            GameUtil.log(TAG + ": onExit");
            getActivity().finish();
        }
    }

    private boolean onTouch(View view, MotionEvent event) {
        GameUtil.log(TAG + ": onTouch");
        if (event.getAction() == MotionEvent.ACTION_MOVE ||
                event.getAction() == MotionEvent.ACTION_DOWN) {
            mViewModel.updatePlayerMove(event.getX(), event.getY());
            return true;
        }
        return false;
    }
}