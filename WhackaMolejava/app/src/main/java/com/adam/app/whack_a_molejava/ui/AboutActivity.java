/**
 * Copyright 2025 Adam Game
 * <p>
 * This class is the about activity for the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-04
 *
 */
package com.adam.app.whack_a_molejava.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adam.app.whack_a_molejava.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    // view binding
    private ActivityAboutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // exit button
        mBinding.btnExit.setOnClickListener(v -> finish());

    }
}