/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.ui.game;

import android.graphics.PointF;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.racinggame2d.application.GameEngine;
import com.adam.app.racinggame2d.application.LeaderboardUseCase;
import com.adam.app.racinggame2d.application.SettingsUseCase;
import com.adam.app.racinggame2d.domain.entity.Car;
import com.adam.app.racinggame2d.domain.entity.LeaderboardRecord;
import com.adam.app.racinggame2d.domain.entity.Obstacle;
import com.adam.app.racinggame2d.domain.entity.Player;
import com.adam.app.racinggame2d.domain.entity.Track;
import com.adam.app.racinggame2d.util.Constants;
import com.adam.app.racinggame2d.util.SingleLiveEvent;
import com.adam.app.racinggame2d.util.Utils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the GameActivity.
 * Adheres to AS-02: Interacts only with UseCases.
 */
@HiltViewModel
public final class GameViewModel extends ViewModel {
    private static final String sTAG = "GameViewModel";

    private final MutableLiveData<Integer> mScore = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> mHp = new MutableLiveData<>(Constants.MAX_CAR_HP);
    private final MutableLiveData<Boolean> mIsGameOver = new MutableLiveData<>(false);
    private final SingleLiveEvent<Void> mGameRestartEvent = new SingleLiveEvent<>();
    private final LeaderboardUseCase mLeaderboardUseCase;
    private final SettingsUseCase mSettingsUseCase;
    private final GameEngine.Factory mGameEngineFactory;
    private GameEngine mGameEngine;
    private GameState mGameState = GameState.IDLE;

    @Inject
    public GameViewModel(LeaderboardUseCase leaderboardUseCase,
                         SettingsUseCase settingsUseCase,
                         GameEngine.Factory gameEngineFactory) {
        this.mLeaderboardUseCase = leaderboardUseCase;
        this.mSettingsUseCase = settingsUseCase;
        this.mGameEngineFactory = gameEngineFactory;
    }

    public LiveData<Integer> getScore() {
        return mScore;
    }

    public LiveData<Integer> getHp() {
        return mHp;
    }

    public LiveData<Boolean> getIsGameOver() {
        return mIsGameOver;
    }

    public LiveData<Void> getGameRestartEvent() {
        return mGameRestartEvent;
    }

    public void setGameUpdateListener(GameEngine.GameUpdateListener listener) {
        if (mGameEngine != null) {
            mGameEngine.setGameUpdateListener(listener);
        }
    }

    public void updateScore() {
        if (mGameEngine != null) {
            mScore.postValue(mGameEngine.getScore());
        }
    }

    public void updateHp() {
        if (mGameEngine != null) {
            mHp.postValue(mGameEngine.getCarHP());
        }
    }

    public float getCarRotationAngle() {
        return mGameEngine != null ? mGameEngine.getCarRotationAngle() : 0f;
    }

    public void prepareGameEngine(int width, int height, String carId, String carName) {
        Utils.logDebug(sTAG, "prepareGameEngine");
        Car car = new Car(carId, carName, Constants.DEFAULT_SPEED, Constants.DEFAULT_ACCELERATOR);
        car.initPosition(width, height);

        String playerName = mSettingsUseCase.execute(SettingsUseCase.Action.GET_PLAYER_NAME, null);
        Player player = new Player(playerName != null ? playerName : "", car);
        Track track = new Track(width, height);

        mGameEngine = mGameEngineFactory.create(player, track);
        mGameEngine.setGameOverListener(() -> mIsGameOver.postValue(true));
    }

    public void startGame() {
        changeState(GameState.RUNNING);
    }

    public void pauseGame() {
        changeState(GameState.PAUSE);
    }

    public void resumeGame() {
        changeState(GameState.RUNNING);
    }

    public void stopGame() {
        changeState(GameState.IDLE);
    }

    public void restartGame() {
        if (mGameEngine != null) {
            changeState(GameState.IDLE);
            mGameEngine.reset();
            mScore.postValue(0);
            mHp.postValue(Constants.MAX_CAR_HP);
            mIsGameOver.postValue(false);
            mGameRestartEvent.call();
            changeState(GameState.RUNNING);
        }
    }

    public boolean isReady() {
        return mGameEngine != null;
    }

    public List<PointF> getCheckpoints() {
        return mGameEngine != null ? mGameEngine.getCheckPoints() : null;
    }

    public List<Obstacle> getObstacles() {
        return mGameEngine != null ? mGameEngine.getObstacles() : null;
    }

    public PointF getCarPosition() {
        return mGameEngine != null ? mGameEngine.getCarPosition() : null;
    }

    public void moveHorizontally(boolean isLeft) {
        if (mGameEngine != null) {
            mGameEngine.moveHorizontally(isLeft);
        }
    }

    public void speedUp(boolean isSpeedUp) {
        if (mGameEngine != null) {
            mGameEngine.speedUp(isSpeedUp);
        }
    }

    public void saveGameResult() {
        if (mGameEngine == null) return;
        String playerName = mSettingsUseCase.execute(SettingsUseCase.Action.GET_PLAYER_NAME, null);
        int score = mGameEngine.getScore();
        mLeaderboardUseCase.execute(LeaderboardUseCase.Action.ADD_SCORE, new LeaderboardRecord(0, playerName, score, String.valueOf(System.currentTimeMillis())));
    }

    private void changeState(GameState next) {
        if (!mGameState.canTransitionTo(next)) {
            Utils.logDebug(sTAG, "Cannot transition to " + next.name() + " from " + mGameState.name());
            return;
        }
        mGameState = next;
        mGameState.onEnter(this);
    }

    private enum GameState {
        IDLE, RUNNING, PAUSE;

        void onEnter(GameViewModel vm) {
            if (vm.mGameEngine == null) return;
            switch (this) {
                case RUNNING:
                    vm.mGameEngine.start();
                    break;
                case PAUSE:
                case IDLE:
                    vm.mGameEngine.stop();
                    break;
            }
        }

        boolean canTransitionTo(GameState next) {
            switch (this) {
                case IDLE:
                case PAUSE:
                    return next == RUNNING || next == IDLE;
                case RUNNING:
                    return next == PAUSE || next == IDLE;
                default:
                    return false;
            }
        }
    }
}
