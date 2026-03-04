/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the native engine service.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/04
 */
package com.adam.app.arenaminifight.data.service;

public class NativeEngine {
    static{
        System.loadLibrary("game_engine");
    }

    public native void updateEngine(float deltaTime);
    public native void updatePlayerPosition(String id, float x, float y);
    public native String getGameState();
}
