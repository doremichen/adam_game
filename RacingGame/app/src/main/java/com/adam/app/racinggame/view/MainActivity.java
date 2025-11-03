/**
 * Copyright (C) 2025 Adam. All Rights Reserved.
 * Description: This class is the main activity of the racing game.
 *
 * @author Adam Chen
 * @since 2025-11-03
 */
package com.adam.app.racinggame.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adam.app.racinggame.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    //TAG
    private static final String TAG = "MainActivity";

    // view binding
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // view binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // Load GameFragment fragment
//        getSupportFragmentManager().beginTransaction()
//                .replace(mBinding.frameContainer.getId(), new GameFragment())
//                .commit();
    }
}