/**
 * File: HomeViewModel.java
 * Description: This class is Home ViewModel
 *
 * @author Adam Chen
 * @version 1.0 - 2026/01/14
 */
package com.adam.app.tapgame.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.tapgame.utils.GameUtils;

public class HomeViewModel extends ViewModel {

    // live data
    private MutableLiveData<GameUtils.NavigationDestination> mNavigateTo = new MutableLiveData<>(GameUtils.NavigationDestination.NONE);

    // get live data
    public LiveData<GameUtils.NavigationDestination> getNavigateTo() {
        return mNavigateTo;
    }

    /**
     * onStartGameClicked
     */
    public void onStartGameClicked() {
        mNavigateTo.setValue(GameUtils.NavigationDestination.START_GAME);
    }

    /**
     * onSettingsClicked
     */
    public void onSettingsClicked() {
        mNavigateTo.setValue(GameUtils.NavigationDestination.SETTINGS);
    }

    /**
     * onAboutClicked
     */
    public void onAboutClicked() {
        mNavigateTo.setValue(GameUtils.NavigationDestination.ABOUT);
    }

    /**
     * onExitClicked
     */
    public void onExitClicked() {
        mNavigateTo.setValue(GameUtils.NavigationDestination.EXIT);
    }

}