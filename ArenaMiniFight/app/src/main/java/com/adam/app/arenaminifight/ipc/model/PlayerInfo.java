/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the player info model of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/03
 */
package com.adam.app.arenaminifight.ipc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayerInfo implements Parcelable {

    // information
    private String mName;
    private String mPlayerId;
    private int mLevel;
    private int mRankScore;

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

    // -- getter --
    public String getName() {
        return mName;
    }

    public String getPlayerId() {
        return mPlayerId;
    }

    public int getLevel() {
        return mLevel;
    }

    public int getRankScore() {
        return mRankScore;
    }
}
