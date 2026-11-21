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

package com.adam.app.arenaminifight.data.gateway;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PointF;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.arenaminifight.GameApplication;
import com.adam.app.arenaminifight.domain.gateway.IGameGateway;
import com.adam.app.arenaminifight.domain.model.ChatMessage;
import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.ipc.IGameService;
import com.adam.app.arenaminifight.ipc.IGameStateListener;
import com.adam.app.arenaminifight.service.GameService;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of IGameGateway using AIDL Binder IPC to communicate with GameService.
 * Trace: REQ-IPC-01, REQ-IPC-02, REQ-IPC-03
 */
public class GameGatewayImpl implements IGameGateway {

    private static final String TAG = "GameGatewayImpl";

    private final Context mContext;
    private final MutableLiveData<Boolean> mIsBind = new MutableLiveData<>(false);
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());

    private Callback<Player> mSpawnCallback;
    private Callback<ChatMessage> mChatCallback;
    private Callback<List<Player>> mGameStateCallback;
    private Callback<String> mAttackCallback;
    private Callback<String> mGameOverCallback;

    private IGameService mGameService;
    private final List<Runnable> mPendingActions = new ArrayList<>();

    private final IGameStateListener.Stub mStateListener = new IGameStateListener.Stub() {
        @Override
        public void onGameStateUpdated(String gameStateStr) throws RemoteException {
            if (mGameStateCallback != null && gameStateStr != null) {
                List<Player> updatedPlayers = parseStateString(gameStateStr);
                mMainHandler.post(() -> mGameStateCallback.onResult(updatedPlayers));
            }
        }

        @Override
        public void onAttackResult(String result) throws RemoteException {
            if (mAttackCallback != null) {
                mMainHandler.post(() -> mAttackCallback.onResult(result));
            }
        }

        @Override
        public void onGameOver(String summary) throws RemoteException {
            GameUtil.log(TAG + ": Game Over received via AIDL: " + summary);
            if (mGameOverCallback != null && summary != null) {
                mMainHandler.post(() -> mGameOverCallback.onResult(summary));
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GameUtil.log("[StartGameFlow][Step 5] GameGatewayImpl: onServiceConnected() -> Service bound via AIDL, flushing pending actions");
            mGameService = IGameService.Stub.asInterface(service);
            try {
                mGameService.registerListener(mStateListener);
            } catch (RemoteException e) {
                GameUtil.log(TAG + ": Error registering AIDL listener: " + e.getMessage());
            }
            mIsBind.postValue(true);
            for (Runnable action : new ArrayList<>(mPendingActions)) {
                try {
                    action.run();
                } catch (Exception e) {
                    GameUtil.log(TAG + ": Error executing pending action: " + e.getMessage());
                }
            }
            mPendingActions.clear();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            GameUtil.log(TAG + ": Service disconnected");
            mGameService = null;
            mIsBind.postValue(false);
        }
    };

    private GameGatewayImpl(Context context) {
        mContext = context;
    }

    public static GameGatewayImpl getInstance() {
        return Helper.sInstance;
    }

    private static class Helper {
        private static final GameGatewayImpl sInstance =
                new GameGatewayImpl(GameApplication.Helper.getInstance().get());
    }

    @Override
    public LiveData<Boolean> isReady() {
        return mIsBind;
    }

    // Trace: REQ-GAME-01
    @Override
    public void bindGameService() {
        GameUtil.log("[StartGameFlow][Step 5] GameGatewayImpl: bindGameService() -> Binding to GameService");
        if (Boolean.TRUE.equals(mIsBind.getValue()) && mGameService != null) {
            return;
        }
        Intent intent = new Intent(mContext, GameService.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindGameService() {
        if (Boolean.TRUE.equals(mIsBind.getValue()) && mGameService != null) {
            try {
                mGameService.unregisterListener(mStateListener);
                mContext.unbindService(mServiceConnection);
            } catch (Exception ignored) {
            }
            mIsBind.setValue(false);
            mGameService = null;
        }
    }

    private void executeOrQueue(Runnable action) {
        if (Boolean.TRUE.equals(mIsBind.getValue()) && mGameService != null) {
            try {
                action.run();
            } catch (Exception e) {
                GameUtil.log(TAG + ": Action execution failed: " + e.getMessage());
            }
        } else {
            mPendingActions.add(action);
            bindGameService();
        }
    }

    private List<Player> parseStateString(String stateStr) {
        List<Player> list = new ArrayList<>();
        String[] players = stateStr.split("\\|");
        for (String p : players) {
            String[] attrs = p.split(":");
            if (attrs.length >= 5) {
                int attackType = attrs.length >= 6 ? Integer.parseInt(attrs[5]) : 0;
                list.add(new Player(attrs[1], attrs[0],
                        new PointF(Float.parseFloat(attrs[2]), Float.parseFloat(attrs[3])),
                        0f, Integer.parseInt(attrs[4]), attackType));
            }
        }
        return list;
    }

    @Override
    public void sendChat(ChatMessage chat, Callback<ChatMessage> callback) {
        mChatCallback = callback;
        if (callback != null && chat != null) {
            mMainHandler.post(() -> callback.onResult(chat));
        }
    }

    // Trace: REQ-GAME-01
    @Override
    public void spawnPlayer(String name, Callback<Player> callback) {
        GameUtil.log("[StartGameFlow][Step 5] GameGatewayImpl: spawnPlayer(name=" + name + ") -> Invoking AIDL mGameService.spawnPlayer()");
        mSpawnCallback = callback;
        executeOrQueue(() -> {
            try {
                Player player = mGameService.spawnPlayer(name);
                if (callback != null && player != null) {
                    mMainHandler.post(() -> callback.onResult(player));
                }
            } catch (RemoteException e) {
                GameUtil.log(TAG + ": spawnPlayer AIDL failed: " + e.getMessage());
            }
        });
    }

    // Trace: REQ-GAME-01, REQ-IPC-01
    @Override
    public void startGame(Callback<List<Player>> callback) {
        GameUtil.log("[StartGameFlow][Step 5] GameGatewayImpl: startGame() -> Invoking AIDL mGameService.startGame()");
        mGameStateCallback = callback;
        executeOrQueue(() -> {
            try {
                mGameService.startGame();
            } catch (RemoteException e) {
                GameUtil.log(TAG + ": startGame AIDL failed: " + e.getMessage());
            }
        });
    }

    @Override
    public void stopGame() {
        GameUtil.log(TAG + ": stopGame via AIDL");
        executeOrQueue(() -> {
            try {
                mGameService.stopGame();
            } catch (RemoteException e) {
                GameUtil.log(TAG + ": stopGame AIDL failed: " + e.getMessage());
            }
        });
    }

    // Trace: REQ-IPC-03, REQ-GAME-02
    @Override
    public void movePlayer(String playerId, float x, float y) {
        executeOrQueue(() -> {
            try {
                mGameService.movePlayer(playerId, x, y);
            } catch (RemoteException e) {
                GameUtil.log(TAG + ": movePlayer AIDL failed: " + e.getMessage());
            }
        });
    }

    // Trace: REQ-IPC-03, REQ-GAME-02
    @Override
    public void stopPlayerMove(String playerId) {
        executeOrQueue(() -> {
            try {
                mGameService.stopPlayerMove(playerId);
            } catch (RemoteException e) {
                GameUtil.log(TAG + ": stopPlayerMove AIDL failed: " + e.getMessage());
            }
        });
    }

    // Trace: REQ-IPC-03, REQ-GAME-03
    @Override
    public void playerAttack(String playerId, boolean isSkill, Callback<String> callback) {
        mAttackCallback = callback;
        executeOrQueue(() -> {
            try {
                mGameService.playerAttack(playerId, isSkill);
            } catch (RemoteException e) {
                GameUtil.log(TAG + ": playerAttack AIDL failed: " + e.getMessage());
            }
        });
    }

    @Override
    public void setGameOverCallback(Callback<String> callback) {
        mGameOverCallback = callback;
    }
}
