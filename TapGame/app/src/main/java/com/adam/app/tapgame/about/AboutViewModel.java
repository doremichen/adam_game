/**
 * File: AboutViewModel.java
 * Description: This class is About View Model
 *
 * @author Adam Chen
 * @version 1.0 - 2026/01/14
 */
package com.adam.app.tapgame.about;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.tapgame.R;
import com.adam.app.tapgame.utils.GameUtils;

public class AboutViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mInstruction = new MutableLiveData<>();

    private final MutableLiveData<GameUtils.NavigationDestination> mNavigateTo
            = new MutableLiveData<>(GameUtils.NavigationDestination.NONE);

    public AboutViewModel(Application application) {
        super(application);
        initInstruction(application);
    }

    private void initInstruction(Context ctx) {
        mInstruction.setValue(
                ctx.getString(R.string.tap_game_instruction)
        );
    }


    public LiveData<String> getInstruction() {
        return mInstruction;
    }

    public LiveData<GameUtils.NavigationDestination> getNavigateTo() {
        return mNavigateTo;
    }

    public void onExitClicked() {
        mNavigateTo.setValue(GameUtils.NavigationDestination.MAIN);
    }
}