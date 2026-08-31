/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import androidx.annotation.RawRes;

import com.adam.app.racinggame2d.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for sound effects and background music.
 */
public final class SoundPlayer {
    private final SoundPool mSoundPool;
    private final Map<Integer, Integer> mSoundMap = new HashMap<>();
    private final Context mContext;
    private MediaPlayer mBgmPlayer;
    private boolean mEnabled;

    public SoundPlayer(Context context, boolean soundEnable) {
        this.mContext = context;
        this.mEnabled = soundEnable;
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attrs)
                .setMaxStreams(4)
                .build();

        preloadShortSounds();
    }

    private void preloadShortSounds() {
        mSoundMap.put(Constants.SOUND_COLLISION, mSoundPool.load(mContext, R.raw.collision, 1));
        mSoundMap.put(Constants.SOUND_ENGINE, mSoundPool.load(mContext, R.raw.engine, 1));
        mSoundMap.put(Constants.SOUND_BUTTON, mSoundPool.load(mContext, R.raw.button, 1));
    }

    public void playShortSound(int soundId, boolean isLooping) {
        if (!mEnabled) return;
        Integer id = mSoundMap.get(soundId);
        int loop = isLooping ? -1 : 0;
        if (id != null) {
            mSoundPool.play(id, 1.0f, 1.0f, 1, loop, 1f);
        }
    }

    public void playBgm(@RawRes int resId, boolean isLooping) {
        if (!mEnabled) return;
        stopBgm();
        mBgmPlayer = MediaPlayer.create(mContext, resId);
        if (mBgmPlayer != null) {
            mBgmPlayer.setLooping(isLooping);
            mBgmPlayer.setVolume(1.0f, 1.0f);
            mBgmPlayer.start();
        }
    }

    public void pauseBgm() {
        if (mBgmPlayer != null && mBgmPlayer.isPlaying()) {
            mBgmPlayer.pause();
        }
    }

    public void resumeBgm() {
        if (!mEnabled) return;
        if (mBgmPlayer != null && !mBgmPlayer.isPlaying()) {
            mBgmPlayer.start();
        }
    }

    public void stopBgm() {
        if (mBgmPlayer != null) {
            if (mBgmPlayer.isPlaying()) {
                mBgmPlayer.stop();
            }
            mBgmPlayer.release();
            mBgmPlayer = null;
        }
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        if (!enabled) {
            stopBgm();
        }
        this.mEnabled = enabled;
    }

    public void release() {
        stopBgm();
        mSoundPool.release();
    }
}
