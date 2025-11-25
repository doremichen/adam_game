/**
 * Copyright (C) 2025 Adam. All rights reserved.
 *
 * This class is the main activity of loto game
 *
 * @Author: Adam Chen
 * @Date: 2025-11-24
 */
package com.adam.app.lottogame;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.adam.app.lottogame.databinding.ActivityMainBinding;
import com.adam.app.lottogame.strategy.IResultStrategy;
import com.adam.app.lottogame.strategy.ResultStrategyFactory;
import com.adam.app.lottogame.viewmodel.LotteryViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    // TAG
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;
    // Select numbers list
    private List<Integer> mSelectedNumbers = new ArrayList<>();
    // Lottery numbers list
    private List<Integer> mDrawnNumbers = new ArrayList<>();

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