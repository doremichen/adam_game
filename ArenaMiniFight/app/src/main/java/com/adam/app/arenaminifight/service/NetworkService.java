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

import android.os.Handler;
import android.os.Looper;

import com.adam.app.arenaminifight.domain.model.ChatMessage;
import com.adam.app.arenaminifight.utils.GameConstants;
import com.adam.app.arenaminifight.utils.GameUtil;

public class NetworkService {

    private static final String TAG = "NetworkService";

    private NetworkService() {
    }

    public static NetworkService getInstance() {
        return Helper.sInstance;
    }

    private static class Helper {
        private static final NetworkService sInstance = new NetworkService();
    }

    public interface NetworkCallback {
        void onMessageReceived(ChatMessage chatMessage);
    }

    public void sentChatToServer(ChatMessage chatMessage, NetworkCallback callback) {
        GameUtil.log(TAG + ": sentChatToServer");

        ChatMessage response = new ChatMessage(
                chatMessage.getSenderId(),
                chatMessage.getSenderName(),
                "Server Response: " + chatMessage.getMessage(),
                System.currentTimeMillis()
        );

        if (callback == null) {
            GameUtil.log(TAG + ": no callback!!!");
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> callback.onMessageReceived(response), GameConstants.NETWORK_SIM_DELAY_MS);
    }
}
