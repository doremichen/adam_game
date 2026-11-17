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
 * Match result model for IPC communication.
 */
public class MatchResult implements Parcelable {

    private boolean mMatched;
    private String mOpponentId;
    private String mOpponentNickname;
    private int mArenaId;

    /**
     * Constructs a MatchResult instance.
     *
     * @param matched          whether match succeeded
     * @param opponentId       opponent ID
     * @param opponentNickname opponent nickname
     * @param arenaId          arena ID
     */
    public MatchResult(boolean matched,
                       String opponentId,
                       String opponentNickname,
                       int arenaId) {
        mMatched = matched;
        mOpponentId = opponentId;
        mOpponentNickname = opponentNickname;
        mArenaId = arenaId;
    }

    protected MatchResult(Parcel in) {
        mMatched = in.readByte() != 0;
        mOpponentId = in.readString();
        mOpponentNickname = in.readString();
        mArenaId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mMatched ? 1 : 0));
        dest.writeString(mOpponentId);
        dest.writeString(mOpponentNickname);
        dest.writeInt(mArenaId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MatchResult> CREATOR = new Creator<MatchResult>() {
        @Override
        public MatchResult createFromParcel(Parcel in) {
            return new MatchResult(in);
        }

        @Override
        public MatchResult[] newArray(int size) {
            return new MatchResult[size];
        }
    };

    /**
     * Returns whether matched.
     *
     * @return boolean match status
     */
    public boolean isMatched() {
        return mMatched;
    }

    /**
     * Returns the opponent ID.
     *
     * @return opponent ID
     */
    public String getOpponentId() {
        return mOpponentId;
    }

    /**
     * Returns the opponent nickname.
     *
     * @return opponent nickname
     */
    public String getOpponentNickname() {
        return mOpponentNickname;
    }

    /**
     * Returns the arena ID.
     *
     * @return arena ID
     */
    public int getArenaId() {
        return mArenaId;
    }
}
