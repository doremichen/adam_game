/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is used to track the coordinate of  the car.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/27
 */
package com.adam.app.racinggame2d.model.entity;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Track {
    // CHECKPOINT_RADIUS
    private static final float CHECKPOINT_RADIUS = 30f;

    // width of screen
    private final int mWidth;
    // height of screen
    private final int mHeight;
    // mCheckPoints: List<float[]>
    private final List<PointF> mCheckPoints;
    // List of Obstacle
    private final List<Obstacle> mObstacles;

    /**
     * Constructor
     *
     * @param width
     * @param height
     * @param checkPoints
     */
    public Track(int width, int height, List<PointF> checkPoints) {

        this.mWidth = width;
        this.mHeight = height;

        this.mCheckPoints = checkPoints != null ? checkPoints : new ArrayList<>();

        this.mObstacles = new ArrayList<>();
    }

    /**
     * generateRandomObstacles
     * generate random obstacles with counts
     *
     * @param count
     */
    public void generateRandomObstacles(int count) {
        // preCheck if count is valid
        if (count <= 0) {
            return;
        }


        // Random
        Random random = new Random();
        // clear obstacles
        mObstacles.clear();

        // Generate random obstacles
        for (int i = 0; i < count; i++) {
            float x = random.nextFloat() * mWidth;
            float y = random.nextFloat() * mHeight;
            float radius = random.nextFloat() * 30f + 20f; // radius between 20 and 50
            Obstacle.Type type;
            String imgPath;

            switch (random.nextInt(3)) {
                case 0: {
                    type = Obstacle.Type.OIL;
                    imgPath = "drawable/obstacle_oil.png";
                }
                break;
                case 1: {
                    type = Obstacle.Type.ROCK;
                    imgPath = "drawable/obstacle_rock.png";
                }
                break;
                default: {
                    type = Obstacle.Type.BOOST;
                    imgPath = "drawable/obstacle_boost.png";
                }
                break;
            }

            mObstacles.add(new Obstacle(new PointF(x, y), radius, type, imgPath));
        }

    }

    /**
     * checkCollisions
     * check collisions between car and obstacles
     *
     * @param car
     * @param callback
     * @return boolean
     * true: if there is a collision
     * false: if there is no collision
     */
    public boolean checkCollisions(Car car, @NonNull CheckPointCallback callback) {
        // position of car
        PointF carPosition = car.getPosition();
        // boundary check
        if (isOutOfBoundary(carPosition)) {
            return true;
        }

        // check points
        if (mCheckPoints != null && !mCheckPoints.isEmpty()) {
            Iterator<PointF> iterator = mCheckPoints.iterator();
            while (iterator.hasNext()) {
                PointF checkPoint = iterator.next();
                float dx = carPosition.x - checkPoint.x;
                float dy = carPosition.y - checkPoint.y;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                // check if car is close to check point
                if (dist <= CHECKPOINT_RADIUS) {
                    // call callback
                    callback.onCheckPointReached();
                    // remove check point
                    iterator.remove();
                }
            }

        }

        // check collisions with obstacles
        for (Obstacle obstacle : mObstacles) {
            float dx = carPosition.x - obstacle.getPosition().x;
            float dy = carPosition.y - obstacle.getPosition().y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist <= obstacle.getRadius()) {
                return true;
            }
        }

        return false;
    }

    /**
     * isOutOfBoundary
     * check if the car is out of boundary
     *
     * @param carPosition PointF
     * @return boolean
     * true: if the car is out of boundary
     * false: if the car is not out of boundary
     */
    private boolean isOutOfBoundary(PointF carPosition) {
        return carPosition.x < 0 || carPosition.x > mWidth || carPosition.y < 0 || carPosition.y > mHeight;
    }

    //--- get ---
    public List<PointF> getCheckPoints() {
        return mCheckPoints;
    }

    public List<Obstacle> getObstacles() {
        return mObstacles;
    }

    public float getWidth() {
        return mWidth;
    }

    public float getHeight() {
        return mHeight;
    }

    /**
     * interface checkPointCallback
     * callback interface for check point
     */
    public interface CheckPointCallback {
        /**
         * onCheckPointReached
         * callback when check point is reached
         */
        void onCheckPointReached();
    }

}
