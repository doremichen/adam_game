/**
 * Copyright 2026 Adam Chen, All rights reserved.
 * <p>
 * Description: This is the main fragment of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/02/11
 *
 */
package com.adam.app.memorycardgame.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.adam.app.memorycardgame.R;
import com.adam.app.memorycardgame.databinding.FragmentMainBinding;
import com.adam.app.memorycardgame.util.CommonUtils;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    // view binding
    private FragmentMainBinding mBinding;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMainBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(this.getViewLifecycleOwner());

        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init view model
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mBinding.setVm(mViewModel);

        // setup view model observer
        setupViewModelObserver();
    }

    /**
     * Setup view model observer
     */
    private void setupViewModelObserver() {
        mViewModel.getNavigateTo().observe(getViewLifecycleOwner(), this::navigateTo);
    }

    private void navigateTo(MainViewModel.NavigationDestination destination) {
        if (destination == MainViewModel.NavigationDestination.NONE) {
            // no action
            return;
        }

        // navigation controller
        NavController nav = Navigation.findNavController(requireView());

        switch (destination) {
            case START_GAME:
                // navigate to game fragment
                nav.navigate(R.id.action_mainFragment_to_gameFragment);
                break;
            case SETTINGS:
                // navigate to settings fragment
                nav.navigate(R.id.action_mainFragment_to_settingFragment);
                break;
            case ABOUT:
                // navigate to about fragment
                nav.navigate(R.id.action_mainFragment_to_aboutFragment);
                break;
            case EXIT:
                // exit app
                getActivity().finish();
                break;
            default:
                break;
        }

        // done
        mViewModel.onDoneNavigating();
    }
}