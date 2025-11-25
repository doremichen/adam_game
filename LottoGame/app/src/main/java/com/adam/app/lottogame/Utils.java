/**
 * Copyright (C) 2025 Adam. All rights reserved.
 *
 * This class is used to provide some utility functions.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-24
 */
package com.adam.app.lottogame;

import android.util.Log;

public class Utils {
    // TAG: LottoGame
    public static final String TAG = "LottoGame";

    /**
     * Logcat with message
     */
    public static void log(String title, String message) {
        Log.i(TAG, title + ": " + message);
    }


}
