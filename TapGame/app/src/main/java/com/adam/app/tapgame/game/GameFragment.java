/**
 * File: GameFragment.java
 * Description: This class is Game Fragment
 *
 * @author Adam Chen
 * @version 1.0 - 2026/01/14
 */
package com.adam.app.tapgame.game;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adam.app.tapgame.R;
import com.adam.app.tapgame.databinding.FragmentGameBinding;
import com.adam.app.tapgame.utils.GameUtils;

public class GameFragment extends Fragment {

    private GameViewModel mViewModel;

    // view binding
    private FragmentGameBinding mBinding;

    // Navigation controller
    private NavController mNavController;


    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // view binding
        mBinding = FragmentGameBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init navigation controller
        mNavController = Navigation.findNavController(view);

        // init view model
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // data binding view
        mBinding.setVm(mViewModel);
        // observer navigation
        mViewModel.getNavigateTo().observe(getViewLifecycleOwner(), this::navigate);

        disableBackPressed();
        
    }

    private void disableBackPressed() {
        requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Do nothing → back button disabled
                        GameUtils.showToast(getContext(), R.string.tap_game_diable_function_toast);
                    }
                });
    }

    private void navigate(GameUtils.NavigationDestination destination) {
        if (destination == GameUtils.NavigationDestination.NONE) {
            return;
        }

        // back to menu page
        if (destination == GameUtils.NavigationDestination.MAIN) {
            mNavController.navigate(R.id.action_gameFragment_to_menuFragment);
        }
    }
}