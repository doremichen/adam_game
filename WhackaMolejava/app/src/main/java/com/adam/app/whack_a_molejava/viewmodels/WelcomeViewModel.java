/**
 * Copyright 2025 Adam Game
 * <p>
 * This class is the view model for the welcome activity.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-02
 *
 */
package com.adam.app.whack_a_molejava.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.adam.app.whack_a_molejava.util.SingleLiveEvent;

public class WelcomeViewModel extends AndroidViewModel {

    private SingleLiveEvent<WelcomeEvent> mEvent = new SingleLiveEvent<WelcomeEvent>();

    public WelcomeViewModel(@NonNull Application application) {
        super(application);
    }

    // --- getter ---
    public SingleLiveEvent<WelcomeEvent> getEvent() {
        return mEvent;
    }

    // --- setter ---
    public void startGame() {
        mEvent.setValue(WelcomeEvent.START_GAME);
    }

    public void setting() {
        mEvent.setValue(WelcomeEvent.SETTING);
    }

    public void about() {
        mEvent.setValue(WelcomeEvent.ABOUT);
    }

    public void exit() {
        mEvent.setValue(WelcomeEvent.EXIT);
    }

    public enum WelcomeEvent {
        START_GAME,
        SETTING,
        ABOUT,
        EXIT
    }


}
