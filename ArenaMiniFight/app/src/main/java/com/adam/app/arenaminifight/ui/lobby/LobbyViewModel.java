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

package com.adam.app.arenaminifight.ui.lobby;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.arenaminifight.domain.gateway.IGameGateway;
import com.adam.app.arenaminifight.domain.model.ChatMessage;
import com.adam.app.arenaminifight.domain.usecase.LobbyUseCase;
import com.adam.app.arenaminifight.utils.GameUtil;
import com.adam.app.arenaminifight.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * Trace: UC-02 (Lobby Chat), UC-03 (Enter Arena), UC-08 (Room Management), UC-14 (Settings)
 */
@HiltViewModel
public class LobbyViewModel extends ViewModel {

    private static final String TAG = "LobbyViewModel";

    private final MutableLiveData<String> mInputMessage = new MutableLiveData<>("");
    private final MutableLiveData<List<ChatMessage>> mChatMessages = new MutableLiveData<>(new ArrayList<>());

    private final SingleLiveEvent<Boolean> mShowInputDialog = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> mNavigateToRoom = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> mNavigateToSettings = new SingleLiveEvent<>();

    private final LobbyUseCase mLobbyUseCase;

    @Inject
    public LobbyViewModel(IGameGateway gateway) {
        mLobbyUseCase = new LobbyUseCase(gateway);
    }

    public MutableLiveData<String> getInputMessage() {
        return mInputMessage;
    }

    public LiveData<List<ChatMessage>> getChatMessages() {
        return mChatMessages;
    }

    public SingleLiveEvent<Boolean> getShowInputDialog() {
        return mShowInputDialog;
    }

    public SingleLiveEvent<Boolean> getNavigateToRoom() {
        return mNavigateToRoom;
    }

    public SingleLiveEvent<Boolean> getNavigateToSettings() {
        return mNavigateToSettings;
    }

    public LiveData<Boolean> isReady() {
        return mLobbyUseCase.execute(LobbyUseCase.Action.IS_READY, null);
    }

    // Trace: UC-03, UC-08
    public void onStartClicked() {
        GameUtil.log(TAG + ": onStartClicked");
        mNavigateToRoom.setValue(true);
    }

    // Trace: UC-14
    public void onSettingsClicked() {
        GameUtil.log(TAG + ": onSettingsClicked");
        mNavigateToSettings.setValue(true);
    }

    // Trace: UC-02
    public void onSendClicked() {
        String text = mInputMessage.getValue();
        if (text != null && !text.trim().isEmpty()) {
            ChatMessage chatMsg = new ChatMessage("123", "Guest", text, System.currentTimeMillis());
            List<ChatMessage> chatMessages = mChatMessages.getValue();
            if (chatMessages != null) {
                chatMessages.add(chatMsg);
                mChatMessages.setValue(chatMessages);
            }

            LobbyUseCase.SendChatParams params = new LobbyUseCase.SendChatParams(chatMsg, this::onMessageReceived);
            mLobbyUseCase.execute(LobbyUseCase.Action.SEND_CHAT, params);
            mInputMessage.setValue("");
        }
    }

    // Trace: UC-02
    private void onMessageReceived(ChatMessage chatMessage) {
        List<ChatMessage> chatMessages = mChatMessages.getValue();
        if (chatMessages != null) {
            chatMessages.add(chatMessage);
            mChatMessages.setValue(chatMessages);
        }
    }

    public void onInputAreaClicked() {
        mShowInputDialog.setValue(true);
    }
}
