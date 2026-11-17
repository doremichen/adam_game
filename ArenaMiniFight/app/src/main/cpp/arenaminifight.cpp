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

/**
 * Description: Game JNI Native Engine
 * Trace: UC-04, UC-05, UC-06, UC-07
 *
 * @author Adam Chen
 * @version 1.0.3 - 2026/03/05
 */

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

// Engine Constants (Google C++ Style: kConstantName)
constexpr float kWorldMinX = 0.0f;
constexpr float kWorldMaxX = 1080.0f;
constexpr float kWorldMinY = 0.0f;
constexpr float kWorldMaxY = 1920.0f;
constexpr float kSpawnRangeX = 800.0f;
constexpr float kSpawnOffsetX = 100.0f;
constexpr float kSpawnRangeY = 1200.0f;
constexpr float kSpawnOffsetY = 100.0f;
constexpr int kInitialHp = 100;
constexpr float kDefaultSpeed = 150.0f;

/**
 * Entity player object in native memory
 */
struct PlayerObject {
    std::string id;
    std::string name;
    float x;
    float y;
    int hp;
    float speed_x;
    float speed_y;
};

// Global native player map
static std::map<std::string, PlayerObject> g_players;
static int g_player_count = 0;

// The java class path
static const char *const kClassPath = "com/adam/app/arenaminifight/data/service/NativeEngine";

// Trace: UC-04 (Spawn Player & AI Bots)
static jobject NativeInitializePlayer(JNIEnv *env, jobject thiz, jstring name) {
    const char *p_name = (name == NULL) ? "UnknownPlayer" : env->GetStringUTFChars(name, nullptr);
    std::string player_id = "P_" + std::to_string(g_player_count++);
    float random_x = static_cast<float>(rand() % static_cast<int>(kSpawnRangeX) + static_cast<int>(kSpawnOffsetX));
    float random_y = static_cast<float>(rand() % static_cast<int>(kSpawnRangeY) + static_cast<int>(kSpawnOffsetY));

    PlayerObject new_player = {
            .id = player_id,
            .name = p_name,
            .x = random_x,
            .y = random_y,
            .hp = kInitialHp,
            .speed_x = kDefaultSpeed,
            .speed_y = kDefaultSpeed,
    };

    g_players[player_id] = new_player;

    if (name != nullptr && p_name != nullptr) {
        env->ReleaseStringUTFChars(name, p_name);
    }

    jclass point_class = env->FindClass("android/graphics/PointF");
    jmethodID point_init = env->GetMethodID(point_class, "<init>", "(FF)V");
    jobject point_obj = env->NewObject(point_class, point_init, random_x, random_y);

    jclass player_class = env->FindClass("com/adam/app/arenaminifight/domain/model/Player");
    jmethodID player_init = env->GetMethodID(player_class, "<init>", "(Ljava/lang/String;Ljava/lang/String;Landroid/graphics/PointF;FI)V");

    return env->NewObject(player_class,
            player_init,
            env->NewStringUTF(new_player.name.c_str()),
            env->NewStringUTF(new_player.id.c_str()), point_obj, 0.0f, new_player.hp);
}

// Trace: UC-06 (Native Physics Loop)
static void NativeUpdateEngine(JNIEnv *env, jobject thiz, jfloat delta_time) {
    LOGD("Engine: Update with deltaTime: %.2f", delta_time);
    for (auto &pair : g_players) {
        PlayerObject& p = pair.second;

        p.x += p.speed_x * delta_time;
        p.y += p.speed_y * delta_time;

        if (p.x < kWorldMinX || p.x > kWorldMaxX) {
            p.speed_x *= -1;
            p.x = (p.x < kWorldMinX) ? kWorldMinX : kWorldMaxX;
        }
        if (p.y < kWorldMinY || p.y > kWorldMaxY) {
            p.speed_y *= -1;
            p.y = (p.y < kWorldMinY) ? kWorldMinY : kWorldMaxY;
        }
    }
}

// Trace: UC-05 (Player Movement & Control)
static void NativeUpdatePlayerPosition(JNIEnv *env, jobject thiz, jstring id, jfloat x, jfloat y) {
    LOGD("Engine: Update player position (%.f, %.f)", x, y);

    const char *p_id = env->GetStringUTFChars(id, nullptr);
    std::string player_id(p_id);

    auto it = g_players.find(player_id);
    if (it != g_players.end()) {
        it->second.x = x;
        it->second.y = y;
    }

    env->ReleaseStringUTFChars(id, p_id);
}

// Trace: UC-06 (State Synchronization)
static jstring NativeGetGameState(JNIEnv *env, jobject thiz) {
    std::stringstream ss;
    for (auto it = g_players.begin(); it != g_players.end(); ++it) {
        PlayerObject& p = it->second;
        ss << p.id << ":" << p.name << ":" << static_cast<int>(p.x) << ":" << static_cast<int>(p.y) << ":" << p.hp;
        if (std::next(it) != g_players.end()) {
            ss << "|";
        }
    }
    return env->NewStringUTF(ss.str().c_str());
}

// Trace: UC-07 (Stop Game & Engine Reset)
static void NativeResetEngine(JNIEnv *env, jobject thiz) {
    LOGD("Engine: Reset engine");
    g_players.clear();
    g_player_count = 0;
}

static const JNINativeMethod g_methods[] = {
        {"nativeInitializePlayer", "(Ljava/lang/String;)Lcom/adam/app/arenaminifight/domain/model/Player;", (void *) NativeInitializePlayer},
        {"updateEngine", "(F)V", (void *) NativeUpdateEngine},
        {"updatePlayerPosition", "(Ljava/lang/String;FF)V", (void *) NativeUpdatePlayerPosition},
        {"getGameState", "()Ljava/lang/String;", (void *) NativeGetGameState},
        {"resetEngine", "()V", (void *) NativeResetEngine},
};

static int RegisterNative(JNIEnv *env) {
    LOGI("[%s] enter\n", __FUNCTION__);
    jclass clazz = env->FindClass(kClassPath);
    if (clazz == nullptr) {
        LOGE("Can not find class");
        return JNI_ERR;
    }

    if (env->RegisterNatives(clazz, g_methods, sizeof(g_methods) / sizeof(g_methods[0])) != JNI_OK) {
        LOGE("Can not register native method");
        return JNI_ERR;
    }

    LOGI("[%s] exit\n", __FUNCTION__);
    return JNI_OK;
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) == JNI_ERR) {
        LOGE("Can not get jni env");
        return JNI_ERR;
    }

    if (RegisterNative(env) == JNI_ERR) {
        LOGE("Can not register native method in jni_onload");
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}
