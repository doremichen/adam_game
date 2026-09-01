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

package com.adam.app.mydeviceinfo.ui.system;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.mydeviceinfo.application.InfoUseCase;
import com.adam.app.mydeviceinfo.domain.model.DeviceInfo;
import com.adam.app.mydeviceinfo.domain.model.SystemSpecs;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the system information feature.
 */
@HiltViewModel
public final class SystemViewModel extends ViewModel {
    private final InfoUseCase mUseCase;
    private final MediatorLiveData<DeviceInfo> mInfoSource = new MediatorLiveData<>();
    
    private final MutableLiveData<String> mManufacturer = new MutableLiveData<>("");
    private final MutableLiveData<String> mModel = new MutableLiveData<>("");
    private final MutableLiveData<String> mCodename = new MutableLiveData<>("");
    private final MutableLiveData<String> mOsInfo = new MutableLiveData<>("");
    private final MutableLiveData<String> mCpuInfo = new MutableLiveData<>("");
    private final MutableLiveData<List<String>> mSensorList = new MutableLiveData<>(new ArrayList<>());

    @Inject
    public SystemViewModel(@NonNull InfoUseCase useCase) {
        this.mUseCase = useCase;
        setupSource();
    }

    private void setupSource() {
        LiveData<DeviceInfo> stream = mUseCase.execute(InfoUseCase.Action.SUBSCRIBE_INFO, null);
        if (stream != null) {
            mInfoSource.addSource(stream, info -> {
                if (info != null) {
                    updateSystem(info.getSystemSpecs());
                }
            });
        }
    }

    private void updateSystem(SystemSpecs specs) {
        mManufacturer.postValue(specs.getManufacturer());
        mModel.postValue(specs.getModel());
        mCodename.postValue(specs.getCodename());
        mOsInfo.postValue(String.format("%s (API %d)", specs.getOsVersion(), specs.getSdkLevel()));
        mCpuInfo.postValue(String.format("%s (%d Cores)", specs.getCpuAbi(), specs.getCpuCores()));
        mSensorList.postValue(specs.getSensorList());
    }

    @NonNull public LiveData<String> getManufacturer() { return mManufacturer; }
    @NonNull public LiveData<String> getModel() { return mModel; }
    @NonNull public LiveData<String> getCodename() { return mCodename; }
    @NonNull public LiveData<String> getOsInfo() { return mOsInfo; }
    @NonNull public LiveData<String> getCpuInfo() { return mCpuInfo; }
    @NonNull public LiveData<List<String>> getSensorList() { return mSensorList; }
    @NonNull public LiveData<DeviceInfo> getInfoSource() { return mInfoSource; }

    public void refreshData() {
        // Auto-updated
    }
}
