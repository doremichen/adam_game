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

/**
 * GameService bound service handling IPC requests and physics loop.
 * Trace: UC-02, UC-04, UC-05, UC-06, UC-07
 */
public class GameService extends Service {

    public static final int UC_SPAWN_PLAYER = 0;
    public static final int UC_NEW_PLAYER = 1;
    public static final int UC_SEND_CHAT = 2;
    public static final int UC_SYNC_CHAT = 3;
    public static final int UC_MOVE_PLAYER = 4;
    public static final int UC_START_GAME = 5;
    public static final int UC_STOP_GAME = 6;
    public static final int UC_GAME_STATE_UPDATE = 7;

    private static final String TAG = "GameService";
    private final Messenger mIncomingHandler = new Messenger(new SvrHandler());

    @Override
    public void onCreate() {
        super.onCreate();
        GameUtil.log(TAG + ": onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        GameUtil.log(TAG + ": onBind");
        return mIncomingHandler.getBinder();
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

    private static class SvrHandler extends Handler {

        private static final String HANDLER_TAG = "SvrHandler";

        private final NetworkService mNetworkService = NetworkService.getInstance();
        private Messenger mClientMessenger;

        private final NativeEngine mNativeEngine;
        private final EngineLoopManager mEngineLoopManager;
        private long mLastFrameTime;

        private final List<Messenger> mClientMessengerList = new ArrayList<>();

        public SvrHandler() {
            super(Looper.getMainLooper());
            mNativeEngine = NativeEngine.getInstance();
            mEngineLoopManager = new EngineLoopManager(this::loopTask);
        }

        // Trace: UC-06 (Native Physics Loop)
        private void loopTask() {
            try {
                long currentTime = System.currentTimeMillis();
                if (mLastFrameTime == 0) {
                    mLastFrameTime = currentTime;
                }

                float deltaTime = (currentTime - mLastFrameTime) / 1000f;
                mLastFrameTime = currentTime;

                mNativeEngine.updateEngine(deltaTime);
                broadcastGameState();
            } catch (Exception e) {
                GameUtil.log(TAG + ": loop exception: " + e.getMessage());
            }
        }

        // Trace: UC-06 (State Synchronization)
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

            for (int i = mClientMessengerList.size() - 1; i >= 0; i--) {
                Messenger messenger = mClientMessengerList.get(i);
                try {
                    messenger.send(msg);
                } catch (RemoteException e) {
                    mClientMessengerList.remove(i);
                }
            }
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            GameUtil.log(HANDLER_TAG + ": handleMessage");

            switch (msg.what) {
                case UC_SPAWN_PLAYER: // Trace: UC-04
                    if (!mClientMessengerList.contains(msg.replyTo)) {
                        mClientMessengerList.add(msg.replyTo);
                    }

                    String name = msg.getData().getString("name");
                    Player player = mNativeEngine.nativeInitializePlayer(name);
                    Message reply = Message.obtain(null, UC_NEW_PLAYER);
                    Bundle data = new Bundle();
                    data.putParcelable("player_data", player);
                    reply.setData(data);
                    try {
                        msg.replyTo.send(reply);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case UC_SEND_CHAT: // Trace: UC-02
                    mClientMessenger = msg.replyTo;
                    ChatMessage chat = msg.getData().getParcelable("chat", ChatMessage.class);
                    if (chat == null) {
                        GameUtil.log(HANDLER_TAG + ": no chat!!!");
                        return;
                    }
                    mNetworkService.sentChatToServer(chat, this::onMessageReceived);
                    break;
                case UC_MOVE_PLAYER: // Trace: UC-05
                    Bundle moveData = msg.getData();
                    String id = moveData.getString("id");
                    float targetX = moveData.getFloat("x");
                    float targetY = moveData.getFloat("y");
                    mNativeEngine.updatePlayerPosition(id, targetX, targetY);
                    break;
                case UC_START_GAME: // Trace: UC-06
                    mEngineLoopManager.start();
                    break;
                case UC_STOP_GAME: // Trace: UC-07
                    mEngineLoopManager.stop();
                    mNativeEngine.resetEngine();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

        // Trace: UC-02
        private void onMessageReceived(ChatMessage chat) {
            if (mClientMessenger == null) {
                GameUtil.log(HANDLER_TAG + ": no client messenger!!!");
                return;
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
