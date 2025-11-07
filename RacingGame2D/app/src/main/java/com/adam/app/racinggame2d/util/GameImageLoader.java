/**
 * Copyright 2025 - Adam Game. All rights reserved.
 * <p>
 * Description: This class is the image loader for the game.
 * <p>
 * Author: Adam Game
 * Created Date: 2025/10/30
 */
package com.adam.app.racinggame2d.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * General image loader that automatically loads PNG / SVG, and supports caching and scaling.
 */
public class GameImageLoader {
    // TAG
    private static final String TAG = "GameImageLoader";
    // Map: String -> Bitmap
    private static final Map<String, Bitmap> CACHE = new HashMap<>();

    /**
     * load
     * load picture from assets (support PNG/SVG)
     * @param context Context
     * @param assetPath asset path
     * @param targetWidth target width
     * @param targetHeight target height
     * @return Bitmap
     */
    public static Bitmap load(Context context, String assetPath, int targetWidth, int targetHeight) {
        try {
            // get bitmap from cache
            if (CACHE.containsKey(assetPath)) {
                return CACHE.get(assetPath);
            }

            Bitmap bitmap;
            // check if svg file in assets
            if (assetPath.toLowerCase().endsWith(".svg")) {
                bitmap = loadSvg(context, assetPath, targetWidth, targetHeight);
            } else {
                bitmap = loadPng(context, assetPath, targetWidth, targetHeight);
            } 

            // put in CACHE
            if (bitmap != null) {
                CACHE.put(assetPath, bitmap);
            }
            return bitmap;
        } catch (Exception e) {
            GameUtil.error(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * loadPng
     * load png from assets
     * @param context Context
     * @param assetPath asset path
     * @param targetWidth target width
     * @param targetHeight target height
     * @return Bitmap
     * @throws IOException IOException
     */
    private static Bitmap loadPng(Context context, String assetPath, int targetWidth, int targetHeight) throws IOException {
        InputStream is = context.getAssets().open(assetPath);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        is.close();
        if (bitmap == null) {
            return null;
        }
        if (targetWidth > 0 && targetHeight > 0) {
            // auto scale bitmap
            return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
        }

        return bitmap;
    }

    private static Bitmap loadSvg(Context context, String assetPath, int targetWidth, int targetHeight) throws IOException, SVGParseException {
        InputStream is = context.getAssets().open(assetPath);
        SVG svg = SVG.getFromInputStream(is);
        is.close();
        if (svg == null) {
            return null;
        }

        Picture picture = svg.renderToPicture();
        PictureDrawable drawable = new PictureDrawable(picture);

        // create bitmap from drawable
        Bitmap bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(targetWidth / (float) drawable.getIntrinsicWidth(), targetHeight / (float) drawable.getIntrinsicHeight());

        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * clearCache
     * clear cache
     */
    public static void clearCache() {
        for (Bitmap b : CACHE.values()) {
            if (b != null && !b.isRecycled()) {
                b.recycle();
            }
        }
        CACHE.clear();
    }

}
