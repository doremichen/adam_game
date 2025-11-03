/**
 * Copyright (C) 2025 Adam. All Rights Reserved.
 * Description: This class is the view of the game.
 *
 * @author Adam Chen
 * @since 2025-11-03
 */
package com.adam.app.racinggame.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.racinggame.R;
import com.adam.app.racinggame.databinding.FragmentGameBinding;
import com.adam.app.racinggame.modelview.GameViewModel;
import com.adam.app.racinggame.util.GameUtil;

public class GameFragment extends Fragment {
    // TAG
    private static final String TAG = "GameFragment";

    // view binding
    private FragmentGameBinding mBinding;

    private GameViewModel mViewModel;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentGameBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        // visibility the count down text view
        mBinding.countdownText.setVisibility(View.VISIBLE);
        startCountdown();

    }

    private void startCountdown() {
        GameUtil.log(TAG, "startCountdown");

        new CountDownTimer(4000, 1000) {
            int count = 3;

            public void onTick(long millisUntilFinished) {
                if (count > 0) {
                    mBinding.countdownText.setText(String.valueOf(count));
                    count--;
                } else {
                    mBinding.countdownText.setText("Go!");
                }
            }

            public void onFinish() {
                mBinding.countdownText.setVisibility(View.GONE);
                // show toast
                GameUtil.showToast(getContext(), getString(R.string.racing_game_start_game_toast));
                // Start Game
                mViewModel.startGame();
            }
        }.start();


    }


}