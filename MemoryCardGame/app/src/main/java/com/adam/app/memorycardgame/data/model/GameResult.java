/**
 * Copyright (C) 2016 Adam Chen. All Rights Reserved.
 * <p>
 * Description: this is the model class for game result.
 * </p>
 *
 * @author Adam Chen
 * @version 1.0 - 2026/02/24
 */
package com.adam.app.memorycardgame.data.model;

public class GameResult {
    private final long mStartTime;
    private final int mMatchCount;
    private final String mTheme;

    public GameResult(long startTime, int matchCount, String theme) {
        mStartTime = startTime;
        mMatchCount = matchCount;
        mTheme = theme;
    }

    //-- getter ---
    public long getStartTime() {
        return mStartTime;
    }

    public int getMatchCount() {
        return mMatchCount;
    }

    public String getTheme() {
        return mTheme;
    }

}
