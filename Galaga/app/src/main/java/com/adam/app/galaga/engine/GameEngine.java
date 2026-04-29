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

package com.adam.app.galaga.engine;


import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.adam.app.galaga.data.model.GameObject;
import com.adam.app.galaga.utils.GameConstants;
import com.adam.app.galaga.utils.GameUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * This is the main class of the game engine.
 */
public class GameEngine {
    // TAG
    private static final String TAG = GameEngine.class.getSimpleName();
    private static final long SHOOT_INTERVAL_MS = 50L;
    // GameObjectManager
    private final GameObjectManager mGameObjectManager = GameObjectManager.getInstance();
    // scheduled executor service
    private ScheduledExecutorService mScheduledExecutorService;
    // Future Task
    private volatile ScheduledFuture<?> mCurentTask;
    // Handler
    private Handler mMainHandler;
    // Engine callback
    private EngineCallback mEngineCallback;
    // total score
    private int mTotalScore;
    private volatile State mCurrentState = State.READY;
    // direction
    private GameObjectManager.Direction mCurrentMovingDirection;
    // check if shooting
    private boolean mIsShooting = false;
    private long mLastShootTime = 0L;


    /**
     * Construct
     *
     * @param engineCallback EngineCallback
     */
    public GameEngine(@NonNull EngineCallback engineCallback) {
        GameUtils.info(TAG, "Construct");
        mEngineCallback = engineCallback;
        this.mMainHandler = new Handler(Looper.getMainLooper());
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        initGame();
    }

    /**
     * init game
     */
    public void initGame() {
        GameUtils.info(TAG, "initGame");
        mTotalScore = 0;
        mGameObjectManager.init();
        updateState(State.READY);
        GameUtils.info(TAG, "Successfully initialized ");
    }

    /**
     * start
     */
    public synchronized void start() {
        GameUtils.info(TAG, "start");
        // early return
        if (mCurentTask != null && !mCurentTask.isCancelled()) {
            GameUtils.info(TAG, "Game is already running");
            return;
        }


        //Start Game
        mCurentTask = mScheduledExecutorService.scheduleWithFixedDelay(
                this::gameLoop,
                1000L,  // delay 1 sec to start for view is ready
                GameConstants.FRAME_PERIOD_MS,
                TimeUnit.MILLISECONDS
        );

        updateState(State.RUNNING);
        GameUtils.info(TAG, "Game started");
    }


    /**
     * pause
     */
    public synchronized void pause() {
        if (mCurrentState == State.RUNNING) {
            stopGameTask();
            updateState(State.PAUSED);
            GameUtils.info(TAG, "Game paused");
        }
    }

    /**
     * resume
     */
    public synchronized void resume() {
        if (mCurrentState == State.PAUSED) {
            start();
            GameUtils.info(TAG, "Game resumed");
        }
    }

    /**
     * stop game task
     */
    public synchronized void stopGameTask() {
        GameUtils.info(TAG, "stop");
        // early return
        if (mCurentTask == null) {
            GameUtils.info(TAG, "Game is not running");
            return;
        }

        if (mCurentTask.isCancelled()) {
            GameUtils.info(TAG, "Game is already stopped");
            return;
        }

        // cancel task
        mCurentTask.cancel(true);
        mCurentTask = null;
        GameUtils.info(TAG, "Game stopped");
    }

    /**
     * clear
     */
    public void clear() {
        GameUtils.info(TAG, "clear");
        // check if game is running
        if (mCurentTask != null && !mCurentTask.isCancelled()) {
            stopGameTask();
            updateState(State.PAUSED);
        }

        mGameObjectManager.clear();

        // shutdown executor service
        mScheduledExecutorService.shutdown();

        try {
            if (!mScheduledExecutorService.awaitTermination(1000, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                // shutdown now
                mScheduledExecutorService.shutdownNow();
                GameUtils.info(TAG, "Game cleared");

            }
        } catch (InterruptedException ie) {
            mScheduledExecutorService.shutdownNow();
            Thread.currentThread().interrupt();
            GameUtils.info(TAG, "Game cleared");
        }

        mScheduledExecutorService = null;
        mCurentTask = null;
        mEngineCallback = null;

        GameUtils.info(TAG, "Game cleared");
    }

    /**
     * set direction
     *
     * @param direction Direction direction
     */
    public void setMoveDirection(GameObjectManager.Direction direction) {
        GameUtils.info(TAG, "setMoveDirection");
        mCurrentMovingDirection = direction;
    }

    /**
     * set shooting
     *
     * @param shooting boolean
     */
    public void setShooting(boolean shooting) {
        GameUtils.info(TAG, "setShooting");
        mIsShooting = shooting;
    }


    /**
     * startNextLevel
     */
    public void startNextLevel() {
        GameUtils.info(TAG, "startNextLevel");
        // early return
        if (mCurrentState == State.RUNNING) {
            GameUtils.info(TAG, "Game is running");
            return;
        }

        mGameObjectManager.nextLevel();
        //mGameObjectManager.loadLevel(nextLevelId);
        start();
    }

    /**
     * getCurrentLevelId
     * @return int
     */
    public int getCurrentLevelId() {
        return mGameObjectManager.getCurrentLevelId();
    }


    /**
     * get metadata title
     * @return String
     */
    public String getMetadataTitle() {
        return mGameObjectManager.getMetadataTitle();
    }

    /**
     * game loop
     */
    private void gameLoop() {
        GameUtils.info(TAG, "gameLoop");
        try {

            // check if game is paused
            if (Thread.currentThread().isInterrupted()) {
                GameUtils.info(TAG, "Game is paused");
                return;
            }

            //early return
            if (mCurrentState == State.GAME_OVER || mCurrentState == State.PAUSED) {
                GameUtils.info(TAG, "Game is over or paused");
                return;
            }

            // update player direction
            if (mCurrentMovingDirection != null && mCurrentState == State.RUNNING) {
                mGameObjectManager.movePlayer(mCurrentMovingDirection);
            }

            // update shooting
            if (mIsShooting && mCurrentState == State.RUNNING) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - mLastShootTime >= SHOOT_INTERVAL_MS) {
                    mGameObjectManager.spawnBullet();
                    mLastShootTime = currentTime;
                }

            }

            updateLogic();
            processCollisions();
            sendFrameToView();

            // check level cleared
            if (mGameObjectManager.isLevelCleared()) {
                // stop game
                stopGameTask();
                updateState(State.CLEARED);
                return;
            }


            // check if player is dead
            if (mGameObjectManager.isPlayerDead()) {
                // stop game
                stopGameTask();
                updateState(State.GAME_OVER);
                return;
            }



        } catch (Exception e) {
            GameUtils.error(TAG, "gameLoop error");
            e.printStackTrace();
        }
    }

    private void updateLogic() {
        //GameUtils.info(TAG, "updateLogic");
        mGameObjectManager.handleAutoFiring();
        mGameObjectManager.updateAll();
        WinningStrategy strategy = mGameObjectManager.getStrategy();
        if (strategy == WinningStrategy.SURVIVAL) {
            long elapsed = System.currentTimeMillis() - mGameObjectManager.getLevelStartTime();
            notifyRemainingTime(elapsed);
        }
    }

    private void processCollisions() {
        //GameUtils.info(TAG, "checkCollisions");
        //(Mark Dead)
        int hitCount = mGameObjectManager.handleCollisions();
        if (hitCount > 0) {
            mTotalScore += hitCount * GameConstants.SCORE_PER_BEE;
            notifyScoreChanged(mTotalScore);
        }

        // Sweep bees
        mGameObjectManager.cleanupEntities();

    }

    private void sendFrameToView() {
        //GameUtils.info(TAG, "sendFrameToView");
        List<GameObject> entities = mGameObjectManager.getAllEntities();
        notifyFrameUpdate(entities);
    }

    private void notifyRemainingTime(long elapsed) {
        final EngineCallback callback = mEngineCallback;
        if (callback != null) mMainHandler.post(() -> callback.onRemainingTime(elapsed));
    }


    private void notifyScoreChanged(int score) {
        final EngineCallback callback = mEngineCallback;
        if (callback != null) mMainHandler.post(() -> callback.onScoreChanged(score));
    }

    private void notifyFrameUpdate(List<GameObject> entities) {
        final EngineCallback callback = mEngineCallback;
        if (callback != null) mMainHandler.post(() -> callback.onFrameUpdate(entities));
    }

    private void updateState(State newState) {
        // early return
        if (mCurrentState == newState) {
            return;
        }

        mCurrentState = newState;
        final EngineCallback callback = mEngineCallback;
        if (callback != null) mMainHandler.post(() -> callback.onGameStateChanged(newState));
    }

    public enum State {
        READY,
        RUNNING,
        PAUSED,
        CLEARED,
        GAME_OVER
    }

    /**
     * EngineCallback interface
     */
    public interface EngineCallback {
        // Trigger score changed when hit bee
        void onScoreChanged(int currentScore);

        // refresh game frame when came object state is changed
        void onFrameUpdate(List<GameObject> entities);

        // Trigger when game state is changed
        void onGameStateChanged(State state);

        void onRemainingTime(long elapsed);
    }

}
