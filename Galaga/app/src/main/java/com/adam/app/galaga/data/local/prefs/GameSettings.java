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

import com.adam.app.galaga.data.model.Bullet;
import com.adam.app.galaga.data.model.Plane;
import com.adam.app.galaga.engine.SoundManager;
import com.adam.app.galaga.utils.GameConstants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class GameSettings {

    public static final String KEY_AUTO_FIRE = "key.auto.fire";
    public static final String KEY_SHOT_TYPE = "key.shot.type";
    public static final String KEY_SOUND_EFFECTS = "key.sound.effects";
    public static final String KEY_BGM = "key.bgm";

    public enum ShotStyle {
        STRAIGHT {
            @Override
            void handle(BulletContext context) {
                context.getBullets().add(new Bullet(context.getX(), context.getY() - GameConstants.BULLET_HEIGHT, 0, -context.getSpeed()));
                if (context.getSoundManager() != null) context.getSoundManager().playSfx(GameConstants.SFX_FIRE);
            }
        },
        SPREAD {
            @Override
            void handle(BulletContext context) {
                context.getBullets().add(new Bullet(context.getX(), context.getY() - GameConstants.BULLET_HEIGHT, -2.0f, -context.getSpeed()));
                context.getBullets().add(new Bullet(context.getX(), context.getY() - GameConstants.BULLET_HEIGHT, 0, -context.getSpeed()));
                context.getBullets().add(new Bullet(context.getX(), context.getY() - GameConstants.BULLET_HEIGHT, 2.0f, -context.getSpeed()));
                if (context.getSoundManager() != null) context.getSoundManager().playSfx(GameConstants.SFX_FIRE);
            }
        },
        CIRCULAR {
            @Override
            void handle(BulletContext context) {
                float speed = context.getSpeed();
                float x = context.getX();
                float y = context.getY();
                for (int i = 0; i < 12; i++) {
                    double angle = Math.toRadians(i * 30);
                    float vx = (float) (speed * Math.cos(angle));
                    float vy = (float) (speed * Math.sin(angle));
                    context.getBullets().add(new Bullet(x, y, vx, vy));
                }
                if (context.getSoundManager() != null) context.getSoundManager().playSfx(GameConstants.SFX_FIRE);
            }
        },
        BACKWARD {
            @Override
            void handle(BulletContext context) {
                context.getBullets().add(new Bullet(context.getX(), context.getY() - GameConstants.BULLET_HEIGHT, 0, -context.getSpeed()));
                context.getBullets().add(new Bullet(context.getX(), context.getY() + GameConstants.PLAYER_HEIGHT, 0, context.getSpeed()));
                if (context.getSoundManager() != null) context.getSoundManager().playSfx(GameConstants.SFX_FIRE);
            }
        },
        LASER {
            @Override
            void handle(BulletContext context) {
                Bullet laser = new Bullet(context.getX(), context.getY() - GameConstants.LASER_HEIGHT, 0, -context.getSpeed() * 2.5f,
                        GameConstants.LASER_WIDTH, GameConstants.LASER_HEIGHT);
                laser.setLaser(true);
                context.getBullets().add(laser);
                if (context.getSoundManager() != null) context.getSoundManager().playSfx(GameConstants.SFX_LASER);
            }
        };

        public void spawn(List<Bullet> bullets, Plane plane, SoundManager soundManager) {
            if (bullets == null || plane == null) return;

            float x = plane.getPosition().x + plane.getRectOfCollision().width() / 2f;
            float y = plane.getPosition().y;
            float speed = GameConstants.BULLET_SPEED;

            // use builder pattern
            BulletContext context = new BulletContext.Builder()
                    .setBullets(bullets)
                    .setX(x)
                    .setY(y)
                    .setSpeed(speed)
                    .setSoundManager(soundManager)
                    .build();

            handle(context);
        }

        abstract void handle(BulletContext context);

        /**
         * BulletContext for Builder pattern
         */
        public static class BulletContext {
            private final List<Bullet> mBullets;
            private final float mX;
            private final float mY;
            private final float mSpeed;
            private final SoundManager mSoundManager;

            private BulletContext(Builder builder) {
                this.mBullets = builder.mBullets;
                this.mX = builder.mX;
                this.mY = builder.mY;
                this.mSpeed = builder.mSpeed;
                this.mSoundManager = builder.mSoundManager;
            }

            public List<Bullet> getBullets() { return mBullets; }
            public float getX() { return mX; }
            public float getY() { return mY; }
            public float getSpeed() { return mSpeed; }
            public SoundManager getSoundManager() { return mSoundManager; }

            public static class Builder {
                private List<Bullet> mBullets;
                private float mX;
                private float mY;
                private float mSpeed;
                private SoundManager mSoundManager;

                public Builder setBullets(List<Bullet> bullets) {
                    this.mBullets = bullets;
                    return this;
                }

                public Builder setX(float x) {
                    this.mX = x;
                    return this;
                }

                public Builder setY(float y) {
                    this.mY = y;
                    return this;
                }

                public Builder setSpeed(float speed) {
                    this.mSpeed = speed;
                    return this;
                }

                public Builder setSoundManager(SoundManager soundManager) {
                    this.mSoundManager = soundManager;
                    return this;
                }

                public BulletContext build() {
                    return new BulletContext(this);
                }
            }
        }
    }

    // prefes mgr
    private final SharedPreferences mPrefesMgr;

    @Inject
    public GameSettings(@ApplicationContext Context context) {
        mPrefesMgr = context.getSharedPreferences(
                context.getPackageName() + "_preferences",
                Context.MODE_PRIVATE
        );
    }

    public void setAutoFire(boolean enable) {
        mPrefesMgr.edit().putBoolean(KEY_AUTO_FIRE, enable).apply();
    }

    public boolean isAutoFire() {
        return mPrefesMgr.getBoolean(KEY_AUTO_FIRE, false);
    }

    public void setShotStyle(ShotStyle style) {
        if (style == null) return;
        mPrefesMgr.edit().putString(KEY_SHOT_TYPE, style.name()).apply();
    }

    public void setSoundEffects(boolean enable) {
        mPrefesMgr.edit().putBoolean(KEY_SOUND_EFFECTS, enable).apply();
    }

    public boolean isSoundEffectsEnabled() {
        return mPrefesMgr.getBoolean(KEY_SOUND_EFFECTS, true);
    }

    public void setBgm(boolean enable) {
        mPrefesMgr.edit().putBoolean(KEY_BGM, enable).apply();
    }

    public boolean isBgmEnabled() {
        return mPrefesMgr.getBoolean(KEY_BGM, true);
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
