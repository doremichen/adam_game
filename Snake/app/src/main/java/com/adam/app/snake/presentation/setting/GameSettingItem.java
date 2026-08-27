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
package com.adam.app.snake.presentation.setting;

import com.adam.app.snake.R;
import com.adam.app.snake.domain.usecase.SettingUseCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Game setting item container
 */
public class GameSettingItem {

    /**
     * UI Representation types
     */
    public enum UiType {
        SWITCH, SPINNER, TEXT
    }

    /**
     * Setting metadata definition
     */
    public enum SettingType {
        WRAP_MODE(SettingUseCase.SettingKey.WRAP_MODE, R.string.snake_game_setting_wrap_mode, UiType.SWITCH),
        SPECIAL_FOOD(SettingUseCase.SettingKey.SPECIAL_FOOD, R.string.snake_game_setting_special_food, UiType.SWITCH),
        MULTI_FOODS_SHOW(SettingUseCase.SettingKey.MULTI_FOODS_SHOW, R.string.snake_game_setting_multi_foods_show, UiType.SWITCH),
        SPECIAL_FREQ(SettingUseCase.SettingKey.SPECIAL_FREQ, R.string.snake_game_setting_special_freq, UiType.SPINNER),
        VERSION(SettingUseCase.SettingKey.VERSION, R.string.app_version, UiType.TEXT);

        private final SettingUseCase.SettingKey mKey;
        private final int mTitleResId;
        private final UiType mUiType;

        SettingType(SettingUseCase.SettingKey key, int titleResId, UiType uiType) {
            this.mKey = key;
            this.mTitleResId = titleResId;
            this.mUiType = uiType;
        }

        public SettingUseCase.SettingKey getKey() {
            return mKey;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public UiType getUiType() {
            return mUiType;
        }
    }

    private final SettingType mSettingType;
    private final Object mValue;
    private List<String> mSpinnerItems = new ArrayList<>();
    private String mInfo;

    /**
     * Constructor for all types
     * @param settingType SettingType
     * @param value Object
     */
    public GameSettingItem(SettingType settingType, Object value) {
        this.mSettingType = settingType;
        this.mValue = value;
    }

    /**
     * Fluent API for spinner items
     * @param items List<String>
     * @return GameSettingItem
     */
    public GameSettingItem setSpinnerItems(List<String> items) {
        this.mSpinnerItems = items;
        return this;
    }

    /**
     * Fluent API for info
     * @param info String
     * @return GameSettingItem
     */
    public GameSettingItem setInfo(String info) {
        this.mInfo = info;
        return this;
    }

    public SettingType getSettingType() {
        return mSettingType;
    }

    public Object getValue() {
        return mValue;
    }

    public List<String> getSpinnerItems() {
        return mSpinnerItems;
    }

    public String getInfo() {
        return mInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSettingItem that = (GameSettingItem) o;
        if (mSettingType != that.mSettingType) return false;
        if (mValue != null ? !mValue.equals(that.mValue) : that.mValue != null) return false;
        if (mSpinnerItems != null ? !mSpinnerItems.equals(that.mSpinnerItems) : that.mSpinnerItems != null)
            return false;
        return mInfo != null ? mInfo.equals(that.mInfo) : that.mInfo == null;
    }

    @Override
    public int hashCode() {
        int result = mSettingType != null ? mSettingType.hashCode() : 0;
        result = 31 * result + (mValue != null ? mValue.hashCode() : 0);
        result = 31 * result + (mSpinnerItems != null ? mSpinnerItems.hashCode() : 0);
        result = 31 * result + (mInfo != null ? mInfo.hashCode() : 0);
        return result;
    }
}
