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

package com.adam.app.galaga.data.model.bee.state;

import com.adam.app.galaga.data.model.Bee;
import com.adam.app.galaga.utils.GameConstants;
import java.util.Random;

public class FormationState implements BeeState {
    private final Random mRandom = new Random();
    private long mLastTurnTime = 0;
    private float mAngle;

    public FormationState() {
        mAngle = (float) (mRandom.nextFloat() * Math.PI * 2);
    }

    @Override
    public void update(BeeStateContext context, Bee bee) {
        long currentTime = System.currentTimeMillis();
        
        if (mRandom.nextFloat() < GameConstants.BEE_DIVE_PROBABILITY) {
            context.changeState(new DivingState());
            return;
        }

        if (currentTime - mLastTurnTime >= GameConstants.BEE_TURN_INTERVAL_MS) {
            mAngle += (float) ((mRandom.nextFloat() - 0.5f) * (Math.PI / 2));
            mLastTurnTime = currentTime;
        }

        float targetDx = bee.getTargetPosition().x - bee.getPosition().x;
        float targetDy = bee.getTargetPosition().y - bee.getPosition().y;

        bee.getPosition().x += (float) (Math.cos(mAngle) * bee.getSpeed() * 0.5f) + targetDx * 0.1f;
        bee.getPosition().y += (float) (Math.sin(mAngle) * bee.getSpeed() * 0.5f) + targetDy * 0.1f;
        
        bee.handleWorldBoundaries();
    }
}
