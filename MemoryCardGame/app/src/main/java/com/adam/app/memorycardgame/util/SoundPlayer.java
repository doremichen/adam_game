/**
 * Copyright (c) 2026 Adam Chen. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 * <p>
 * Description: This is the sound player.
 * </p>
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/02/23
 */
package com.adam.app.memorycardgame.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.adam.app.memorycardgame.R;

public class SoundPlayer {
    private static volatile SoundPlayer sInstance;
    private SoundPool mSoundPool;

    private int mSoundFlip;
    private int mSoundMatch;

    private boolean mIsLoaded;

    private SoundPlayer(Context context) {
        // init sound player
        initSoundPool(context);
    }

    /**
     * Singleton
     *
     * @param ctx Context
     * @return SoundPlayer
     */
    public static SoundPlayer getInstance(Context ctx) {
        if (sInstance == null) {
            synchronized (SoundPlayer.class) {
                if (sInstance == null) {
                    sInstance = new SoundPlayer(ctx);
                }
            }
        }
        return sInstance;
    }


    private void initSoundPool(Context context) {
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(attrs)
                .build();

        // load sound files
        mSoundFlip = mSoundPool.load(context, R.raw.sound_flip, 1);
        mSoundMatch = mSoundPool.load(context, R.raw.sound_match, 1);


        // set complete listener
        mSoundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            mIsLoaded = true;
        });
    }

    public void playFlipSound() {
        playSound(mSoundFlip);
    }


    public void playMatchSound() {
        playSound(mSoundMatch);
    }


    public void release() {
        if (mSoundPool == null) {
            return;
        }

        mSoundPool.release();
        mSoundPool = null;
        mIsLoaded = false;
        sInstance = null;
    }


    private void playSound(int mSoundFlip) {
        if (!mIsLoaded) {
            return;
        }

        // play
        mSoundPool.play(mSoundFlip, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
