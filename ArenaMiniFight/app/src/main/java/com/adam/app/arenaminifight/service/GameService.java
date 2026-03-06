/**
 * Copyright 2023 Adam Chen. All rights reserved.
 * <p>
 * Description: This is the game bound service of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/03
 */
package com.adam.app.arenaminifight.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.adam.app.arenaminifight.data.service.NativeEngine;
import com.adam.app.arenaminifight.domain.model.ChatMessage;
import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.service.engine.EngineLoopManager;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

public class GameService extends Service {
    // request
    public static final int UC_SPAWN_PLAYER = 0;
    public static final int UC_NEW_PLAYER = 1;
    public static final int UC_SEND_CHAT = 2;
    public static final int UC_SYNC_CHAT = 3;
    public static final int UC_MOVE_PLAYER = 4;
    public static final int UC_START_GAME = 5;
    public static final int UC_STOP_GAME = 6;
    public static final int UC_GAME_STATE_UPDATE = 7;



    // TAG
    private static final String TAG = "GameService";
    private final Messenger mInComingHandler = new Messenger(new SvrHandler());

    @Override
    public void onCreate() {
        super.onCreate();
        GameUtil.log(TAG + ": onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        GameUtil.log(TAG + ": onBind");
        return mInComingHandler.getBinder();
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
    }

    /**
     * inComing handler
     * Handle the request from client
     */
    private static class SvrHandler extends Handler {
        // HANDLER_TAG
        private static final String HANDLER_TAG = "SvrHandler";

        // Network service
        private final NetworkService mNetworkService = NetworkService.getInstance();
        private Messenger mClientMessenger;

        // native engine
        private NativeEngine mNativeEngine;
        // engine loop manager
        private EngineLoopManager mEngineLoopManager;
        // last frame time
        private long mLastFrameTime;

        // list of client messenger
        List<Messenger> mClientMessengerList = new ArrayList<>();

        public SvrHandler() {
            super(Looper.getMainLooper());
            mNativeEngine = NativeEngine.getInstance();
            // initial engine loop manager
            mEngineLoopManager = new EngineLoopManager(this::loopTask);
        }

        private void loopTask() {
            //GameUtil.log(TAG + ": run game task!!!");
            try {
                long currentTime = System.currentTimeMillis();
                if (mLastFrameTime == 0) {
                    mLastFrameTime = currentTime;
                }

                float deltaTime = (currentTime - mLastFrameTime) / 1000f;
                mLastFrameTime = currentTime;

                // update engine
                mNativeEngine.updateEngine(deltaTime);

                // broadcast client
                broadcastGameState();
            } catch (Exception e) {
                GameUtil.log(TAG + "loop exception!!!");
            }

        }

        private void broadcastGameState() {
            String state = mNativeEngine.getGameState();
            if (state == null || state.isEmpty()) {
                GameUtil.log(HANDLER_TAG + ": no state!!!");
                return;
            }

            Message msg = Message.obtain(null, GameService.UC_GAME_STATE_UPDATE);
            Bundle data = new Bundle();
            data.putString("game_state_str", state);
            msg.setData(data);

            // broadcast all client
            for (Messenger messenger : mClientMessengerList) {
                try {
                    messenger.send(msg);
                } catch (RemoteException e) {
                    mClientMessengerList.remove(messenger); // 移除失效的連線
                }
            }

        }


        @Override
        public void handleMessage(@NonNull Message msg) {
            GameUtil.log(HANDLER_TAG + ": handleMessage");

            switch (msg.what) {
                case UC_SPAWN_PLAYER:
                    // update client messenger list
                    if (!mClientMessengerList.contains(msg.replyTo))
                        mClientMessengerList.add(msg.replyTo);

                    String name = msg.getData().getString("name");
                    // JNI
                    Player player = mNativeEngine.nativeInitializePlayer(name);
                    // reply to repository
                    Message reply = Message.obtain(null, UC_NEW_PLAYER);
                    // build bundle
                    Bundle data = new Bundle();
                    data.putParcelable("player_data", player);
                    reply.setData(data);
                    try {
                        msg.replyTo.send(reply);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case UC_SEND_CHAT:
                    mClientMessenger = msg.replyTo;
                    // get data from bundle
                    ChatMessage chat = msg.getData().getParcelable("chat", ChatMessage.class);
                    if (chat == null) {
                        GameUtil.log(HANDLER_TAG + ": no chat!!!");
                        return;
                    }
                    mNetworkService.sentChatToServer(chat, this::onMessageReceived);
                    break;
                case UC_MOVE_PLAYER:
                    Bundle moveData = msg.getData();
                    String id = moveData.getString("id");
                    float targetX = moveData.getFloat("x");
                    float targetY = moveData.getFloat("y");
                    // Call JNI Engine for collision detection and location update
                    mNativeEngine.updatePlayerPosition(id, targetX, targetY);
                    break;
                case UC_START_GAME:
                    mEngineLoopManager.start();
                    break;
                case UC_STOP_GAME:
                    mEngineLoopManager.stop();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

        private void onMessageReceived(ChatMessage chat) {
            if (mClientMessenger == null) {
                GameUtil.log(HANDLER_TAG + ": no client messenger!!!");
            }

            try {
                Message reply = Message.obtain(null, UC_SYNC_CHAT, chat);
                reply.obj = chat;
                mClientMessenger.send(reply);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        }
    }


}