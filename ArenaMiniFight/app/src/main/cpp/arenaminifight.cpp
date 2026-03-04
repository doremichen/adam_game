/**
 * Copyright 2026 Adam Chen
 *
 * Description: Game JNI
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/04
 */

// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("arenaminifight_jni");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("_jni")
//      }
//    }
#define LOG_TAG "Game_JNI"

#include <cstdlib>
#include <cstring>
#include <unistd.h>
#include <cassert>
#include "jni.h"
#include <android/log.h>
#include <csignal>
#include <sys/time.h>

#define __DEBUG__

#ifdef __DEBUG__
#define LOGV(...) __android_log_print( ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__ )
#define LOGD(...) __android_log_print( ANDROID_LOG_DEBUG,  LOG_TAG, __VA_ARGS__ )
#define LOGI(...) __android_log_print( ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__ )
#define LOGW(...) __android_log_print( ANDROID_LOG_WARN,  LOG_TAG, __VA_ARGS__ )
#define LOGE(...) __android_log_print( ANDROID_LOG_ERROR,  LOG_TAG, __VA_ARGS__ )
#else
#define LOGV(...)
#define LOGD(...)
#define LOGI(...)
#define LOGW(...)
#define LOGE(...)
#endif

/**
 * java data context
 */
struct javadata_t {
    jclass class_demo;
    jfieldID fid_Objdata;
    jmethodID mid_Objmethod;
    jfieldID fid_Clazzdata;
    jmethodID mid_Clazzmethod;
} javadata;

// The java class path
static const char *const classPath = "com/adam/app/arenaminifight/data/service/NativeEngine";

// native function
static
void _updateEngine(JNIEnv *env, jobject thiz, jfloat deltaTime)
{
    // TODO: 1. 遍歷所有玩家與子彈座標
    // TODO: 2. 執行碰撞演算法 (AABB 或 Circle Collision)
    // TODO: 3. 更新 AI 路徑 (AI Pathfinding)
}

static
void _updatePlayerPosition(JNIEnv *env, jobject thiz, jstring id, jfloat x, jfloat y)
{
    const char *pPlayer_id = env->GetStringUTFChars(id, nullptr);

    // --- 這裡進入核心 C++ 邏輯 ---
    // 1. 更新內部的玩家座標矩陣
    // 2. 執行碰撞檢查 (UC-05: Collision Engine)
    // 3. 如果發生碰撞，可能需要修正座標或觸發扣血

    LOGD("Player %s moved to: (%.2f, %.2f)", pPlayer_id, x, y);
    env->ReleaseStringUTFChars(id, pPlayer_id);
}

jstring _getGameState(JNIEnv *env, jobject thiz)
{
    //TODO
    return env->NewStringUTF("TODO");
}

/**
 * jni native method
 */
static const JNINativeMethod gMethods[] = {
        // name                  // signature                         // fnPtr
        {"updateEngine", "(F)V", (void *) _updateEngine},
        {"updatePlayerPosition", "(Ljava/lang/String;FF)V", (void *) _updatePlayerPosition},
        {"getGameState", "()Ljava/lang/String;", (void *) _getGameState},
};

static
int registerNative(JNIEnv *env) {
    LOGI("[%s] enter\n", __FUNCTION__);
    jclass clazz = env->FindClass(classPath);
    // null check
    if (clazz == nullptr) {
        LOGE("Can not find class");
        return JNI_ERR;
    }

    // register native method
    if (env->RegisterNatives(clazz, gMethods, sizeof(gMethods)/ sizeof(gMethods[0])) != JNI_OK) {
        LOGE("Can not register native method");
        return JNI_ERR;
    }
    LOGI("[%s] exit\n", __FUNCTION__);
    return JNI_OK;
}

/**
 * The load function of jni
 */
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        LOGE("Can not get jni env");
        return -1;
    }

    // Get jclass with env->FindClass.
    // Register methods with env->RegisterNatives.
    if (registerNative(env) == -1) {
        LOGE("Can not register native method in jni_onload");
        return -1;
    }
    return JNI_VERSION_1_6;
}