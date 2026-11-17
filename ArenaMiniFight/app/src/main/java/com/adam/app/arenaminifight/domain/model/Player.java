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

package com.adam.app.arenaminifight.domain.model;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Player domain entity representing a player or AI bot in the game.
 */
public class Player implements Parcelable {

    private final String mName;
    private final String mPlayerId;
    private final PointF mPosition;
    private final float mDirection;
    private final int mHp;

    /**
     * Constructs a Player instance.
     *
     * @param name      player display name
     * @param playerId  unique player identifier
     * @param position  2D position coordinates
     * @param direction orientation angle
     * @param hp        health points
     */
    public Player(String name, String playerId, PointF position, float direction, int hp) {
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

    /**
     * Returns the player name.
     *
     * @return player name
     */
    public String getName() {
        return mName;
    }

    /**
     * Returns the unique player identifier.
     *
     * @return player ID
     */
    public String getPlayerId() {
        return mPlayerId;
    }

    /**
     * Returns the 2D position point.
     *
     * @return PointF position
     */
    public PointF getPosition() {
        return mPosition;
    }

    /**
     * Returns the player direction angle.
     *
     * @return direction angle
     */
    public float getDirection() {
        return mDirection;
    }

    /**
     * Returns the current health points.
     *
     * @return HP value
     */
    public int getHp() {
        return mHp;
    }

    /**
     * Returns the X coordinate.
     *
     * @return X coordinate Float
     */
    public Float getX() {
        return mPosition != null ? mPosition.x : 0f;
    }

    /**
     * Returns the Y coordinate.
     *
     * @return Y coordinate Float
     */
    public Float getY() {
        return mPosition != null ? mPosition.y : 0f;
    }
}
