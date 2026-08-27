/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.domain.usecase;

import com.adam.app.snake.domain.repository.ISettingRepository;

import javax.inject.Inject;

/**
 * Setting use case - Refactored for Type Safety using Generics
 */
public class SettingUseCase {

    private final ISettingRepository mRepository;

    @Inject
    public SettingUseCase(ISettingRepository repository) {
        this.mRepository = repository;
    }

    /**
     * Setting operations
     */
    public enum Operation {
        GET_SETTING,
        SAVE_SETTING
    }

    /**
     * Setting keys
     */
    public enum SettingKey {
        WRAP_MODE,
        SPECIAL_FOOD,
        MULTI_FOODS_SHOW,
        SPECIAL_FREQ,
        USER_NAME,
        VERSION
    }

    /**
     * Execute operation with generic type support
     * @param <T> Expected return or input type
     * @param operation Operation to perform
     * @param key Target setting key
     * @param value Value for SAVE_SETTING (can be null for GET_SETTING)
     * @return T value for GET_SETTING, null for SAVE_SETTING
     */
    @SuppressWarnings("unchecked")
    public <T> T execute(Operation operation, SettingKey key, T value) {
        if (operation == null || key == null) {
            return null;
        }

        switch (operation) {
            case GET_SETTING:
                return (T) mRepository.getSetting(key);
            case SAVE_SETTING:
                mRepository.saveSetting(key, value);
                return null;
            default:
                return null;
        }
    }

    /**
     * Type-safe helper for getting settings
     */
    @SuppressWarnings("unchecked")
    public <T> T getSetting(SettingKey key) {
        return (T) execute(Operation.GET_SETTING, key, null);
    }

    /**
     * Type-safe helper for saving settings
     */
    public <T> void saveSetting(SettingKey key, T value) {
        execute(Operation.SAVE_SETTING, key, value);
    }
}
