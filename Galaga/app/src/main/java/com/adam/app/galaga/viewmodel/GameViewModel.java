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

import com.adam.app.galaga.data.model.GameObject;
import com.adam.app.galaga.engine.GameEngine;
import com.adam.app.galaga.engine.GameObjectManager;
import com.adam.app.galaga.utils.GameUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the view model for the game.
 */
public class GameViewModel extends ViewModel implements GameEngine.EngineCallback {
    // TAG
    private static final String TAG = GameViewModel.class.getSimpleName();

    // game engine
    private final GameEngine mGameEngine;

    // --- Live data ---
    private final MutableLiveData<List<GameObject>> mEntities = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> mScore = new MutableLiveData<>(0);
    private final MutableLiveData<GameEngine.State> mCurrentState = new MutableLiveData<>(GameEngine.State.READY);


    /**
     * Constructor
     */
    public GameViewModel() {
        mGameEngine = new GameEngine(this);
        // start game
        startGame();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // clear
        mGameEngine.clear();
    }

    // --- getter  live data ---
    public LiveData<List<GameObject>> getEntities() {
        return mEntities;
    }

    public LiveData<Integer> getScore() {
        return mScore;
    }

    public LiveData<GameEngine.State> getCurrentState() {
        return mCurrentState;
    }

    // --- public method ---
    public void startGame() {
        GameUtils.info(TAG, "startGame");
        mGameEngine.start();
    }

    public void pauseGame() {
        GameUtils.info(TAG, "stopGame");
        mGameEngine.pause();
    }

    /**
     * just left and right
     * @param direction float
     */
    public void movePlayer(float direction) {
        GameUtils.info(TAG, "movePlayer");
        mGameEngine.movePlayer(direction);
    }

    /**
     * set direction
     * @param direction Direction direction
     */
    public  void setMoveDirection(GameObjectManager.Direction direction) {
        GameUtils.info(TAG, "setMoveDirection");
        mGameEngine.setMoveDirection(direction);
    }

    /**
     * set shooting
     * @param shooting boolean
     */
    public void setShooting(boolean shooting) {
        GameUtils.info(TAG, "setShooting");
        mGameEngine.setShooting(shooting);
    }

    /**
     * move player with direction: up, down, left, right
     * @param direction Direction
     */
    public void movePlayer(GameObjectManager.Direction direction) {
        GameUtils.info(TAG, "movePlayer");
        mGameEngine.movePlayer(direction);
    }

    public void shoot() {
        GameUtils.info(TAG, "shoot");
        mGameEngine.shoot();
    }


    @Override
    public void onScoreChanged(int currentScore) {
        GameUtils.info(TAG, "onScoreChanged");
        mScore.setValue(currentScore);
    }

    @Override
    public void onFrameUpdate(List<GameObject> entities) {
        mEntities.setValue(entities);
    }


    @Override
    public void onGameStateChanged(GameEngine.State state) {
        // update state
        mCurrentState.setValue(state);
    }
}
