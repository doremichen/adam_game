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
import com.adam.app.tapgame.utils.SettingsManager;


public class GameViewModel extends AndroidViewModel {

    // TAG
    private static final String TAG = GameViewModel.class.getSimpleName();

    private final SettingsManager mSettingMgr;

    // live data
    private final MutableLiveData<Integer> mScore = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> mTimeLeft = new MutableLiveData<>(10);
    private final MutableLiveData<GameUtils.NavigationDestination> mNavigateTo = new MutableLiveData<>(GameUtils.NavigationDestination.NONE);


    private CountDownTimer mTimer;

    // --- get live data ---
    public LiveData<Integer> getScore() {
        return mScore;
    }

    public LiveData<Integer> getTimeLeft() {
        return mTimeLeft;
    }


    public LiveData<GameUtils.NavigationDestination> getNavigateTo() {
        return mNavigateTo;
    }

    // --- constructor ---
    public GameViewModel(@NonNull Application application) {
        super(application);

        mSettingMgr = SettingsManager.getInstance(application);

        startTimer();
    }

    private void startTimer() {

        int totalTime = mSettingMgr.getInterval();

        mTimer = new CountDownTimer(totalTime * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                int displayTime = (int) (millisUntilFinished / 1000);
                GameUtils.log(TAG + ": onTick: " + String.valueOf(displayTime));
                mTimeLeft.setValue(displayTime);
            }

            @Override
            public void onFinish() {
                GameUtils.log(TAG + ": onFinish");
                mTimeLeft.setValue(-1);
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
        if (mScore == null) {
            throw new NullPointerException("mScore is null");
        }

        int current = mScore.getValue();
        boolean gameState = mSettingMgr.isEasyMode();
        mScore.setValue((gameState)
                ? current + 1
                : current + 20);
    }

    public void onBack2Menu() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        // navigate to menu
        mNavigateTo.setValue(GameUtils.NavigationDestination.MAIN);

    }

}