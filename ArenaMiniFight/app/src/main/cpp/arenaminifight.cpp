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
 * Trace: UC-04, UC-05, UC-06, UC-07, UC-10
 *
 * @author Adam Chen
 * @version 1.0.5 - 2026/03/05
 */

#define LOG_TAG "NativeEngine"

#include <jni.h>
#include <string>
#include <vector>
#include <map>
#include <sstream>
#include <cstdlib>
#include <cmath>
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

// Engine Constants
constexpr float kWorldMinX = 50.0f;
constexpr float kWorldMaxX = 1030.0f;
constexpr float kWorldMinY = 100.0f;
constexpr float kWorldMaxY = 1820.0f;
constexpr float kSpawnRangeX = 800.0f;
constexpr float kSpawnOffsetX = 100.0f;
constexpr float kSpawnRangeY = 1200.0f;
constexpr float kSpawnOffsetY = 100.0f;
constexpr int kInitialHp = 100;
constexpr float kDefaultSpeed = 180.0f;

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
    int attack_type; // 0: none, 1: attack, 2: skill
    float attack_timer;
    float target_x;
    float target_y;
    bool is_moving;
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
            .attack_type = 0,
            .attack_timer = 0.0f,
            .target_x = random_x,
            .target_y = random_y,
            .is_moving = false,
    };

    g_players[player_id] = new_player;

    if (name != nullptr && p_name != nullptr) {
        env->ReleaseStringUTFChars(name, p_name);
    }

    jclass point_class = env->FindClass("android/graphics/PointF");
    jmethodID point_init = env->GetMethodID(point_class, "<init>", "(FF)V");
    jobject point_obj = env->NewObject(point_class, point_init, random_x, random_y);

    jclass player_class = env->FindClass("com/adam/app/arenaminifight/domain/model/Player");
    jmethodID player_init = env->GetMethodID(player_class, "<init>", "(Ljava/lang/String;Ljava/lang/String;Landroid/graphics/PointF;FII)V");

    return env->NewObject(player_class,
            player_init,
            env->NewStringUTF(new_player.name.c_str()),
            env->NewStringUTF(new_player.id.c_str()), point_obj, 0.0f, new_player.hp, 0);
}

// Trace: UC-06 (Native Physics Loop & Smooth Movement & AI Combat)
static void NativeUpdateEngine(JNIEnv *env, jobject thiz, jfloat delta_time) {
    LOGD("Engine: Update with deltaTime: %.2f", delta_time);

    // Find main player P_0 position for AI tracking
    float main_x = 540.0f;
    float main_y = 960.0f;
    auto p0_it = g_players.find("P_0");
    if (p0_it != g_players.end()) {
        main_x = p0_it->second.x;
        main_y = p0_it->second.y;
    }

    for (auto &pair : g_players) {
        PlayerObject& p = pair.second;

        // Update attack timer
        if (p.attack_timer > 0.0f) {
            p.attack_timer -= delta_time;
            if (p.attack_timer <= 0.0f) {
                p.attack_timer = 0.0f;
                p.attack_type = 0;
            }
        }

        // Smooth Movement towards target destination when moving
        if (p.is_moving) {
            float dx = p.target_x - p.x;
            float dy = p.target_y - p.y;
            float dist = std::sqrt(dx * dx + dy * dy);

            if (dist > 8.0f) {
                float move_speed = kDefaultSpeed * 1.4f;
                p.x += (dx / dist) * move_speed * delta_time;
                p.y += (dy / dist) * move_speed * delta_time;
            } else {
                if (p.id == "P_0") {
                    p.is_moving = false;
                }
            }
        }

        // AI behavior: AI bots track and chase player P_0 when not player-controlled
        if (p.id != "P_0" && p.name.rfind("AI_", 0) == 0 && !p.is_moving) {
            float dx = main_x - p.x;
            float dy = main_y - p.y;
            float dist = std::sqrt(dx * dx + dy * dy);

            if (dist > 70.0f) {
                float vx = (dx / dist) * 130.0f;
                float vy = (dy / dist) * 130.0f;
                p.x += vx * delta_time;
                p.y += vy * delta_time;
            } else {
                // AI attacks main player when in close range
                if (p.attack_timer <= 0.0f && p0_it != g_players.end()) {
                    p0_it->second.hp -= 10;
                    p.attack_type = 1;
                    p.attack_timer = 0.4f;
                    LOGD("AI Combat: %s attacked player P_0 for 10 damage! HP: %d", p.name.c_str(), p0_it->second.hp);
                    if (p0_it->second.hp <= 0) {
                        p0_it->second.hp = kInitialHp;
                        p0_it->second.x = 540.0f;
                        p0_it->second.y = 960.0f;
                    }
                }
            }
        }

        // Boundary collision check & clamping
        if (p.x < kWorldMinX) p.x = kWorldMinX;
        if (p.x > kWorldMaxX) p.x = kWorldMaxX;
        if (p.y < kWorldMinY) p.y = kWorldMinY;
        if (p.y > kWorldMaxY) p.y = kWorldMaxY;
    }
}

// Trace: UC-05 (Player Smooth Movement Target)
static void NativeUpdatePlayerPosition(JNIEnv *env, jobject thiz, jstring id, jfloat x, jfloat y) {
    const char *p_id = env->GetStringUTFChars(id, nullptr);
    std::string player_id(p_id);

    auto it = g_players.find(player_id);
    if (it != g_players.end()) {
        it->second.target_x = x;
        it->second.target_y = y;
        it->second.is_moving = true;
    }

    env->ReleaseStringUTFChars(id, p_id);
}

// Trace: UC-05 (Stop Player Movement)
static void NativeStopPlayerMove(JNIEnv *env, jobject thiz, jstring id) {
    const char *p_id = env->GetStringUTFChars(id, nullptr);
    std::string player_id(p_id);

    auto it = g_players.find(player_id);
    if (it != g_players.end()) {
        it->second.is_moving = false;
    }

    env->ReleaseStringUTFChars(id, p_id);
}

// Trace: UC-06 (State Synchronization)
static jstring NativeGetGameState(JNIEnv *env, jobject thiz) {
    std::stringstream ss;
    for (auto it = g_players.begin(); it != g_players.end(); ++it) {
        PlayerObject& p = it->second;
        ss << p.id << ":" << p.name << ":" << static_cast<int>(p.x) << ":" << static_cast<int>(p.y) << ":" << p.hp << ":" << p.attack_type;
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

// Trace: UC-10 (Player Attack & Combat)
static jstring NativePlayerAttack(JNIEnv *env, jobject thiz, jstring id, jboolean is_skill) {
    const char *p_id = env->GetStringUTFChars(id, nullptr);
    std::string attacker_id(p_id);
    env->ReleaseStringUTFChars(id, p_id);

    auto it = g_players.find(attacker_id);
    if (it == g_players.end()) {
        return env->NewStringUTF("Attacker not found!");
    }

    PlayerObject& attacker = it->second;
    float range = is_skill ? 600.0f : 400.0f;
    int damage = is_skill ? 45 : 20;
    int hits = 0;
    std::string hit_target = "";

    attacker.attack_type = is_skill ? 2 : 1;
    attacker.attack_timer = 0.5f;

    for (auto &pair : g_players) {
        if (pair.first == attacker_id) continue;
        PlayerObject& target = pair.second;
        float dx = target.x - attacker.x;
        float dy = target.y - attacker.y;
        float dist = std::sqrt(dx * dx + dy * dy);

        if (dist <= range) {
            target.hp -= damage;
            hits++;
            hit_target = target.name;
            LOGD("Combat: %s hit %s for %d damage (HP: %d)", attacker.name.c_str(), target.name.c_str(), damage, target.hp);

            if (target.hp <= 0) {
                target.hp = kInitialHp;
                target.x = static_cast<float>(rand() % static_cast<int>(kSpawnRangeX) + static_cast<int>(kSpawnOffsetX));
                target.y = static_cast<float>(rand() % static_cast<int>(kSpawnRangeY) + static_cast<int>(kSpawnOffsetY));
                LOGD("Combat: %s defeated and respawned!", target.name.c_str());
            }
        }
    }

    // Auto-target nearest enemy if none in range
    if (hits == 0 && g_players.size() > 1) {
        PlayerObject* nearest = nullptr;
        float min_dist = 99999.0f;
        for (auto &pair : g_players) {
            if (pair.first == attacker_id) continue;
            PlayerObject& target = pair.second;
            float dx = target.x - attacker.x;
            float dy = target.y - attacker.y;
            float dist = std::sqrt(dx * dx + dy * dy);
            if (dist < min_dist) {
                min_dist = dist;
                nearest = &target;
            }
        }
        if (nearest != nullptr) {
            nearest->hp -= damage;
            hits++;
            hit_target = nearest->name;
            if (nearest->hp <= 0) {
                nearest->hp = kInitialHp;
                nearest->x = static_cast<float>(rand() % static_cast<int>(kSpawnRangeX) + static_cast<int>(kSpawnOffsetX));
                nearest->y = static_cast<float>(rand() % static_cast<int>(kSpawnRangeY) + static_cast<int>(kSpawnOffsetY));
            }
        }
    }

    std::stringstream result_ss;
    if (hits > 0) {
        result_ss << (is_skill ? "技能施放！命中 " : "攻擊！命中 ") << hit_target << " (-" << damage << " 生命)";
    } else {
        result_ss << (is_skill ? "技能施放！全場清空。" : "攻擊！揮擊命中。");
    }

    return env->NewStringUTF(result_ss.str().c_str());
}

static const JNINativeMethod g_methods[] = {
        {"nativeInitializePlayer", "(Ljava/lang/String;)Lcom/adam/app/arenaminifight/domain/model/Player;", (void *) NativeInitializePlayer},
        {"updateEngine", "(F)V", (void *) NativeUpdateEngine},
        {"updatePlayerPosition", "(Ljava/lang/String;FF)V", (void *) NativeUpdatePlayerPosition},
        {"nativeStopPlayerMove", "(Ljava/lang/String;)V", (void *) NativeStopPlayerMove},
        {"getGameState", "()Ljava/lang/String;", (void *) NativeGetGameState},
        {"resetEngine", "()V", (void *) NativeResetEngine},
        {"nativePlayerAttack", "(Ljava/lang/String;Z)Ljava/lang/String;", (void *) NativePlayerAttack},
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
