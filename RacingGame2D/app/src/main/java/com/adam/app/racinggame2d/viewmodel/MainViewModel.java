/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the view model of main activity
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/28
 */
package com.adam.app.racinggame2d.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {
    // TAG
    private static final String TAG = "MainViewModel";

    // MutableLiveData _navigateEvent
    private final MutableLiveData<NavigateEvent> _navigateEvent = new MutableLiveData<>();
    // LiveData navigateEvent
    public LiveData<NavigateEvent> mNavigateEvent = _navigateEvent;

    // Player name
    private String mPlayerName;


    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * set player name
     *
     * @param playerName player name
     */
    public void setPlayerName(String playerName) {
        mPlayerName = playerName;
    }


    /**
     * navigate to start game
     */
    public void navigateToStartGame() {
        _navigateEvent.setValue(NavigateEvent.START_GAME);
    }

    /**
     * navigate to setting
     */
    public void navigateToSetting() {
        _navigateEvent.setValue(NavigateEvent.SETTING);
    }

    /**
     * navigate to leader board
     */
    public void navigateToLeaderBoard() {
        _navigateEvent.setValue(NavigateEvent.LEADER_BOARD);
    }

    /**
     * navigate to about
     */
    public void navigateToAbout() {
        _navigateEvent.setValue(NavigateEvent.ABOUT);
    }

    /**
     * navigate to exit
     */
    public void navigateToExit() {
        _navigateEvent.setValue(NavigateEvent.EXIT);
    }

    /**
     * NavigateEvent
     */
    public enum NavigateEvent {
        START_GAME, SETTING, LEADER_BOARD, ABOUT, EXIT
    }


}
