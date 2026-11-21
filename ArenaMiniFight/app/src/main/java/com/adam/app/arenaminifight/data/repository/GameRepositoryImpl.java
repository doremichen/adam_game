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

package com.adam.app.arenaminifight.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import com.adam.app.arenaminifight.data.gateway.GameGatewayImpl;
import com.adam.app.arenaminifight.data.local.AppDatabase;
import com.adam.app.arenaminifight.data.local.ScoreEntity;
import com.adam.app.arenaminifight.domain.gateway.IGameGateway;
import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.domain.model.ScoreRecord;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Concrete implementation of GameRepository coordinating Gateway (IPC/JNI), Room DB, and SharedPreferences.
 * Trace: UC-GAME-01 ~ UC-GAME-12, UC-MENU-03, UC-MENU-04, REQ-NFR-04
 */
@Singleton
public class GameRepositoryImpl {

    private static final String TAG = GameRepositoryImpl.class.getSimpleName();
    private static final String PREF_SOUND_VOLUME = "pref_sound_volume";

    private final IGameGateway mGameGateway;
    private final AppDatabase mAppDatabase;
    private final SharedPreferences mPreferences;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    @Inject
    public GameRepositoryImpl(Context context) {
        mGameGateway = GameGatewayImpl.getInstance();
        mAppDatabase = AppDatabase.getInstance(context);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public LiveData<Boolean> isServiceReady() {
        return mGameGateway.isReady();
    }

    public void bindService() {
        GameUtil.log(TAG + ": bindService");
        mGameGateway.bindGameService();
    }

    public void unbindService() {
        GameUtil.log(TAG + ": unbindService");
        mGameGateway.unbindGameService();
    }

    public void spawnPlayer(String name, IGameGateway.Callback<Player> callback) {
        mGameGateway.spawnPlayer(name, callback);
    }

    public void startGame(IGameGateway.Callback<List<Player>> callback) {
        mGameGateway.startGame(callback);
    }

    public void stopGame() {
        mGameGateway.stopGame();
    }

    // Trace: UC-GAME-02 (Virtual D-pad directional movement)
    public void movePlayer(String playerId, float dx, float dy) {
        mGameGateway.movePlayer(playerId, dx, dy);
    }

    public void stopPlayerMove(String playerId) {
        mGameGateway.stopPlayerMove(playerId);
    }

    // Trace: UC-GAME-03, UC-GAME-04
    public void playerAttack(String playerId, boolean isSkill, IGameGateway.Callback<String> callback) {
        mGameGateway.playerAttack(playerId, isSkill, callback);
    }

    // Trace: UC-MENU-03 (Setting persistence)
    public float getSoundVolume() {
        return mPreferences.getFloat(PREF_SOUND_VOLUME, 1.0f);
    }

    public void saveSoundVolume(float volume) {
        mPreferences.edit().putFloat(PREF_SOUND_VOLUME, volume).apply();
    }

    // Trace: UC-MENU-04, UC-GAME-12 (Leaderboard persistence)
    public void saveScore(ScoreRecord record) {
        mExecutor.execute(() -> {
            try {
                ScoreEntity entity = new ScoreEntity(
                        record.getPlayerName(),
                        record.getSurvivalTime(),
                        record.getKillCount(),
                        record.getLevel(),
                        record.getTimestamp()
                );
                mAppDatabase.scoreDao().insertScore(entity);
                GameUtil.log(TAG + ": Score saved successfully.");
            } catch (Exception e) {
                GameUtil.log(TAG + ": Error saving score: " + e.getMessage());
            }
        });
    }

    public void getTopScores(IGameGateway.Callback<List<ScoreRecord>> callback) {
        mExecutor.execute(() -> {
            try {
                List<ScoreEntity> entities = mAppDatabase.scoreDao().getTopScores();
                List<ScoreRecord> records = new ArrayList<>();
                for (ScoreEntity entity : entities) {
                    records.add(new ScoreRecord(
                            entity.getId(),
                            entity.getPlayerName(),
                            entity.getSurvivalTime(),
                            entity.getKillCount(),
                            entity.getLevel(),
                            entity.getTimestamp()
                    ));
                }
                if (callback != null) {
                    callback.onResult(records);
                }
            } catch (Exception e) {
                GameUtil.log(TAG + ": Error fetching scores: " + e.getMessage());
                if (callback != null) {
                    callback.onResult(new ArrayList<>());
                }
            }
        });
    }
}
