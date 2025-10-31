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

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.util.GameUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Track {

    // TAG
    private static final String TAG = Track.class.getSimpleName();

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
    // scroll offset
    private float mScrollOffsetY = 0f;

    /**
     * Constructor
     *
     * @param width       width of screen
     * @param height      height of screen
     * @param checkPoints List of check points
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
     * @param count count of obstacles
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

        // define obstacle types
        List<ObstacleData> obstacleTypes = Arrays.asList(
                new ObstacleData(Obstacle.Type.OIL, R.drawable.obstacle_oil),
                new ObstacleData(Obstacle.Type.ROCK, R.drawable.obstacle_rock),
                new ObstacleData(Obstacle.Type.BOOST, R.drawable.obstacle_boost)
        );


        // Generate random obstacles
        for (int i = 0; i < count; i++) {
            float x = random.nextFloat() * mWidth;
            float y = random.nextFloat() * mHeight - mHeight;  // random y between 0 and height
            float radius = random.nextFloat() * 30f + 20f; // radius between 20 and 50

            ObstacleData selected = obstacleTypes.get(random.nextInt(obstacleTypes.size()));
            mObstacles.add(new Obstacle(new PointF(x, y), radius, selected.type, selected.imgRes));

        }

    }

    /**
     * ObstacleData
     *  This class is used to record the obstacle data
     *  type: Obstacle.Type
     *  imgPath: String
     */
    private static class ObstacleData {
        Obstacle.Type type;
        int imgRes;

        ObstacleData(Obstacle.Type type, int imgRes) {
            this.type = type;
            this.imgRes = imgRes;
        }
    }

    /**
     * checkCollisions
     * check collisions between car and obstacles
     *
     * @param car      Car
     * @param callback CheckPointCallback
     * @return boolean
     * true: if there is a collision
     * false: if there is no collision
     */
    public boolean checkCollisions(Car car, @NonNull CheckPointCallback callback) {
        GameUtil.log(TAG, "checkCollisions");
        // position of car
        PointF carPosition = car.getPosition();
        // boundary check
        if (isOutOfBoundary(carPosition)) {
            GameUtil.log(TAG, "car is out of boundary");
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
                GameUtil.log(TAG, "car is hit by obstacle");
                return true;
            }
        }
        GameUtil.log(TAG, "car is not hit by obstacle");
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

    /**
     * scroll
     *  scroll the track
     * @param deltaY the distance to scroll
     */
    public void scroll(float deltaY) {
        GameUtil.log(TAG, "=================> scroll: " + deltaY);
        mScrollOffsetY += deltaY;
        for (PointF cp : mCheckPoints) {
            cp.y += deltaY;
            if (cp.y > mHeight) cp.y -= mHeight; // roll back to the top if it reaches the bottom
        }
        for (Obstacle o : mObstacles) {
            PointF pos = o.getPosition();
            pos.y += deltaY;
            if (pos.y > mHeight) pos.y -= mHeight;
        }
    }

    /**
     * update
     * update the track
     * @param deltaTime the time interval
     * @param scrollSpeed the scroll speed
     */
    public void update(float deltaTime, float scrollSpeed) {
        scroll(scrollSpeed * deltaTime);
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
