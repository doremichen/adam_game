/**
 * This class is the Main activity of the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.adam.app.flappybird.databinding.ActivityMainBinding;
import com.adam.app.flappybird.util.GameUtil;

public class MainActivity extends AppCompatActivity {

    // view binding
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.cardStart.setOnClickListener(this::startGame);
        mBinding.cardSetting.setOnClickListener(this::openSetting);
        mBinding.cardLeaderBord.setOnClickListener(this::openLeaderBord);
        mBinding.cardAbout.setOnClickListener(this::openAbout);
        mBinding.cardExit.setOnClickListener(this::exitGame);

    }

    private void openLeaderBord(View view) {
        showUnImplemented();
    }

    private void exitGame(View view) {
        finish();
    }

    private void openAbout(View view) {
        showUnImplemented();
    }

    private void openSetting(View view) {
        showUnImplemented();
    }

    private void startGame(View view) {
        // start game
        startActivity(GameActivity.createIntent(this));
    }

    private void showUnImplemented() {
        GameUtil.showToast(this, "Unimplemented!!!");
    }

}