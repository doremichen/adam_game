/**
 * This class is the About fragment for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-09
 */
package com.adam.app.tic_tac_toe.views.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adam.app.tic_tac_toe.R;
import com.adam.app.tic_tac_toe.databinding.FragmentAboutBinding;
import com.adam.app.tic_tac_toe.utils.GameUtils;
import com.adam.app.tic_tac_toe.viewmodels.AboutViewModel;

public class AboutFragment extends Fragment {

    private AboutViewModel mViewModel;
    // view binding
    private FragmentAboutBinding mBinding;


    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // view binding
        mBinding = FragmentAboutBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AboutViewModel.class);

        // observer
        mViewModel.getAboutText().observe(getViewLifecycleOwner(), aboutText -> {
            mBinding.textAbout.setText(aboutText);
        });

        // version
        mBinding.textVersion.setText(getString(R.string.tic_tac_toe_version, GameUtils.VERSION_NAME));

        // button click listener
        mBinding.btnPrivacy.setOnClickListener(v -> GameUtils.unImplemented(getContext()));
        mBinding.btnWebsite.setOnClickListener(v -> GameUtils.unImplemented(getContext()));

    }


}