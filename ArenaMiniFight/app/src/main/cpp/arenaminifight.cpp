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
#define LOG_TAG "NativeEngine"

#include <jni.h>
#include <string>
#include <vector>
#include <cstdlib>
#include <android/log.h>

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
    jclass class_Player;
    jmethodID mid_PlayerInit;
    jclass class_PointF;
    jmethodID mid_PointFInit;
} javadata;

// The java class path
static const char *const classPath = "com/adam/app/arenaminifight/data/service/NativeEngine";

// native function
static
jobject _nativeInitializePlayer(JNIEnv *env, jobject thiz, jstring name)
{
    if (name == NULL) { // assure name is not null
        LOGW("Player name is null");
        name = env->NewStringUTF("UnknownPlayer");
    }

    // 模擬 C++ 引擎計算隨機初始位置
    float randomX = static_cast<float>(rand() % 800 + 100);
    float randomY = static_cast<float>(rand() % 1200 + 100);

    // 呼叫 Java 的 PointF 構造函數
    jclass pointClass = env->FindClass("android/graphics/PointF");
    jmethodID pointInit = env->GetMethodID(pointClass, "<init>", "(FF)V");
    jobject pointObj = env->NewObject(pointClass, pointInit, randomX, randomY);

    // 呼叫 Java 的 Player 構造函數 (需對應你的 Player.java 參數)
    jclass playerClass = env->FindClass("com/adam/app/arenaminifight/domain/model/Player");
    jmethodID playerInit = env->GetMethodID(playerClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;Landroid/graphics/PointF;FI)V");

    return env->NewObject(playerClass, playerInit, name, env->NewStringUTF("p_local_01"), pointObj, 0.0f, 100);
}

static
void _updateEngine(JNIEnv *env, jobject thiz, jfloat deltaTime)
{
    // UC-15: AI Movement & UC-09: Hit Detection
    // 這裡通常會遍歷一個 std::vector<Entity*>
    // 1. 根據 deltaTime 更新所有 AI 的 x, y
    // 2. 檢查玩家是否與 AI 發生 AABB 碰撞
    // 3. 檢查玩家是否出界 (Arena Boundary)
}

static
void _updatePlayerPosition(JNIEnv *env, jobject thiz, jstring id, jfloat x, jfloat y)
{
    const char *pPlayer_id = env->GetStringUTFChars(id, nullptr);

    // UC-05: 物理引擎修正
    // 假設競技場邊界是 0~1080
    float correctedX = x;
    float correctedY = y;

    if (correctedX < 0) correctedX = 0;
    if (correctedX > 1080) correctedX = 1080;

    LOGD("Player %s: Input(%.f,%.f) -> Engine(%.f,%.f)", pPlayer_id, x, y, correctedX, correctedY);

    // TODO: 將修正後的座標存回 C++ 的 Map/Vector 中供 _getGameState 使用
    env->ReleaseStringUTFChars(id, pPlayer_id);
}

// UC-06: 同步所有玩家狀態
static
jstring _getGameState(JNIEnv *env, jobject thiz)
{
    // 實戰中，這裡會將所有玩家座標組合成 JSON 或 Protobuf 字串傳回 Java
    // 暫時回傳模擬數據
    return env->NewStringUTF("P01:500:500|AI01:200:300");
}

/**
 * jni native method
 */
static const JNINativeMethod gMethods[] = {
        // name                  // signature                         // fnPtr
        {"nativeInitializePlayer", "(Ljava/lang/String;)Lcom/adam/app/arenaminifight/domain/model/Player;", (void *) _nativeInitializePlayer},
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
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) == JNI_ERR) {
        LOGE("Can not get jni env");
        return JNI_ERR;
    }

    // Get jclass with env->FindClass.
    // Register methods with env->RegisterNatives.
    if (registerNative(env) == JNI_ERR) {
        LOGE("Can not register native method in jni_onload");
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}