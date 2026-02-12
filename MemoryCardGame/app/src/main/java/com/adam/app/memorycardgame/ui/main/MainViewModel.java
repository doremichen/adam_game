/**
 * Copyright 2026 Adam Chen, All rights reserved.
 * <p>
 * Description: This class is the main view model for the memory card game.
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/11
 *
 */
package com.adam.app.memorycardgame.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.memorycardgame.util.CommonUtils;

public class MainViewModel extends AndroidViewModel {

    // TAG
    private static final String TAG = "MainViewModel";

    // --- live Data
    private final MutableLiveData<NavigationDestination> mNavigateTo = new MutableLiveData<>(NavigationDestination.NONE);

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<NavigationDestination> getNavigateTo() {
        return mNavigateTo;
    }

    public void onStartClicked() {
        CommonUtils.log(TAG + ".onStartClicked()");
        mNavigateTo.setValue(NavigationDestination.START_GAME);

    }

    public void onSettingClicked() {
        CommonUtils.log(TAG + ".onSettingClicked()");
        mNavigateTo.setValue(NavigationDestination.SETTINGS);

    }

    public void onExitClicked() {
        CommonUtils.log(TAG + ".onExitClicked()");
        mNavigateTo.setValue(NavigationDestination.EXIT);

    }

    public void onAboutClicked() {
        CommonUtils.log(TAG + ".onAboutClicked()");
        mNavigateTo.setValue(NavigationDestination.ABOUT);
    }


    public void onDoneNavigating() {
        CommonUtils.log(TAG + ".onDoneNavigating()");
        mNavigateTo.setValue(NavigationDestination.NONE);
    }


    // define NavigationDestination
    public enum NavigationDestination {
        START_GAME,
        SETTINGS,
        ABOUT,
        EXIT,
        NONE
    }

}