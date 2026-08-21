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

package com.adam.app.galaga.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.galaga.data.local.entities.ScoreRecord;
import com.adam.app.galaga.data.model.GameObject;
import com.adam.app.galaga.domain.usecase.GameUseCase;
import com.adam.app.galaga.engine.Direction;
import com.adam.app.galaga.engine.GameEngine;
import com.adam.app.galaga.engine.GameObjectManager;
import com.adam.app.galaga.utils.GameConstants;
import com.adam.app.galaga.utils.GameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * This is the view model for the game.
 */
@HiltViewModel
public class GameViewModel extends ViewModel implements GameEngine.EngineCallback {
    private static final String TAG = GameViewModel.class.getSimpleName();

    private final GameEngine mGameEngine;
    private final GameUseCase mGameUseCase;

    private final MutableLiveData<List<GameObject>> mEntities = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> mScore = new MutableLiveData<>(0);
    private final MutableLiveData<GameEngine.State> mCurrentState = new MutableLiveData<>(GameEngine.State.READY);
    private final MutableLiveData<Integer> mCurrentLevelData = new MutableLiveData<>(1);
    private final MutableLiveData<String> mCurrentLevelTitle = new MutableLiveData<>("");
    private final MutableLiveData<String> mRemainingTime = new MutableLiveData<>("");

    private int mFinalScore;

    @Inject
    public GameViewModel(GameUseCase gameUseCase, GameObjectManager gameObjectManager) {
        this.mGameUseCase = gameUseCase;
        mGameEngine = new GameEngine(this, gameObjectManager);
        startGame();
    }

    @Override
    protected void onCleared() {
        mGameEngine.clear();
    }

    public LiveData<List<GameObject>> getEntities() {
        return mEntities;
    }

    public LiveData<Integer> getScore() {
        return mScore;
    }

    public int getFinalScore() {
        return mFinalScore;
    }

    public LiveData<GameEngine.State> getCurrentState() {
        return mCurrentState;
    }

    public LiveData<Integer> getCurrentLevelData() {
        return mCurrentLevelData;
    }

    public LiveData<String> getCurrentLevelTitle() {
        return mCurrentLevelTitle;
    }

    public LiveData<String> getRemainingTime() {
        return mRemainingTime;
    }

    public void startGame() {
        GameUtils.info(TAG, "startGame");
        mGameEngine.start();
    }

    public void pauseGame() {
        GameUtils.info(TAG, "pauseGame");
        mGameEngine.pause();
    }

    public void resumeGame() {
        GameUtils.info(TAG, "resumeGame");
        if (mCurrentState.getValue() == GameEngine.State.PAUSED) {
            mGameEngine.resume();
        }
    }

    public void setMoveDirection(Direction direction) {
        mGameEngine.setMoveDirection(direction);
    }

    public void setShooting(boolean shooting) {
        mGameEngine.setShooting(shooting);
    }

    public void saveScore(String name) {
        GameUtils.info(TAG, "saveScore");
        String finalName = (name == null || name.isEmpty()) ? "Guest" : name;
        ScoreRecord record = new ScoreRecord(finalName, mFinalScore, System.currentTimeMillis());
        mGameUseCase.execute(new GameUseCase.Request(GameUseCase.ActionType.INSERT_SCORE, record));
    }

    private void handleLevelTransition() {
        String title = mGameEngine.getMetadataTitle();
        mCurrentLevelTitle.setValue(title);
        mCurrentLevelData.setValue(mGameEngine.getCurrentLevelId());

        GameUtils.runOnMainThread(() -> {
            try {
                mGameEngine.startNextLevel();
            } catch (RuntimeException e) {
                GameUtils.error(TAG, "handleLevelTransition error: " + e.getMessage());
                mCurrentState.setValue(GameEngine.State.GAME_OVER);
            }
        }, GameConstants.LEVEL_TRANSITION_DELAY_MS);
    }

    @Override
    public void onScoreChanged(int currentScore) {
        mScore.setValue(currentScore);
        mFinalScore = currentScore;
    }

    @Override
    public void onFrameUpdate(List<GameObject> entities) {
        mEntities.setValue(entities);
    }

    @Override
    public void onGameStateChanged(GameEngine.State state) {
        mCurrentState.setValue(state);
        if (state == GameEngine.State.CLEARED) {
            handleLevelTransition();
        }
    }

    @Override
    public void onRemainingTime(long elapsed) {
        long totalDuration = GameConstants.LEVEL_DURATION_MS;
        long remaining = Math.max(0, totalDuration - elapsed);
        mRemainingTime.postValue(String.format(Locale.getDefault(), "Time: %ds", remaining / 1000));
    }
}
