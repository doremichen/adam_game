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
import com.adam.app.arenaminifight.utils.GameUtil;

public class GameService extends Service {
    // request
    public static final int UC_SPAWN_PLAYER = 0;
    public static final int UC_NEW_PLAYER = 1;
    public static final int UC_SEND_CHAT = 2;
    public static final int UC_SYNC_CHAT = 3;
    public static final int UC_MOVE_PLAYER = 4;
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
     *   Handle the request from client
     */
    private static class SvrHandler extends Handler {
        // HANDLER_TAG
        private static final String HANDLER_TAG = "SvrHandler";

        // Network service
        private final NetworkService mNetworkService = NetworkService.getInstance();
        private Messenger mClientMessenger;

        // native engine
        private NativeEngine mNativeEngine;


        public SvrHandler() {
            super(Looper.getMainLooper());
            mNativeEngine = NativeEngine.getInstance();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            GameUtil.log(HANDLER_TAG + ": handleMessage");

//            boolean result = GameActionType.executeFrom(msg.what, msg);
//            if (!result) {
//                super.handleMessage(msg);
//            }

            switch (msg.what) {
                case UC_SPAWN_PLAYER:
                    String name = msg.getData().getString("name");
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
                    // position
                    float x = msg.arg1 / 100f;
                    float y = msg.arg2 / 100f;
                    // Call JNI Engine for collision detection and location update
                    mNativeEngine.updatePlayerPosition("LOCAL_PLAYER", x, y);
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