/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is used to create a car object.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/27
 */
package com.adam.app.racinggame2d.model.entity;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.adam.app.racinggame2d.util.Constants;
import com.adam.app.racinggame2d.util.GameUtil;

public class Car {
    // TAG
    private static final String TAG = Car.class.getSimpleName();
    private static final float SLIP_DURATION = 4.0f;    // slip duration
    private static final float BOOST_DURATION = 3.0f;  // boost duration
    private final DefaultInfo mDefault;
    private final float mBoostFactor = 1.5f;  // the boost factor
    // mId
    private String mId;
    // mName
    private String mName;
    // mAcceleration: float
    private float mAcceleration;
    // mSpeed: float
    private float mSpeed;
    // position of car
    private PointF mPosition;
    // horizontal speed of car
    private float mHorizontalSpeed = 0f;
    // car life time
    private int mCarHP = Constants.MAX_CAR_HP;  // hp of car
    // --- Slip Control ---
    private boolean mIsSlipping = false;  //used to check if slipping
    private float mSlipTimer = 0f; // the remaining time of slipping
    private float mSlipAngle = 0f; // the angle of slipping
    private float mSlipIntensity = 0f; // the intensity of slipping
    // --- Boost Control ---
    private boolean mIsBoosting = false;  //used to check if boosting
    private float mBoostTimer = 0f; // the remaining time of boosting
    private float mOriginalSpeedBeforeBoost = 0f;   // record the original speed
    // --- Rock Control ---
    private boolean mIsRock = false;
    // Game difficulty setting
    private Settings.GameDifficulty mDifficultySetting = Settings.GameDifficulty.EASY;

    /**
     * Constructor
     *
     * @param id
     * @param name
     * @param speed
     * @param acceleration
     */
    public Car(String id, String name, float speed, float acceleration) {
        this.mId = id;
        this.mName = name;
        this.mSpeed = speed;
        this.mAcceleration = acceleration;

        mDefault = new DefaultInfo(id, name, speed, acceleration);

        // initialize position
        this.mPosition = new PointF(0.0f, 0.0f);

    }

    /**
     * initPosition
     * initialize position of car
     *
     * @param viewWidth  the width of view
     * @param viewHeight the height of view
     */
    public void initPosition(float viewWidth, float viewHeight) {
        GameUtil.log(TAG, "initPosition");
        this.mPosition.x = viewWidth / 2f;
        this.mPosition.y = viewHeight * 0.85f;
        //
        mDefault.setPosition(new PointF(this.mPosition.x, this.mPosition.y));
    }

    public DefaultInfo getDefaultInfo() {
        return mDefault;
    }

    /**
     * updateSpeed
     * update speed of car
     *
     * @param isAccelerating : boolean     : float
     */
    public void updateSpeed(boolean isAccelerating) {
        accelerate(isAccelerating ? 1f : -1f);
        GameUtil.log(TAG, "updateSpeed: " + this.mSpeed);

        // limit speed range
        if (this.mSpeed > Constants.MAX_SPEED) {
            this.mSpeed = Constants.MAX_SPEED;
            GameUtil.log(TAG, "mSpeed: " + this.mSpeed);
        } else if (this.mSpeed < Constants.MIN_SPEED) {
            this.mSpeed = Constants.MIN_SPEED;
            GameUtil.log(TAG, "mSpeed: " + this.mSpeed);
        }
    }

    /**
     * accelerate
     * accelerate instance
     *
     * @param deltaTime: float
     */
    public void accelerate(float deltaTime) {
        // accelerate instance by acceleration multiple by delta time
        mSpeed += mAcceleration * deltaTime;
    }

    // --- Get/Set ---
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
        this.mSpeed = speed;
        // limit speed range
        this.mSpeed = Math.max(Constants.MIN_SPEED, Math.min(Constants.MAX_SPEED, this.mSpeed));
    }

    public float getAcceleration() {
        return mAcceleration;
    }

    public PointF getPosition() {
        return mPosition;
    }

    public void setPosition(PointF position) {
        GameUtil.log(TAG, "setPosition: " + position.toString());
        this.mPosition = position;
    }

    public int getCarHP() {
        return mCarHP;
    }


    /**
     * getAngle
     * get angle of slip
     * @return float
     */
    public float getRotationAngle() {
        return (mIsSlipping) ? (float) Math.sin(mSlipAngle) * 10f : 0f;
    }

    /**
     * moveHorizontally
     * move instance left or right by speed multiple by delta time
     *
     * @param isLeft : boolean
     */
    public void moveHorizontally(boolean isLeft) {
        float sensitivity = (this.mDifficultySetting != null)
                ? this.mDifficultySetting.getCtlSensitivity()
                : 1f;
        float distance = mSpeed * Constants.DELTA_TIME * sensitivity;
        // normal move
        float moveDir = isLeft ? -1f : 1f;

        // slip
        if (mIsSlipping) {
            float slipOffset = (float) Math.sin(mSlipAngle) * mSlipIntensity * 0.5f;
            this.mPosition.x += (moveDir * distance) + slipOffset * Constants.DELTA_TIME;
        } else {
            this.mPosition.x += moveDir * distance;
        }
    }

    /**
     * moveVertically
     * move instance up or down by speed multiple by delta time
     */
    public void startSlip() {
        if (mIsSlipping) {
            GameUtil.log(TAG, "startSlip: already slipping");
            return;
        }
        mIsSlipping = true;
        mSlipTimer = (this.mDifficultySetting != null)
                ? this.mDifficultySetting.getObstacleEffectDuration()
                : (float) SLIP_DURATION;
        // set slip intensity by speed or difficulty
        float baseIntensity = 15f;
        if (this.mDifficultySetting != null) {
            baseIntensity *= this.mDifficultySetting.getCtlSensitivity();
        }
        mSlipIntensity = baseIntensity + (float)(Math.random() * 10f);
        mSlipAngle = (float)(Math.random() * Math.PI * 2);

        GameUtil.log(TAG, "startSlip");
    }

    /**
     * updateSlip
     * update slip timer
     *
     * @param deltaTime : float
     */
    public void updateSlip(float deltaTime) {
        if (mIsSlipping) {
            mSlipTimer -= deltaTime;
            mSlipAngle += deltaTime * 6f; // increase slip angle
            if (mSlipTimer <= 0f) {
                mIsSlipping = false;
                mSlipAngle = 0f;
                mSlipIntensity = 0f;
            }
        }
        GameUtil.log(TAG, "updateSlip: " + mSlipTimer);
    }


    public void startBoost() {
        if (mIsBoosting) {
            GameUtil.log(TAG, "startBoost: already boosting");
            mBoostTimer = (this.mDifficultySetting != null) ? this.mDifficultySetting.getObstacleEffectDuration() : (float) BOOST_DURATION;   //reset boost timer
            return;
        }

        this.mOriginalSpeedBeforeBoost = mSpeed;
        this.mSpeed *= mBoostFactor;
        mIsBoosting = true;
        mBoostTimer = BOOST_DURATION;

        // Limit speed
        if (this.mSpeed > Constants.MAX_SPEED) {
            this.mSpeed = Constants.MAX_SPEED;
        }

        GameUtil.log(TAG, "startBoost");
    }

    public void updateBoost(float deltaTime) {
        if (mIsBoosting) {
            mBoostTimer -= deltaTime;
            if (mBoostTimer <= 0f) {
                mIsBoosting = false;
                mSpeed = Math.min(mOriginalSpeedBeforeBoost, Constants.MAX_SPEED);
                GameUtil.log(TAG, "Car boost ended, speed reset to " + mSpeed);
            }
        }
    }

    public void startRock() {
        if (mIsRock) {
            GameUtil.log(TAG, "startRock: already rock");
            return;
        }
        // set speed to 0
        this.mSpeed *= 0.7f;
        // decrease hp
        this.mCarHP--;

        mIsRock = true;
        GameUtil.log(TAG, "startRock");
    }

    public boolean updateRock() {
        GameUtil.log(TAG, "updateRock: " + mIsRock);
        if (mIsRock) {
            return mCarHP <= 0;
        }
        return false;
    }

    public void unsetRock() {
        GameUtil.log(TAG, "unsetRock");
        if (!mIsRock) {
            GameUtil.log(TAG, "unsetRock: not rock");
            return;
        }

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
        this.mCarHP = 3;
        GameUtil.log(TAG, "reset: " + this);
    }

    /**
     * applyTo
     * apply to settings
     *
     * @param difficultySetting : Settings.GameDifficulty
     */
    public void applyTo(Settings.GameDifficulty difficultySetting) {
        this.mDifficultySetting = difficultySetting;
        // set acceleration
        this.mAcceleration *= difficultySetting.getFrictionCoefficient();
    }

    private static class DefaultInfo {
        private final String mId;
        // mName
        private final String mName;
        // mAcceleration: float
        private final float mAcceleration;
        // mSpeed: float
        private final float mSpeed;

        private PointF mPosition;

        public DefaultInfo(String id, String name, float speed, float acceleration) {
            this.mId = id;
            this.mName = name;
            this.mSpeed = speed;
            this.mAcceleration = acceleration;
        }

        //--- get ---
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

        //--- set ---
        public void setPosition(PointF position) {
            this.mPosition = position;
        }

        @NonNull
        @Override
        public String toString() {
            return "DefaultInfo {" + "mId='" + mId + '\'' + ", mName='" + mName + '\'' + ", mAcceleration=" + mAcceleration + ", mSpeed=" + mSpeed + ", mPosition=" + mPosition + "}";
        }
    }
}
