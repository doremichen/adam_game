/*
 * Copyright (c) 2026 Adam Chen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.adam.app.tic_tac_toe.ui.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.adam.app.tic_tac_toe.R;
import com.adam.app.tic_tac_toe.databinding.FragmentGameBinding;
import com.adam.app.tic_tac_toe.domain.entities.Board;
import com.adam.app.tic_tac_toe.domain.entities.Player;
import com.adam.app.tic_tac_toe.ui.utils.Utils;
import com.adam.app.tic_tac_toe.ui.viewmodels.GameViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for the game screen.
 */
@AndroidEntryPoint
public class GameFragment extends Fragment {

    private GameViewModel mViewModel;
    private FragmentGameBinding mBinding;
    private NavController mNavCtl;

    @Nullable
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
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mBinding.setViewModel(mViewModel);
        mNavCtl = NavHostFragment.findNavController(this);

        setupObservers();
    }

    private void setupObservers() {
        mViewModel.getIsGameOver().observe(getViewLifecycleOwner(), this::onGameOver);
    }

    private void onGameOver(@Nullable Boolean isGameOver) {
        if (Boolean.TRUE.equals(isGameOver)) {
            Utils.DialogButtonContent positiveBtn = new Utils.DialogButtonContent(
                    getString(R.string.tic_tac_toe_play_again_btn),
                    () -> mViewModel.resetGame()
            );
            Utils.DialogButtonContent negativeBtn = new Utils.DialogButtonContent(
                    getString(R.string.tic_tac_toe_cancel_btn),
                    () -> mNavCtl.navigate(R.id.action_gameFragment_to_menuFragment)
            );
            
            Player winner = mViewModel.getBoardWinner();
            String title = getString(R.string.tic_tac_toe_game_over_title);
            String message = (winner != null)
                    ? getString(R.string.tic_tac_toe_game_over_message, winner.name())
                    : getString(R.string.tic_tac_toe_draw_game_over_message);
            Utils.showAlertDialog(requireContext(), title, message, positiveBtn, negativeBtn);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
