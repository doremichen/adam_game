/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the match result model of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/03
 */
package com.adam.app.arenaminifight.ipc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MatchResult implements Parcelable {

    private boolean mMatched;
    private String mOpponentId;
    private String mOpponentNickname;
    private int mArenaId;


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

    // -- getter --
    public boolean isMatched() {
        return mMatched;
    }

    public String getOpponentId() {
        return mOpponentId;
    }

    public String getOpponentNickname() {
        return mOpponentNickname;
    }

    public int getArenaId() {
        return mArenaId;
    }
}
