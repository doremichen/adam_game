/**
 * Copyrights © 2021 Adam. All rights reserved.
 * <p>
 * Description: This is the game result fragment.
 * </p>
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/24
 */
package com.adam.app.memorycardgame.ui.game;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adam.app.memorycardgame.R;
import com.adam.app.memorycardgame.databinding.FragmentGameResultBinding;
import com.adam.app.memorycardgame.util.CommonUtils;

public class GameResultFragment extends Fragment {

    // TAG
    private static final String TAG = "GameResultFragment";

    // view binding
    private FragmentGameResultBinding mBinding;

    // view model
    private GameResultViewModel mViewModel;

    public static GameResultFragment newInstance() {
        return new GameResultFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CommonUtils.log(TAG + ": onCreateView");
        mBinding = FragmentGameResultBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CommonUtils.log(TAG + ": onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        // init view model
        mViewModel = new ViewModelProvider(this).get(GameResultViewModel.class);
        // set view model
        mBinding.setVm(mViewModel);

        GameResultFragmentArgs args = GameResultFragmentArgs.fromBundle(getArguments());
        // set data
        mViewModel.setData(
                args.getPlayTime(),
                args.getMatchCount(),
                args.getTheme()
        );

        // execute data binding
        mBinding.executePendingBindings();

        // set on exit game button listener
        mBinding.btnExitGame.setOnClickListener(this::onExitGame);

        setupBackPressedHandler();
    }

    private void onExitGame(View view) {
        // exit app
        requireActivity().finish();
    }

    @Override
    public void onDestroy() {
        CommonUtils.log(TAG + ": onDestroy");
        super.onDestroy();
    }

    private void setupBackPressedHandler() {
        // add back press call back
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        CommonUtils.log(TAG + ": handleOnBackPressed");
                        // Nav controller
                        NavController nav = Navigation.findNavController(requireView());
                        final NavBackStackEntry previousBackStackEntry = nav.getPreviousBackStackEntry();

                        if (previousBackStackEntry == null) {
                            return;
                        }

                        SavedStateHandle savedStateHandle =
                                previousBackStackEntry
                                .getSavedStateHandle();
                        // set saved state true
                        savedStateHandle.set(GameFragment.Key_ACTION_RESTART, true);
                        // pop back stack
                        nav.popBackStack();
                    }
                });
    }
}