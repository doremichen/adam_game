/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the player model of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/03
 */
package com.adam.app.arenaminifight.domain.model;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {
    // TAG
    private static final String TAG = "Player";

    private final String mName;
    private final String mPlayerId;
    private final PointF mPosition;
    private final float mDirection;
    private final int mHp;

    public Player(String name,
                  String playerId,
                  PointF position,
                  float direction,
                  int hp) {
        mName = name;
        mPlayerId = playerId;
        mPosition = position;
        mDirection = direction;
        mHp = hp;
    }

    protected Player(Parcel in) {
        mName = in.readString();
        mPlayerId = in.readString();
        mPosition = in.readParcelable(PointF.class.getClassLoader(), PointF.class);
        mDirection = in.readFloat();
        mHp = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mPlayerId);
        dest.writeParcelable(mPosition, flags);
        dest.writeFloat(mDirection);
        dest.writeInt(mHp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    // -- getter --
    public String getName() {
        return mName;
    }

    public String getPlayerId() {
        return mPlayerId;
    }

    public PointF getPosition() {
        return mPosition;
    }

    public float getDirection() {
        return mDirection;
    }

    public int getHp() {
        return mHp;
    }
}
