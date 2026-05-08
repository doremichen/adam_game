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
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

/**
 * This class is the Game vibrator that handle the vibration of the game.
 *
 * GameVibrator vibrator = new GameVibrator(this);
 *
 * // Short vibration
 * vibrator.vibrateShort();
 *
 * // Long vibration
 * vibrator.vibrateLong();
 *
 * // Custom vibration pattern
 * vibrator.startVibration(new long[]{0, 150, 80, 200});
 *
 * // Stop vibration
 * vibrator.stopVibration();
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-12-04
 */
public class GameVibrator {

    private Vibrator mVibrator;
    private VibratorManager mVibratorMgr;
    private final SettingsManager mSettingManager;


    private static volatile GameVibrator sInstance;

    private GameVibrator(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mVibratorMgr = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
        } else {
            mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

        mSettingManager = SettingsManager.getInstance(context);
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
        boolean isVibOn = mSettingManager.isVibrationOn();
        if (!isVibOn) return;

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
        boolean isVibOn = mSettingManager.isVibrationOn();
        if (!isVibOn) return;

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
