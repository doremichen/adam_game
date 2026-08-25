/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.presentation.ui;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import androidx.annotation.RawRes;
import com.adam.app.tetrisgame.util.Utils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GameSoundManager {
    private SoundPool mSoundPool;
    private final Map<Integer, Integer> mSoundMap;
    private MediaPlayer mMediaPlayer;

    public GameSoundManager() {
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attrs)
                .setMaxStreams(5)
                .build();
        mSoundMap = new HashMap<>();
    }

    public boolean hasRawResource(Context context, int resourceId) {
        try {
            String packageName = context.getPackageName();
            Class<?> rawClass = Class.forName(packageName + ".R$raw");
            Field[] fields = rawClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getInt(null) == resourceId) return true;
            }
        } catch (Exception e) {
            Utils.log("Error checking raw resource", e);
        }
        return false;
    }

    public void playShortSound(Context context, @RawRes int rawId) {
        Integer soundId = mSoundMap.get(rawId);
        if (soundId == null) {
            soundId = mSoundPool.load(context, rawId, 1);
            mSoundMap.put(rawId, soundId);
            final int playSoundId = soundId;
            mSoundPool.setOnLoadCompleteListener((soundPool, id, status) -> {
                if (status == 0 && playSoundId == id) {
                    mSoundPool.play(playSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
                }
            });
        } else {
            mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    public void stopMusic() {
        if (mMediaPlayer != null) mMediaPlayer.stop();
    }

    public void release() {
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
