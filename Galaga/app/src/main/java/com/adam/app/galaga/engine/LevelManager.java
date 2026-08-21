/*
 * Copyright (c) 2026 Adam Chen
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

package com.adam.app.galaga.engine;

import com.adam.app.galaga.data.model.LevelConfig;
import com.adam.app.galaga.domain.repository.ILevelRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelManager {
    private final ILevelRepository mLevelRepository;
    private int mCurrentLevelId = 1;

    @Inject
    public LevelManager(ILevelRepository levelRepository) {
        this.mLevelRepository = levelRepository;
    }

    public LevelConfig enterLevel(int levelId) {
        mCurrentLevelId = levelId;
        return mLevelRepository.getLevelConfig(levelId);
    }

    public LevelConfig nextLevel() {
        return enterLevel(mCurrentLevelId + 1);
    }

    public int getCurrentLevelId() {
        return mCurrentLevelId;
    }
}
