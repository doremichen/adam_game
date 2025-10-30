/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is used to represent the obstacle in the game.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/28
 */
package com.adam.app.racinggame2d.model.entity;

import android.graphics.PointF;

public class Obstacle {

    // Obstacle type: Type
    private final Type mType;
    // Image path: String
    private final String mImagePath;
    // Position: PointF
    private PointF mPosition;
    // radius of collision: float
    private float mRadius;
    /**
     * Constructor
     *
     * @param position  PointF
     * @param radius    float
     * @param type      Type
     * @param imagePath String
     */
    public Obstacle(PointF position, float radius, Type type, String imagePath) {
        this.mPosition = position;
        this.mRadius = radius;
        this.mType = type;
        this.mImagePath = imagePath;
    }

    //--- get ---
    public PointF getPosition() {
        return mPosition;
    }

    public float getRadius() {
        return mRadius;
    }

    public Type getType() {
        return mType;
    }

    public String getImagePath() {
        return mImagePath;
    }

    /**
     * Type of obstacle
     */
    public enum Type {
        OIL, ROCK, BOOST
    }
}
