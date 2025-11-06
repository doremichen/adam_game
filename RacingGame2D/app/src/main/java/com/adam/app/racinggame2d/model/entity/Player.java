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

public class Player {
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
        mPlayerScore = 0;
        mCar.reset();
    }
}
