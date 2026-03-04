/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the result fragment of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/02
 */
package com.adam.app.arenaminifight.ui.result;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adam.app.arenaminifight.R;

public class ResultFragment extends Fragment {

    private ResultViewModel mViewModel;

    public static ResultFragment newInstance() {
        return new ResultFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        // TODO: Use the ViewModel
    }

}