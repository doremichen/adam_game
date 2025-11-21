/**
 * This class is used to provide the sound effects and background music.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-18
 */
package com.adam.app.flappybird.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import androidx.annotation.RawRes;

import com.adam.app.flappybird.R;

import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {

    private final SoundPool mSoundPool;
    private final Map<Integer, Integer> mSoundMap = new HashMap<>();
    private MediaPlayer mBgmPlayer;
    private boolean mEnabled = true;
    private final Context mContext;

    /**
     * Constructor
     * @param context the Context
     * @param soundEnable enable sound effect
     */
    public SoundPlayer(Context context, boolean soundEnable) {
        mContext = context;
        mEnabled = soundEnable;
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

    /**
     * preloadShortSounds
     *  preload the short sounds
     */
    private void preloadShortSounds() {
        mSoundMap.put(GameConstants.SOUND_WING, mSoundPool.load(mContext, R.raw.swing, 1));
        mSoundMap.put(GameConstants.SOUND_HIT, mSoundPool.load(mContext, R.raw.hit, 1));
        mSoundMap.put(GameConstants.SOUND_POINT, mSoundPool.load(mContext, R.raw.point, 1));
    }

    // === short sound ===
    /**
     * playShortSound
     * @param soundId the short sound
     * @param isLooping is looping
     */
    public void playShortSound(int soundId, boolean isLooping) {
        if (!mEnabled) {
            //GameUtil.showToast(mContext, "Sound is disabled");
            return;
        }
        // sound id
        Integer id = mSoundMap.get(soundId);
        int loop = isLooping ? -1 : 0;
        if (id != null) {
            mSoundPool.play(id, 1.0f, 1.0f, 1, loop, 1f);
        }
    }

    // === background music ===
    /**
     * playBgm
     * @param resId the resource id
     * @param isLooping is looping
     */
    public void playBgm(@RawRes int resId, boolean isLooping) {
        if (!mEnabled) {
            //GameUtil.showToast(mContext, "Sound is disabled");
            return;
        }
        // stop bgm
        stopBgm();
        mBgmPlayer = MediaPlayer.create(mContext, resId);
        if (mBgmPlayer != null) {
            mBgmPlayer.setLooping(isLooping);
            mBgmPlayer.setVolume(1.0f, 1.0f);
            mBgmPlayer.start();
        }
    }

    /**
     * pauseBgm
     */
    public void pauseBgm() {
        if (mBgmPlayer != null && mBgmPlayer.isPlaying()) {
            mBgmPlayer.pause();
        }
    }

    /**
     * resumeBgm
     */
    public void resumeBgm() {
        if (!mEnabled) {
            //GameUtil.showToast(mContext, "Sound is disabled");
            return;
        }

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

    // == state control ===
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            stopBgm();
        }
        mEnabled = enabled;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    /**
     * release
     * release the resources
     */
    public void release() {
        stopBgm();
        mSoundPool.release();
    }

    public void playSwingSound() {
        playShortSound(GameConstants.SOUND_WING, false);
    }

    public void playHitSound() {
        playShortSound(GameConstants.SOUND_HIT, false);
    }

    public void playPointSound() {
        playShortSound(GameConstants.SOUND_POINT, false);
    }
}
