/**
 * This class is used to provide draw pipe methods
 *
 * @author Adam Chen
 * @version 1.0
 * @since 2025-11-20
 */
package com.adam.app.flappybird.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

import com.adam.app.flappybird.R;

public class PipeRenderUtil {
    private final Paint mPipePaint;
    private final Paint mCapPaint;

    private final Bitmap mBitmap;
    private final boolean mUseBitmap;

    // --- Constants ---
    private static final float PIPE_CORNER_RADIUS = 20f;
    private static final float PIPE_CAP_MARGIN = 5f;
    private static final int PIPE_COLOR_START = Color.rgb(0, 150, 0);
    private static final int PIPE_COLOR_END = Color.rgb(0, 200, 0);

    /**
     * constructor
     */
    public PipeRenderUtil(Context context, boolean useBitmap) {
        mPipePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPipePaint.setStyle(Paint.Style.FILL);
        // set shadow layer
        mPipePaint.setShadowLayer(10, 5, 5, Color.argb(80, 0, 100, 0));

        mCapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // set color
        mCapPaint.setColor(Color.argb(0, 120, 0, 0));

        mUseBitmap = useBitmap;
        mBitmap = useBitmap ? BitmapFactory.decodeResource(context.getResources(), R.drawable.pipe) : null;
    }

    /**
     * draw pipes
     * @param canvas Canvas
     * @param left float
     * @param right float
     * @param top float
     * @param bottom float
     */
    public void drawPipe(Canvas canvas,
                         float left,
                         float right,
                         float top,
                         float bottom) {
        // which draw
        if (this.mUseBitmap && this.mBitmap != null) {
            drawPipeBitmap(canvas, left, right, top, bottom);
        } else {
            drawPipeGradient(canvas, left, right, top, bottom);
        }

    }

    /**
     * draw pipe with gradient
     * @param canvas Canvas
     * @param left float
     * @param right float
     * @param top float
     * @param bottom float
     */
    private void drawPipeGradient(Canvas canvas, float left, float right, float top, float bottom) {
        float pipeWidth = right - left;
        float capHeight = pipeWidth * 0.2f;

        // Top pipe
        drawPipeSegment(canvas, left, right, 0, top, capHeight, mPipePaint, mCapPaint);

        // Bottom pipe
        drawPipeSegment(canvas, left, right, bottom, canvas.getHeight(), capHeight, mPipePaint, mCapPaint);
    }

    private void drawPipeBitmap(Canvas canvas, float left, float right, float top, float bottom) {
        Rect src = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        Rect dstTop = new Rect((int) left, 0, (int) right, (int) top);
        Rect dstBottom = new Rect((int) left, (int) bottom, (int) right, canvas.getHeight());
        canvas.drawBitmap(mBitmap, src, dstTop, null);
        canvas.drawBitmap(mBitmap, src, dstBottom, null);
    }

    private void drawPipeSegment(Canvas canvas,
                                 float left, float right,
                                 float top, float bottom,
                                 float capHeight,
                                 Paint pipePaint, Paint capPaint) {
        // Create gradient
        LinearGradient gradient = new LinearGradient(
                left, top, right, bottom,
                PIPE_COLOR_START, PIPE_COLOR_END,
                Shader.TileMode.CLAMP
        );
        pipePaint.setShader(gradient);

        // draw cap
        canvas.drawRect(left - PIPE_CAP_MARGIN, bottom,
                right + PIPE_CAP_MARGIN, bottom + capHeight, capPaint);

        // draw round pipe
        canvas.drawRoundRect(left, top, right, bottom,
                PIPE_CORNER_RADIUS, PIPE_CORNER_RADIUS, pipePaint);

        // draw cap
        canvas.drawRect(left - PIPE_CAP_MARGIN, bottom,
                right + PIPE_CAP_MARGIN, bottom + capHeight, capPaint);

        // clear Shader
        pipePaint.setShader(null);
    }
}
