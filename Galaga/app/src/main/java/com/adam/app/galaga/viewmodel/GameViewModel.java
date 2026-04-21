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

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.galaga.data.local.entities.ScoreRecord;
import com.adam.app.galaga.data.model.GameObject;
import com.adam.app.galaga.data.repository.GameRepository;
import com.adam.app.galaga.engine.GameEngine;
import com.adam.app.galaga.engine.GameObjectManager;
import com.adam.app.galaga.utils.GameUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the view model for the game.
 */
public class GameViewModel extends AndroidViewModel implements GameEngine.EngineCallback {
    // TAG
    private static final String TAG = GameViewModel.class.getSimpleName();

    // game engine
    private final GameEngine mGameEngine;

    // game repository
    private final GameRepository mGameRepository;


    // --- Live data ---
    private final MutableLiveData<List<GameObject>> mEntities = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> mScore = new MutableLiveData<>(0);
    private final MutableLiveData<GameEngine.State> mCurrentState = new MutableLiveData<>(GameEngine.State.READY);


    private int mFinalScore;


    /**
     * Constructor
     */
    public GameViewModel(Application application) {
        super(application);
        // init
        mGameRepository = GameRepository.getInstance(application);
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

    public int getFinalScore() {
        return mFinalScore;
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

    public void resumeGame() {
        GameUtils.info(TAG, "resumeGame");
        if (mCurrentState.getValue() == GameEngine.State.PAUSED) {
            mGameEngine.resume();
        }
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
     * Save score
     *
     * @param name Player name
     */
    public void saveScore(String name) {
        GameUtils.info(TAG, "saveScore");
        String finalName = (name == null || name.isEmpty()) ? "Guest" : name;
        // new record
        ScoreRecord record = new ScoreRecord(finalName, mFinalScore, System.currentTimeMillis());

        // save record
        mGameRepository.insertScore(record);

    }

    private int mCurrentLevel = 1;

    private void handleLevelTransition() {
        mCurrentLevel++;
        // delay 2 sec before start next level
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                try {
                    // NEXT LEVEL
                    mGameEngine.startNextLevel(mCurrentLevel);
                } catch (RuntimeException e) {
                    GameUtils.error(TAG, "handleLevelTransition error");
                    GameUtils.error(TAG, e.getMessage());
                    // Show game over screen
                    mCurrentState.setValue(GameEngine.State.GAME_OVER);
                }
            }, 2000L);
    }

    @Override
    public void onScoreChanged(int currentScore) {
        GameUtils.info(TAG, "onScoreChanged");
        mScore.setValue(currentScore);
        mFinalScore = currentScore;
    }

    @Override
    public void onFrameUpdate(List<GameObject> entities) {
        mEntities.setValue(entities);
    }


    @Override
    public void onGameStateChanged(GameEngine.State state) {
        // update state
        mCurrentState.setValue(state);

        // handle level
        if (state == GameEngine.State.CLEARED) {
            handleLevelTransition();
        }

    }


}
