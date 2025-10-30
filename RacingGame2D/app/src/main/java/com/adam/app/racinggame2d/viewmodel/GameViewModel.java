/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the view model of Game Activity.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/29
 */
package com.adam.app.racinggame2d.viewmodel;

import android.app.Application;
import android.graphics.PointF;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.racinggame2d.model.entity.Car;
import com.adam.app.racinggame2d.model.entity.Obstacle;
import com.adam.app.racinggame2d.model.entity.Player;
import com.adam.app.racinggame2d.model.entity.Track;
import com.adam.app.racinggame2d.util.GameUtil;
import com.adam.app.racinggame2d.util.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

public class GameViewModel extends AndroidViewModel {

    // TAG
    private static final String TAG = GameViewModel.class.getSimpleName();

    // live data: score
    private MutableLiveData<Integer> mScore = new MutableLiveData<>(0);
    // live date: isGameOver
    private MutableLiveData<Boolean> mIsGameOver = new MutableLiveData<>(false);

    // Game engine
    private GameEngine mGameEngine;
    // Player
    private Player mPlayer;
    // Track
    private Track mTrack;

    // Game state
    private GameState mGameState = GameState.IDLE;

    public GameViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * get score
     */
    public LiveData<Integer> getScore() {
        return mScore;
    }

    /**
     * get isGameOver
     */
    public LiveData<Boolean> getIsGameOver() {
        return mIsGameOver;
    }

    /**
     * set game update listener
     * ```
     *
     * @param listener listener
     */
    public void setGameUpdateListener(GameEngine.GameUpdateListener listener) {
        if (mGameEngine == null) {
            GameUtil.log(TAG, "Game engine is null!");
            return;
        }

        mGameEngine.setGameUpdateListener(listener);
    }


    /**
     * update score
     */
    public void updateScore() {
        mScore.postValue(mGameEngine.getScore());
    }

    /**
     * onGameOver
     * set game over
     *
     * @param isGameOver is game over
     */
    public void onGameOver(boolean isGameOver) {
        mIsGameOver.postValue(isGameOver);
    }

    /**
     * prepareGameEngine
     * prepare game engine
     *
     * @param width  width
     * @param height height
     */
    public void prepareGameEngine(int width, int height) {
        GameUtil.log(TAG, "Prepare game engine");
        Car car = new Car("BXP1234", "car1", 100f, 30f);
        car.initPosition(width, height);
        // get player name from shared preferences
        String playerName = SharedPrefHelper.getInstance(getApplication()).getPlayerName();
        // log
        GameUtil.log(TAG, "Player name: " + playerName);
        // create player
        mPlayer = new Player(playerName, car);

        // generate list of checkpoints
        List<PointF> checkpoints = new ArrayList<>();
        checkpoints.add(new PointF(width * 0.2f, height * 0.3f));
        checkpoints.add(new PointF(width * 0.5f, height * 0.5f));
        checkpoints.add(new PointF(width * 0.8f, height * 0.7f));

        // create Track
        Track track = new Track(width, height, checkpoints);

        // create GameEngine
        mGameEngine = new GameEngine(mPlayer, track);
    }


    /**
     * startGame
     * start game engine
     */
    public void startGame() {
        GameUtil.log(TAG, "Start game");
        changeState(GameState.RUNNING);
    }

    /**
     * pauseGame
     * pause game engine
     */
    public void pauseGame() {
        GameUtil.log(TAG, "Pause game");
        changeState(GameState.PAUSE);
    }

    /**
     * resumeGame
     * resume game engine
     */
    public void resumeGame() {
        GameUtil.log(TAG, "Resume game");
        changeState(GameState.RUNNING);
    }

    /**
     * stopGame
     * stop game engine
     */
    public void stopGame() {
        GameUtil.log(TAG, "Stop game");
        changeState(GameState.IDLE);
    }

    /**
     * isReady
     * check if game is ready
     *
     * @return is ready
     */
    public boolean isReady() {
        return mGameEngine != null;
    }


    /**
     * getCheckpoints
     * get checkpoints
     *
     * @return checkpoints
     */
    public List<PointF> getCheckpoints() {
        return mGameEngine.getCheckPoints();
    }

    /**
     * getObstacles
     * get obstacles
     *
     * @return obstacles
     */
    public List<Obstacle> getObstacles() {
        return mGameEngine.getObstacles();
    }

    /**
     * getCarPosition
     * get car position
     *
     * @return car position
     */
    public PointF getCarPosition() {
        return mGameEngine.getCarPosition();
    }

    /**
     * setCarPosition
     * set car position
     *
     * @param position car position
     */
    public void setCarPosition(PointF position) {
        mGameEngine.setCarPosition(position);
    }

    /**
     * changeState
     * change game state
     *
     * @param next next state
     */
    private void changeState(GameState next) {
        if (!mGameState.canTransitionTo(next)) {
            String msg = String.format("Can not transition to %s from %s", next.name(), mGameState.name());
            GameUtil.log(TAG, msg);
            return;
        }
        mGameState = next;
        next.onEnter(this);
    }

    /**
     * moveHorizontally
     * move horizontally
     *
     * @param isLeft is left
     */
    public void moveHorizontally(boolean isLeft) {
        mGameEngine.moveHorizontally(isLeft);
    }


    /**
     * enum GameState
     * define game state
     * IDLE
     * RUNNING
     * PAUSE
     */
    private enum GameState {
        IDLE,
        RUNNING,
        PAUSE;

        /**
         * onEnter
         * enter game state
         *
         * @param vm view model
         */
        void onEnter(GameViewModel vm) {
            GameUtil.log(TAG, "Enter " + this.name());
            GameEngine gameEngine = vm.mGameEngine;
            if (gameEngine == null) {
                GameUtil.log(TAG, "Game engine is null!");
                return;
            }
            switch (this) {
                case RUNNING:
                    gameEngine.start();
                    break;
                case PAUSE:
                    gameEngine.stop();
                    break;
            }
        }

        /**
         * canTransitionTo
         * check if can transition to next state
         *
         * @param next next state
         * @return can transition
         * true: can transition
         * false: can not transition
         * @see GameState#IDLE
         * @see GameState#RUNNING
         * @see GameState#PAUSE
         */
        boolean canTransitionTo(GameState next) {
            switch (this) {
                case IDLE:
                case PAUSE:
                    return next == RUNNING || next == IDLE;
                case RUNNING:
                    return next == PAUSE || next == IDLE;
                default:
                    return false;
            }
        }
    }


}
