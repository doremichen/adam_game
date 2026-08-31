/*
 * Copyright (c) 2026 Adam Game
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

package com.adam.app.racinggame2d.domain.entity;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.adam.app.racinggame2d.util.Constants;
import com.adam.app.racinggame2d.util.Utils;

/**
 * Domain entity representing the car in the game.
 */
public class Car {
    private static final String sTAG = "Car";
    private static final float sSLIP_DURATION = 4.0f;
    private static final float sBOOST_DURATION = 3.0f;
    private final DefaultInfo mDefault;
    private final float mBoostFactor = 1.5f;

    private String mId;
    private String mName;
    private float mAcceleration;
    private float mSpeed;
    private PointF mPosition;
    private float mHorizontalSpeed = 0f;
    private int mCarHP = Constants.MAX_CAR_HP;

    private boolean mIsSlipping = false;
    private float mSlipTimer = 0f;
    private float mSlipAngle = 0f;
    private float mSlipIntensity = 0f;

    private boolean mIsBoosting = false;
    private float mBoostTimer = 0f;
    private float mOriginalSpeedBeforeBoost = 0f;

    private boolean mIsRock = false;
    private Settings.GameDifficulty mDifficultySetting = Settings.GameDifficulty.EASY;

    public Car(String id, String name, float speed, float acceleration) {
        this.mId = id;
        this.mName = name;
        this.mSpeed = speed;
        this.mAcceleration = acceleration;
        this.mDefault = new DefaultInfo(id, name, speed, acceleration);
        this.mPosition = new PointF(0.0f, 0.0f);
    }

    public void initPosition(float viewWidth, float viewHeight) {
        Utils.logDebug(sTAG, "initPosition");
        this.mPosition.x = viewWidth / 2f;
        this.mPosition.y = viewHeight * 0.85f;
        mDefault.setPosition(new PointF(this.mPosition.x, this.mPosition.y));
    }

    public DefaultInfo getDefaultInfo() {
        return mDefault;
    }

    public void updateSpeed(boolean isAccelerating) {
        accelerate(isAccelerating ? 1f : -1f);
        Utils.logDebug(sTAG, "updateSpeed: " + this.mSpeed);

        if (this.mSpeed > Constants.MAX_SPEED) {
            this.mSpeed = Constants.MAX_SPEED;
        } else if (this.mSpeed < Constants.MIN_SPEED) {
            this.mSpeed = Constants.MIN_SPEED;
        }
    }

    public void accelerate(float deltaTime) {
        mSpeed += mAcceleration * deltaTime;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float speed) {
        this.mSpeed = Math.max(Constants.MIN_SPEED, Math.min(Constants.MAX_SPEED, speed));
    }

    public float getAcceleration() {
        return mAcceleration;
    }

    public PointF getPosition() {
        return mPosition;
    }

    public void setPosition(PointF position) {
        Utils.logDebug(sTAG, "setPosition: " + position.toString());
        this.mPosition = position;
    }

    public int getCarHP() {
        return mCarHP;
    }

    public float getRotationAngle() {
        return (mIsSlipping) ? (float) Math.sin(mSlipAngle) * 10f : 0f;
    }

    public void moveHorizontally(boolean isLeft) {
        float sensitivity = (this.mDifficultySetting != null)
                ? this.mDifficultySetting.getCtlSensitivity()
                : 1f;
        float distance = mSpeed * Constants.DELTA_TIME * sensitivity;
        float moveDir = isLeft ? -1f : 1f;

        if (mIsSlipping) {
            float slipOffset = (float) Math.sin(mSlipAngle) * mSlipIntensity * 0.5f;
            this.mPosition.x += (moveDir * distance) + slipOffset * Constants.DELTA_TIME;
        } else {
            this.mPosition.x += moveDir * distance;
        }
    }

    public void startSlip() {
        if (mIsSlipping) return;
        mIsSlipping = true;
        mSlipTimer = (this.mDifficultySetting != null)
                ? this.mDifficultySetting.getObstacleEffectDuration()
                : sSLIP_DURATION;
        float baseIntensity = 15f;
        if (this.mDifficultySetting != null) {
            baseIntensity *= this.mDifficultySetting.getCtlSensitivity();
        }
        mSlipIntensity = baseIntensity + (float) (Math.random() * 10f);
        mSlipAngle = (float) (Math.random() * Math.PI * 2);
    }

    public void updateSlip(float deltaTime) {
        if (mIsSlipping) {
            mSlipTimer -= deltaTime;
            mSlipAngle += deltaTime * 6f;
            if (mSlipTimer <= 0f) {
                mIsSlipping = false;
                mSlipAngle = 0f;
                mSlipIntensity = 0f;
            }
        }
    }

    public void startBoost() {
        if (mIsBoosting) {
            mBoostTimer = (this.mDifficultySetting != null) ? this.mDifficultySetting.getObstacleEffectDuration() : sBOOST_DURATION;
            return;
        }
        this.mOriginalSpeedBeforeBoost = mSpeed;
        this.mSpeed *= mBoostFactor;
        mIsBoosting = true;
        mBoostTimer = sBOOST_DURATION;
        if (this.mSpeed > Constants.MAX_SPEED) {
            this.mSpeed = Constants.MAX_SPEED;
        }
    }

    public void updateBoost(float deltaTime) {
        if (mIsBoosting) {
            mBoostTimer -= deltaTime;
            if (mBoostTimer <= 0f) {
                mIsBoosting = false;
                mSpeed = Math.min(mOriginalSpeedBeforeBoost, Constants.MAX_SPEED);
            }
        }
    }

    public void startRock() {
        if (mIsRock) return;
        this.mSpeed *= 0.7f;
        this.mCarHP--;
        mIsRock = true;
    }

    public boolean updateRock() {
        if (mIsRock) {
            return mCarHP <= 0;
        }
        return false;
    }

    public void unsetRock() {
        mIsRock = false;
    }

    @NonNull
    @Override
    public String toString() {
        return "Car{" + "mId='" + mId +
                ", mName='" + mName +
                ", mAcceleration=" + mAcceleration +
                ", mSpeed=" + mSpeed +
                ", mPosition=" + mPosition +
                '}';
    }

    public void reset() {
        this.mSpeed = mDefault.getSpeed();
        this.mPosition.x = mDefault.getPosition().x;
        this.mPosition.y = mDefault.getPosition().y;
        this.mHorizontalSpeed = 0f;
        this.mAcceleration = mDefault.getAcceleration();
        this.mName = mDefault.getName();
        this.mId = mDefault.getId();
        this.mIsSlipping = false;
        this.mIsBoosting = false;
        this.mIsRock = false;
        this.mCarHP = Constants.MAX_CAR_HP;
        Utils.logDebug(sTAG, "reset: " + this);
    }

    public void applyTo(Settings.GameDifficulty difficultySetting) {
        this.mDifficultySetting = difficultySetting;
        this.mAcceleration *= difficultySetting.getFrictionCoefficient();
    }

    public static class DefaultInfo {
        private final String mId;
        private final String mName;
        private final float mAcceleration;
        private final float mSpeed;
        private PointF mPosition;

        public DefaultInfo(String id, String name, float speed, float acceleration) {
            this.mId = id;
            this.mName = name;
            this.mSpeed = speed;
            this.mAcceleration = acceleration;
        }

        public String getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }

        public float getSpeed() {
            return mSpeed;
        }

        public float getAcceleration() {
            return mAcceleration;
        }

        public PointF getPosition() {
            return mPosition;
        }

        public void setPosition(PointF position) {
            this.mPosition = position;
        }
    }
}
