/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.presentation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.snake.R;
import com.adam.app.snake.databinding.ActivitySettingBinding;
import com.adam.app.snake.presentation.setting.GameSettingAdapter;
import com.adam.app.snake.presentation.setting.GameSettingItem;
import com.adam.app.snake.presentation.viewmodel.SettingViewModel;

import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Snake game setting activity
 */
@AndroidEntryPoint
public class SettingActivity extends AppCompatActivity {

    // view binding
    private ActivitySettingBinding mBinding;

    // view model
    private SettingViewModel mViewModel;

    /**
     * Create intent for SettingActivity
     * @param context Context
     * @return Intent
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view model
        mViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        // view binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        // Build setting items automatically using ViewModel
        List<GameSettingItem> settingItems = mViewModel.buildSettingItems(this);

        // recycler view set layout manager
        mBinding.recyclerGameSettings.setLayoutManager(new LinearLayoutManager(this));
        
        // recycler view set adapter
        final GameSettingAdapter adapter = new GameSettingAdapter(this, 
                (key, value) -> mViewModel.saveSetting(key, value));
        mBinding.recyclerGameSettings.setAdapter(adapter);
        adapter.submitList(settingItems);
        
        // set divider
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        mBinding.recyclerGameSettings.addItemDecoration(divider);

        // exit button click listener
        mBinding.btnOk.setOnClickListener(v -> finish());
    }

}
