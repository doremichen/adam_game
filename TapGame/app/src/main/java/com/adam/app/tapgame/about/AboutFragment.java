/**
 * File: AboutFragment.java
 * Description: This class is About Fragment
 *
 * @author Adam Chen
 * @version 1.0 - 2026/01/14
 */
package com.adam.app.tapgame.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.adam.app.tapgame.R;
import com.adam.app.tapgame.databinding.FragmentAboutBinding;
import com.adam.app.tapgame.utils.GameUtils;

public class AboutFragment extends Fragment {

    // TAG
    private static final String TAG = AboutFragment.class.getSimpleName();

    // view binding
    private FragmentAboutBinding mBinding;

    private AboutViewModel mViewModel;

    // Navigation controller
    private NavController mNavController;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        GameUtils.log(TAG + ": onCreateView");
        // view binding
        mBinding = FragmentAboutBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GameUtils.log(TAG + ": onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        // init navigation controller
        mNavController = Navigation.findNavController(view);


        // init view model
        mViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        // data binding view
        mBinding.setVm(mViewModel);

        // observer navigation
        mViewModel.getNavigateTo().observe(getViewLifecycleOwner(), this::navigate);

//        disableBackPressed();

    }

    private void navigate(GameUtils.NavigationDestination destination) {
        GameUtils.log(TAG + ": navigate");
        GameUtils.log(TAG + ": destination = " + destination.name());
        if (destination == GameUtils.NavigationDestination.NONE) {
            return;
        }

        // back to menu page
        if (destination == GameUtils.NavigationDestination.MAIN) {
            GameUtils.log(TAG + ": back to main");
            mNavController.popBackStack();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GameUtils.log(TAG + ": onDestroyView");
        mBinding = null;
        mViewModel = null;
        mNavController = null;
        GameUtils.log(TAG + ": onDestroyView done");
    }


    //    private void disableBackPressed() {
//        requireActivity()
//                .getOnBackPressedDispatcher()
//                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
//                    @Override
//                    public void handleOnBackPressed() {
//                        // Do nothing → back button disabled
//                        GameUtils.showToast(getContext(), R.string.tap_game_diable_function_toast);
//                    }
//                });
//
//    }
}