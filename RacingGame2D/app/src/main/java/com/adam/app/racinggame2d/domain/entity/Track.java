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

import com.adam.app.racinggame2d.R;
import com.adam.app.racinggame2d.util.Constants;
import com.adam.app.racinggame2d.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Domain entity representing the racing track and its obstacles.
 */
public final class Track {
    private static final String sTAG = "Track";
    private static final float sCHECKPOINT_RADIUS = 30f;

    private final int mWidth;
    private final int mHeight;
    private final List<Obstacle> mObstacles;
    private List<PointF> mBackupCheckPoints;
    private List<PointF> mCheckPoints;
    private Obstacle.Type mObstacleType = Obstacle.Type.NONE;
    private Settings.GameDifficulty mDifficultySetting = Settings.GameDifficulty.EASY;

    public Track(int width, int height) {
        Utils.logDebug(sTAG, "Constructor");
        this.mWidth = width;
        this.mHeight = height;
        this.mObstacles = new ArrayList<>();
    }

    private List<PointF> generateRandomCheckpoints() {
        List<PointF> checkPoints = new ArrayList<>();
        int count = (this.mDifficultySetting != null) ? this.mDifficultySetting.getCheckpointCount() : Settings.GameDifficulty.EASY.getCheckpointCount();
        float distance = (this.mDifficultySetting != null) ? this.mDifficultySetting.getCheckpointDistance() : Settings.GameDifficulty.EASY.getCheckpointDistance();

        Random random = new Random();
        float currentY = this.mHeight * 0.2f;

        for (int i = 0; i < count; i++) {
            float x = this.mWidth * (0.2f + 0.6f * random.nextFloat());
            float randomOffset = (random.nextFloat() - 0.5f) * distance * 0.4f;
            float y = currentY + randomOffset;
            checkPoints.add(new PointF(x, y));
            currentY = distance;
        }
        return checkPoints;
    }

    public void generateRandomObstacles() {
        int count = (this.mDifficultySetting != null) ? this.mDifficultySetting.getObstacleCount() : Settings.GameDifficulty.EASY.getObstacleCount();
        Random random = new Random();
        mObstacles.clear();

        List<ObstacleData> obstacleTypes = Arrays.asList(
                new ObstacleData(Obstacle.Type.OIL, R.drawable.obstacle_oil),
                new ObstacleData(Obstacle.Type.ROCK, R.drawable.obstacle_rock),
                new ObstacleData(Obstacle.Type.BOOST, R.drawable.obstacle_boost)
        );

        for (int i = 0; i < count; i++) {
            float x = random.nextFloat() * mWidth;
            float y = random.nextFloat() * mHeight - mHeight;
            float radius = random.nextFloat() * 30f + 20f;

            float spawnRate = (this.mDifficultySetting != null) ? this.mDifficultySetting.getObstacleSpawnRate() : Settings.GameDifficulty.EASY.getObstacleSpawnRate();
            if (random.nextFloat() > spawnRate) {
                continue;
            }

            ObstacleData selected = obstacleTypes.get(random.nextInt(obstacleTypes.size()));
            mObstacles.add(new Obstacle(new PointF(x, y), radius, selected.mType, selected.mImgRes));
        }
    }

    public boolean checkCollisions(Car car, @NonNull CheckPointCallback callback) {
        PointF carPosition = car.getPosition();

        if (mCheckPoints != null) {
            if (mCheckPoints.isEmpty()) {
                mCheckPoints = Utils.deepCopyPoints(mBackupCheckPoints);
            } else {
                Iterator<PointF> iterator = mCheckPoints.iterator();
                while (iterator.hasNext()) {
                    PointF checkPoint = iterator.next();
                    float dx = carPosition.x - checkPoint.x;
                    float dy = carPosition.y - checkPoint.y;
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    if (dist <= sCHECKPOINT_RADIUS) {
                        callback.onCheckPointReached();
                        iterator.remove();
                    }
                }
            }
        }

        for (Obstacle obstacle : mObstacles) {
            float dx = carPosition.x - obstacle.getPosition().x;
            float dy = carPosition.y - obstacle.getPosition().y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist <= obstacle.getRadius()) {
                mObstacleType = obstacle.getType();
                return true;
            }
        }
        return false;
    }

    public boolean checkBoundary(Car car) {
        PointF carPosition = car.getPosition();
        return carPosition.x <= Constants.BOUNDARY_VALUE || carPosition.x >= mWidth - Constants.BOUNDARY_VALUE;
    }

    public void scroll(float deltaY) {
        for (PointF cp : mCheckPoints) {
            cp.y += deltaY;
            if (cp.y > mHeight) cp.y -= mHeight;
        }
        for (Obstacle o : mObstacles) {
            PointF pos = o.getPosition();
            pos.y += deltaY;
            if (pos.y > mHeight) pos.y -= mHeight;
        }
    }

    public void update(float deltaTime, float scrollSpeed) {
        scroll(scrollSpeed * deltaTime);
    }

    public List<PointF> getCheckPoints() {
        return mCheckPoints;
    }

    public void setCheckPoints(List<PointF> checkPoints) {
        mCheckPoints = Utils.deepCopyPoints(checkPoints);
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
        Utils.logDebug(sTAG, "reset");
        mCheckPoints = Utils.deepCopyPoints(mBackupCheckPoints);
        generateRandomObstacles();
    }

    public void applyTo(Settings.GameDifficulty difficultySetting) {
        mDifficultySetting = difficultySetting;
        this.mCheckPoints = generateRandomCheckpoints();
        mBackupCheckPoints = Utils.deepCopyPoints(mCheckPoints);
    }

    public interface CheckPointCallback {
        void onCheckPointReached();
    }

    private static final class ObstacleData {
        private final Obstacle.Type mType;
        private final int mImgRes;

        ObstacleData(Obstacle.Type type, int imgRes) {
            this.mType = type;
            this.mImgRes = imgRes;
        }
    }
}
