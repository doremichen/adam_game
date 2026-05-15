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

package com.adam.app.galaga.engine.strategy;

import android.graphics.PointF;
import com.adam.app.galaga.utils.GameConstants;

/**
 * Circle entry strategy with spinning and approach phases.
 */
public class CircleEntryStrategy extends BaseEntryStrategy {

    public CircleEntryStrategy() {
        super(GameConstants.CIRCLE_DURATION);
    }

    @Override
    public PointF getPosition(long elapsedMillis, PointF targetPos) {
        float t = Math.min(1.0f, (float) elapsedMillis / mDuration);
        
        if (t < GameConstants.CIRCLE_PHASE_THRESHOLD) {
            float spinT = t / GameConstants.CIRCLE_PHASE_THRESHOLD;
            return calculateSpinPosition(spinT);
        }

        float moveT = (t - GameConstants.CIRCLE_PHASE_THRESHOLD) / (1.0f - GameConstants.CIRCLE_PHASE_THRESHOLD);
        return calculateMovePosition(moveT, targetPos);
    }

    private PointF calculateSpinPosition(float t) {
        float radius = GameConstants.CIRCLE_RADIUS_START * (1 - t) + GameConstants.CIRCLE_RADIUS_END;
        float angle = t * GameConstants.CIRCLE_TOTAL_ANGLES;
        float centerX = GameConstants.GAME_WIDTH / 2f;
        float centerY = GameConstants.GAME_HEIGHT * GameConstants.CIRCLE_CENTER_Y_RATIO;
        
        float x = centerX + (float) Math.cos(angle) * radius;
        float y = centerY + (float) Math.sin(angle) * radius - (1 - t) * GameConstants.CIRCLE_Y_OFFSET_START;
        return new PointF(x, y);
    }

    private PointF calculateMovePosition(float t, PointF targetPos) {
        float startX = GameConstants.GAME_WIDTH / 2f + (float) Math.cos(GameConstants.CIRCLE_TOTAL_ANGLES) * GameConstants.CIRCLE_RADIUS_END;
        float startY = GameConstants.GAME_HEIGHT * GameConstants.CIRCLE_CENTER_Y_RATIO + (float) Math.sin(GameConstants.CIRCLE_TOTAL_ANGLES) * GameConstants.CIRCLE_RADIUS_END;
        
        float x = startX + (targetPos.x - startX) * t;
        float y = startY + (targetPos.y - startY) * t;
        return new PointF(x, y);
    }
}
