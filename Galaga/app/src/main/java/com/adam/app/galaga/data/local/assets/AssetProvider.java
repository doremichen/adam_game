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

package com.adam.app.galaga.data.local.assets;

import android.content.Context;

import com.adam.app.galaga.utils.GameUtils;
import com.google.gson.Gson;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AssetProvider {
    // TAG
    private static final String TAG = AssetProvider.class.getSimpleName();
    // Context
    private final Context mContext;
    // Gson
    private final Gson mGson;


    private static volatile AssetProvider sInstance;

    private AssetProvider(Context context) {
        mContext = context.getApplicationContext();
        mGson = new Gson();
    }

    /**
     * DCLP
     * @param context Context
     * @return AssetProvider
     */
    public static AssetProvider getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AssetProvider.class) {
                if (sInstance == null) {
                    sInstance = new AssetProvider(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * loadJsonFromAssets
     * @param fileName String, e.g. levels/level_1.json
     * @param clazz Class<T>
     * @return T
     */
    public <T> T loadJsonFromAssets(String fileName, Class<T> clazz) {
        GameUtils.info(TAG, "loadJsonFromAssets");
        String jsonString = loadStringFromAssets(fileName);
        if (jsonString == null) {
            return null;
        }
        return mGson.fromJson(jsonString, clazz);
    }

    /**
     * loadStringFromAssets
     * @param fileName String
     * @return String
     */
    private String loadStringFromAssets(String fileName) {
        GameUtils.info(TAG, "loadStringFromAssets");
        try (InputStream inputStream = mContext.getAssets().open(fileName)) {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            GameUtils.error(TAG, "loadStringFromAssets error");
            e.printStackTrace();
        }
        return null;
    }
}
