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

package com.adam.app.mydeviceinfo.ui.tools;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.mydeviceinfo.R;
import com.adam.app.mydeviceinfo.application.InfoUseCase;
import com.adam.app.mydeviceinfo.domain.model.DeviceInfo;
import com.adam.app.mydeviceinfo.ui.SingleLiveEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the tools feature.
 */
@HiltViewModel
public final class ToolsViewModel extends ViewModel {
    private final InfoUseCase mUseCase;
    private final SingleLiveEvent<Integer> mToastEvent = new SingleLiveEvent<>();
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    @Inject
    public ToolsViewModel(@NonNull InfoUseCase useCase) {
        this.mUseCase = useCase;
    }

    @NonNull
    public LiveData<Integer> getToastEvent() {
        return mToastEvent;
    }

    public void onExportClicked() {
        mExecutor.execute(() -> {
            DeviceInfo info = mUseCase.execute(InfoUseCase.Action.FETCH_ALL_INFO, null);
            if (info != null) {
                Boolean success = mUseCase.execute(InfoUseCase.Action.EXPORT_REPORT, info);
                mToastEvent.postValue(Boolean.TRUE.equals(success) ? R.string.toast_export_successful : R.string.toast_export_failed);
            }
        });
    }

    public void onVibrationTestClicked() {
        mExecutor.execute(() -> {
            mUseCase.execute(InfoUseCase.Action.EXECUTE_HW_TEST, 1); // 1 = Vibration
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mExecutor.shutdown();
    }
}
