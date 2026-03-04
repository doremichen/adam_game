/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This class is the welcome fragment
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/04
 */
package com.adam.app.arenaminifight.ui.welcome;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adam.app.arenaminifight.R;
import com.adam.app.arenaminifight.databinding.FragmentWelcomeBinding;
import com.adam.app.arenaminifight.utils.GameUtil;

public class WelcomeFragment extends Fragment {

    // TAG
    private static final String TAG = "WelcomeFragment";

    // view binding
    private FragmentWelcomeBinding mBinding;

    private WelcomeViewModel mViewModel;

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init view model
        mViewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);
        // data biding
        mBinding.setVm(mViewModel);
        mBinding.setLifecycleOwner(this);

        // observer log in status
        mViewModel.getIsLoginSuccess().observe(getViewLifecycleOwner(), status -> {
            GameUtil.log(TAG + " login status: " + status);
            if (!status) {
                // show toast
                //GameUtil.showToast(this.getContext(), getString(R.string.arenaminifight_game_login_failed));
                // consumer the event
                return;
            }

            // delay 500 ms
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // show toast
                GameUtil.showToast(this.getContext(), getString(R.string.arenaminifight_game_login_success));
                // navigate to lobby
                navigateToLobby();
            }, 500L);
        });

    }

    private void navigateToLobby() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_welcome_to_lobby);
    }
}