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

package com.adam.app.galaga.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.adam.app.galaga.GalagaApplication;
import com.adam.app.galaga.data.model.Bullet;
import com.adam.app.galaga.data.model.Plane;
import com.adam.app.galaga.utils.GameConstants;

import java.util.List;

public class GameSettings {
    // TAG
    private static final String TAG = GameSettings.class.getSimpleName();

    //--- Keys ---
    public static final String KEY_AUTO_FIRE = "key.auto.fire";
    public static final String KEY_SHOT_TYPE = "key.shot.type";

    public enum ShotStyle {
        STRAIGHT {
            @Override
            void handle(List<Bullet> bullets, float x, float y, float speed) {
                bullets.add(new Bullet(x, y, 0, -speed));
            }
        },
        SPREAD {
            @Override
            void handle(List<Bullet> bullets, float x, float y, float speed) {
                bullets.add(new Bullet(x, y, -2.0f, -speed));
                bullets.add(new Bullet(x, y, 0, -speed));
                bullets.add(new Bullet(x, y, 2.0f, -speed));
            }
        },
        CIRCULAR {
            @Override
            void handle(List<Bullet> bullets, float x, float y, float speed) {
                for (int i = 0; i < 8; i++) {
                    double angle = Math.toRadians(i * 45);
                    float vx = (float) (speed * Math.cos(angle));
                    float vy = (float) (speed * Math.sin(angle));
                    bullets.add(new Bullet(x, y, vx, vy));
                }
            }
        },
        BACKWARD {
            @Override
            void handle(List<Bullet> bullets, float x, float y, float speed) {
                bullets.add(new Bullet(x, y, 0, -speed)); // forward
                bullets.add(new Bullet(x, y, 0, speed));  // back
            }
        };

        public void  spawn(List<Bullet> bullets, Plane plane) {
            if (bullets == null || plane == null) return;

            float x = plane.getPosition().x + plane.getRectOfCollision().width()/2f;
            float y = plane.getPosition().y;
            float speed = GameConstants.BULLET_SPEED;

            // handle
            handle(bullets, x, y, speed);
        }

        abstract void handle(List<Bullet> bullets, float x, float y, float speed);

    }


    // shared preference
    private final SharedPreferences mPrefesMgr;

    private static class Helper {
        private static final GameSettings sInstance = new GameSettings();
    }

    private GameSettings() {
        Context context = GalagaApplication.getAppContext();
        mPrefesMgr = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }

    public static GameSettings getInstance() {
        return Helper.sInstance;
    }


    //--- setter/getter ---
    public void setAutoFire(boolean autoFire) {
        mPrefesMgr.edit().putBoolean(KEY_AUTO_FIRE, autoFire).apply();
    }
    public boolean isAutoFire() {
        return mPrefesMgr.getBoolean(KEY_AUTO_FIRE, false);
    }
    public void setShotStyle(ShotStyle style) {
        if (style == null) return;
        mPrefesMgr.edit().putString(KEY_SHOT_TYPE, style.name()).apply();
    }
    public ShotStyle getShotStyle() {
        String styleStr = mPrefesMgr.getString(KEY_SHOT_TYPE, ShotStyle.STRAIGHT.name());
        try {
            return ShotStyle.valueOf(styleStr);
        } catch (IllegalArgumentException | NullPointerException e) {
            // return default
            return ShotStyle.STRAIGHT;
        }
    }

}
