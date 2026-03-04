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
import com.adam.app.arenaminifight.service.GameService;
import com.adam.app.arenaminifight.utils.GameUtil;

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

    public interface GameServiceCallback {
        void onMessageReceived(ChatMessage chatMessage);
    }

    private GameServiceCallback mGameServiceCallback;

    private Messenger mSvrMessenger;
    private Messenger mClientMessenger = new Messenger(new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == GameService.UC_SYNC_CHAT) {
                if (mGameServiceCallback == null) {
                    GameUtil.log(TAG + ": no callback!!!");
                    return;
                }

                ChatMessage chatMessage = (ChatMessage) msg.obj;
                mGameServiceCallback.onMessageReceived(chatMessage);
            }
            super.handleMessage(msg);
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

    public void sendChat(ChatMessage chat, GameServiceCallback callback) {
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
            throw new RuntimeException(e);
        }


    }

}
