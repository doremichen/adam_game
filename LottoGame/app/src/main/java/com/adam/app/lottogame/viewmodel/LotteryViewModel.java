/**
 * Copyright (C) 2025 Adam. All rights reserved.
 * <p>
 * This class is the view model of loto game.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-24
 */
package com.adam.app.lottogame.viewmodel;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.lottogame.R;
import com.adam.app.lottogame.data.LottoHistoryFactory;
import com.adam.app.lottogame.repository.LottoHistoryRepository;
import com.adam.app.lottogame.strategy.IResultStrategy;
import com.adam.app.lottogame.strategy.ResultStrategyFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LotteryViewModel extends AndroidViewModel {

    // TAG
    private static final String TAG = "LotteryViewModel";

    // --- Live data ---
    private final MutableLiveData<List<Integer>> mSelectedNumbers = new MutableLiveData<>(null);
    private final MutableLiveData<List<Integer>> mDrawnNumbers = new MutableLiveData<>(null);
    private final MutableLiveData<String> mResult = new MutableLiveData<>("");
    private final MutableLiveData<List<Integer>> mAINumbers = new MutableLiveData<>(null);
    private final MutableLiveData<String> mVsResult = new MutableLiveData<>("");

    // loto repository
    private final LottoHistoryRepository mRepository;

    private final Context mContext;

    // draw id
    private int mDrawId = 0;


    public LotteryViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mRepository = new LottoHistoryRepository(mContext);
    }

    // --- get live data ---
    public LiveData<List<Integer>> getSelectedNumber() {
        return mSelectedNumbers;
    }

    public LiveData<List<Integer>> getDrawnNumber() {
        return mDrawnNumbers;
    }

    public LiveData<String> getResult() {
        return mResult;
    }

    public LiveData<List<Integer>> getAINumbers() {
        return mAINumbers;
    }

    public LiveData<String> getVsResult() {
        return mVsResult;
    }


    /**
     * generate random number
     * select number
     */
    public void generateNumber() {
        List<Integer> numbers = generateRandomNumbers();
        mSelectedNumbers.setValue(numbers);
    }

    /**
     * Draw random number
     */
    public void draw() {
        // clear ai vs player result
        mVsResult.setValue("");

        List<Integer> selectedNumbers = mSelectedNumbers.getValue();
        if (selectedNumbers == null || selectedNumbers.isEmpty()) {
            mResult.setValue("Please select numbers first");
            return;
        }

        drawnNumbers();

        int matchCount = countMatch(selectedNumbers, mDrawnNumbers.getValue());
        IResultStrategy strategy = ResultStrategyFactory.getStrategy(matchCount);
        String result = strategy.getResultText(mContext);

        mResult.setValue(result);
    }

    @NonNull
    private void drawnNumbers() {
        List<Integer> drawnNumbers = generateRandomNumbers();
        mDrawnNumbers.setValue(drawnNumbers);
        // update id
        mDrawId++;
        // save to database
        mRepository.add(LottoHistoryFactory.create(mDrawId, drawnNumbers));
    }

    /**
     * playVsAI
     */
    public void playVsAI() {
        // clear result
        mResult.setValue("");
        mDrawnNumbers.setValue(null);
        mAINumbers.setValue(null);
        mVsResult.setValue("");

        // step 1：玩家若未選號 → 自動選
        List<Integer> selectedNumbers = mSelectedNumbers.getValue();
        if (selectedNumbers == null || selectedNumbers.isEmpty()) {
            selectedNumbers = generateRandomNumbers();
            mSelectedNumbers.setValue(selectedNumbers);
        }

        // step 2：AI 選號
        List<Integer> aiNumbers = generateRandomNumbers();
        mAINumbers.setValue(aiNumbers);

        // step 3：開獎
        drawnNumbers();

        int playerMatchCount = countMatch(selectedNumbers, mDrawnNumbers.getValue());
        int aiMatchCount = countMatch(aiNumbers, mDrawnNumbers.getValue());

        // step 4：產生結果文字
        String result;
        if (playerMatchCount > aiMatchCount) {
            result = mContext.getString(R.string.loto_game_palyer_win_result, String.valueOf(playerMatchCount), String.valueOf(aiMatchCount));
        } else if (playerMatchCount < aiMatchCount) {
            result = mContext.getString(R.string.loto_game_ai_win_result, String.valueOf(playerMatchCount), String.valueOf(aiMatchCount));
        } else {
            result = mContext.getString(R.string.loto_game_tie_result);
        }
        mVsResult.setValue(result);
    }



    // --- private method ---
    private List<Integer> generateRandomNumbers() {
        List<Integer> numbers = new ArrayList<>();
        Random random = new Random();

        while (numbers.size() < 6) {
            int num = random.nextInt(49) + 1;
            if (!numbers.contains(num)) numbers.add(num);
        }
        Collections.sort(numbers);
        return numbers;
    }

    private int countMatch(List<Integer> selectedNumbers, List<Integer> drawnNumbers) {
        int count = 0;
        for (int n : selectedNumbers) {
            if (drawnNumbers.contains(n)) count++;
        }
        return count;
    }

    public static String formatNumbers(List<Integer> numbers) {
        return numbers == null ? "" : TextUtils.join(", ", numbers);
    }


    @BindingAdapter("numbersText")
    public static void setNumbersText(TextView tv, List<Integer> numbers) {
        if (numbers == null) {
            tv.setText("");
            return;
        }

        String formatted = LotteryViewModel.formatNumbers(numbers);

        //
        if (tv.getId() == R.id.tv_selected_numbers) {
            tv.setText(tv.getContext().getString(R.string.tv_select_your_number, formatted));
        } else if (tv.getId() == R.id.tv_drawn_numbers) {
            tv.setText(tv.getContext().getString(R.string.tv_show_lottery_number, formatted));
        } else if (tv.getId() == R.id.tv_ai_numbers) {
            tv.setText(tv.getContext().getString(R.string.loto_game_show_ai_number, formatted));
        }
    }


}
