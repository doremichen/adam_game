/*
 * Copyright (c) 2026 Adam Chen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.adam.app.arenaminifight.ui.welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.arenaminifight.domain.gateway.IGameGateway;
import com.adam.app.arenaminifight.domain.usecase.WelcomeUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * Trace: UC-01 (User Welcome & Service Initialization)
 */
@HiltViewModel
public class WelcomeViewModel extends ViewModel {

    private static final String TAG = "WelcomeViewModel";

    private final WelcomeUseCase mWelcomeUseCase;

    @Inject
    public WelcomeViewModel(IGameGateway gateway) {
        mWelcomeUseCase = new WelcomeUseCase(gateway);
        // Trace: UC-01
        mWelcomeUseCase.execute(WelcomeUseCase.Action.BIND_SERVICE, null);
    }

    public LiveData<Boolean> getIsLoginSuccess() {
        // Trace: UC-01
        return mWelcomeUseCase.execute(WelcomeUseCase.Action.IS_READY, null);
    }
}
