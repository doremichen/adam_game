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

package com.adam.app.arenaminifight.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.adam.app.arenaminifight.data.service.NativeEngine;
import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.ipc.IGameService;
import com.adam.app.arenaminifight.ipc.IGameStateListener;
import com.adam.app.arenaminifight.service.engine.EngineLoopManager;
import com.adam.app.arenaminifight.utils.GameUtil;

/**
 * GameService remote bound service handling IPC requests and physics loop via AIDL.
 * Trace: REQ-IPC-01, REQ-IPC-02, REQ-IPC-03, REQ-GAME-01, REQ-GAME-05, REQ-GAME-10, REQ-GAME-11, REQ-GAME-12
 */
public class GameService extends Service {

    private static final String TAG = "GameService";
    private static final long AI_SPAWN_INTERVAL_MS = 10000L; // 10 seconds (REQ-GAME-10)

    private final NativeEngine mNativeEngine = NativeEngine.getInstance();
    private final RemoteCallbackList<IGameStateListener> mCallbackList = new RemoteCallbackList<>();
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());

    private EngineLoopManager mEngineLoopManager;
    private long mLastFrameTime;
    private int mAiBotCount = 0;
    private boolean mIsGameRunning = false;

    private final Runnable mAiSpawnRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsGameRunning) {
                spawnEnemyInternal();
                mMainHandler.postDelayed(this, AI_SPAWN_INTERVAL_MS);
            }
        }
    };

    private void spawnEnemyInternal() {
        mAiBotCount++;
        String botName = "AI_Bot_" + mAiBotCount;
        GameUtil.log(TAG + ": Spawning auto AI enemy: " + botName);
        mNativeEngine.nativeInitializePlayer(botName);
    }

    private final IGameService.Stub mBinder = new IGameService.Stub() {

        @Override
        public void registerListener(IGameStateListener listener) throws RemoteException {
            if (listener != null) {
                mCallbackList.register(listener);
                GameUtil.log(TAG + ": Registered AIDL listener");
            }
        }

        @Override
        public void unregisterListener(IGameStateListener listener) throws RemoteException {
            if (listener != null) {
                mCallbackList.unregister(listener);
                GameUtil.log(TAG + ": Unregistered AIDL listener");
            }
        }

        @Override
        public Player spawnPlayer(String name) throws RemoteException {
            GameUtil.log("[StartGameFlow][Step 6] GameService: AIDL spawnPlayer(name=" + name + ") -> Calling NativeEngine.nativeInitializePlayer()");
            Player player = mNativeEngine.nativeInitializePlayer(name);
            GameUtil.log("[StartGameFlow][Step 6] GameService: Spawned player ID: " + (player != null ? player.getPlayerId() : "null"));
            return player;
        }

        @Override
        public void startGame() throws RemoteException {
            GameUtil.log("[StartGameFlow][Step 6] GameService: AIDL startGame() -> Starting EngineLoopManager and AI 10s spawn timer");
            mIsGameRunning = true;
            mLastFrameTime = System.currentTimeMillis();
            if (mEngineLoopManager != null) {
                mEngineLoopManager.start();
            }
            mMainHandler.removeCallbacks(mAiSpawnRunnable);
            mMainHandler.postDelayed(mAiSpawnRunnable, AI_SPAWN_INTERVAL_MS);
        }

        @Override
        public void stopGame() throws RemoteException {
            GameUtil.log(TAG + ": AIDL stopGame");
            mIsGameRunning = false;
            mMainHandler.removeCallbacks(mAiSpawnRunnable);
            if (mEngineLoopManager != null) {
                mEngineLoopManager.stop();
            }
            mNativeEngine.resetEngine();
        }

        @Override
        public void movePlayer(String playerId, float x, float y) throws RemoteException {
            mNativeEngine.updatePlayerPosition(playerId, x, y);
        }

        @Override
        public void stopPlayerMove(String playerId) throws RemoteException {
            if (playerId != null) {
                mNativeEngine.nativeStopPlayerMove(playerId);
            }
        }

        @Override
        public void playerAttack(String playerId, boolean isSkill) throws RemoteException {
            String result = mNativeEngine.nativePlayerAttack(playerId, isSkill);
            notifyAttackResult(result);
        }

        @Override
        public String getGameState() throws RemoteException {
            return mNativeEngine.getGameState();
        }

        @Override
        public void spawnEnemy() {
            spawnEnemyInternal();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        GameUtil.log(TAG + ": onCreate");
        mNativeEngine.resetEngine(); // Ensure clean slate on service creation
        mEngineLoopManager = new EngineLoopManager(this::loopTask);
    }

    @Override
    public IBinder onBind(Intent intent) {
        GameUtil.log(TAG + ": onBind");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        GameUtil.log(TAG + ": onStartCommand");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GameUtil.log(TAG + ": onDestroy");
        mIsGameRunning = false;
        mMainHandler.removeCallbacks(mAiSpawnRunnable);
        if (mEngineLoopManager != null) {
            mEngineLoopManager.stop();
        }
        mCallbackList.kill();
        mNativeEngine.resetEngine();
    }

    // Trace: REQ-NFR-01 (60 FPS Native Physics Loop)
    private void loopTask() {
        try {
            long currentTime = System.currentTimeMillis();
            if (mLastFrameTime == 0) {
                mLastFrameTime = currentTime;
            }

            float deltaTime = (currentTime - mLastFrameTime) / 1000f;
            mLastFrameTime = currentTime;

            mNativeEngine.updateEngine(deltaTime);
            String state = mNativeEngine.getGameState();
            if (state != null && !state.isEmpty()) {
                if (isPlayerDead(state)) {
                    mIsGameRunning = false;
                    if (mEngineLoopManager != null) {
                        mEngineLoopManager.stop();
                    }
                    notifyGameOver("Game Over! You were defeated in battle.");
                    return;
                }
                broadcastGameState(state);
            }
        } catch (Exception e) {
            GameUtil.log(TAG + ": loop exception: " + e.getMessage());
        }
    }

    private boolean isPlayerDead(String stateStr) {
        String[] players = stateStr.split("\\|");
        for (String p : players) {
            String[] attrs = p.split(":");
            if (attrs.length >= 5 && "P_0".equals(attrs[0])) {
                try {
                    int hp = Integer.parseInt(attrs[4]);
                    return hp <= 0;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return false;
    }

    private void notifyGameOver(String summary) {
        int count = mCallbackList.beginBroadcast();
        for (int i = 0; i < count; i++) {
            try {
                mCallbackList.getBroadcastItem(i).onGameOver(summary);
            } catch (RemoteException e) {
                GameUtil.log(TAG + ": RemoteException notifying game over: " + e.getMessage());
            }
        }
        mCallbackList.finishBroadcast();
    }

    // Trace: REQ-IPC-02 (State Synchronization over Binder)
    private void broadcastGameState(String state) {
        int count = mCallbackList.beginBroadcast();
        for (int i = 0; i < count; i++) {
            try {
                mCallbackList.getBroadcastItem(i).onGameStateUpdated(state);
            } catch (RemoteException e) {
                GameUtil.log(TAG + ": RemoteException broadcasting state: " + e.getMessage());
            }
        }
        mCallbackList.finishBroadcast();
    }

    private void notifyAttackResult(String result) {
        if (result == null) return;
        int count = mCallbackList.beginBroadcast();
        for (int i = 0; i < count; i++) {
            try {
                mCallbackList.getBroadcastItem(i).onAttackResult(result);
            } catch (RemoteException e) {
                GameUtil.log(TAG + ": RemoteException notifying attack result: " + e.getMessage());
            }
        }
        mCallbackList.finishBroadcast();
    }
}
