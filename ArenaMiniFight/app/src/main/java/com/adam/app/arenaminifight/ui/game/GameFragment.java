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

package com.adam.app.arenaminifight.ui.game;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.adam.app.arenaminifight.R;
import com.adam.app.arenaminifight.databinding.FragmentGameBinding;
import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.utils.GameConstants;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GameFragment extends Fragment {

    private static final String TAG = "GameFragment";

    private FragmentGameBinding mBinding;
    private GameViewModel mViewModel;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        GameUtil.log("[StartGameFlow][Step 3] GameFragment: onCreateView()");
        mBinding = FragmentGameBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GameUtil.log("[StartGameFlow][Step 3] GameFragment: onViewCreated() -> Initializing ViewModel & starting game");

        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        mViewModel.initPlayerFromJni(GameConstants.DefaultPlayerName);
        mViewModel.startGame();

        mViewModel.getPlayers().observe(getViewLifecycleOwner(), this::onPlayers);
        mViewModel.getExitEvent().observe(getViewLifecycleOwner(), this::onExit);
        mViewModel.getNavigateToResult().observe(getViewLifecycleOwner(), shouldNavigate -> {
            if (Boolean.TRUE.equals(shouldNavigate) && getView() != null) {
                GameUtil.log(TAG + ": Navigating to ResultFragment on Game Over");
                Navigation.findNavController(view).navigate(R.id.action_game_to_result);
            }
        });

        // Setup D-Pad virtual directional buttons (UC-GAME-02)
        setupVirtualDPad();

        // Handle system back button with confirmation dialog (UC-GAME-12 Path B)
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    private void setupVirtualDPad() {
        // Move Up
        mBinding.btnDpadUp.setOnClickListener(v -> mViewModel.updatePlayerMove(0, -10f));
        // Move Down
        mBinding.btnDpadDown.setOnClickListener(v -> mViewModel.updatePlayerMove(0, 10f));
        // Move Left
        mBinding.btnDpadLeft.setOnClickListener(v -> mViewModel.updatePlayerMove(-10f, 0));
        // Move Right
        mBinding.btnDpadRight.setOnClickListener(v -> mViewModel.updatePlayerMove(10f, 0));
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_exit_title))
                .setMessage(getString(R.string.dialog_exit_message))
                .setPositiveButton(getString(R.string.dialog_ok), (dialog, which) -> {
                    mViewModel.stopGame();
                    mViewModel.saveScoreToDatabase();
                    mViewModel.getNavigateToResult().setValue(true);
                })
                .setNegativeButton(getString(R.string.dialog_cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void onPlayers(List<Player> players) {
        mBinding.gameSurfaceView.updatePlayers(players);
    }

    private void onExit(Boolean isExit) {
        if (Boolean.TRUE.equals(isExit) && getActivity() != null) {
            GameUtil.log(TAG + ": onExit");
            mViewModel.stopGame();
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
