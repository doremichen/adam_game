/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the game view model of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/02
 */
package com.adam.app.arenaminifight.ui.game;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.arenaminifight.data.repository.GameRepository;

public class GameViewModel extends ViewModel {
    private final MutableLiveData<String> mPlayerStatus = new MutableLiveData<>("");
    public LiveData<String> getPlayerStatus() {
        return mPlayerStatus;
    }

    private final MutableLiveData<Boolean> mExitEvent = new MutableLiveData<>(false);
    public LiveData<Boolean> getExitEvent() {
        return mExitEvent;
    }

    private final GameRepository nmRepository = GameRepository.getInstance();

    public void updatePlayerMove(float x, float y) {
        mPlayerStatus.setValue(String.format("(%.1f, %.1f)", x, y));
        nmRepository.movePlayer(x, y);
    }

    public void onExitClicked() {
        mExitEvent.setValue(true);
    }
}