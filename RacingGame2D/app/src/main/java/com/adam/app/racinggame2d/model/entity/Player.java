/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the entity of the player.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/27
 */
package com.adam.app.racinggame2d.model.entity;

import androidx.annotation.NonNull;
import com.adam.app.racinggame2d.util.GameUtil;


public class Player {
    //TAG
    private static final String TAG = "Player";
    // mPlayerName: String
    private String mPlayerName;
    // mPlayerScore: Int
    private int mPlayerScore;
    // mCar: Car
    private Car mCar;

    /**
     * Constructor
     * @param playerName: String
     * @param car: Car
     */
    public Player(@NonNull String playerName, @NonNull Car car) {
        mPlayerName = playerName;
        mCar = car;
        // init score
        mPlayerScore = 0;
    }

    /**
     * addScore
     *  add score to player
     * @param score: Int
     */
    public void addScore(int score) {
        mPlayerScore += score;
    }

    /**
     * getScore
     */
    public int getScore() {
        GameUtil.log(TAG, "getScore: " + mPlayerScore);
        return mPlayerScore;
    }

    /**
     * getCar
     */
    public Car getCar() {
        return mCar;
    }

    /**
     * getName
     */
    public String getName() {
        return mPlayerName;
    }

    public void reset() {
        GameUtil.log(TAG, "reset");
        mPlayerScore = 0;
        mCar.reset();
    }
}
