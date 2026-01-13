/**
 * This class is the Main fragment for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.adam.app.tic_tac_toe.R;
import com.adam.app.tic_tac_toe.databinding.FragmentMainBinding;
import com.adam.app.tic_tac_toe.viewmodels.MainViewModel;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    // view binding
    private FragmentMainBinding mBinding;
    private NavController mNavCtl;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMainBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init view model
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mBinding.setViewModel(mViewModel);

        mNavCtl = NavHostFragment.findNavController(this);

        setupNavigationObserver();
    }

    private void setupNavigationObserver() {
        mViewModel.getNavigateTo().observe(getViewLifecycleOwner(), destination -> {
            if (destination == MainViewModel.NavigationDestination.NONE) {
                return;  // avoid to trigger again
            }
            switch (destination) {
                case START_GAME:
                    // navigate to game fragment
                    mNavCtl.navigate(R.id.action_mainFragment_to_gameFragment);
                    break;
                case SETTINGS:
                    // navigate to settings fragment
                    mNavCtl.navigate(R.id.action_mainFragment_to_settingsFragment);
                    break;
                case ABOUT:
                    // navigate to about fragment
                    mNavCtl.navigate(R.id.action_mainFragment_to_aboutFragment);
                    break;
                case EXIT:
                    // finish activity
                    requireActivity().finish();
                    break;
            }
            // done
            mViewModel.onNavigationDone();
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null; // avoid to memory leak

    }
}