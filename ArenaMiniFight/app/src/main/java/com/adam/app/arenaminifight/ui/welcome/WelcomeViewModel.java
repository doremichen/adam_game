/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This class is the view model of welcome fragment
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/04
 */
package com.adam.app.arenaminifight.ui.welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.arenaminifight.data.repository.GameRepository;

public class WelcomeViewModel extends ViewModel {
    // TAG
    private static final String TAG = "WelcomeViewModel";

    // Game repository
    private final GameRepository mRepository = GameRepository.getInstance();

    // constructor
    public WelcomeViewModel() {
        // bind service
        mRepository.bindGameService();
    }

    public LiveData<Boolean> getIsLoginSuccess() {
        return mRepository.isReady();
    }
}