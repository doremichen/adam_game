/**
 * Copyright 2025 - Adam Game. All rights reserved.
 *
 * Description: This class is used to create a car object.
 *
 * Author: Adam Game
 * Created Date: 2025/10/27
 */
package com.adam.app.racinggame2d.model.entity;

import android.graphics.PointF;

public class Car {
    // mId
    private String mId;
    // mName
    private String mName;
    // mSpeed: float
    private float mSpeed;
    // mAcceleration: float
    private float mAcceleration;
    // mAngle: float
    private float mAngle;

    private PointF mPosition;

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
        this.mAngle = 0.0f;
        this.mPosition = new PointF(0.0f, 0.0f);

    }

    /**
     * move
     *  move coordinates
     *
     *  @param deltaTime: float
     */
    public void move(float deltaTime) {
        // move instance by speed multiple by delta time
         float instance = mSpeed * deltaTime;
         this.mPosition.x = (float)Math.cos(Math.toRadians(mAngle)) * instance;
         this.mPosition.y = (float)Math.sin(Math.toRadians(mAngle)) * instance;
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

    public float getAcceleration() {
        return mAcceleration;
    }

    public PointF getPosition() {
        return mPosition;
    }


    public float getDirection() {
        return mAngle;
    }

    public void setDirection(float direction) {
        this.mAngle = direction;
    }

    public void setSpeed(float speed) {
        this.mSpeed = speed;
    }

    public void setPosition(PointF position) {
        this.mPosition = position;
    }
}
