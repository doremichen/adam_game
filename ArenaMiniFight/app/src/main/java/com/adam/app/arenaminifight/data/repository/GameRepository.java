/**
 * Copyright 2023 Adam Chen. All rights reserved.
 * <p>
 * Description: This is the game repository of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/03
 */
package com.adam.app.arenaminifight.data.repository;

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
import com.adam.app.arenaminifight.domain.model.ChatMessage;
import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.service.GameService;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton
 */
public class GameRepository {
    // TAG
    private static final String TAG = "GameRepository";

    private Context mContext;

    private GameRepository(Context context) {
        mContext = context;
    }

    /**
     * getInstance
     */
    public static GameRepository getInstance() {
        return Helper.INSTANCE;
    }

    private static class Helper {
        private static final GameRepository INSTANCE = new GameRepository(GameApplication.Helper.getInstance().get());
    }

    // live data: isBind
    private final MutableLiveData<Boolean> mIsBind = new MutableLiveData<>(false);
    public LiveData<Boolean> isReady() {
        return mIsBind;
    }

    public interface Callback<T> {
        void onResult(T result);
    }

    private Callback<Player> mPlayerCallback;

    private Callback<ChatMessage> mGameServiceCallback;

    private Callback<List<Player>> mGameStateCallback;

    private Messenger mSvrMessenger;
    private Messenger mClientMessenger = new Messenger(new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case GameService.UC_SYNC_CHAT:
                    if (mGameServiceCallback == null) {
                        GameUtil.log(TAG + ": no callback!!!");
                        return;
                    }

                    ChatMessage chatMessage = (ChatMessage) msg.obj;
                    mGameServiceCallback.onResult(chatMessage);
                    break;
                case GameService.UC_NEW_PLAYER:
                    Player player = msg.getData().getParcelable("player_data", Player.class);
                    GameUtil.log(TAG + ": new player: " + player.getName());
                    // callback to game view model
                    if (mPlayerCallback != null) {
                        mPlayerCallback.onResult(player);
                    }
                    break;
                case GameService.UC_GAME_STATE_UPDATE:
                    String stateStr = msg.getData().getString("game_state_str");
                    if (stateStr != null) {
                        List<Player> updatedPlayers = parseStateString(stateStr);
                        if (mGameStateCallback != null) {
                            mGameStateCallback.onResult(updatedPlayers);
                        }
                    }
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

    // service connection
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSvrMessenger = new Messenger(service);
            mIsBind.postValue(true); // here is not main thread state
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSvrMessenger = null;
            mIsBind.postValue(false);
        }
    };

    public void bindGameService() {
        Intent intent = new Intent(mContext, GameService.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindGameService() {
        if (Boolean.TRUE.equals(mIsBind.getValue())) {
            mContext.unbindService(mServiceConnection);
            mIsBind.setValue(false);
        }
    }

    public void sendChat(ChatMessage chat, Callback<ChatMessage> callback) {
        if (Boolean.FALSE.equals(mIsBind.getValue())) {
            throw new IllegalStateException("Service not connected");
        }
        // IPC
        try {
            Message msg = Message.obtain(null, GameService.UC_SEND_CHAT);
            // build data bundle
            Bundle bundle = new Bundle();
            bundle.putParcelable("chat", chat);
            msg.setData(bundle);

            msg.replyTo = mClientMessenger;
            mSvrMessenger.send(msg);
            mGameServiceCallback = callback;
        } catch (RemoteException e) {
            throw new RuntimeException("Send chat failed");
        }
    }

    public void spawnPlayer(String name, Callback<Player> callback) {

        if (Boolean.FALSE.equals(mIsBind.getValue())) {
            throw new IllegalStateException("Service not connected");
        }

        mPlayerCallback = callback;

        // invoke service initial player
        try {
            Message msg = Message.obtain(null, GameService.UC_SPAWN_PLAYER);
            Bundle data = new Bundle();
            data.putString("name", name);
            msg.setData(data);

            msg.replyTo = mClientMessenger;
            mSvrMessenger.send(msg);
        } catch (RemoteException e) {
            throw new RuntimeException("Spawn player failed");
        }
    }

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
            throw new RuntimeException("Start game failed");
        }
    }

    public void stopGame() {
        GameUtil.log(TAG + ": stopGame");
        if (Boolean.FALSE.equals(mIsBind.getValue())) {
            throw new IllegalStateException("Service not connected");
        }

        Message msg = Message.obtain(null, GameService.UC_STOP_GAME);
        try {
            mSvrMessenger.send(msg);
        } catch (RemoteException e) {
            throw new RuntimeException("Stop game failed");
        }
    }



    /**
     * move player
     * @param x
     * @param y
     */
    public void movePlayer(float x, float y) {
        // check service
        if (Boolean.FALSE.equals(mIsBind.getValue())) {
            throw new IllegalStateException("Service not connected");
        }

        try {
            Message msg = Message.obtain(null, GameService.UC_MOVE_PLAYER);
            msg.arg1 = (int) (x*100);
            msg.arg2 = (int) (y*100);
            mSvrMessenger.send(msg);
        } catch (RemoteException e) {
            GameUtil.log(TAG + ": move player failed");
        }
    }

}
