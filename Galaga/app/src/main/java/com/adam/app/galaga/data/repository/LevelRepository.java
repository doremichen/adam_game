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

package com.adam.app.galaga.data.repository;

import android.content.Context;

import com.adam.app.galaga.data.local.assets.AssetProvider;
import com.adam.app.galaga.data.model.LevelConfig;

import java.util.HashMap;
import java.util.Map;

public class LevelRepository {
    // TAG
    private static final String TAG = LevelRepository.class.getSimpleName();
    private static volatile LevelRepository sInstance;
    // AssetProvider
    private final AssetProvider mAssetProvider;
    // mLevelCache
    private final Map<Integer, LevelConfig> mLevelCache = new HashMap<>();

    private LevelRepository(Context context) {
        mAssetProvider = AssetProvider.getInstance(context);
    }

    public static LevelRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LevelRepository.class) {
                if (sInstance == null) {
                    sInstance = new LevelRepository(context);
                }
            }
        }
        return sInstance;
    }


    /**
     * getLevelConfig
     *
     * @param levelId int
     * @return LevelConfig
     */
    public LevelConfig getLevelConfig(int levelId) {
        // early return
        if (mLevelCache.containsKey(levelId)) {
            return mLevelCache.get(levelId);
        }

        String fileName = String.format("levels/level_%d.json", levelId);

        // get level config
        LevelConfig levelConfig = mAssetProvider.loadJsonFromAssets(fileName, LevelConfig.class);

        // catch
        if (levelConfig != null) {
            mLevelCache.put(levelId, levelConfig);
        }

        return levelConfig;
    }

}
