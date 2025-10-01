/**
 * Copyright 2015 the Adam Game
 *
 * Description: This class is the snake game setting item that support multi-type
 *
 * Author: Adam Chen
 * Date: 2025/10/01
 */
package com.adam.app.snake.setting;

import java.util.ArrayList;
import java.util.List;

public class GameSettingItem {
    /**
     * static Class TYPE
     * SWITCH: switch option
     * SPINNER: spinner option
     * TEXT: text option
     */
    public static class TYPE {
        public static final int SWITCH = 0;
        public static final int SPINNER = 1;
        public static final int TEXT = 2;
    }

    // TYPE
    private final int type; // optional type
    // Key
    private final String key; // shared preference key
    // title
    private final String title; // title

    // for switch option
    private boolean mIsEnable;

    // for spinner option
    private List<String> mSpinnerItems = new ArrayList<>();
    private int mSpinnerIndex;

    // for text option
    private String mTextValue;

    /**
     * switch constructor
     *
     * @param type: TYPE
     * @param key:  String
     * @param title: String
     * @param isEnable: boolean
     */
    public GameSettingItem(int type, String key, String title, boolean isEnable) {
        this.type = type;
        this.key = key;
        this.title = title;
        this.mIsEnable = isEnable;
    }

    /**
     * spinner constructor
     *
     * @param type: TYPE
     * @param key: String
     * @param title: String
     * @param spinnerItems: List<String>
     * @param spinnerIndex: int
     */
    public GameSettingItem(int type,
                           String key,
                           String title,
                           List<String> spinnerItems,
                           int spinnerIndex) {
        this.type = type;
        this.key = key;
        this.title = title;
        this.mSpinnerItems = spinnerItems;
        this.mSpinnerIndex = spinnerIndex;
    }

    /**
     * text constructor
     *
     * @param type: TYPE
     * @param key: String
     * @param title: String
     * @param textValue: String
     */
    public GameSettingItem(int type, String key, String title, String textValue) {
        this.type = type;
        this.key = key;
        this.title = title;
        this.mTextValue = textValue;
    }

    // Getter
    public int getType() {
        return type;
    }
    public String getKey() {
        return key;
    }
    public String getTitle() {
        return title;
    }
    // switch
    public boolean isEnable() {
        return mIsEnable;
    }
    public void setEnable(boolean enable) {
        mIsEnable = enable;
    }
    // spinner
    public List<String> getSpinnerItems() {
        return mSpinnerItems;
    }
    public int getSpinnerIndex() {
        return mSpinnerIndex;
    }
    public void setSpinnerIndex(int spinnerIndex) {
        mSpinnerIndex = spinnerIndex;
    }

    // text
    public String getTextValue() {
        return mTextValue;
    }
    public void setTextValue(String textValue) {
        mTextValue = textValue;
    }
}
