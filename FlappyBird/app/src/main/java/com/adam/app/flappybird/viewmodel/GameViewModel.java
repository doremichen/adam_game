/**
 * This class is the view model for the Flappy Bird game.
 * It contains the game state and the bird object.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.viewmodel;

import android.app.Application;
import android.graphics.PointF;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.flappybird.model.Bird;
import com.adam.app.flappybird.model.GameState;
import com.adam.app.flappybird.model.Pipe;
import com.adam.app.flappybird.util.GameUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameViewModel extends AndroidViewModel {
    // Live data: game state
    private final MutableLiveData<GameState> mGameState = new MutableLiveData<>(GameState.READY);
    private final MutableLiveData<Integer> mScore = new MutableLiveData<>(0);

    private int lastScoredPipeIndex = -1;


    private Bird mBird;
    private List<Pipe> mPipes = new ArrayList<>();


    private Random mRandom = new Random();
    private float mPipeSpawnX = 1200f;
    private float mPipeSpawnInterval = 600f;

    // Constructor
    public GameViewModel(@NonNull Application application) {
        super(application);
        resetGame();

    }

    /**
     * reset game
     */
    private void resetGame() {
        this.mBird = new Bird(new PointF(200f, 300f));
        this.mPipes.clear();
        this.mPipes.add(new Pipe(new PointF(mPipeSpawnX, mRandom.nextInt(400) + 200)));
        // set game state as ready
        this.mGameState.postValue(GameState.READY);
    }

    /**
     * start game
     */
    public void startGame() {
        this.mGameState.postValue(GameState.RUNNING);
    }

    /**
     * stop game
     */
    public void stopGame() {
        this.mGameState.postValue(GameState.READY);
    }

    /**
     * update
     */
    public void update() {
        if (mGameState.getValue() == GameState.RUNNING) {
            // update bird
            mBird.update();
            // update pipes
            handlePipes();
            // update game status
            updateGameStatus();
        }
    }


    /**
     * update game status
     */
    private void updateGameStatus() {
        // boundary
        if (isCollidingWithBounds()) return;
        // pipe
        for (int i = 0; i < mPipes.size(); i++) {
            Pipe pipe = mPipes.get(i);
            if (isCollidingWithPipes(pipe)) return;

            // check if bird passed pipe
            updateScore(pipe);

        }

    }

    /**
     * update score
     *
     * @param pipe pipe
     */
    private void updateScore(Pipe pipe) {
        if (!pipe.isMarkScored() && mBird.getPosition().x > pipe.getPosition().x + pipe.getPipeWidth()) {
            addScore();
            // mark pipe as scored
            pipe.setMarkScored(true);
        }
    }

    /**
     * check if colliding with pipes
     * @param pipe pipe
     * @return true if colliding with pipes otherwise false
     */
    private boolean isCollidingWithPipes(Pipe pipe) {
        boolean inXrange = mBird.getPosition().x + GameUtil.COLLISION_RANGE > pipe.getPosition().x
                && mBird.getPosition().x - GameUtil.COLLISION_RANGE < pipe.getPosition().x + pipe.getPipeWidth();
        boolean inYrange = mBird.getPosition().y + GameUtil.COLLISION_RANGE > pipe.getBottomPipeTopY()
                || mBird.getPosition().y - GameUtil.COLLISION_RANGE < pipe.getTopPipeBottomY();
        if (inXrange && inYrange) {
            mGameState.postValue(GameState.GAME_OVER);
            return true;
        }
        return false;
    }

    /**
     * check if colliding with bounds
     * @return true if colliding with bounds otherwise false
     */
    private boolean isCollidingWithBounds() {
        if (mBird.getPosition().y < 0 || mBird.getPosition().y > 2000) {
            mGameState.postValue(GameState.GAME_OVER);
            return true;
        }
        return false;
    }

    /**
     * produce new pipe and move
     */
    private void handlePipes() {
        List<Pipe> newPipes = new ArrayList<>();
        for (Pipe pipe : mPipes) {
            pipe.update();
            if (pipe.getPosition().x + pipe.getPipeWidth() > 0) {
                newPipes.add(pipe);
            }
        }
        // update list of pipes
        mPipes.clear();
        this.mPipes = newPipes;
        // add new pipe
        Pipe lastPipe = mPipes.get(mPipes.size() - 1);
        if (lastPipe.getPosition().x < mPipeSpawnX  - mPipeSpawnInterval) {
            mPipes.add(new Pipe(new PointF(mPipeSpawnX, mRandom.nextInt(400) + 200)));
        }
    }

    /**
     * flap
     */
    public void flap() {
        if (mGameState.getValue() == GameState.RUNNING) {
            mBird.flap();
        }

        if (mGameState.getValue() == GameState.GAME_OVER) {
            resetGame();
        }
    }

    // --- setter ---

    /**
     * add score
     */
    public void addScore() {
        mScore.postValue(mScore.getValue() + 5);
    }



    // --- getter ---
    public PointF getBirdPosition() {
        return mBird.getPosition();
    }

    public List<Pipe> getPipes() {
        return mPipes;
    }

    public LiveData<GameState> getGameState() {
        return mGameState;
    }

    public LiveData<Integer> getScore() {
        return mScore;
    }

    public boolean isGameOver() {
        return mGameState.getValue() == GameState.GAME_OVER;
    }
}
