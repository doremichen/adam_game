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
package com.adam.app.snake.data.repository;

import com.adam.app.snake.data.file.SharedPreferenceManager;
import com.adam.app.snake.domain.repository.ISettingRepository;
import com.adam.app.snake.domain.usecase.SettingUseCase;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of ISettingRepository using SharedPreferenceManager
 * Refactored to minimize casting and remove unnecessary instanceof checks.
 */
@Singleton
public class SettingRepository implements ISettingRepository {

    private final SharedPreferenceManager mPreferenceManager;
    private final Map<SettingUseCase.SettingKey, SettingHandler> mHandlerMap = new EnumMap<>(SettingUseCase.SettingKey.class);

    @Inject
    public SettingRepository(SharedPreferenceManager preferenceManager) {
        this.mPreferenceManager = preferenceManager;
        initializeHandlers();
    }

    private void initializeHandlers() {
        for (SettingHandler handler : SettingHandler.values()) {
            mHandlerMap.put(handler.getDomainKey(), handler);
        }
    }

    @Override
    public <T> T getSetting(SettingUseCase.SettingKey key) {
        SettingHandler handler = mHandlerMap.get(key);
        // Double casting at the edge is necessary due to Enum's single-type nature, 
        // but it's safe because the UseCase layer enforces the type contract.
        return handler != null ? (T) handler.get(mPreferenceManager) : null;
    }

    @Override
    public <T> void saveSetting(SettingUseCase.SettingKey key, T value) {
        SettingHandler handler = mHandlerMap.get(key);
        if (handler != null) {
            handler.save(mPreferenceManager, value);
        }
    }

    /**
     * Internal enum to handle specific setting operations.
     * Logic is verticalized and trusts the type contract from the domain layer.
     */
    private enum SettingHandler {
        WRAP_MODE(SettingUseCase.SettingKey.WRAP_MODE) {
            @Override
            Object get(SharedPreferenceManager prefs) {
                return prefs.getBoolean(SharedPreferenceManager.Keys.WRAP_MODE, false);
            }
            @Override
            void save(SharedPreferenceManager prefs, Object value) {
                prefs.putBoolean(SharedPreferenceManager.Keys.WRAP_MODE, (Boolean) value);
            }
        },
        SPECIAL_FOOD(SettingUseCase.SettingKey.SPECIAL_FOOD) {
            @Override
            Object get(SharedPreferenceManager prefs) {
                return prefs.getBoolean(SharedPreferenceManager.Keys.SPECIAL_FOOD, false);
            }
            @Override
            void save(SharedPreferenceManager prefs, Object value) {
                prefs.putBoolean(SharedPreferenceManager.Keys.SPECIAL_FOOD, (Boolean) value);
            }
        },
        MULTI_FOODS_SHOW(SettingUseCase.SettingKey.MULTI_FOODS_SHOW) {
            @Override
            Object get(SharedPreferenceManager prefs) {
                return prefs.getBoolean(SharedPreferenceManager.Keys.MULTI_FOODS_SHOW, false);
            }
            @Override
            void save(SharedPreferenceManager prefs, Object value) {
                prefs.putBoolean(SharedPreferenceManager.Keys.MULTI_FOODS_SHOW, (Boolean) value);
            }
        },
        SPECIAL_FREQ(SettingUseCase.SettingKey.SPECIAL_FREQ) {
            @Override
            Object get(SharedPreferenceManager prefs) {
                return prefs.getInt(SharedPreferenceManager.Keys.SPECIAL_FREQ, 0);
            }
            @Override
            void save(SharedPreferenceManager prefs, Object value) {
                prefs.putInt(SharedPreferenceManager.Keys.SPECIAL_FREQ, (Integer) value);
            }
        },
        USER_NAME(SettingUseCase.SettingKey.USER_NAME) {
            @Override
            Object get(SharedPreferenceManager prefs) {
                return prefs.getString(SharedPreferenceManager.Keys.USER_NAME, "");
            }
            @Override
            void save(SharedPreferenceManager prefs, Object value) {
                prefs.putString(SharedPreferenceManager.Keys.USER_NAME, (String) value);
            }
        },
        VERSION(SettingUseCase.SettingKey.VERSION) {
            @Override
            Object get(SharedPreferenceManager prefs) {
                return prefs.getFloat(SharedPreferenceManager.Keys.VERSION, 0.01f);
            }
            @Override
            void save(SharedPreferenceManager prefs, Object value) {
                prefs.putFloat(SharedPreferenceManager.Keys.VERSION, (Float) value);
            }
        };

        private final SettingUseCase.SettingKey mDomainKey;

        SettingHandler(SettingUseCase.SettingKey domainKey) {
            this.mDomainKey = domainKey;
        }

        public SettingUseCase.SettingKey getDomainKey() {
            return mDomainKey;
        }

        abstract Object get(SharedPreferenceManager prefs);
        abstract void save(SharedPreferenceManager prefs, Object value);
    }
}
