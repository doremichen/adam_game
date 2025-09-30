/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the snake game setting item
 *
 * Author: Adam Chen
 * Date: 2025/09/30
 */
package com.adam.app.snake.setting;

public class SettingItem {
    // title
    private final String mTitle;
    // switch option
    private final boolean mIsEnable;

    public SettingItem(String title, boolean isEnable) {
        mTitle = title;
        mIsEnable = isEnable;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isEnable() {
        return mIsEnable;
    }

}
