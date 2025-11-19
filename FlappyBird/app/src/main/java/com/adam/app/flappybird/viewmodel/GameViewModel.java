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
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adam.app.flappybird.R;
import com.adam.app.flappybird.manager.PipesManager;
import com.adam.app.flappybird.model.Bird;
import com.adam.app.flappybird.model.GameState;
import com.adam.app.flappybird.model.Pipe;
import com.adam.app.flappybird.util.GameConstants;
import com.adam.app.flappybird.util.GameUtil;
import com.adam.app.flappybird.util.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameViewModel extends AndroidViewModel {
    // TAG
    private static final String TAG = GameViewModel.class.getSimpleName();

    // Live data: game state
    private final MutableLiveData<GameState> mGameState = new MutableLiveData<>(GameState.READY);
    private final MutableLiveData<Integer> mScore = new MutableLiveData<>(0);
    private final SoundPlayer mPlayer;
    private Bird mBird;
    private PipesManager mPipesManager;
    private float mScreenWidth = GameConstants.SCREEN_WIDTH;
    private float mScreenHeight = GameConstants.SCREEN_HEIGHT;

    private Context mContext;

    // Constructor
    public GameViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mPlayer = new SoundPlayer(mContext, true);
    }

    public void init(float screenWidth, float screenHeight) {
        // init width and height
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        // get bird bmp
        Bitmap birdBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bird);
        mBird = new Bird(birdBitmap, new PointF(200f, 300f));
        mPipesManager = new PipesManager(screenWidth);

        // set game state as ready
        this.mGameState.postValue(GameState.READY);
        this.mScore.postValue(0);
    }


    /**
     * start game
     */
    public void startGame() {
        initPipes();
        this.mGameState.postValue(GameState.RUNNING);
    }

    private void initPipes() {
        if (!validCheck()) return;
        // reset pipes
        this.mPipesManager.initInitialPipes();
    }

    /**
     * stop game
     */
    public void stopGame() {
        this.mGameState.postValue(GameState.READY);
    }

    /**
     * update
     *
     * @param deltaSec delta seconds
     */
    public void update(float deltaSec) {
        if (!validCheck()) return;

        if (mGameState.getValue() != GameState.RUNNING) {
            GameUtil.info(TAG, "Game is not running");
            return;
        }

        // cap delta
        if (deltaSec > GameConstants.MAX_DELTA) deltaSec = GameConstants.MAX_DELTA;

        // update bird
        mBird.update(deltaSec);
        // update pipes
        mPipesManager.update(deltaSec);
        // update game status
        updateGameStatus();

    }

    /**
     * update game status
     */
    private void updateGameStatus() {

        // check collision
        if (checkCollision()) {
            mGameState.postValue(GameState.GAME_OVER);
            return;
        }

    }

    private boolean checkCollision() {
        // bounds
        if (mBird.getPosition().y - GameConstants.BIRD_RADIUS < 0 ||
                mBird.getPosition().y + GameConstants.BIRD_RADIUS > mScreenHeight) {
            // play hit short sound
            mPlayer.playHitSound();
            return true;
        }

        float birdX = mBird.getPosition().x;
        float birdY = mBird.getPosition().y;

        // pipes
        for (Pipe p : mPipesManager.getPipesSnapshot()) {
            float pipeX = p.getPosition().x;
            boolean inXrange = birdX + GameConstants.BIRD_RADIUS > pipeX &&
                    birdX - GameConstants.BIRD_RADIUS < p.getRightX();
            boolean outOfGap = birdY - GameConstants.BIRD_RADIUS < p.getTopPipeBottomY() ||
                    birdY + GameConstants.BIRD_RADIUS > p.getBottomPipeTopY();

            if (inXrange && outOfGap) {
                // play hit short sound
                mPlayer.playHitSound();
                updateScore(p);
                return true;
            }

            updateScore(p);
        }
        return false;
    }

    /**
     * update score
     *
     * @param pipe pipe
     */
    private void updateScore(Pipe pipe) {
        if (!pipe.isMarkScored() && mBird.getPosition().x > pipe.getRightX()) {
            // play point short sound
            mPlayer.playPointSound();

            addScore();
            // mark pipe as scored
            pipe.setMarkScored(true);
        }
    }


    /**
     * flap
     */
    public void flap() {
        if (!validCheck()) return;
        if (mGameState.getValue() == GameState.RUNNING) {
            // play swing sound
            mPlayer.playSwingSound();
            mBird.flap();
        }
    }

    /**
     * draw
     * @param canvas canvas
     * @param paint paint
     */
    public void draw(Canvas canvas, Paint paint) {
        // draw bird
        mBird.draw(canvas, paint);
    }

    public void release() {
        mPlayer.release();
    }

    /**
     * add score
     */
    public void addScore() {
        mScore.postValue(mScore.getValue() + 5);
    }

    public PointF getBirdPosition() {
        PointF position = (validCheck()) ? mBird.getPosition() : new PointF(0, 0);
        return position;
    }

    public List<Pipe> getPipes() {
        List<Pipe> pipes = (validCheck()) ? mPipesManager.getPipesSnapshot() : new ArrayList<>();
        return pipes;
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

    private boolean validCheck() {
        if (mBird == null || mPipesManager == null) {
            GameUtil.info(TAG, "Please check initialization!!!");
            return false;
        }
        return true;
    }
}
