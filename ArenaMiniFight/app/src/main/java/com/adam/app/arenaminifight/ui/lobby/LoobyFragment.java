/**
 * Copyright 2023 Adam Chen. All rights reserved.
 * <p>
 * Description: This is the lobby fragment of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/02
 */
package com.adam.app.arenaminifight.ui.lobby;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.arenaminifight.databinding.FragmentLoobyBinding;
import com.adam.app.arenaminifight.domain.model.ChatMessage;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

public class LoobyFragment extends Fragment {
    // TAG
    private static final String TAG = "LobbyFragment";

    private LoobyViewModel mViewModel;
    // view binding
    private FragmentLoobyBinding mBinding;

    private ChatAdapter mChatAdapter;


    public static LoobyFragment newInstance() {
        return new LoobyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLoobyBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init view model
        mViewModel = new ViewModelProvider(this).get(LoobyViewModel.class);

        // init adapter
        mChatAdapter = new ChatAdapter();
        mBinding.rvChat.setAdapter(mChatAdapter);
        mBinding.rvChat.setLayoutManager(new LinearLayoutManager(getContext()));

        // data binding
        mBinding.setVm(mViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        observerReady();
        observerChatList();
        observerNavigateToGame();
        observerInputEvent();
    }

    private void observerInputEvent() {
        mViewModel.getShowInputDialog().observe(getViewLifecycleOwner(), this::showInputDialog);
    }

    private void showInputDialog(Boolean isShowing) {
        if (isShowing) {
            // show input dialog
            showMessageInputDialog();
            // consumer the event
            mViewModel.doneShowingDialog();
        }
    }

    private void showMessageInputDialog() {
        // new edit input widget
        final EditText input = new EditText(getContext());
        // hint text
        input.setHint("Enter message");

        // show dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Send Message");
        builder.setView(input);
        builder.setPositiveButton("Ok", (dialog, which) -> {
            String text = input.getText().toString();
            // update input in lobby layout
            mViewModel.mInputMessage.setValue(text);
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void observerReady() {
        mViewModel.isReady().observe(getViewLifecycleOwner(), this::onGameReady);
    }

    private void onGameReady(Boolean isReady) {
        // update send button
        mBinding.btnSendChat.setEnabled(isReady);
        // update start button
        mBinding.btnStartGame.setAlpha(isReady ? 1.0f : 0.5f);
    }

    private void observerNavigateToGame() {
        mViewModel.getNavigateToGame().observe(getViewLifecycleOwner(), this::navigateToGame);
        // done
        mViewModel.doneNavigating();
    }

    private void navigateToGame(Boolean aBoolean) {
        // navigate to game view
        GameUtil.showUnImplementedToast(this.getContext());
    }

    private void observerChatList() {
        mViewModel.getChatMessages().observe(getViewLifecycleOwner(), this::updateChatList);
    }

    private void updateChatList(List<ChatMessage> chatMessages) {
        // update RecyclerView
        mChatAdapter.submitList(new ArrayList<>(chatMessages));

        // scroll to bottom
        if (!chatMessages.isEmpty()) {
            mBinding.rvChat.smoothScrollToPosition(chatMessages.size() - 1);
        }

        // log
        GameUtil.dumpList("Chat Messages", chatMessages);
    }
}