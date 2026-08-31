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

import com.adam.app.racinggame2d.util.Utils;

/**
 * Domain entity representing the player.
 */
public final class Player {
    private static final String sTAG = "Player";
    private final Car mCar;
    private String mPlayerName;
    private int mPlayerScore;

    public Player(@NonNull String playerName, @NonNull Car car) {
        this.mPlayerName = playerName;
        this.mCar = car;
        this.mPlayerScore = 0;
    }

    public void addScore(int score) {
        mPlayerScore += score;
    }

    public int getScore() {
        Utils.logDebug(sTAG, "getScore: " + mPlayerScore);
        return mPlayerScore;
    }

    public Car getCar() {
        return mCar;
    }

    public String getName() {
        return mPlayerName;
    }

    public void reset() {
        Utils.logDebug(sTAG, "reset");
        mPlayerScore = 0;
        mCar.reset();
    }
}
