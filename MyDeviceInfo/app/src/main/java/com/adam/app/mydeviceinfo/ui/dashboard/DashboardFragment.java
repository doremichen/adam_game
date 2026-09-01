/*
 * Copyright (c) 2026 Adam Chen
 */

package com.adam.app.mydeviceinfo.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.mydeviceinfo.databinding.FragmentDashboardBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public final class DashboardFragment extends Fragment {
    private FragmentDashboardBinding mBinding;
    private DashboardViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentDashboardBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        // Activate the reactive stream
        mViewModel.getInfoSource().observe(getViewLifecycleOwner(), info -> {
            // Data is handled by ViewModel's MediatorLiveData
        });

        return mBinding.getRoot();
    }
}
