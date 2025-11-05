/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the about activity.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/11/05
 */
package com.adam.app.racinggame2d.view.about;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adam.app.racinggame2d.databinding.ActivityAboutBinding;

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