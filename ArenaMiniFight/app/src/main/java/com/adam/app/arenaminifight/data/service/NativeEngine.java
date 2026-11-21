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

package com.adam.app.arenaminifight.data.service;

import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.utils.GameUtil;

/**
 * JNI Native Engine wrapper interfacing with the C++ physics, collision, and AI simulation engine.
 * Trace: REQ-JNI-01, REQ-JNI-02, REQ-JNI-03
 */
public class NativeEngine {

    private static final String TAG = "NativeEngine";

    static {
        try {
            System.loadLibrary("game_engine");
            GameUtil.log(TAG + ": load library success.");
        } catch (Throwable t) {
            GameUtil.log(TAG + ": load library failed: " + t.getMessage());
        }
    }

    private NativeEngine() {
    }

    private static class Helper {
        private static final NativeEngine sInstance = new NativeEngine();
    }

    public static NativeEngine getInstance() {
        return Helper.sInstance;
    }

    public native Player nativeInitializePlayer(String name);

    public native void updateEngine(float deltaTime);

    public native void updateWorld(float deltaTime);

    public native void updatePlayerPosition(String id, float x, float y);

    public native void nativeStopPlayerMove(String id);

    public native String getGameState();

    public native void resetEngine();

    public native String nativePlayerAttack(String id, boolean isSkill);

    public native boolean checkCollision(float x1, float y1, float r1, float x2, float y2, float r2);

    public native float[] pathFind(float startX, float startY, float endX, float endY);

    public native void spawnEnemy();
}
