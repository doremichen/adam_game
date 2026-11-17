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
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.arenaminifight.GameApplication;
import com.adam.app.arenaminifight.domain.gateway.IGameGateway;
import com.adam.app.arenaminifight.domain.model.ChatMessage;
import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.service.GameService;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of IGameGateway using IPC Messenger to communicate with GameService.
 * Trace: UC-01, UC-02, UC-04, UC-05, UC-06, UC-07
 */
public class GameGatewayImpl implements IGameGateway {

    private static final String TAG = "GameGatewayImpl";

    private final Context mContext;
    private final MutableLiveData<Boolean> mIsBind = new MutableLiveData<>(false);

    private Callback<Player> mSpawnCallback;
    private Callback<ChatMessage> mChatCallback;
    private Callback<List<Player>> mGameStateCallback;

    private Messenger mSvrMessenger;
    private final Messenger mClientMessenger = new Messenger(new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case GameService.UC_SYNC_CHAT: // Trace: UC-02
                    if (mChatCallback == null) {
                        GameUtil.log(TAG + ": no callback!!!");
                        return;
                    }

                    ChatMessage chatMessage = (ChatMessage) msg.obj;
                    mChatCallback.onResult(chatMessage);
                    break;
                case GameService.UC_NEW_PLAYER: // Trace: UC-04
                    if (mSpawnCallback == null) {
                        GameUtil.log(TAG + ": no callback!!!");
                        return;
                    }
                    Player player = msg.getData().getParcelable("player_data", Player.class);
                    if (player == null) {
                        GameUtil.log(TAG + ": no player!!!");
                        return;
                    }
                    GameUtil.log(TAG + ": new player: " + player.getName());
                    mSpawnCallback.onResult(player);
                    break;
                case GameService.UC_GAME_STATE_UPDATE: // Trace: UC-06
                    if (mGameStateCallback == null) {
                        GameUtil.log(TAG + ": no callback!!!");
                        return;
                    }
                    String stateStr = msg.getData().getString("game_state_str");
                    if (stateStr == null) {
                        GameUtil.log(TAG + ": no state!!!");
                        return;
                    }
                    List<Player> updatedPlayers = parseStateString(stateStr);
                    mGameStateCallback.onResult(updatedPlayers);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

        private List<Player> parseStateString(String stateStr) {
            List<Player> list = new ArrayList<>();
            String[] players = stateStr.split("\\|");
            for (String p : players) {
                String[] attrs = p.split(":");
                if (attrs.length >= 5) {
                    list.add(new Player(attrs[1], attrs[0],
                            new PointF(Float.parseFloat(attrs[2]), Float.parseFloat(attrs[3])),
                            0f, Integer.parseInt(attrs[4])));
                }
            }
            return list;
        }
    });

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSvrMessenger = new Messenger(service);
            mIsBind.postValue(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSvrMessenger = null;
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

    // Trace: UC-01
    @Override
    public void bindGameService() {
        Intent intent = new Intent(mContext, GameService.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindGameService() {
        if (Boolean.TRUE.equals(mIsBind.getValue())) {
            mContext.unbindService(mServiceConnection);
            mIsBind.setValue(false);
        }
    }

    // Trace: UC-02
    @Override
    public void sendChat(ChatMessage chat, Callback<ChatMessage> callback) {
        if (Boolean.FALSE.equals(mIsBind.getValue())) {
            throw new IllegalStateException("Service not connected");
        }
        try {
            Message msg = Message.obtain(null, GameService.UC_SEND_CHAT);
            Bundle bundle = new Bundle();
            bundle.putParcelable("chat", chat);
            msg.setData(bundle);

            msg.replyTo = mClientMessenger;
            mSvrMessenger.send(msg);
            mChatCallback = callback;
        } catch (RemoteException e) {
            throw new RuntimeException("Send chat failed", e);
        }
    }

    // Trace: UC-04
    @Override
    public void spawnPlayer(String name, Callback<Player> callback) {
        if (Boolean.FALSE.equals(mIsBind.getValue())) {
            throw new IllegalStateException("Service not connected");
        }

        mSpawnCallback = callback;

        try {
            Message msg = Message.obtain(null, GameService.UC_SPAWN_PLAYER);
            Bundle data = new Bundle();
            data.putString("name", name);
            msg.setData(data);

            msg.replyTo = mClientMessenger;
            mSvrMessenger.send(msg);
        } catch (RemoteException e) {
            throw new RuntimeException("Spawn player failed", e);
        }
    }

    // Trace: UC-06
    @Override
    public void startGame(Callback<List<Player>> callback) {
        GameUtil.log(TAG + ": startGame");
        if (Boolean.FALSE.equals(mIsBind.getValue())) {
            throw new IllegalStateException("Service not connected");
        }

        Message msg = Message.obtain(null, GameService.UC_START_GAME);
        try {
            mSvrMessenger.send(msg);
            mGameStateCallback = callback;
        } catch (RemoteException e) {
            throw new RuntimeException("Start game failed", e);
        }
    }

    // Trace: UC-07
    @Override
    public void stopGame() {
        GameUtil.log(TAG + ": stopGame");
        if (Boolean.FALSE.equals(mIsBind.getValue())) {
            throw new IllegalStateException("Service not connected");
        }

        Message msg = Message.obtain(null, GameService.UC_STOP_GAME);
        try {
            mSvrMessenger.send(msg);
        } catch (RemoteException e) {
            throw new RuntimeException("Stop game failed", e);
        }
    }

    // Trace: UC-05
    @Override
    public void movePlayer(String playerId, float x, float y) {
        if (Boolean.FALSE.equals(mIsBind.getValue())) {
            throw new IllegalStateException("Service not connected");
        }

        try {
            Message msg = Message.obtain(null, GameService.UC_MOVE_PLAYER);
            Bundle data = new Bundle();
            data.putString("id", playerId);
            data.putFloat("x", x);
            data.putFloat("y", y);
            msg.setData(data);
            mSvrMessenger.send(msg);
        } catch (RemoteException e) {
            GameUtil.log(TAG + ": move player failed");
        }
    }
}
