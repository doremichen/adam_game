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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.adam.app.memorycardgame.data.model.Card;
import com.adam.app.memorycardgame.databinding.FragmentGameBinding;
import com.adam.app.memorycardgame.ui.game.adapter.CardAdapter;
import com.adam.app.memorycardgame.util.CommonUtils;

public class GameFragment extends Fragment implements CardClickListener {

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

    @Override
    public void onCardClicked(Card card) {
        mViewModel.onCardClicked(card);
    }

    // public method to observe live data
    private <T> void observe(LiveData<T> liveData, Observer<T> observer) {
        liveData.observe(getViewLifecycleOwner(), observer);
    }
}