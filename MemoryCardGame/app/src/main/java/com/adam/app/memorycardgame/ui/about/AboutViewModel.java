/**
 * Copyright (C) 2026 Adam Chen. All rights reserved.
 * <p>
 *     Description: This is the About ViewModel.
 * </p>
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/23
 *
 */
package com.adam.app.memorycardgame.ui.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutViewModel extends ViewModel {
    // Live data: about text
    private final MutableLiveData<String> mAboutText = new MutableLiveData<>();
    public LiveData<String> getAboutText() {
        return mAboutText;
    }

    /**
     * set about text
     * @param text about text
     */
    public void setAboutText(String text) {
        mAboutText.setValue(text);
    }
}