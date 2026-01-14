/**
 * File: HomeFragment.java
 * Description: This class is Home Fragment
 *
 * @author Adam Chen
 * @version 1.0 - 2026/01/14
 */
package com.adam.app.tapgame.home;

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

import com.adam.app.tapgame.R;
import com.adam.app.tapgame.databinding.FragmentHomeBinding;
import com.adam.app.tapgame.utils.GameUtils;

public class HomeFragment extends Fragment {

    // view binding
    private FragmentHomeBinding mBinding;

    // view model
    private HomeViewModel mViewModel;

    // navigation control
    private NavController mNavController;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // view binding
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        // set life cycle owner
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init view model
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // data bind view model
        mBinding.setViewModel(mViewModel);

        // init navigation control
        mNavController = Navigation.findNavController(view);

        // observer navigation
        mViewModel.getNavigateTo().observe(getViewLifecycleOwner(), this::navigate);


    }

    private void navigate(GameUtils.NavigationDestination destination) {
        if (destination == GameUtils.NavigationDestination.NONE) {
            return;
        }

        // dispatch
        switch (destination) {
            case START_GAME:
                // navigate to game fragment
                mNavController.navigate(R.id.action_mainFragment_to_gameFragment);
                break;
            case SETTINGS:
                GameUtils.showUnImplemented(this.getContext());
                break;
            case ABOUT:
                GameUtils.showUnImplemented(this.getContext());
                break;
            case EXIT:
                // exit app
                getActivity().finish();
                break;
            default:
                break;
        }

    }
}