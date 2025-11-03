/**
 * Copyright (C) 2025 Adam. All Rights Reserved.
 * Description: This class is a ViewModel for the Racing GameFragment.
 *
 * @author Adam Chen
 * @since 2025-11-03
 */
package com.adam.app.racinggame.modelview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.adam.app.racinggame.util.GameUtil;


public class GameViewModel extends AndroidViewModel {
    // TAG
    private static final String TAG = GameViewModel.class.getSimpleName();

    public GameViewModel(@NonNull Application application) {
        super(application);
    }

    public void startGame() {
        GameUtil.log(TAG, "startGame");
    }
}