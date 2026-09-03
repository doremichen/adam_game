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

package com.adam.app.mydeviceinfo.application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adam.app.mydeviceinfo.domain.model.DeviceInfo;
import com.adam.app.mydeviceinfo.domain.repository.IDeviceRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Highly cohesive enum-based UseCase for device information operations.
 */
@Singleton
public final class InfoUseCase {
    private final IDeviceRepository mRepository;

    /**
     * Constructs the UseCase with a repository.
     * @param repository The repository for device information.
     */
    @Inject
    public InfoUseCase(@NonNull IDeviceRepository repository) {
        this.mRepository = repository;
    }

    /**
     * Actions supported by this UseCase.
     */
    public enum Action {
        FETCH_ALL_INFO {
            @Override
            public <T> T handle(@NonNull IDeviceRepository repo, @Nullable Object data) {
                return (T) repo.fetchDeviceInfo();
            }
        },
        EXPORT_REPORT {
            @Override
            public <T> T handle(@NonNull IDeviceRepository repo, @Nullable Object data) {
                if (data instanceof DeviceInfo) {
                    return (T) Boolean.valueOf(repo.exportDeviceInfo((DeviceInfo) data));
                }
                return (T) Boolean.FALSE;
            }
        },
        SUBSCRIBE_INFO {
            @Override
            public <T> T handle(@NonNull IDeviceRepository repo, @Nullable Object data) {
                return (T) repo.getDeviceInfoStream();
            }
        },
        EXECUTE_HW_TEST {
            @Override
            public <T> T handle(@NonNull IDeviceRepository repo, @Nullable Object data) {
                if (data instanceof Integer) {
                    repo.runHardwareTest((Integer) data);
                }
                return null;
            }
        };

        /**
         * Generic handle method to execute the action logic.
         * @param repo The repository to interact with.
         * @param data Optional data required for the action.
         * @param <T> Return type.
         * @return The result of the action.
         */
        abstract <T> T handle(@NonNull IDeviceRepository repo, @Nullable Object data);
    }

    /**
     * Executes the specified action.
     * @param action The action to execute.
     * @param data Optional data for the action.
     * @param <T> Expected return type.
     * @return The result of the action execution.
     */
    @Nullable
    public <T> T execute(@NonNull Action action, @Nullable Object data) {
        return action.handle(mRepository, data);
    }
}
