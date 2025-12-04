/**
 * Copyright 2025 Adam Game
 * <p>
 * This class is the sound manager
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-04
 *
 */
package com.adam.app.whack_a_molejava.controller;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import androidx.annotation.RawRes;

import com.adam.app.whack_a_molejava.util.GameUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GameSoundManager {

    // TAG
    private static final String TAG = "GameSoundManager";

    private SettingsManager mSettingManager;


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

    public boolean hasRawResource(Context context, int resourceId) {
        try {
            String packageName = context.getPackageName();
            Class<?> rawClass = Class.forName(packageName + ".R$raw");
            Field[] fields = rawClass.getDeclaredFields();

            for (Field field : fields) {
                int id = field.getInt(null);  // static field
                if (id == resourceId) {
                    return true;
                }
            }
        } catch (Exception e) {
            GameUtils.log(TAG, "RawResourceChecker" + " Error checking raw resource " + e.toString());
        }
        return false;
    }


    public boolean hasSound(int rawId) {
        return mSoundMap.containsKey(rawId);
    }


    // sound pool
    private SoundPool mSoundPool;
    // map: raw id -> sound id
    private Map<Integer, Integer> mSoundMap;
    // mediaplayer
    private MediaPlayer mMediaPlayer;

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
            soundId = mSoundPool.load(context, rawId, 1);
            mSoundMap.put(rawId, soundId);

            final int playSoundId = soundId;
            mSoundPool.setOnLoadCompleteListener((soundPool, id, status) -> {
                // check if sound is loaded
                if (status != 0) {
                    GameUtils.log(TAG, "sound is not loaded");
                    return;
                }
                // check sound id
                if (playSoundId != id) {
                    GameUtils.log(TAG, "sound id is not correct");
                    return;
                }
                GameUtils.log(TAG, "sound is loaded and play!!!");
                mSoundPool.play(playSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
            });
        } else {
            GameUtils.log(TAG, "sound is play!!!");
            mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }

    }

    /**
     * play musid
     * @param context Context
     * @param rawId raw id
     * @param loop boolean
     */
    public void playMusic(Context context, @RawRes int rawId, boolean loop) {
        boolean isSoundOn = mSettingManager.isSoundOn();
        if (!isSoundOn) return;

        stopMusic();
        release();
        mMediaPlayer = MediaPlayer.create(context, rawId);
        mMediaPlayer.setLooping(loop);
        mMediaPlayer.start();
    }

    /**
     * stop music
     */
    public void stopMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
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
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
