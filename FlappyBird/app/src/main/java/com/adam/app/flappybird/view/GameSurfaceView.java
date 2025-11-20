/**
 * This class is the game view. It is responsible for drawing the game objects on the canvas.
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-17
 */
package com.adam.app.flappybird.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.adam.app.flappybird.R;
import com.adam.app.flappybird.model.Pipe;
import com.adam.app.flappybird.util.BackgroundLayer;
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

    private BackgroundLayer mBgFar;
    private BackgroundLayer mBgNear;

    private long mLastTimesNs;

    // surface holder
    private SurfaceHolder mSurfaceHolder;
    private Paint mBirdPaint;
    private Paint mPipePaint;
    private Paint mBgPaint;

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

        this.mPipePaint = new Paint();
        this.mPipePaint.setColor(Color.GREEN);

        this.mBgPaint = new Paint();
        this.mBgPaint.setColor(Color.CYAN);


        mService = Executors.newSingleThreadExecutor();
    }

    private void initBg() {
        Bitmap bg1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_far);
        Bitmap bg2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_near);

        int w = getWidth();
        mBgFar = new BackgroundLayer(bg1, 50f, w);
        mBgNear = new BackgroundLayer(bg2, 100f, w);
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

            // update the background
            mBgFar.update(deltaSec);
            mBgNear.update(deltaSec);

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

            // update game status
            mViewModel.updateGameStatus();

        }
    }

    private void drawGame(Canvas canvas) {
        if (mViewModel == null) {
            GameUtil.info(TAG, "mViewModel is not ready!!!");
            return;
        }

        // draw the background
        mBgFar.draw(canvas, mBgPaint, 0f);
        mBgNear.draw(canvas, mBgPaint, 0f);

        //canvas.drawColor(Color.CYAN);

        // set yellow
        mViewModel.draw(canvas, mBirdPaint);
        drawPipes(canvas);
        drawText(canvas);

    }

    private void drawPipes(Canvas canvas) {
        // set green
        mPipePaint.setColor(Color.GREEN);

        // get pipes
        List<Pipe> pipes = mViewModel.getPipes();
        for (Pipe pipe : pipes) {
            float left = GameUtil.SCALE.wX(pipe.getPosition().x);
            float right = GameUtil.SCALE.wX(pipe.getRightX());
            float top = GameUtil.SCALE.wY(pipe.getTopPipeBottomY());
            float bottom = GameUtil.SCALE.wY(pipe.getBottomPipeTopY());

            // top pipe
            canvas.drawRect(
                    left,
                    0,
                    right,
                    top,
                    mPipePaint
            );

            // bottom pipe
            canvas.drawRect(
                    left,
                    bottom,
                    right,
                    GameUtil.SCALE.wY(canvas.getHeight()),
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
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        GameUtil.info(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        GameUtil.info(TAG, "surfaceCreated");
        GameUtil.info(TAG, "Width: " + getWidth() + ", Height: " + getHeight() + ".");
        int w = getWidth();
        int h = getHeight();

        //SCALE.setScale((float) w / GameConstants.SCREEN_WIDTH, (float) h / GameConstants.SCREEN_HEIGHT);
        GameUtil.info(TAG, "Scale: " + GameUtil.SCALE.getScaleX() + ", " + GameUtil.SCALE.getScaleY());

        // init background
        initBg();

        // init
        mViewModel.init(w, h);
        // start game
        mLastTimesNs = System.nanoTime();
        mViewModel.startGame();
        mFuture = mService.submit(this);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        GameUtil.info(TAG, "surfaceDestroyed");
        // stop the game
        try {
            mFuture.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mFuture = null;
        }

        mService.shutdown();

        // release resources
        release();
    }

    /**
     * Release the resources.
     */
    private void release() {
        if (mBgFar != null) {
            mBgFar.release();
            mBgFar = null;
        }

        if (mBgNear != null) {
            mBgNear.release();
            mBgNear = null;
        }
    }

}
