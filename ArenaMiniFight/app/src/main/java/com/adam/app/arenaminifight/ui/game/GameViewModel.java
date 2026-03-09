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
import com.adam.app.arenaminifight.utils.GameConstants;
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

    private final GameRepository mRepository = GameRepository.getInstance();

    private String mMainPlayerId = "";

    public void onExitClicked() {
        GameUtil.log(TAG + ": onExitClicked()");
        mExitEvent.setValue(true);
    }

    public void onAddAIClicked() {
        String aiName = "AI_" + (int)(Math.random() * 1000);
        GameUtil.log(TAG + ": onAddAIClicked -> " + aiName);
        mRepository.spawnPlayer(aiName, this::onPlayerSpawned);
    }

    public void updatePlayerMove(float x, float y) {
        GameUtil.log(TAG + ": updatePlayerMove(" + x + ", " + y + ")");
        if (mMainPlayerId.isEmpty()) {
            return;
        }

        mPlayerStatus.setValue(String.format("(%.1f, %.1f)", x, y));
        mRepository.movePlayer(mMainPlayerId, x, y);
    }

    public void setPlayerStatus(String status) {
        GameUtil.log(TAG + ": setPlayerStatus(" + status + ")");
        mPlayerStatus.setValue(status);
    }

    public void initPlayerFromJni(String playerName) {
        GameUtil.log(TAG + ": initPlayerFromJni(" + playerName + ")");
        mRepository.spawnPlayer(playerName, this::onPlayerSpawned);
    }

    public void startGame() {
        GameUtil.log(TAG + ": startGame");
        mRepository.startGame(this::onPlayersData);
    }

    private void onPlayersData(List<Player> players) {
        GameUtil.log(TAG + ": onPlayersData");
        if (players == null || players.isEmpty()) {
            GameUtil.log(TAG + ": players is null or empty");
            return;
        }

        // update ui
        mPlayers.setValue(players);
    }

    public void stopGame() {
        GameUtil.log(TAG + ": stopGame");
        mRepository.stopGame();
        // reset player status
        setPlayerStatus("");
        mPlayers.setValue(new ArrayList<>());
    }


    private void onPlayerSpawned(Player player) {
        if (player == null) return;
        GameUtil.log(TAG + ": onPlayerSpawned(" + player.getName() + " @ " + player.getPlayerId() + ")");
        if (GameConstants.DefaultPlayerName.equals(player.getName())) {
            mMainPlayerId = player.getPlayerId();
        }
        // update players
        List<Player> currentList = mPlayers.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(player);
        mPlayers.setValue(currentList);

        // update player status
        setPlayerStatus(String.format("(%.1f, .1%f)", player.getX(), player.getY()));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        GameUtil.log(TAG + ": onClear");
        // stop game
        stopGame();
    }
}