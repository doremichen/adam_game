/**
 * File: GameState.java
 * Description: This class is Game State model
 *
 * @author Adam Chen
 * @version 1.0 - 2026/01/14
 */
package com.adam.app.tapgame.model;

public class GameState {
    // score
    private final int mScore;
    // time left
    private final int mTimeLeft;
    // double score
    private final boolean mDoubleScore;

    public GameState(int score, int timeLeft, boolean doubleScore) {
        mScore = score;
        mTimeLeft = timeLeft;
        mDoubleScore = doubleScore;
    }

    // -- getter --
    public int getScore() {
        return mScore;
    }

    public int getTimeLeft() {
        return mTimeLeft;
    }

    public boolean getDoubleScore() {
        return mDoubleScore;
    }

}
