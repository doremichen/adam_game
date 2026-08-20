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
package com.adam.app.lottogame.domain;

import com.adam.app.lottogame.core.LottoAIEngine;
import com.adam.app.lottogame.data.LottoHistoryFactory;
import com.adam.app.lottogame.repository.LottoHistoryRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Consolidated UseCase for Lotto game logic.
 */
@Singleton
public class LottoUseCase {

    private final LottoHistoryRepository mRepository;
    private final LottoAIEngine mEngine;
    private final Random mRandom = new Random();

    private int mDrawId = 0;

    @Inject
    public LottoUseCase(LottoHistoryRepository repository, LottoAIEngine engine) {
        mRepository = repository;
        mEngine = engine;
    }

    /**
     * Executes the specified lotto action.
     *
     * @param action The action to execute.
     * @param params Parameters for the action.
     * @return The result of the action.
     */
    public Object execute(LottoAction action, Object... params) {
        switch (action) {
            case GENERATE_NUMBERS:
                return generateRandomNumbers();
            case DRAW:
                return performDraw();
            case VS_AI:
                return performVsAi((List<Integer>) params[0]);
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    public List<Integer> generateRandomNumbers() {
        List<Integer> numbers = new ArrayList<>();
        while (numbers.size() < 6) {
            int num = mRandom.nextInt(49) + 1;
            if (!numbers.contains(num)) {
                numbers.add(num);
            }
        }
        Collections.sort(numbers);
        return numbers;
    }

    private List<Integer> performDraw() {
        List<Integer> drawnNumbers = generateRandomNumbers();
        mDrawId++;
        mRepository.add(LottoHistoryFactory.create(mDrawId, drawnNumbers));
        return drawnNumbers;
    }

    private VsAiResult performVsAi(List<Integer> playerNumbers) {
        // AI Training
        mEngine.trainBestStrategy(6, 3);
        List<Integer> aiNumbers = mEngine.selectNumbers(6);
        List<Integer> drawnNumbers = performDraw();

        return new VsAiResult(aiNumbers, drawnNumbers);
    }

    public int countMatch(List<Integer> selected, List<Integer> drawn) {
        if (selected == null || drawn == null) return 0;
        int count = 0;
        for (int n : selected) {
            if (drawn.contains(n)) count++;
        }
        return count;
    }

    public void shutdown() {
        mRepository.shutdown();
    }

    /**
     * Result wrapper for VS AI action.
     */
    public static class VsAiResult {
        public final List<Integer> aiNumbers;
        public final List<Integer> drawnNumbers;

        public VsAiResult(List<Integer> aiNumbers, List<Integer> drawnNumbers) {
            this.aiNumbers = aiNumbers;
            this.drawnNumbers = drawnNumbers;
        }
    }
}
