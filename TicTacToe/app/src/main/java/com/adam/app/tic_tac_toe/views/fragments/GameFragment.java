/**
 * This class is the Game fragment for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.views.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.adam.app.tic_tac_toe.databinding.FragmentGameBinding;
import com.adam.app.tic_tac_toe.models.Board;
import com.adam.app.tic_tac_toe.models.Player;
import com.adam.app.tic_tac_toe.utils.GameUtils;
import com.adam.app.tic_tac_toe.viewmodels.GameViewModel;
import com.adam.app.tic_tac_toe.R;

public class GameFragment extends Fragment {

    private GameViewModel mViewModel;
    // view binding
    private FragmentGameBinding mBinding;

    // Navigation control
    private NavController mNavCtl;


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
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mBinding.setViewModel(mViewModel);

        // init navigation control
        mNavCtl = NavHostFragment.findNavController(this);


        Button[][] buttons = {
                {mBinding.button00, mBinding.button01, mBinding.button02},
                {mBinding.button10, mBinding.button11, mBinding.button12},
                {mBinding.button20, mBinding.button21, mBinding.button22}
        };

        setupObservers(buttons);
    }

    private void setupObservers(Button[][] buttons) {
        // Observe the game status and update the UI accordingly
        mViewModel.getBoardState().observe(getViewLifecycleOwner(), boardState -> {
            if (boardState != null) {
                for (int i = 0; i < Board.BOARD_SIZE; i++) {
                    for (int j = 0; j < Board.BOARD_SIZE; j++) {
                        if (boardState[i][j] != null) {
                            buttons[i][j].setText(boardState[i][j].name());
                        } else {
                            buttons[i][j].setText("");
                        }
                    }
                }

            }
        });

        mViewModel.getStatusText().observe(getViewLifecycleOwner(), statusText -> {
            mBinding.textViewStatus.setText(statusText);
        });

        mViewModel.getIsGameOver().observe(getViewLifecycleOwner(), this::onGameOver);
    }

    private void onGameOver(Boolean isGameOver) {
        if (isGameOver) {
            // show dialog
            GameUtils.DialogButtonContent positiveBtn = new GameUtils.DialogButtonContent(
                    getString(R.string.tic_tac_toe_play_again_btn),
                    () -> mViewModel.resetGame()
            );
            GameUtils.DialogButtonContent nagativeBtn = new GameUtils.DialogButtonContent(
                    getString(R.string.tic_tac_toe_cancel_btn),
                    () -> {
                        // navigate to main fragment
                        mNavCtl.navigate(R.id.action_gameFragment_to_menuFragment);
                    }
            );
            // show alert dialog
            Player winner = mViewModel.getBoardWinner();
            String title = getString(R.string.tic_tac_toe_game_over_title);
            String message = (winner != null)
                    ? getString(R.string.tic_tac_toe_game_over_message, winner.name())
                    : getString(R.string.tic_tac_toe_draw_game_over_message);
            GameUtils.showAlertDialog(getContext(), title, message, positiveBtn, nagativeBtn);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null; // avoid to memory leak
    }
}