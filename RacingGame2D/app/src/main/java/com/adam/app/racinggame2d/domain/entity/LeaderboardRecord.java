/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.domain.entity;

import androidx.annotation.NonNull;

/**
 * Domain entity representing a record in the leaderboard.
 */
public final class LeaderboardRecord {
    private final int mId;
    private final String mPlayerName;
    private final int mScore;
    private final String mTimestamp;

    public LeaderboardRecord(int id, String playerName, int score, String timestamp) {
        this.mId = id;
        this.mPlayerName = playerName;
        this.mScore = score;
        this.mTimestamp = timestamp;
    }

    public int getId() {
        return mId;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public int getScore() {
        return mScore;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "LeaderboardRecord{" +
                "mId=" + mId +
                ", mPlayerName='" + mPlayerName + '\'' +
                ", mScore=" + mScore +
                ", mTimestamp='" + mTimestamp + '\'' +
                '}';
    }
}
