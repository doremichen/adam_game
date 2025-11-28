/**
 * copyright 2025 Adam
 * Description: GameSoundManager is the manager of the game sound.
 * Author: Adam
 * Date: 2025/06/24
 */
package com.adam.app.tetrisgame.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import androidx.annotation.RawRes;

import com.adam.app.tetrisgame.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GameSoundManager {

    public GameSoundManager() {
        Utils.log("GameSoundManager constructor");
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
                int id = field.getInt(null);  // static field
                if (id == resourceId) {
                    return true;
                }
            }
        } catch (Exception e) {
            Utils.log("RawResourceChecker", "Error checking raw resource", e);
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
        Utils.log("playShortSound");
        Integer soundId = mSoundMap.get(rawId);
        if (soundId == null) {
            soundId = mSoundPool.load(context, rawId, 1);
            mSoundMap.put(rawId, soundId);

            final int playSoundId = soundId;
            mSoundPool.setOnLoadCompleteListener((soundPool, id, status) -> {
                // check if sound is loaded
                if (status != 0) {
                    Utils.log("sound is not loaded");
                    return;
                }
                // check sound id
                if (playSoundId != id) {
                    Utils.log("sound id is not correct");
                    return;
                }
                Utils.log("sound is loaded and play!!!");
                mSoundPool.play(playSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
            });
        } else {
            Utils.log("sound is play!!!");
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
        Utils.log("release");
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
