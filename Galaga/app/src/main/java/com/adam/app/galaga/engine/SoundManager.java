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

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.adam.app.galaga.R;
import com.adam.app.galaga.data.local.prefs.GameSettings;
import com.adam.app.galaga.utils.GameConstants;
import com.adam.app.galaga.utils.GameUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * SoundManager - Manages SoundPool for SFX and MediaPlayer for BGM.
 * Integrated with res/raw resources.
 */
public class SoundManager {
    private static final String TAG = SoundManager.class.getSimpleName();

    private SoundPool mSoundPool;
    private MediaPlayer mMediaPlayer;
    private final Map<String, Integer> mSoundMap = new HashMap<>();
    private final GameSettings mSettings = GameSettings.getInstance();
    private boolean mIsInitialized = false;

    private SoundManager() {}

    private static class Helper {
        private static final SoundManager INSTANCE = new SoundManager();
    }

    public static SoundManager getInstance() {
        return Helper.INSTANCE;
    }

    /**
     * Initializes the SoundPool and loads resources from res/raw.
     */
    public void init(Context context) {
        if (mIsInitialized) return;

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        // Load SFX resources
        loadSfx(context, GameConstants.SFX_FIRE, R.raw.sfx_fire);
        loadSfx(context, GameConstants.SFX_LASER, R.raw.sfx_laser);
        loadSfx(context, GameConstants.SFX_EXPLOSION, R.raw.sfx_explosion);
        loadSfx(context, GameConstants.SFX_LEVEL_START, R.raw.sfx_level_start);
        
        mIsInitialized = true;
        GameUtils.info(TAG, "SoundManager initialized and resources loaded");
    }

    private void loadSfx(Context context, String key, int resId) {
        try {
            int soundId = mSoundPool.load(context, resId, 1);
            mSoundMap.put(key, soundId);
        } catch (Exception e) {
            GameUtils.error(TAG, "Failed to load sound: " + key);
        }
    }

    /**
     * Plays a sound effect if SFX is enabled in settings.
     */
    public void playSfx(String key) {
        if (!mIsInitialized || !mSettings.isSoundEffectsEnabled()) return;

        Integer soundId = mSoundMap.get(key);
        if (soundId != null) {
            mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    /**
     * Plays background music from res/raw if BGM is enabled.
     */
    public void playBgm(Context context) {
        if (!mSettings.isBgmEnabled()) return;

        stopBgm();

        try {
            mMediaPlayer = MediaPlayer.create(context, R.raw.bgm_main);
            if (mMediaPlayer != null) {
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();
                GameUtils.info(TAG, "BGM started: bgm_main");
            }
        } catch (Exception e) {
            GameUtils.error(TAG, "Failed to start BGM: " + e.getMessage());
        }
    }

    public void stopBgm() {
        if (mMediaPlayer != null) {
            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
            } catch (Exception e) {
                // Ignore
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
            GameUtils.info(TAG, "BGM stopped");
        }
    }

}
