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

public class HomeViewModel extends ViewModel {

    // live data
    private MutableLiveData<NavigationDestination> mNavigateTo = new MutableLiveData<>(NavigationDestination.NONE);

    // get live data
    public LiveData<NavigationDestination> getNavigateTo() {
        return mNavigateTo;
    }

    /**
     * onStartGameClicked
     */
    public void onStartGameClicked() {
        mNavigateTo.setValue(NavigationDestination.START_GAME);
    }

    /**
     * onSettingsClicked
     */
    public void onSettingsClicked() {
        mNavigateTo.setValue(NavigationDestination.SETTINGS);
    }

    /**
     * onAboutClicked
     */
    public void onAboutClicked() {
        mNavigateTo.setValue(NavigationDestination.ABOUT);
    }

    /**
     * onExitClicked
     */
    public void onExitClicked() {
        mNavigateTo.setValue(NavigationDestination.EXIT);
    }



    public enum NavigationDestination {
        START_GAME,
        SETTINGS,
        ABOUT,
        EXIT,
        NONE
    }
}