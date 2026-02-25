/**
 * Copyright (c) 2021 Adam Chen. All rights reserved.
 * <p>
 * Description: This is the game fragment for the memory card game.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/02/12
 */
package com.adam.app.memorycardgame.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.adam.app.memorycardgame.R;
import com.adam.app.memorycardgame.data.model.Card;
import com.adam.app.memorycardgame.databinding.FragmentGameBinding;
import com.adam.app.memorycardgame.ui.game.adapter.CardAdapter;
import com.adam.app.memorycardgame.util.CommonUtils;

public class GameFragment extends Fragment implements CardClickListener {

    public static final String Key_ACTION_RESTART = "action_restart";
    //  TAG
    private static final String TAG = "GameFragment";
    private GameViewModel mViewModel;

    // view binding
    private FragmentGameBinding mBinding;

    // card adapter
    private CardAdapter mCardAdapter;


    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentGameBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init view model
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // set view model
        mBinding.setVm(mViewModel);

        setupRecycler();
        observeCards();
        observeGameResult();
        setupBackPressedHandler();
        setupSaveState(view);
    }


    @Override
    public void onPause() {
        super.onPause();
        CommonUtils.log(TAG + ": onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonUtils.log(TAG + ": onResume");
    }

    private void setupRecycler() {
        mCardAdapter = new CardAdapter(this);
        mBinding.rvCards.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        // set adapter
        mBinding.rvCards.setAdapter(mCardAdapter);
    }

    private void observeCards() {
        observe(mViewModel.getCards(), cards -> {
            CommonUtils.log(TAG + ": observe cards");
            // submit
            mCardAdapter.submitList(cards);
        });
    }

    private void observeGameResult() {
        observe(mViewModel.getGameResult(), gameResult -> {
            CommonUtils.log(TAG + ": observe game result");
            if (gameResult == null) {
                return;
            }

            // show game result
            long playTime = gameResult.getStartTime();
            int matchCount = gameResult.getMatchCount();
            String theme = gameResult.getTheme();

            CommonUtils.log(TAG + ": play time: " + playTime);
            CommonUtils.log(TAG + ": match count: " + matchCount);
            CommonUtils.log(TAG + ": theme: " + theme);

            // navigate to game result fragment
            NavDirections action = GameFragmentDirections.Companion.actionGameFragmentToGameResultFragment(
                    playTime,
                    matchCount,
                    theme
            );
            NavController nav = Navigation.findNavController(requireView());
            nav.navigate(action);

            // consume game result
            mViewModel.clearGameResult();
        });
    }


    @Override
    public void onCardClicked(Card card) {
        mViewModel.onCardClicked(card);
    }

    // public method to observe live data
    private <T> void observe(LiveData<T> liveData, Observer<T> observer) {
        liveData.observe(getViewLifecycleOwner(), observer);
    }

    private void setupBackPressedHandler() {
        // add back press call back
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        showExitGameDialog();
                    }
                });
    }

    private void showExitGameDialog() {
        CommonUtils.DialogButtonContent positiveButton = new CommonUtils.DialogButtonContent(
                getString(android.R.string.ok),
                () -> {
                    mViewModel.notifyGameFinished();
                }
        );

        CommonUtils.DialogButtonContent nagetiveButton = new CommonUtils.DialogButtonContent(
                getString(android.R.string.cancel),
                () -> {
                    // do nothing
                }
        );

        // show dialog
        CommonUtils.showAlertDialog(requireContext(),
                getString(R.string.memory_card_game_dlg_title),
                getString(R.string.memory_card_game_dlg_message),
                positiveButton,
                nagetiveButton);

    }

    private void setupSaveState(View view) {
        // nav controller
        NavController navController = Navigation.findNavController(view);
        navController.getCurrentBackStackEntry()
                .getSavedStateHandle()
                .getLiveData(Key_ACTION_RESTART)
                .observe(getViewLifecycleOwner(), shouldRestart -> {
                    CommonUtils.log(TAG + ": observer should restart: " + shouldRestart);
                    if (Boolean.TRUE.equals(shouldRestart)) {
                        mViewModel.restartGame();

                        // consume Key_ACTION_RESTART
                        SavedStateHandle savedStateHandle = navController.getCurrentBackStackEntry()
                                .getSavedStateHandle();
                        savedStateHandle.remove(Key_ACTION_RESTART);
                    }
                });

    }

}