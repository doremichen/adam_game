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

package com.adam.app.arenaminifight.ipc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Player info model for IPC communication.
 */
public class PlayerInfo implements Parcelable {

    private String mName;
    private String mPlayerId;
    private int mLevel;
    private int mRankScore;

    /**
     * Constructs a PlayerInfo instance.
     *
     * @param name      player name
     * @param playerId  player ID
     * @param level     player level
     * @param rankScore rank score
     */
    public PlayerInfo(String name,
                      String playerId,
                      int level,
                      int rankScore) {
        mName = name;
        mPlayerId = playerId;
        mLevel = level;
        mRankScore = rankScore;
    }

    protected PlayerInfo(Parcel in) {
        mName = in.readString();
        mPlayerId = in.readString();
        mLevel = in.readInt();
        mRankScore = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mPlayerId);
        dest.writeInt(mLevel);
        dest.writeInt(mRankScore);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlayerInfo> CREATOR = new Creator<PlayerInfo>() {
        @Override
        public PlayerInfo createFromParcel(Parcel in) {
            return new PlayerInfo(in);
        }

        @Override
        public PlayerInfo[] newArray(int size) {
            return new PlayerInfo[size];
        }
    };

    /**
     * Returns player name.
     *
     * @return name string
     */
    public String getName() {
        return mName;
    }

    /**
     * Returns player ID.
     *
     * @return player ID string
     */
    public String getPlayerId() {
        return mPlayerId;
    }

    /**
     * Returns player level.
     *
     * @return level int
     */
    public int getLevel() {
        return mLevel;
    }

    /**
     * Returns rank score.
     *
     * @return rank score int
     */
    public int getRankScore() {
        return mRankScore;
    }
}
