/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.application;

import android.content.Context;
import android.graphics.PointF;

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.domain.entity.Car;
import com.adam.app.racinggame2d.domain.entity.Obstacle;
import com.adam.app.racinggame2d.domain.entity.Player;
import com.adam.app.racinggame2d.domain.entity.Settings;
import com.adam.app.racinggame2d.domain.entity.Track;
import com.adam.app.racinggame2d.util.Constants;
import com.adam.app.racinggame2d.util.SoundPlayer;
import com.adam.app.racinggame2d.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * Application layer service that orchestrates game logic.
 */
public final class GameEngine {
    private static final String sTAG = "GameEngine";

    private final Player mPlayer;
    private final Track mTrack;
    private final SoundPlayer mSoundPlayer;
    private final ScheduledExecutorService mService;
    private Future<?> mUpdateController;
    private volatile boolean mIsRunning;
    private GameUpdateListener mUpdateCallback;
    private OnGameOverListener mGameOverCallback;

    public GameEngine(Context context, SettingsUseCase settingsUseCase, Player player, Track track) {
        this.mPlayer = player;
        this.mTrack = track;
        this.mIsRunning = false;
        this.mService = Executors.newSingleThreadScheduledExecutor();

        Settings settings = settingsUseCase.execute(SettingsUseCase.Action.LOAD_SETTINGS, null);
        boolean isSoundEnable = settings == null || settings.isSoundEnable();
        Settings.GameDifficulty difficulty = settings != null ? settings.getDifficulty() : Settings.GameDifficulty.EASY;

        mPlayer.getCar().applyTo(difficulty);
        mTrack.applyTo(difficulty);
        mSoundPlayer = new SoundPlayer(context, isSoundEnable);
    }

    public void setGameUpdateListener(GameUpdateListener callback) {
        this.mUpdateCallback = callback;
    }

    public void setGameOverListener(OnGameOverListener callback) {
        this.mGameOverCallback = callback;
    }

    public void start() {
        Utils.logDebug(sTAG, "Game started");
        if (mUpdateController != null) {
            mUpdateController.cancel(true);
        }

        mTrack.generateRandomObstacles();
        mIsRunning = true;
        mUpdateController = mService.scheduleWithFixedDelay(
                () -> {
                    if (mIsRunning) updateGameFrame(Constants.DELTA_TIME);
                },
                0,
                Constants.UPDATE_INTERVAL_MS,
                TimeUnit.MICROSECONDS);

        mSoundPlayer.playShortSound(Constants.SOUND_BUTTON, false);
        mSoundPlayer.playBgm(R.raw.background_music, true);
    }

    public void stop() {
        Utils.logDebug(sTAG, "Game stopped");
        mIsRunning = false;
        if (mUpdateController != null) {
            mUpdateController.cancel(true);
        }
        mSoundPlayer.stopBgm();
        mSoundPlayer.playShortSound(Constants.SOUND_BUTTON, false);
    }

    private void updateGameFrame(float deltaTime) {
        Car car = mPlayer.getCar();

        mTrack.update(deltaTime, car.getSpeed());
        car.updateSlip(deltaTime);
        car.updateBoost(deltaTime);
        if (car.updateRock()) {
            handleGameOver();
            return;
        }

        float x = Math.max(Constants.BOUNDARY_VALUE, Math.min(mTrack.getWidth() - Constants.BOUNDARY_VALUE, car.getPosition().x));
        car.setPosition(new PointF(x, car.getPosition().y));

        if (mTrack.checkBoundary(car)) {
            handleGameOver();
            return;
        }

        if (mTrack.checkCollisions(car, () -> {
            mPlayer.addScore(Constants.COLLISION_SCORE);
            mSoundPlayer.playShortSound(Constants.SOUND_COLLISION, false);
        })) {
            handleObstacleEffect(car);
        } else {
            car.unsetRock();
        }

        if (mUpdateCallback != null) {
            mUpdateCallback.onUpdate();
        }
    }

    private void handleObstacleEffect(Car car) {
        Obstacle.Type type = mTrack.getObstacleType();
        ObstacleEffectStrategy.handle(type, car);
    }

    private void handleGameOver() {
        stop();
        if (mGameOverCallback != null) {
            mGameOverCallback.onGameOver();
        }
    }

    public List<PointF> getCheckPoints() {
        return mTrack.getCheckPoints();
    }

    public List<Obstacle> getObstacles() {
        return mTrack.getObstacles();
    }

    public PointF getCarPosition() {
        return mPlayer.getCar().getPosition();
    }

    public int getScore() {
        return mPlayer.getScore();
    }

    public void reset() {
        mPlayer.reset();
        mTrack.reset();
    }

    public void moveHorizontally(boolean isLeft) {
        mPlayer.getCar().moveHorizontally(isLeft);
    }

    public void speedUp(boolean isSpeedUp) {
        mPlayer.getCar().updateSpeed(isSpeedUp);
    }

    public int getCarHP() {
        return mPlayer.getCar().getCarHP();
    }

    public float getCarRotationAngle() {
        return mPlayer.getCar().getRotationAngle();
    }

    private enum ObstacleEffectStrategy {
        OIL {
            @Override
            void applyTo(Car car) {
                car.startSlip();
            }
        },
        ROCK {
            @Override
            void applyTo(Car car) {
                car.startRock();
            }
        },
        BOOST {
            @Override
            void applyTo(Car car) {
                car.startBoost();
            }
        };

        private static final Map<Obstacle.Type, ObstacleEffectStrategy> sMap = new HashMap<>() {{
            put(Obstacle.Type.OIL, OIL);
            put(Obstacle.Type.ROCK, ROCK);
            put(Obstacle.Type.BOOST, BOOST);
        }};

        public static void handle(Obstacle.Type type, Car car) {
            ObstacleEffectStrategy strategy = sMap.get(type);
            if (strategy != null) strategy.applyTo(car);
        }

        abstract void applyTo(Car car);
    }

    public interface GameUpdateListener {
        void onUpdate();
    }

    public interface OnGameOverListener {
        void onGameOver();
    }

    public static final class Factory {
        private final Context mContext;
        private final SettingsUseCase mSettingsUseCase;

        @Inject
        public Factory(@ApplicationContext Context context, SettingsUseCase settingsUseCase) {
            this.mContext = context;
            this.mSettingsUseCase = settingsUseCase;
        }

        public GameEngine create(Player player, Track track) {
            return new GameEngine(mContext, mSettingsUseCase, player, track);
        }
    }
}
