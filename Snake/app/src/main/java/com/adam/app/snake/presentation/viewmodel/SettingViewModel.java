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
package com.adam.app.snake.presentation.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.adam.app.snake.R;
import com.adam.app.snake.domain.usecase.SettingUseCase;
import com.adam.app.snake.presentation.setting.GameSettingItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * Setting view model
 */
@HiltViewModel
public class SettingViewModel extends ViewModel {

    private final SettingUseCase mUseCase;

    @Inject
    public SettingViewModel(SettingUseCase useCase) {
        this.mUseCase = useCase;
    }

    /**
     * Build all setting items for the UI
     * @param context Context for string resources
     * @return List of GameSettingItem
     */
    public List<GameSettingItem> buildSettingItems(Context context) {
        List<GameSettingItem> items = new ArrayList<>();
        for (GameSettingItem.SettingType type : GameSettingItem.SettingType.values()) {
            // Using type-safe helper method
            Object value = mUseCase.getSetting(type.getKey());
            GameSettingItem item = new GameSettingItem(type, value);
            
            // Special handling for Spinner items
            if (type == GameSettingItem.SettingType.SPECIAL_FREQ) {
                List<String> freqItems = new ArrayList<>();
                freqItems.add(context.getString(R.string.snake_game_setting_freq_low));
                freqItems.add(context.getString(R.string.snake_game_setting_freq_middle));
                freqItems.add(context.getString(R.string.snake_game_setting_freq_high));
                item.setSpinnerItems(freqItems);
            }
            
            // Special handling for Version info
            if (type == GameSettingItem.SettingType.VERSION) {
                item.setInfo(String.valueOf(value));
            }
            
            items.add(item);
        }
        return items;
    }

    /**
     * Save setting by key
     * @param key SettingKey
     * @param value Object
     */
    public void saveSetting(SettingUseCase.SettingKey key, Object value) {
        mUseCase.saveSetting(key, value);
    }
}
