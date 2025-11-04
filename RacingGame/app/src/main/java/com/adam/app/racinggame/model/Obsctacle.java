package com.adam.app.racinggame.model;

import android.graphics.PointF;

public class Obsctacle {
    //TAG
    private static final String TAG = "Obstacle";

    // fields
    private final Type mType;
    private final int mImgRes;
    private final PointF mPosition;
    private final float mRadius;

    /**
     * Constructor
     * @param position
     * @param radius
     * @param type
     * @param imgRes
     */
    public Obsctacle(PointF position, float radius, Type type, int imgRes) {
        mPosition = position;
        mRadius = radius;
        mType = type;
        mImgRes = imgRes;
    }

    //--- get ---
    public Type getType() {
        return mType;
    }
    public int getImgRes() {
        return mImgRes;
    }
    public PointF getPosition() {
        return mPosition;
    }
    public float getRadius() {
        return mRadius;
    }

    /**
     * Type
     */
    public enum Type {
        Rock;
    }
}
