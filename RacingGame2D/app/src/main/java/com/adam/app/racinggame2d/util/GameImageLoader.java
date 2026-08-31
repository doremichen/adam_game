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
 * Image loader for loading PNG and SVG assets with caching.
 */
public final class GameImageLoader {
    private static final String sTAG = "GameImageLoader";
    private static final Map<String, Bitmap> sCACHE = new HashMap<>();

    private GameImageLoader() {
        // Prevent instantiation
    }

    public static Bitmap load(Context context, String assetPath, int targetWidth, int targetHeight) {
        try {
            if (sCACHE.containsKey(assetPath)) {
                return sCACHE.get(assetPath);
            }

            Bitmap bitmap;
            if (assetPath.toLowerCase().endsWith(".svg")) {
                bitmap = loadSvg(context, assetPath, targetWidth, targetHeight);
            } else {
                bitmap = loadPng(context, assetPath, targetWidth, targetHeight);
            }

            if (bitmap != null) {
                sCACHE.put(assetPath, bitmap);
            }
            return bitmap;
        } catch (Exception e) {
            Utils.logError(sTAG, e.getMessage());
        }
        return null;
    }

    private static Bitmap loadPng(Context context, String assetPath, int targetWidth, int targetHeight) throws IOException {
        try (InputStream is = context.getAssets().open(assetPath)) {
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            if (bitmap == null) return null;
            if (targetWidth > 0 && targetHeight > 0) {
                return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
            }
            return bitmap;
        }
    }

    private static Bitmap loadSvg(Context context, String assetPath, int targetWidth, int targetHeight) throws IOException, SVGParseException {
        try (InputStream is = context.getAssets().open(assetPath)) {
            SVG svg = SVG.getFromInputStream(is);
            if (svg == null) return null;

            Picture picture = svg.renderToPicture();
            PictureDrawable drawable = new PictureDrawable(picture);
            Bitmap bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.scale(targetWidth / (float) drawable.getIntrinsicWidth(), targetHeight / (float) drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    public static void clearCache() {
        for (Bitmap b : sCACHE.values()) {
            if (b != null && !b.isRecycled()) {
                b.recycle();
            }
        }
        sCACHE.clear();
    }
}
