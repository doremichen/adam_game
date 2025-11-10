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
import com.adam.app.racinggame2d.util.Constants;
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
    // List of Obstacle
    private final List<Obstacle> mObstacles;
    // backup check points
    private final List<PointF> mBackupCheckPoints;
    // mCheckPoints: List<float[]>
    private List<PointF> mCheckPoints;
    // Obstacle type
    private Obstacle.Type mObstacleType = Obstacle.Type.NONE;
    // scroll offset
    private float mScrollOffsetY = 0f;

    // game difficulty setting
    private Settings.GameDifficulty mDifficultySetting = Settings.GameDifficulty.EASY;

    /**
     * Constructor
     *
     * @param width       width of screen
     * @param height      height of screen
     * @param checkPoints List of check points
     */
    public Track(int width, int height, List<PointF> checkPoints) {
        GameUtil.log(TAG, "Constructor");
        this.mWidth = width;
        this.mHeight = height;

        // dump checkPoints
        GameUtil.dumpList("checkPoints", checkPoints);

        this.mCheckPoints = checkPoints != null ? checkPoints : new ArrayList<>();
        adjustCheckPointsSpacing();

        mBackupCheckPoints = GameUtil.deepCopyPoints(mCheckPoints);

        // dump backup checkPoints
        GameUtil.dumpList("mBackupCheckPoints", mBackupCheckPoints);


        this.mObstacles = new ArrayList<>();
    }


    private void adjustCheckPointsSpacing() {
        if (this.mDifficultySetting == null || mCheckPoints == null) return;

        float factor = this.mDifficultySetting.getCheckpointDistance();
        for (int i = 1; i < mCheckPoints.size(); i++) {
            PointF prev = mCheckPoints.get(i - 1);
            PointF curr = mCheckPoints.get(i);
            curr.y = prev.y - factor;  // adjust distance
        }
    }


    /**
     * generateRandomObstacles
     * generate random obstacles with counts
     */
    public void generateRandomObstacles() {
       int count = (this.mDifficultySetting != null)? this.mDifficultySetting.getObstacleCount() : Settings.GameDifficulty.EASY.getObstacleCount();
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

            // spawn rate
            float spawnRate = (this.mDifficultySetting != null)? this.mDifficultySetting.getObstacleSpawnRate() : Settings.GameDifficulty.EASY.getObstacleSpawnRate();
            if (random.nextFloat() > spawnRate) {
                continue;
            }

            ObstacleData selected = obstacleTypes.get(random.nextInt(obstacleTypes.size()));
            mObstacles.add(new Obstacle(new PointF(x, y), radius, selected.type, selected.imgRes));

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
        GameUtil.log(TAG, "carPosition.x: " + carPosition.x + ", carPosition.y: " + carPosition.y);

        // check points
        if (mCheckPoints != null) {
            if (mCheckPoints.isEmpty()) {
                GameUtil.log(TAG, "check points is empty");
                // restore check points
                mCheckPoints = GameUtil.deepCopyPoints(mBackupCheckPoints);
            } else {
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
        }

        // check collisions with obstacles
        for (Obstacle obstacle : mObstacles) {
            float dx = carPosition.x - obstacle.getPosition().x;
            float dy = carPosition.y - obstacle.getPosition().y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist <= obstacle.getRadius()) {
                GameUtil.log(TAG, "car is hit by obstacle");
                mObstacleType = obstacle.getType();
                return true;
            }
        }
        GameUtil.log(TAG, "car is not hit by obstacle");
        return false;
    }


    /**
     * checkBoundary
     * check if the car is out of boundary
     *
     * @param car Car
     * @return boolean
     * true: if the car is out of boundary
     * false: if the car is not out of boundary
     */
    public boolean checkBoundary(Car car) {
        // car position
        PointF carPosition = car.getPosition();
        return carPosition.x <= Constants.BOUNDARY_VALUE || carPosition.x >= mWidth - Constants.BOUNDARY_VALUE;
    }


    /**
     * scroll
     * scroll the track
     *
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
     *
     * @param deltaTime   the time interval
     * @param scrollSpeed the scroll speed
     */
    public void update(float deltaTime, float scrollSpeed) {
        scroll(scrollSpeed * deltaTime);
    }

    //--- get ---
    public List<PointF> getCheckPoints() {
        return mCheckPoints;
    }

    //--- set ---
    public void setCheckPoints(List<PointF> checkPoints) {
        mCheckPoints = GameUtil.deepCopyPoints(checkPoints);
    }

    public List<Obstacle> getObstacles() {
        return mObstacles;
    }

    public Obstacle.Type getObstacleType() {
        return mObstacleType;
    }


    public float getWidth() {
        return mWidth;
    }

    public float getHeight() {
        return mHeight;
    }

    public void reset() {
        GameUtil.log(TAG, "reset");
        mScrollOffsetY = 0f;
        mCheckPoints = GameUtil.deepCopyPoints(mBackupCheckPoints);
        generateRandomObstacles();
    }

    /**
     * applyTo
     * apply the settings to the game
     *
     * @param difficultySetting GameDifficulty
     */
    public void applyTo(Settings.GameDifficulty difficultySetting) {
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

    /**
     * ObstacleData
     * This class is used to record the obstacle data
     * type: Obstacle.Type
     * imgPath: String
     */
    private static class ObstacleData {
        Obstacle.Type type;
        int imgRes;

        ObstacleData(Obstacle.Type type, int imgRes) {
            this.type = type;
            this.imgRes = imgRes;
        }
    }

}
