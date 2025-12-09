/**
 * This class is the About ViewModel for the Tic Tac Toe game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-09
 */
package com.adam.app.tic_tac_toe.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.tic_tac_toe.R;

public class AboutViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mAboutText = new MutableLiveData<>("");

    public AboutViewModel(@NonNull Application application) {
        super(application);
        mAboutText.setValue(application.getString(R.string.tic_tac_toe_about_instruction));
    }

    public LiveData<String> getAboutText() {
        return mAboutText;
    }

}