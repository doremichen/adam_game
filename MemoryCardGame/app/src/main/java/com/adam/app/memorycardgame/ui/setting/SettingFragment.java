/**
 * Copyright (c) 2026 Adam Chen. All rights reserved.
 * <p>
 * Description: This class is used to show the setting page.
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/12
 */
package com.adam.app.memorycardgame.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.memorycardgame.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {

    private SettingViewModel mViewModel;
    // view binding
    private FragmentSettingBinding mBinding;


    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSettingBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init view model
        mViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        // set view model
        mBinding.setVm(mViewModel);

        // attach preference fragment
        getChildFragmentManager()
                .beginTransaction()
                .replace(mBinding.prefContainer.getId(), new SettingsPreferenceFragment())
                .commit();
    }
}