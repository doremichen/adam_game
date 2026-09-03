/*
 * Copyright (c) 2026 Adam Chen
 */

package com.adam.app.mydeviceinfo.ui.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.mydeviceinfo.common.Utils;
import com.adam.app.mydeviceinfo.databinding.FragmentToolsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public final class ToolsFragment extends Fragment {
    private FragmentToolsBinding mBinding;
    private ToolsViewModel mViewModel;

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentToolsBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ToolsViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        setupObservers();
        return mBinding.getRoot();
    }

    /**
     * Sets up observers for ViewModel events.
     */
    private void setupObservers() {
        mViewModel.getToastEvent().observe(getViewLifecycleOwner(), resId -> {
            if (resId != null && resId != 0) {
                Utils.showToast(requireContext(), getString(resId));
            }
        });
    }
}
