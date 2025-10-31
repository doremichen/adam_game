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

import com.adam.app.racinggame2d.util.GameUtil;

public class Car {
    // TAG
    private static final String TAG = Car.class.getSimpleName();

    // mId
    private final String mId;
    // mName
    private final String mName;
    // mAcceleration: float
    private final float mAcceleration;
    // mSpeed: float
    private float mSpeed;
    // position of car
    private PointF mPosition;
    // horizontal speed of car
    private float mHorizontalSpeed = 0f;


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

        // initialize position
        this.mPosition = new PointF(0.0f, 0.0f);

    }


    /**
     * initPosition
     *  initialize position of car
     *
     * @param viewWidth the width of view
     * @param viewHeight the height of view
     */
    public void initPosition(float viewWidth, float viewHeight) {
        GameUtil.log(TAG, "initPosition");
        this.mPosition.x = viewWidth / 2f;
        this.mPosition.y = viewHeight * 0.85f;
        GameUtil.log(TAG, "mPosition: " + this.mPosition.toString());
    }

    /**
     * accelerate
     *  accelerate instance
     *
     *  @param deltaTime: float
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

    /**
     * moveHorizontally
     * move instance left or right by speed multiple by delta time
     *
     * @param instance : float
     * @param isLeft   : boolean
     */
    public void moveHorizontally(float instance, boolean isLeft) {
        this.mPosition.x += isLeft ? -instance : instance;
    }

    @NonNull
    @Override
    public String toString() {
        return "Car{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mAcceleration=" + mAcceleration +
                ", mSpeed=" + mSpeed +
                ", mPosition=" + mPosition +
                '}';
    }
}
