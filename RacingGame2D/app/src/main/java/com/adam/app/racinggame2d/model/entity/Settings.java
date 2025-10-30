/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is used to save the settings of the game.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/30
 */
package com.adam.app.racinggame2d.model.entity;

public class Settings {
    // sound
    private boolean mSoundEnable;

    /**
     * constructor
     */
    public Settings() {
        mSoundEnable = true;
    }

    public boolean isSoundEnable() {
        return mSoundEnable;
    }

    public void setSoundEnable(boolean enable) {
        mSoundEnable = enable;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "mSoundEnable=" + mSoundEnable +
                '}';
    }

}
