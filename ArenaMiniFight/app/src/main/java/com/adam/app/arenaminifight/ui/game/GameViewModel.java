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

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.arenaminifight.domain.gateway.IGameGateway;
import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.domain.usecase.GameUseCase;
import com.adam.app.arenaminifight.utils.GameConstants;
import com.adam.app.arenaminifight.utils.GameUtil;
import com.adam.app.arenaminifight.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * Trace: UC-04 (Spawn), UC-05 (Movement), UC-06 (Physics/Sync), UC-07 (Reset), UC-10 (Combat)
 */
@HiltViewModel
public class GameViewModel extends ViewModel {

    private static final String TAG = GameViewModel.class.getSimpleName();

    private final MutableLiveData<String> mPlayerStatus = new MutableLiveData<>("");
    private final MutableLiveData<List<Player>> mPlayers = new MutableLiveData<>(new ArrayList<>());
    private final SingleLiveEvent<Boolean> mExitEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> mNavigateToResult = new SingleLiveEvent<>();

    private final GameUseCase mGameUseCase;
    private String mMainPlayerId = "";

    @Inject
    public GameViewModel(IGameGateway gateway) {
        mGameUseCase = new GameUseCase(gateway);
    }

    public LiveData<String> getPlayerStatus() {
        return mPlayerStatus;
    }

    public LiveData<List<Player>> getPlayers() {
        return mPlayers;
    }

    public SingleLiveEvent<Boolean> getExitEvent() {
        return mExitEvent;
    }

    public SingleLiveEvent<Boolean> getNavigateToResult() {
        return mNavigateToResult;
    }

    public void onExitClicked() {
        GameUtil.log(TAG + ": onExitClicked()");
        mExitEvent.setValue(true);
    }

    // Trace: UC-10
    public void onAttackClicked() {
        GameUtil.log(TAG + ": onAttackClicked()");
        setPlayerStatus("Action: ATTACK!");
    }

    // Trace: UC-10
    public void onSkillClicked() {
        GameUtil.log(TAG + ": onSkillClicked()");
        setPlayerStatus("Action: SKILL CAST!");
    }

    // Trace: UC-04
    public void onAddAIClicked() {
        String aiName = "AI_" + (int) (Math.random() * 1000);
        GameUtil.log(TAG + ": onAddAIClicked -> " + aiName);
        GameUseCase.SpawnParams params = new GameUseCase.SpawnParams(aiName, this::onPlayerSpawned);
        mGameUseCase.execute(GameUseCase.Action.SPAWN_PLAYER, params);
    }

    // Trace: UC-05
    public void updatePlayerMove(float x, float y) {
        if (mMainPlayerId.isEmpty()) {
            return;
        }

        mPlayerStatus.setValue(String.format("(%.1f, %.1f)", x, y));
        GameUseCase.MoveParams params = new GameUseCase.MoveParams(mMainPlayerId, x, y);
        mGameUseCase.execute(GameUseCase.Action.MOVE_PLAYER, params);
    }

    public void setPlayerStatus(String status) {
        mPlayerStatus.setValue(status);
    }

    // Trace: UC-04
    public void initPlayerFromJni(String playerName) {
        GameUtil.log(TAG + ": initPlayerFromJni(" + playerName + ")");
        GameUseCase.SpawnParams params = new GameUseCase.SpawnParams(playerName, this::onPlayerSpawned);
        mGameUseCase.execute(GameUseCase.Action.SPAWN_PLAYER, params);
    }

    // Trace: UC-06
    public void startGame() {
        GameUtil.log(TAG + ": startGame");
        IGameGateway.Callback<List<Player>> callback = this::onPlayersData;
        mGameUseCase.execute(GameUseCase.Action.START_GAME, callback);
    }

    // Trace: UC-06
    private void onPlayersData(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return;
        }
        mPlayers.setValue(players);
    }

    // Trace: UC-07
    public void stopGame() {
        GameUtil.log(TAG + ": stopGame");
        mGameUseCase.execute(GameUseCase.Action.STOP_GAME, null);
        setPlayerStatus("");
        mPlayers.setValue(new ArrayList<>());
    }

    // Trace: UC-04
    private void onPlayerSpawned(Player player) {
        if (player == null) return;
        GameUtil.log(TAG + ": onPlayerSpawned(" + player.getName() + " @ " + player.getPlayerId() + ")");
        if (GameConstants.DefaultPlayerName.equals(player.getName())) {
            mMainPlayerId = player.getPlayerId();
        }

        List<Player> currentList = mPlayers.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(player);
        mPlayers.setValue(currentList);

        setPlayerStatus(String.format("(%.1f, %.1f)", player.getX(), player.getY()));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        GameUtil.log(TAG + ": onCleared");
        stopGame();
    }
}
