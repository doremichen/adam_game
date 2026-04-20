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

package com.adam.app.galaga.ui.game;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.galaga.databinding.ActivityGameBinding;
import com.adam.app.galaga.viewmodel.GameViewModel;

public class GameActivity extends AppCompatActivity {

    // TAG
    private static final String TAG = GameActivity.class.getSimpleName();

    // view binding
    private ActivityGameBinding mBinding;
    // view model
    private GameViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        // view model
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        // data binding
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        // observer live data
        mViewModel.getEntities().observe(this, entities -> {
            mBinding.gameSurfaceView.updateEntities(entities);
        });

    }
}