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

import com.adam.app.arenaminifight.GameApplication;
import com.adam.app.arenaminifight.data.local.AppDatabase;
import com.adam.app.arenaminifight.data.local.ScoreEntity;
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
        gateway.setGameOverCallback(summary -> {
            GameUtil.log(TAG + ": Game Over callback received: " + summary);
            saveScoreToDatabase();
            mNavigateToResult.setValue(true);
        });
    }

    public void saveScoreToDatabase() {
        new Thread(() -> {
            try {
                AppDatabase db =
                        AppDatabase.getInstance(
                                GameApplication.Helper.getInstance().get()
                        );
                db.scoreDao().insertScore(new ScoreEntity(
                        "abb", 60L, 5, 1, System.currentTimeMillis()
                ));
                GameUtil.log(TAG + ": Score persisted to Room DB on game over successfully");
            } catch (Exception e) {
                GameUtil.log(TAG + ": Failed to persist score: " + e.getMessage());
            }
        }).start();
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
        if (mMainPlayerId.isEmpty()) {
            setPlayerStatus("No active player!");
            return;
        }
        GameUseCase.AttackParams params = new GameUseCase.AttackParams(mMainPlayerId, false, this::onAttackResult);
        mGameUseCase.execute(GameUseCase.Action.PLAYER_ATTACK, params);
    }

    // Trace: UC-10
    public void onSkillClicked() {
        GameUtil.log(TAG + ": onSkillClicked()");
        if (mMainPlayerId.isEmpty()) {
            setPlayerStatus("No active player!");
            return;
        }
        GameUseCase.AttackParams params = new GameUseCase.AttackParams(mMainPlayerId, true, this::onAttackResult);
        mGameUseCase.execute(GameUseCase.Action.PLAYER_ATTACK, params);
    }

    private void onAttackResult(String result) {
        if (result != null) {
            setPlayerStatus(result);
        }
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

    // Trace: UC-05
    public void stopPlayerMove() {
        if (mMainPlayerId.isEmpty()) {
            return;
        }
        GameUseCase.StopMoveParams params = new GameUseCase.StopMoveParams(mMainPlayerId);
        mGameUseCase.execute(GameUseCase.Action.STOP_MOVE_PLAYER, params);
    }

    public void setPlayerStatus(String status) {
        mPlayerStatus.setValue(status);
    }

    // Trace: UC-04
    public void initPlayerFromJni(String playerName) {
        GameUtil.log("[StartGameFlow][Step 4] GameViewModel: initPlayerFromJni(playerName=" + playerName + ") -> Executing SPAWN_PLAYER");
        GameUseCase.SpawnParams params = new GameUseCase.SpawnParams(playerName, this::onPlayerSpawned);
        mGameUseCase.execute(GameUseCase.Action.SPAWN_PLAYER, params);
    }

    // Trace: UC-06
    public void startGame() {
        GameUtil.log("[StartGameFlow][Step 4] GameViewModel: startGame() -> Executing START_GAME");
        IGameGateway.Callback<List<Player>> callback = this::onPlayersData;
        mGameUseCase.execute(GameUseCase.Action.START_GAME, callback);
    }

    // Trace: UC-06
    private void onPlayersData(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return;
        }
        GameUtil.log("[StartGameFlow][Step 4] GameViewModel: onPlayersData(received " + players.size() + " players)");

        if (mMainPlayerId.isEmpty() && !players.isEmpty()) {
            for (Player p : players) {
                if (p != null && p.getPlayerId() != null) {
                    if (GameConstants.DefaultPlayerName.equals(p.getName()) || mMainPlayerId.isEmpty()) {
                        mMainPlayerId = p.getPlayerId();
                        GameUtil.log(TAG + ": Fallback assigned mMainPlayerId = " + mMainPlayerId + " (" + p.getName() + ")");
                        break;
                    }
                }
            }
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
        GameUtil.log("[StartGameFlow][Step 4] GameViewModel: onPlayerSpawned(id=" + player.getPlayerId() + ", name=" + player.getName() + ")");
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
