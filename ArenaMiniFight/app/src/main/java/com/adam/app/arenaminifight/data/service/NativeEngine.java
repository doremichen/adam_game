/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the native engine service.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/04
 */
package com.adam.app.arenaminifight.data.service;

import com.adam.app.arenaminifight.domain.model.Player;
import com.adam.app.arenaminifight.utils.GameUtil;

/**
 * JNI native engine service
 */
public class NativeEngine {
    // TAG
    private static final String TAG = "NativeEngine";

    // static initializer
    static{
        try {
            System.loadLibrary("game_engine");
            GameUtil.log(TAG + ": load library success.");
        } catch (Exception e) {
            GameUtil.log(TAG + ": load library failed.");
            e.printStackTrace();
        }

    }

    /**
     * Singleton
     */

    private NativeEngine() {
        // singleton
    }

    private static class Helper {
        private static final NativeEngine INSTANCE = new NativeEngine();
    }

    public static NativeEngine getInstance() {
        return Helper.INSTANCE;
    }


    public native Player nativeInitializePlayer(String name);

    /**
     * 更新引擎狀態 (UC-15)
     * @param deltaTime 兩幀之間的時間差 (秒)，用於精確計算位移
     */
    public native void updateEngine(float deltaTime);

    /**
     * 更新玩家目標位置 (UC-06)
     * C++ 層會在此處進行邊界檢查與碰撞判斷
     * @param id 玩家唯一的 PlayerID
     */
    public native void updatePlayerPosition(String id, float x, float y);

    /**
     * 獲取當前所有玩家與物件的狀態 (UC-07)
     * 建議回傳格式： "ID:X:Y:HP|ID:X:Y:HP"
     */
    public native String getGameState();

    /**
     * 重置引擎狀態 (UC-08)
     */
    public native void resetEngine();
}
