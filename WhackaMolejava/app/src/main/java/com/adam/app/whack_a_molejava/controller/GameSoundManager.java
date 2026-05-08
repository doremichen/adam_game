/*
 * MIT License
 *
 * Copyright (c) 2025 Adam Chen
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
package com.adam.app.whack_a_molejava.controller;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.annotation.RawRes;

import com.adam.app.whack_a_molejava.util.GameUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is the sound manager
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-04
 */
public class GameSoundManager {

    // TAG
    private static final String TAG = "GameSoundManager";

    private final SettingsManager mSettingManager;
    // sound pool
    private SoundPool mSoundPool;
    // map: raw id -> sound id
    private final Map<Integer, Integer> mSoundMap;

    public GameSoundManager(Context context) {
        GameUtils.log(TAG, "GameSoundManager constructor");
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attrs)
                .setMaxStreams(5)
                .build();

        mSoundMap = new HashMap<>();

        mSettingManager = SettingsManager.getInstance(context);

    }


    /**
     * play short sound
     * @param context Context
     * @param rawId raw id
     */
    public void playShortSound(Context context, @RawRes int rawId) {
        GameUtils.log(TAG, "playShortSound");
        boolean isSoundOn = mSettingManager.isSoundOn();
        if (!isSoundOn) return;

        Integer soundId = mSoundMap.get(rawId);
        if (soundId == null) {
            int loadedId = mSoundPool.load(context, rawId, 1);
            mSoundMap.put(rawId, loadedId);

            mSoundPool.setOnLoadCompleteListener((soundPool, id, status) -> {
                // check if sound is loaded
                if (status != 0) {
                    GameUtils.log(TAG, "sound is not loaded");
                    return;
                }
                // check sound id
                if (loadedId != id) {
                    GameUtils.log(TAG, "sound id is not correct");
                    return;
                }
                GameUtils.log(TAG, "sound is loaded and play!!!");
                mSoundPool.play(loadedId, 1.0f, 1.0f, 1, 0, 1.0f);
            });
        } else {
            GameUtils.log(TAG, "sound is play!!!");
            mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }

    }

    /**
     * release all resources
     */
    public void release() {
        GameUtils.log(TAG, "release");
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
    }
}
