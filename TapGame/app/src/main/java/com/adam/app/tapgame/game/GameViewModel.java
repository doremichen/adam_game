/**
 * File: GameViewModel.java
 * Description: This class is Game View Model
 *
 * @author Adam Chen
 * @version 1.0 - 2026/01/14
 */
package com.adam.app.tapgame.game;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.tapgame.utils.GameUtils;


public class GameViewModel extends AndroidViewModel {

    private static final int TOTAL_TIME = 10;

    // live data
    private final MutableLiveData<Integer> mScore = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> mTimeLeft = new MutableLiveData<>(10);
    private final MutableLiveData<Boolean> mGameState = new MutableLiveData<>(false);
    private final MutableLiveData<GameUtils.NavigationDestination> mNavigateTo = new MutableLiveData<>(GameUtils.NavigationDestination.NONE);


    private CountDownTimer mTimer;

    // --- get live data ---
    public LiveData<Integer> getScore() {
        return mScore;
    }

    public LiveData<Integer> getTimeLeft() {
        return mTimeLeft;
    }

    // two way data binding
    public MutableLiveData<Boolean> getGameState() {
        return mGameState;
    }

    public LiveData<GameUtils.NavigationDestination> getNavigateTo() {
        return mNavigateTo;
    }

    // --- constructor ---
    public GameViewModel(@NonNull Application application) {
        super(application);
        startTimer();
    }

    private void startTimer() {
        mTimer = new CountDownTimer(TOTAL_TIME * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeft.setValue((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mTimeLeft.setValue(0);
            }
        };
        // start timer
        mTimer.start();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    public void onTab() {
        int current = mScore.getValue();
        mScore.setValue((Boolean.TRUE.equals(mGameState.getValue()))
                ? current + 1
                : current + 2);
    }

    public void onBack2Menu() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        // navigate to menu
        mNavigateTo.setValue(GameUtils.NavigationDestination.MAIN);

    }

}