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

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.arenaminifight.R;
import com.adam.app.arenaminifight.databinding.FragmentLobbyBinding;
import com.adam.app.arenaminifight.domain.model.ChatMessage;
import com.adam.app.arenaminifight.ui.settings.SettingsActivity;
import com.adam.app.arenaminifight.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LobbyFragment extends Fragment {

    private LobbyViewModel mViewModel;
    private FragmentLobbyBinding mBinding;
    private ChatAdapter mChatAdapter;

    public static LobbyFragment newInstance() {
        return new LobbyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLobbyBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(LobbyViewModel.class);

        mChatAdapter = new ChatAdapter();
        mBinding.rvChat.setAdapter(mChatAdapter);
        mBinding.rvChat.setLayoutManager(new LinearLayoutManager(getContext()));

        mBinding.setVm(mViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        observeChatList();
        observeNavigateToRoom();
        observeNavigateToSettings();
        observeInputEvent();
    }

    private void observeInputEvent() {
        mViewModel.getShowInputDialog().observe(getViewLifecycleOwner(), this::showInputDialog);
    }

    private void showInputDialog(Boolean isShowing) {
        if (Boolean.TRUE.equals(isShowing)) {
            showMessageInputDialog();
        }
    }

    private void showMessageInputDialog() {
        final EditText input = new EditText(getContext());
        input.setHint(getString(R.string.dialog_input_hint));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dialog_send_message_title));
        builder.setView(input);
        builder.setPositiveButton(getString(R.string.dialog_ok), (dialog, which) -> {
            String text = input.getText().toString();
            mViewModel.getInputMessage().setValue(text);
            dialog.dismiss();
        });
        builder.setNegativeButton(getString(R.string.dialog_cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void observeNavigateToRoom() {
        mViewModel.getNavigateToRoom().observe(getViewLifecycleOwner(), this::navigateToRoom);
    }

    private void navigateToRoom(Boolean toRoom) {
        if (Boolean.TRUE.equals(toRoom)) {
            Navigation.findNavController(requireView()).navigate(R.id.action_lobby_to_room);
        }
    }

    private void observeNavigateToSettings() {
        mViewModel.getNavigateToSettings().observe(getViewLifecycleOwner(), this::navigateToSettings);
    }

    private void navigateToSettings(Boolean toSettings) {
        if (Boolean.TRUE.equals(toSettings) && getContext() != null) {
            startActivity(new Intent(getContext(), SettingsActivity.class));
        }
    }

    private void observeChatList() {
        mViewModel.getChatMessages().observe(getViewLifecycleOwner(), this::updateChatList);
    }

    private void updateChatList(List<ChatMessage> chatMessages) {
        mChatAdapter.submitList(new ArrayList<>(chatMessages));

        if (!chatMessages.isEmpty()) {
            mBinding.rvChat.smoothScrollToPosition(chatMessages.size() - 1);
        }

        GameUtil.dumpList("Chat Messages", chatMessages);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
