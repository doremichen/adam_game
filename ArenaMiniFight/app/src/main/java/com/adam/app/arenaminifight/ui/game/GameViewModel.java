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
import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

public class GameViewModel extends ViewModel {
    // TAG
    private static final String TAG = GameViewModel.class.getSimpleName();

    // live data: player status
    private final MutableLiveData<String> mPlayerStatus = new MutableLiveData<>("");
    public LiveData<String> getPlayerStatus() {
        return mPlayerStatus;
    }

    // live data: players
    private final MutableLiveData<List<Player>> mPlayers = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<Player>> getPlayers() {
        return mPlayers;
    }

    private final MutableLiveData<Boolean> mExitEvent = new MutableLiveData<>(false);
    public LiveData<Boolean> getExitEvent() {
        return mExitEvent;
    }

    private final GameRepository nmRepository = GameRepository.getInstance();

    public void onExitClicked() {
        GameUtil.log(TAG + ": onExitClicked()");
        mExitEvent.setValue(true);
    }

    public void updatePlayerMove(float x, float y) {
        GameUtil.log(TAG + ": updatePlayerMove(" + x + ", " + y + ")");
        mPlayerStatus.setValue(String.format("(%.1f, %.1f)", x, y));
        nmRepository.movePlayer(x, y);
    }

    public void setPlayerStatus(String status) {
        GameUtil.log(TAG + ": setPlayerStatus(" + status + ")");
        mPlayerStatus.setValue(status);
    }

}