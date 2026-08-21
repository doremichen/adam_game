/*
 * Copyright (c) 2026 Adam Chen
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

package com.adam.app.galaga.ui.adapter;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.adam.app.galaga.engine.Direction;
import com.adam.app.galaga.engine.GameEngine;
import com.adam.app.galaga.viewmodel.GameViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class UIBinding {

    private static int sActiveDirectionId = View.NO_ID;

    @BindingAdapter("onTouchDirection")
    public static void setOnTouchDirection(View view, GameViewModel viewModel) {
        int id = view.getId();
        Direction direction = Direction.fromResId(id);
        
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sActiveDirectionId = id;
                    viewModel.setMoveDirection(direction);
                    v.setPressed(true);
                    v.performClick();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    int x = v.getLeft() + (int) event.getX();
                    int y = v.getTop() + (int) event.getY();
                    if (!rect.contains(x, y)) {
                        handleRelease(view, id, viewModel);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    handleRelease(view, id, viewModel);
                    break;
            }
            return true;
        });
    }

    private static void handleRelease(View view, int id, GameViewModel viewModel) {
        view.setPressed(false);
        if (sActiveDirectionId == id) {
            viewModel.setMoveDirection(null);
            sActiveDirectionId = View.NO_ID;
        }
    }

    @BindingAdapter("onTouchFire")
    public static void setOnTouchFire(View view, GameViewModel viewModel) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    viewModel.setShooting(true);
                    v.setPressed(true);
                    v.performClick();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    viewModel.setShooting(false);
                    v.setPressed(false);
                    break;
            }
            return true;
        });
    }

    @BindingAdapter("formattedDate")
    public static void setFormattedDate(TextView view, long timestamp) {
        if (timestamp <= 0) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        view.setText(sdf.format(timestamp));
    }

    @BindingAdapter("gameState")
    public static void handleGameStateChange(View view, GameEngine.State state) {
        if (state == GameEngine.State.CLEARED) {
            view.setVisibility(View.VISIBLE);
            view.setAlpha(0f);
            view.setScaleX(0.8f);
            view.setScaleY(0.8f);

            view.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(500)
                    .setInterpolator(new OvershootInterpolator())
                    .start();

            view.postDelayed(() -> view.animate().alpha(0f).setDuration(400).start(), 1500);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
