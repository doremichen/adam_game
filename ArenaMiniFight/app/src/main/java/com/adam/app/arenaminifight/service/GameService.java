/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the game bound service of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/03
 */
package com.adam.app.arenaminifight.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.adam.app.arenaminifight.domain.model.ChatMessage;
import com.adam.app.arenaminifight.utils.GameUtil;

public class GameService extends Service {
    // TAG
    private static final String TAG = "GameService";

    // request
    public static final int UC_SEND_CHAT = 0;
    public static final int UC_SYNC_CHAT = 1;
    public static final int UC_MOVE_PLAYER = 2;


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

        public SvrHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            GameUtil.log(HANDLER_TAG + ": handleMessage");
            switch (msg.what) {
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
                    // TODO: 執行 UC-05: 同步座標
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