/**
 * This class is the game view. It is responsible for drawing the game objects on the canvas.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.adam.app.flappybird.model.Pipe;
import com.adam.app.flappybird.util.GameConstants;
import com.adam.app.flappybird.util.GameUtil;
import com.adam.app.flappybird.viewmodel.GameViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "GameSurfaceView";

    private static final long GAME_FPS = 16L;
    // executor service
    private final ExecutorService mService;
    private Future<?> mFuture;

    private long mLastTimesNs;

    // surface holder
    private SurfaceHolder mSurfaceHolder;
    private Paint mBirdPaint;
    private Paint mPipePaint;

    // view model
    private GameViewModel mViewModel;

    // constructor
    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setZOrderMediaOverlay(true);
        this.mSurfaceHolder = getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        this.mBirdPaint = new Paint();
        this.mBirdPaint.setColor(Color.RED);
        this.mBirdPaint.setTextSize(48f);
        this.mPipePaint = new Paint();
        this.mPipePaint.setColor(Color.GREEN);

        mService = Executors.newSingleThreadExecutor();
    }

    /**
     * Set the view model.
     *
     * @param viewModel the view model
     */
    public void setViewModel(GameViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    /**
     * Start the game.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            // game over
            if (mViewModel.isGameOver()) {
                GameUtil.info(TAG, "Game over!");
                break;
            }

            if (!this.mSurfaceHolder.getSurface().isValid()) continue;

            long now = System.nanoTime();
            float deltaSec = (now - mLastTimesNs) / 1_000_000_000f;
            mLastTimesNs = now;

            if (deltaSec > GameConstants.MAX_DELTA) deltaSec = GameConstants.MAX_DELTA;

            // update the game
            this.mViewModel.update(deltaSec);
            // draw
            Canvas canvas = this.mSurfaceHolder.lockCanvas();
            if (canvas != null) {
                drawGame(canvas);
                this.mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
            try {
                Thread.sleep(GAME_FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void drawGame(Canvas canvas) {
        if (mViewModel == null) {
            GameUtil.info(TAG, "mViewModel is not ready!!!");
            return;
        }

        // draw the background
        canvas.drawColor(Color.CYAN);

        drawPipes(canvas);
        drawBird(canvas);
        drawText(canvas);

    }

    private void drawPipes(Canvas canvas) {
        // set green
        mPipePaint.setColor(Color.GREEN);
        // get pipes
        List<Pipe> pipes = mViewModel.getPipes();
        for (Pipe pipe : pipes) {
            // top pipe
            canvas.drawRect(
                    pipe.getPosition().x,
                    0,
                    pipe.getRightX(),
                    pipe.getTopPipeBottomY(),
                    mPipePaint
            );

            // bottom pipe
            canvas.drawRect(
                    pipe.getPosition().x,
                    pipe.getBottomPipeTopY(),
                    pipe.getRightX(),
                    2000,
                    mPipePaint
            );
        }
    }

    private void drawBird(Canvas canvas) {
        // set yellow
        mBirdPaint.setColor(Color.YELLOW);
        // circle
        canvas.drawCircle(
                mViewModel.getBirdPosition().x,
                mViewModel.getBirdPosition().y,
                (float) GameConstants.COLLISION_RANGE,
                mBirdPaint
        );
    }

    private void drawText(Canvas canvas) {
        //TODO: draw text

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // start the game
        mLastTimesNs = System.nanoTime();
        mFuture = mService.submit(this);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        // stop the game
        try {
            mFuture.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mFuture = null;
        }

        mService.shutdown();
    }
}
