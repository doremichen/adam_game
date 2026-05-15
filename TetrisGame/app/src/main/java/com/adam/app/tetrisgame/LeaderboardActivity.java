/*
 * Copyright (c) 2025 Adam
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