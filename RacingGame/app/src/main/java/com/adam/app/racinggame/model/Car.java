/**
 * Copyright (C) 2025 Adam. All Rights Reserved.
 * Description: This class is the car model
 *
 * @author Adam Chen
 * @since 2025-11-03
 */
package com.adam.app.racinggame.model;

import android.graphics.PointF;

public class Car {
    // TAG
    private static final String TAG = "Car";
    // fields
    private String mId;
    private String mName;
    private PointF mPosition;
    private float mSpeed;
    private float mAcceleration;

    /**
     * Constructor
     *
     * @param id
     * @param name
     * @param speed
     * @param acceleration
     */
    public Car(String id, String name, float speed, float acceleration) {
        mId = id;
        mName = name;
        mSpeed = speed;
        mAcceleration = acceleration;

        // initial position
        mPosition = new PointF(0, 0);
    }

    //--- get ---
    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public PointF getPosition() {
        return mPosition;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public float getAcceleration() {
        return mAcceleration;
    }


}
