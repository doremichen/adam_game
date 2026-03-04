/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the network service of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/03
 */
package com.adam.app.arenaminifight.service;

import android.os.Handler;
import android.os.Looper;

import com.adam.app.arenaminifight.domain.model.ChatMessage;
import com.adam.app.arenaminifight.utils.GameUtil;

public class NetworkService {
    private static final String TAG = "NetworkService";

    private NetworkService() {
        // Private constructor to prevent instantiation from outside
    }

    public static NetworkService getInstance() {
        return Helper.INSTANCE;
    }

    private static class Helper {
        private static final NetworkService INSTANCE = new NetworkService();
    }

    /**
     * Callback
     */
    public interface NetworkCallback {
        void onMessageReceived(ChatMessage chatMessage);
    }

    /**
     * sent chat to server
     * @param chatMessage chat message
     * @param callback callback
     */
    public void sentChatToServer(ChatMessage chatMessage, NetworkCallback callback) {
        GameUtil.log(TAG + ": sentChatToServer");

        // simulate network delay
        ChatMessage response = new ChatMessage(chatMessage.getSenderId(),
                chatMessage.getSenderName(),
                "Server Response: " + chatMessage.getMessage(),
                System.currentTimeMillis());
        if (callback == null) {
            GameUtil.log(TAG + ": no callback!!!");
            return;
        }

        // delay 2000s
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            callback.onMessageReceived(response);
        }, 2000L);
    }




}
