/**
 * Copyright (c) 2026 LottoGame
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
import androidx.lifecycle.ViewModel;

import com.adam.app.lottogame.R;
import com.adam.app.lottogame.domain.LottoAction;
import com.adam.app.lottogame.domain.LottoUseCase;
import com.adam.app.lottogame.strategy.IResultStrategy;
import com.adam.app.lottogame.strategy.ResultStrategyFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class LotteryViewModel extends ViewModel {

    // TAG
    private static final String TAG = "LotteryViewModel";

    // --- Live data ---
    private final MutableLiveData<List<Integer>> mSelectedNumbers = new MutableLiveData<>(null);
    private final MutableLiveData<List<Integer>> mDrawnNumbers = new MutableLiveData<>(null);
    private final MutableLiveData<String> mResult = new MutableLiveData<>("");
    private final MutableLiveData<List<Integer>> mAINumbers = new MutableLiveData<>(null);
    private final MutableLiveData<String> mVsResult = new MutableLiveData<>("");

    private final LottoUseCase mLottoUseCase;
    private final Context mContext;

    @Inject
    public LotteryViewModel(@ApplicationContext Context context, LottoUseCase lottoUseCase) {
        mContext = context;
        mLottoUseCase = lottoUseCase;
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
        List<Integer> numbers = (List<Integer>) mLottoUseCase.execute(LottoAction.GENERATE_NUMBERS);
        mSelectedNumbers.setValue(numbers);
    }

    /**
     * Draw random number
     */
    public void draw() {
        // clear ai vs player result
        mVsResult.setValue("");
        // clear ai number
        mAINumbers.setValue(null);
        // clear result
        mResult.setValue("");

        List<Integer> selectedNumbers = mSelectedNumbers.getValue();
        if (selectedNumbers == null || selectedNumbers.isEmpty()) {
            mResult.setValue(mContext.getString(R.string.info_please_select_number_first));
            return;
        }

        List<Integer> drawnNumbers = (List<Integer>) mLottoUseCase.execute(LottoAction.DRAW);
        mDrawnNumbers.setValue(drawnNumbers);

        int matchCount = mLottoUseCase.countMatch(selectedNumbers, drawnNumbers);
        IResultStrategy strategy = ResultStrategyFactory.getStrategy(matchCount);
        String result = strategy.getResultText(mContext);

        mResult.setValue(result);
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
            selectedNumbers = (List<Integer>) mLottoUseCase.execute(LottoAction.GENERATE_NUMBERS);
            mSelectedNumbers.setValue(selectedNumbers);
        }

        // step 2 & 3：AI 選號 & 開獎 (via UseCase)
        LottoUseCase.VsAiResult vsAiResult = (LottoUseCase.VsAiResult) mLottoUseCase.execute(LottoAction.VS_AI, selectedNumbers);
        
        mAINumbers.setValue(vsAiResult.aiNumbers);
        mDrawnNumbers.setValue(vsAiResult.drawnNumbers);

        int playerMatchCount = mLottoUseCase.countMatch(selectedNumbers, vsAiResult.drawnNumbers);
        int aiMatchCount = mLottoUseCase.countMatch(vsAiResult.aiNumbers, vsAiResult.drawnNumbers);

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

    @Override
    protected void onCleared() {
        super.onCleared();
        mLottoUseCase.shutdown();
    }

    public void onClear() {
        // Keep for backward compatibility if needed, but onCleared is preferred
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
