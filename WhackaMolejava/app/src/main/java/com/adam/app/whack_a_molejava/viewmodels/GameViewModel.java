/**
 * Copyright 2025 Adam Game
 * <p>
 * This class is the view model for the game activity.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-03
 *
 */
package com.adam.app.whack_a_molejava.viewmodels;

import android.app.Application;
import android.graphics.PointF;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.whack_a_molejava.controller.GameEngine;
import com.adam.app.whack_a_molejava.model.Mole;

import java.util.List;

public class GameViewModel extends AndroidViewModel {

    // Live data
    private MutableLiveData<Integer> mScore = new MutableLiveData<>(0);
    private MutableLiveData<Integer> mRemainingTime = new MutableLiveData<>(0);
    private MutableLiveData<Boolean> mGameOver = new MutableLiveData<>(false);

    // Game engine
    private GameEngine mGameEngine;

    public GameViewModel(@NonNull Application application) {
        super(application);
        // init Game engine
        mGameEngine = new GameEngine(30, new GameEngine.GameCallback() {

            @Override
            public void onScoreChanged(int score) {
                mScore.postValue(score);
            }

            @Override
            public void onTimeChanged(int sec) {
                mRemainingTime.postValue(sec);
            }

            @Override
            public void onGameOver() {
                mGameOver.postValue(true);
            }
        });
        // start game
        mGameEngine.changeState(GameEngine.WAMGameState.RUN);
    }

    /**
     * hit mole
     * @param x x position
     * @param y y position
     */
    public void hitMole(float x, float y) {
        mGameEngine.hitMole(new PointF(x, y));
    }

    /**
     * get moles
     * @return list of moles
     */
    public List<Mole> getMoles() {
        return mGameEngine.getMoles();
    }

    /**
     * setMolePositions
     */
    public void setMolePositions(List<PointF> centers) {
        mGameEngine.setMolePositions(centers);
    }

    public void changeState(GameEngine.WAMGameState state) {
        mGameEngine.changeState(state);
    }

    public GameEngine.WAMGameState getState() {
        return mGameEngine.getState();
    }

    public LiveData<Integer> getScore() {
        return mScore;
    }

    public LiveData<Integer> getRemainingTime() {
        return mRemainingTime;
    }

    public LiveData<Boolean> getGameOver() {
        return mGameOver;
    }

    public void update() {
        mGameEngine.update();
    }
}
