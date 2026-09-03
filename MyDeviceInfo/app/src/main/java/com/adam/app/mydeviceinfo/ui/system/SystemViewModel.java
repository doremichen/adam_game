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

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.mydeviceinfo.application.InfoUseCase;
import com.adam.app.mydeviceinfo.common.Constants;
import com.adam.app.mydeviceinfo.domain.model.DeviceInfo;
import com.adam.app.mydeviceinfo.domain.model.SystemSpecs;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

import com.adam.app.mydeviceinfo.R;

/**
 * ViewModel for the system information feature.
 */
@HiltViewModel
public final class SystemViewModel extends ViewModel {
    private final Context mContext;
    private final InfoUseCase mUseCase;
    private final MediatorLiveData<DeviceInfo> mInfoSource = new MediatorLiveData<>();
    
    private final MutableLiveData<String> mManufacturer = new MutableLiveData<>(Constants.EMPTY_STRING);
    private final MutableLiveData<String> mModel = new MutableLiveData<>(Constants.EMPTY_STRING);
    private final MutableLiveData<String> mCodename = new MutableLiveData<>(Constants.EMPTY_STRING);
    private final MutableLiveData<String> mOsInfo = new MutableLiveData<>(Constants.EMPTY_STRING);
    private final MutableLiveData<String> mCpuInfo = new MutableLiveData<>(Constants.EMPTY_STRING);
    private final MutableLiveData<List<String>> mSensorList = new MutableLiveData<>(new ArrayList<>());

    /**
     * Constructs the SystemViewModel.
     * @param context Application context.
     * @param useCase The use case for device information.
     */
    @Inject
    public SystemViewModel(@ApplicationContext @NonNull Context context, @NonNull InfoUseCase useCase) {
        this.mContext = context;
        this.mUseCase = useCase;
        setupSource();
    }

    /**
     * Initializes the reactive source for system information.
     */
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

    /**
     * Updates system LiveData with fresh specs.
     * @param specs System specifications entity.
     */
    private void updateSystem(@NonNull SystemSpecs specs) {
        mManufacturer.postValue(specs.getManufacturer());
        mModel.postValue(specs.getModel());
        mCodename.postValue(specs.getCodename());
        mOsInfo.postValue(mContext.getString(R.string.label_os_api_format, specs.getOsVersion(), specs.getSdkLevel()));
        mCpuInfo.postValue(mContext.getString(R.string.label_cpu_cores_format, specs.getCpuAbi(), specs.getCpuCores()));
        mSensorList.postValue(specs.getSensorList());
    }

    @NonNull public LiveData<String> getManufacturer() { return mManufacturer; }
    @NonNull public LiveData<String> getModel() { return mModel; }
    @NonNull public LiveData<String> getCodename() { return mCodename; }
    @NonNull public LiveData<String> getOsInfo() { return mOsInfo; }
    @NonNull public LiveData<String> getCpuInfo() { return mCpuInfo; }
    @NonNull public LiveData<List<String>> getSensorList() { return mSensorList; }
    @NonNull public LiveData<DeviceInfo> getInfoSource() { return mInfoSource; }

}
