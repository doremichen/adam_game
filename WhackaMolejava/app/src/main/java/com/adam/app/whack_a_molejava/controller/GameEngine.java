/**
 * Copyright 2025 Adam Game
 * <p>
 * This class is the Game engine that handle the logic of the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-03
 *
 */
package com.adam.app.whack_a_molejava.controller;

import android.graphics.PointF;

import com.adam.app.whack_a_molejava.model.Mole;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {

    private final long mGameDurationMs;

    // end time
    private long mGameEndTime;
    // game callback
    private final GameCallback mGameCallback;
    // list of moles
    private final List<Mole> mMoles = new ArrayList<>(9);
    // list of position
    private final List<PointF> mBackupPositions = new ArrayList<>(9);

    // random
    private final Random mRandom = new Random();

    // score
    private int mScore;

    // game state
    private volatile WAMGameState mGameState = WAMGameState.IDLE;


    // spawn control
    private long mLastGuaranteedSpawn = 0L;
    private final long GUARANTEED_SPAWN_INTERVAL_MS = 800L; // at least every 800ms spawn one

    /**
     * Constructor
     *
     * @param durationSec duration time
     * @param callback    callback
     */
    public GameEngine(int durationSec, GameCallback callback) {
        // duration time
        mGameDurationMs = durationSec * 1000L;
        mGameEndTime = System.currentTimeMillis() + mGameDurationMs;
        mGameCallback = callback;
        initMoles();
    }


    /**
     * backup cell positions
     * @param list list of points
     */
    public void backupCellPositions(List<PointF> list) {
        if (list == null) return;
        for (int i = 0; i < list.size(); i++) {
            mBackupPositions.add(list.get(i));
        }
    }


    /**
     * Allow host (View) to set actual mole center positions (after measuring view).
     */
    public void setMolePositions(List<PointF> centers) {
        if (centers == null) return;
        int count = Math.min(centers.size(), mMoles.size());
        for (int i = 0; i < count; i++) {
            mMoles.get(i).setPosition(centers.get(i));
        }
    }

    /**
     * update
     */
    public void update() {

        long now = System.currentTimeMillis();

        // Time update
        int remain = (int) ((mGameEndTime - now) / 1000L);
        if (remain < 0) remain = 0;
        mGameCallback.onTimeChanged(remain);

        if (remain == 0) {
            // stop logic, time over (ViewModel will mark game over)
            mGameCallback.onGameOver();
            return;
        }

        // Update visible state: hide expired
        for (Mole mole : mMoles) {
            if (mole.isVisible()) {
                if (now >= mole.getVisibleUntil()) {
                    mole.setVisible(false);
                }
            }
        }

        // spawn logic
        // 1) guaranteed spawn every GUARANTEED_SPAWN_INTERVAL_MS
        if (now - mLastGuaranteedSpawn >= GUARANTEED_SPAWN_INTERVAL_MS) {
            spawnOne(now);
            mLastGuaranteedSpawn = now;
        } else {
            // 2) random spawn attempts each tick: modest probability
            float chance = 0.12f; // ~12% per tick (tune)
            if (mRandom.nextFloat() < chance) {
                spawnOne(now);
            }
        }
    }

    private void spawnOne(long now) {
        // pick a random mole that is currently not visible
        List<Mole> candidates = new ArrayList<>();
        for (Mole mole : mMoles) {
            if (!mole.isVisible()) candidates.add(mole);
        }
        if (candidates.isEmpty()) return;
        Mole chosen = candidates.get(mRandom.nextInt(candidates.size()));
        chosen.setVisible(true);
        long duration = 600L + mRandom.nextInt(700); // 600 ~ 1299 ms
        chosen.setVisibleUntil(now + duration);
        chosen.setImgIndex(mRandom.nextInt(1)); // if multiple bmp, change 1->n
    }

    /**
     * hit mole
     *
     * @param position position
     */
    public void hitMole(PointF position) {
        for (Mole mole : mMoles) {
            if (mole.isVisible()) {
                float dx = position.x - mole.getPosition().x;
                float dy = position.y - mole.getPosition().y;
                if (Math.sqrt(dx * dx + dy * dy) < dpToPx(64)) { // use dp-based radius
                    mole.setVisible(false);
                    mScore++;
                    mGameCallback.onScoreChanged(mScore);
                    return;
                }
            }
        }
    }

    // helper - adjust to device density if needed (fallback)
    private float dpToPx(float dp) {
        // can't access resources here; caller can translate. Keep 64 px fallback.
        return dp;
    }

    /**
     * get moles
     */
    public List<Mole> getMoles() {
        return mMoles;
    }

    public void changeState(WAMGameState state) {
        mGameState = state;
    }

    public WAMGameState getState() {
        return mGameState;
    }

    public void reset() {
        // 1. Reset end time for the new game
        mGameEndTime = System.currentTimeMillis() + mGameDurationMs;

        // 2. Reset score
        mScore = 0;

        // 3. Re-initialize the moles for the new game
        initMoles();

        // 4. set mole positions
        setMolePositions(mBackupPositions);


        // 5. Reset game state to running
        changeState(WAMGameState.RUN);
    }



    /**
     * init moles
     */
    private void initMoles() {
        mMoles.clear();
        for (int i = 0; i < 9; i++) {
            mMoles.add(new Mole(new PointF(0f, 0f)));
        }
        // optional: show one mole at start so user sees something immediately
        if (!mMoles.isEmpty()) {
            Mole m = mMoles.get(0);
            m.setVisible(true);
            long now = System.currentTimeMillis();
            m.setVisibleUntil(now + 900L);
            m.setImgIndex(mRandom.nextInt(1)); // if multiple bitmaps exist
            mLastGuaranteedSpawn = now;
        }
    }

    /**
     * Callback
     */
    public interface GameCallback {
        void onScoreChanged(int score);

        void onTimeChanged(int sec);

        void onGameOver();
    }

    /**
     * Game state
     */
    public enum WAMGameState {
        IDLE,
        RUN,
        GAME_OVER
    }


}
