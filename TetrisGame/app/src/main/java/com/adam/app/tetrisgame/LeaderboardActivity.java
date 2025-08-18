/**
 * Description: LeaderboardActivity is the activity of the leaderboard.
 * Author: Adam Chen
 * Date: 2025/08/15
 */
package com.adam.app.tetrisgame;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adam.app.tetrisgame.data.ScoreDatabase;
import com.adam.app.tetrisgame.data.ScoreRepository;
import com.adam.app.tetrisgame.databinding.ActivityLeaderboardBinding;
import com.adam.app.tetrisgame.model.ScoreRecord;
import com.adam.app.tetrisgame.ui.adapter.ScoreAdapter;
import com.adam.app.tetrisgame.ui.dialog.ProgressDialog;

import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    // view binding
    private ActivityLeaderboardBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view binding
        mBinding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // set layout manager to recycler view
        mBinding.recyclerViewScores.setLayoutManager(new LinearLayoutManager(this));

        // show loading progressbar dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.progress_loading));
        progressDialog.show();

        // get score list from database
        ScoreRepository scoreRepository = new ScoreRepository(this);
        scoreRepository.getTopScores(scoreList -> {
                    // hide progressbar dialog
                    progressDialog.dismiss();
                    // set adapter to recycler view
                    mBinding.recyclerViewScores.setAdapter(new ScoreAdapter(scoreList));
                    // set empty text visibility
                    mBinding.textViewEmpty.setVisibility(scoreList.isEmpty() ? View.VISIBLE : View.GONE);
                    // set list visibility
                    mBinding.recyclerViewScores.setVisibility(scoreList.isEmpty() ? View.GONE : View.VISIBLE);
        });



    }
}