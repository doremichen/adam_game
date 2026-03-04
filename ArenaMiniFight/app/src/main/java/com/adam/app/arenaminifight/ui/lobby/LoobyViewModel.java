/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the lobby view model of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/02
 */
package com.adam.app.arenaminifight.ui.lobby;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adam.app.arenaminifight.data.repository.GameRepository;
import com.adam.app.arenaminifight.domain.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class LoobyViewModel extends ViewModel {
    // TAG
    private static final String TAG = "LobbyViewModel";

    private final GameRepository mGameRepository = GameRepository.getInstance();

    // live data
    private final MutableLiveData<List<ChatMessage>> mChatMessages = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<ChatMessage>> getChatMessages() {
        return mChatMessages;
    }
    // live data: notify to show input dialog
    private final MutableLiveData<Boolean> mShowInputDialog = new MutableLiveData<>(false);
    public LiveData<Boolean> getShowInputDialog() {
        return mShowInputDialog;
    }

    // current input message
    public final MutableLiveData<String> mInputMessage = new MutableLiveData<>("");

    // navigate to game
    private final MutableLiveData<Boolean> mNavigateToGame = new MutableLiveData<>(false);
    public LiveData<Boolean> getNavigateToGame() {
        return mNavigateToGame;
    }

    public LoobyViewModel() {
        // bind game service
        mGameRepository.bindGameService();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // unbind game service
        mGameRepository.unbindGameService();
    }

    public LiveData<Boolean> isReady() {
        return mGameRepository.isReady();
    }

    // UC-02 Auto Login
    public void performAutoLogin() {
        // 邏輯：檢查本地代碼或產生 Guest ID
    }

    public void onStartClicked() {
        mNavigateToGame.setValue(true);
    }

    public void onSendClicked() {
        String text = mInputMessage.getValue();
        if (text != null && !text.trim().isEmpty()) {
            ChatMessage chatMsg = new ChatMessage("123",
                    "Guest",
                    text, System.currentTimeMillis());
            // update UI
            List<ChatMessage> chatMessages = mChatMessages.getValue();
            if (chatMessages != null) {
                chatMessages.add(chatMsg);
                mChatMessages.setValue(chatMessages);
            }
            // send text to server by repository
            mGameRepository.sendChat(chatMsg, this::onMessageReceived);
            // clear
            mInputMessage.setValue("");
        }
    }

    private void onMessageReceived(ChatMessage chatMessage) {
        // update ui
        List<ChatMessage> chatMessages = mChatMessages.getValue();
        if (chatMessages != null) {
            chatMessages.add(chatMessage);
            mChatMessages.setValue(chatMessages);
        }
    }

    public void doneNavigating() {
        mNavigateToGame.setValue(false);
    }

    public void onInputAreaClicked() {
        mShowInputDialog.setValue(true);
    }

    public void doneShowingDialog() {
        mShowInputDialog.setValue(false);
    }
}