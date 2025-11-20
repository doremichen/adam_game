/**
 * This class is the pipes manager that manages the pipes in the game.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-18
 */
package com.adam.app.flappybird.manager;

import android.graphics.PointF;

import com.adam.app.flappybird.model.Pipe;
import com.adam.app.flappybird.util.GameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PipesManager {
    // TAG
    private static final String TAG = "PipesManager";

    // List of pipes
    private final List<Pipe> mPipes = new ArrayList<>();
    // random
    private final Random mRandom = new Random();
    // screen width
    private float mScreenWidth;
    private float mScreenHeight;
    private float mSpawnX;  // initial spawn x coordinate
    private float mSpawnInterval; // px interval between pipes


    // Constructor
    public PipesManager(float screenWidth, float screenHeight) {
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mSpawnX = screenWidth + 200f;
        mSpawnInterval = GameConstants.SPAWN_INTERVAL;

        //initInitialPipes();
    }

    public void initInitialPipes() {
        // clear
        mPipes.clear();
        // add
        mPipes.add(new Pipe(new PointF(mSpawnX, randomGapY())));
        mPipes.add(new Pipe(new PointF(mSpawnX + mSpawnInterval, randomGapY())));
    }

    private float randomGapY() {
        // keep gap center inside reasonable bounds
        float margin = 200f;
        float min = margin + GameConstants.PIPE_GAP / 2f;
        float max = mScreenHeight - margin - GameConstants.PIPE_GAP / 2f; // assume screen height ~1200; fine to clamp at runtime
        return min + mRandom.nextFloat() * (max - min);
    }

    /**
     * getPipesSnapshot
     *
     * @return List<Pipe>
     */
    public List<Pipe> getPipesSnapshot() {
        return new ArrayList<>(mPipes);
    }

    /**
     * update pipes position
     *
     * @param deltaTime delta time
     */
    public void update(float deltaTime) {
        // update pipe position
        for (Pipe pipe : mPipes) {
            pipe.update(deltaTime);
        }
        // remove pipes that fully left screen
        mPipes.removeIf(pipe -> pipe.getRightX() < -50f);

        // spawn new pipe if last pipe is sufficiently left
        Pipe lastPipe = mPipes.get(mPipes.size() - 1);
        if (lastPipe.getPosition().x < mSpawnX - mSpawnInterval) {
            mPipes.add(new Pipe(new PointF(mSpawnX, randomGapY())));
        }
    }

}
