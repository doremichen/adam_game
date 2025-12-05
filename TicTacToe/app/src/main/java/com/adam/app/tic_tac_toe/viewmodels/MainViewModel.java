/**
 * This class is the Main view model for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-05
 */
package com.adam.app.tic_tac_toe.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    // define NavigationDestination
    public enum NavigationDestination {
        START_GAME,
        SETTINGS,
        ABOUT,
        EXIT,
        NONE
    }

    // --- live Data
    private final MutableLiveData<NavigationDestination> mNavigateTo = new MutableLiveData<>(NavigationDestination.NONE);
    // --- get
    public LiveData<NavigationDestination> getNavigateTo() {
        return mNavigateTo;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
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

    /**
     * onNavigationDone
     */
    public void onNavigationDone() {
        mNavigateTo.setValue(NavigationDestination.NONE);
    }

}