package com.adam.app.lottogame;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.adam.app.lottogame.R;
import com.adam.app.lottogame.databinding.ActivityMainBinding;
import com.adam.app.lottogame.strategy.IResultStrategy;
import com.adam.app.lottogame.strategy.ResultStrategyFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    // Select numbers list
    private List<Integer> mSelectedNumbers = new ArrayList<>();
    // Lottery numbers list
    private List<Integer> mDrawnNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // set button click listener
        mBinding.btnGenerate.setOnClickListener(v -> {
            mSelectedNumbers.clear();
            mSelectedNumbers.addAll(generateRandomNumbers());
            String selectedNumbers = formateNumbers(mSelectedNumbers);
            String selectedText = getString(R.string.tv_select_your_number, selectedNumbers);
            mBinding.tvSelectedNumbers.setText(selectedText);
        });
        mBinding.btnDraw.setOnClickListener(v -> {
            // check mSelectedNumbers is empty
            if (mSelectedNumbers.isEmpty()) {
                // show please select number first in result text view
                mBinding.tvResult.setText(getStringById(R.string.info_please_select_number_first));
                return;
            }

            mDrawnNumbers.clear();
            mDrawnNumbers.addAll(generateRandomNumbers());
            String drawnNumbers = formateNumbers(mDrawnNumbers);
            mBinding.tvDrawnNumbers.setText(getString(R.string.tv_show_lottery_number, drawnNumbers));

            // count match
            int matchCount = countMatch(mSelectedNumbers, mDrawnNumbers);
            Utils.log("matchCount: " + matchCount);
            // show result
            IResultStrategy strategy = ResultStrategyFactory.getStrategy(matchCount);
            Utils.log("strategy: " + strategy.getClass().getSimpleName());
            String resultText = strategy.getResultText(this);
            mBinding.tvResult.setText(getString(R.string.tv_prize_result, resultText));

        });

        mBinding.btnExit.setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * Generate random int numbers to list, there are six numbers in the list
     * which are generated randomly from 1 to 49.
     */
    private List<Integer> generateRandomNumbers() {
        List<Integer> numbers = new ArrayList<>();
        Random random = new Random();

        while (numbers.size() < 6) {
            int number = random.nextInt(49) + 1;
            if (!numbers.contains(number)) {
                numbers.add(number);
            }
        }

        // sort
        Collections.sort(numbers);
        return numbers;
    }

    /**
     * Count match from list 1 and list 2
     */
    private int countMatch(List<Integer> list1, List<Integer> list2) {
        int count = 0;
        for (int i = 0; i < list1.size(); i++) {
            if (list2.contains(list1.get(i))) {
                count++;
            }
        }
        return count;
    }

    /**
     * get string by resource string id
     */
    private String getStringById(int id) {
        return getResources().getString(id);
    }

    /**
     * format number as string
     */
    private String formateNumbers(List<Integer> numbers) {
        return TextUtils.join(", ", numbers);
    }
}