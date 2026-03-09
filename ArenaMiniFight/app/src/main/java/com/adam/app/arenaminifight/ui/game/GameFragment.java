/**
 * Copyright 2023 Adam Chen. All rights reserved.
 * <p>
 * Description: This is the game fragment of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/02
 */
package com.adam.app.arenaminifight.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.arenaminifight.databinding.FragmentGameBinding;
import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.utils.GameConstants;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.List;

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

        // init view model
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // data binding
        mBinding.setVm(mViewModel);
        mBinding.setLifecycleOwner(this);

        // init player
        mViewModel.initPlayerFromJni(GameConstants.DefaultPlayerName);
        // start game
        mViewModel.startGame();

        // observer players
        mViewModel.getPlayers().observe(getViewLifecycleOwner(), this::onPlayers);


        // set touch listener
        mBinding.gameSurfaceView.setOnTouchListener(this::onTouch);

        // observer exit event
        mViewModel.getExitEvent().observe(getViewLifecycleOwner(), this::onExit);

    }

    @Override
    public void onResume() {
        super.onResume();
        GameUtil.log(TAG + ": onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        GameUtil.log(TAG + ": onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GameUtil.log(TAG + ":onDestroy");
    }

    private void onPlayers(List<Player> players) {
        // update players
        mBinding.gameSurfaceView.updatePlayers(players);
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