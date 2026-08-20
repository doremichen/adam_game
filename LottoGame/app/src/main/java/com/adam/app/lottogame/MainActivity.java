/**
 * Copyright (c) 2026 LottoGame
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
package com.adam.app.lottogame;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.lottogame.databinding.ActivityMainBinding;
import com.adam.app.lottogame.viewmodel.LotteryViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    // TAG
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;

    // view model
    private LotteryViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        // init view model
        mViewModel = new ViewModelProvider(this).get(LotteryViewModel.class);

        mBinding.setVm(mViewModel);
        mBinding.setLifecycleOwner(this);

        mBinding.btnGenerate.setOnClickListener(this::generateRandomNumbers);
        mBinding.btnDraw.setOnClickListener(this::drawLotteryNumbers);
        mBinding.btnVsAi.setOnClickListener(this::vsAi);
        mBinding.btnExit.setOnClickListener(v -> finish());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // mViewModel.onClear() is no longer needed as onCleared() is handled by lifecycle
    }

    private void vsAi(View view) {
        mViewModel.playVsAI();
    }

    private void drawLotteryNumbers(View view) {
        mViewModel.draw();
    }

    private void generateRandomNumbers(View view) {
        mViewModel.generateNumber();
    }

}