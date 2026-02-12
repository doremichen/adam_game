/**
 * Copyright 2026 Adam Chen, All rights reserved.
 * <p>
 * Description: This is the main activity for the memory card game.
 *
 * @author Adam Chen
 * @version 1.0 - 2026-02-11
 *
 */
package com.adam.app.memorycardgame;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adam.app.memorycardgame.databinding.ActivityMainBinding;
import com.adam.app.memorycardgame.util.CommonUtils;

public class MainActivity extends AppCompatActivity {
    // TAG
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.log(TAG + "onCreate");
        // view binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}