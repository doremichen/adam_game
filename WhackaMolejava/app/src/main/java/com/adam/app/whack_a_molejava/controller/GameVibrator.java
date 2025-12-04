/**
 * Copyright 2025 Adam Game
 * <p>
 * This class is the Game vibrator that handle the vibration of the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-04
 *
 */
package com.adam.app.whack_a_molejava.controller;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

public class GameVibrator {

    private Vibrator mVibrator;
    private VibratorManager mVibratorMgr;

    private static GameVibrator sInstance;

    private GameVibrator(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mVibratorMgr = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
        } else {
            mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public static GameVibrator getInstance(Context context) {
        if (sInstance == null) {
            synchronized (GameVibrator.class) {
                if (sInstance == null) {
                    sInstance = new GameVibrator(context);
                }
            }
        }
        return sInstance;
    }

    private Vibrator getVibrator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return mVibratorMgr.getDefaultVibrator();
        }
        return mVibrator;
    }


    /**
     * Vibrate short (100ms)
     */
    public void vibrateShort() {
        vibrate(100L);
    }

    /**
     * Vibrate long (500ms)
     */
    public void vibrateLong() {
        vibrate(500L);
    }

    private void vibrate(long duration) {
        Vibrator vibrator = getVibrator();
        if (vibrator == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(duration);
        }
    }

    /**
     * Vibrate with pattern
     * @param pattern
     */
    public void startVibration(long[] pattern) {
        Vibrator vibrator = getVibrator();
        if (vibrator == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));
        } else {
            vibrator.vibrate(pattern, 0);
        }
    }

    /**
     * Stop vibration
     */
    public void stopVibration() {
        Vibrator vibrator = getVibrator();
        if (vibrator == null) return;
        vibrator.cancel();
    }


}
