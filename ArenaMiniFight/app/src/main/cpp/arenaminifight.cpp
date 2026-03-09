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
#include <map>
#include <sstream>
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
 * Entity player
 */
struct PlayerObject {
    std::string id;
    std::string name;
    float x;
    float y;
    int hp;
    float speedX;
    float speedY;
};

// Global variable
static std::map<std::string, PlayerObject> g_players;

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

static int g_player_count = 0;

// native function
static
jobject _nativeInitializePlayer(JNIEnv *env, jobject thiz, jstring name)
{
    const char *pName = (name == NULL) ? "UnknownPlayer" : env->GetStringUTFChars(name, nullptr);
    std::string playerId = "P_" + std::to_string(g_player_count++);
    // 模擬 C++ 引擎計算隨機初始位置
    float randomX = static_cast<float>(rand() % 800 + 100);
    float randomY = static_cast<float>(rand() % 1200 + 100);

    // build new player
    PlayerObject newPlayer = {
            .id = playerId,
            .name = pName,
            .x = randomX,
            .y = randomY,
            .hp = 100,
            .speedX = 150.0f,
            .speedY = 150.0f,
    };

    // add to g_players
    g_players[playerId] = newPlayer;

    if (name != nullptr && pName != nullptr) env->ReleaseStringUTFChars(name, pName); // avoid to memory leak

    // new pointF
    jclass pointClass = env->FindClass("android/graphics/PointF");
    jmethodID pointInit = env->GetMethodID(pointClass, "<init>", "(FF)V");
    jobject pointObj = env->NewObject(pointClass, pointInit, randomX, randomY);

    // new player
    jclass playerClass = env->FindClass("com/adam/app/arenaminifight/domain/model/Player");
    jmethodID playerInit = env->GetMethodID(playerClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;Landroid/graphics/PointF;FI)V");

    return env->NewObject(playerClass,
            playerInit,
            env->NewStringUTF(newPlayer.name.c_str()),
            env->NewStringUTF(newPlayer.id.c_str()), pointObj, 0.0f, newPlayer.hp);
}

static
void _updateEngine(JNIEnv *env, jobject thiz, jfloat deltaTime)
{
    LOGD("Engine: Update with deltaTime: %.2f", deltaTime);
    // look up all players in g_players
    for (auto &player : g_players) {
        PlayerObject& p = player.second;

        p.x += p.speedX * deltaTime;
        p.y += p.speedY * deltaTime;

        // bounce logic
        if (p.x < 0 || p.x > 1080) {
            p.speedX *= -1;
            p.x = (p.x < 0) ? 0 : 1080;
        }
        if (p.y < 0 || p.y > 1920) {
            p.speedY *= -1;
            p.y = (p.y < 0) ? 0 : 1920;
        }
    }
    LOGD("Engine: Update complete");
}

static
void _updatePlayerPosition(JNIEnv *env, jobject thiz, jstring id, jfloat x, jfloat y)
{
    LOGD("Engine: Update player position (%.f, %.f)", x, y);

    const char *pId = env->GetStringUTFChars(id, nullptr);
    std::string player_id(pId);

    // 如果玩家存在於引擎中，更新其座標（通常用於玩家手動拖曳/點擊）
    if (g_players.find(player_id) != g_players.end()) {
        g_players[player_id].x = x;
        g_players[player_id].y = y;
        LOGD("Engine: Player %s moved to (%.f, %.f)", pId, x, y);
    }

    env->ReleaseStringUTFChars(id, pId);
    LOGD("Engine: Update player position complete");
}

// UC-06: 同步所有玩家狀態
static
jstring _getGameState(JNIEnv *env, jobject thiz)
{
    LOGD("Engine: Get game state");
    // 將所有玩家狀態拼接成字串格式: "ID:Name:X:Y:HP|..."
    std::stringstream ss;
    for (auto it = g_players.begin(); it != g_players.end(); ++it) {
        PlayerObject& p = it->second;
        ss << p.id << ":" << p.name << ":" << (int)p.x << ":" << (int)p.y << ":" << p.hp;
        if (std::next(it) != g_players.end()) {
            ss << "|";
        }
    }
    LOGD("Engine: Game state: %s", ss.str().c_str());
    return env->NewStringUTF(ss.str().c_str());
}

static
void _resetEngine(JNIEnv *env, jobject thiz)
{
    LOGD("Engine: Reset engine");
    g_players.clear();
    g_player_count = 0;
    LOGD("Engine: Reset engine complete");
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
        {"resetEngine", "()V", (void *) _resetEngine},
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