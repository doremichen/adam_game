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

package com.adam.app.arenaminifight.domain.usecase;

import com.adam.app.arenaminifight.domain.gateway.IGameGateway;
import com.adam.app.arenaminifight.domain.model.ChatMessage;

/**
 * Highly cohesive Enum-based UseCase for Lobby operations.
 */
public final class LobbyUseCase {

    private final IGameGateway mGateway;

    public LobbyUseCase(IGameGateway gateway) {
        mGateway = gateway;
    }

    public enum Action {
        SEND_CHAT {
            @Override
            <T> T handle(IGameGateway gw, Object data) {
                SendChatParams params = (SendChatParams) data;
                gw.sendChat(params.mChatMessage, params.mCallback);
                return null;
            }
        },
        IS_READY {
            @Override
            @SuppressWarnings("unchecked")
            <T> T handle(IGameGateway gw, Object data) {
                return (T) gw.isReady();
            }
        };

        abstract <T> T handle(IGameGateway gw, Object data);
    }

    public <T> T execute(Action action, Object data) {
        return action.handle(mGateway, data);
    }

    public static class SendChatParams {
        public final ChatMessage mChatMessage;
        public final IGameGateway.Callback<ChatMessage> mCallback;

        public SendChatParams(ChatMessage chatMessage, IGameGateway.Callback<ChatMessage> callback) {
            mChatMessage = chatMessage;
            mCallback = callback;
        }
    }
}
