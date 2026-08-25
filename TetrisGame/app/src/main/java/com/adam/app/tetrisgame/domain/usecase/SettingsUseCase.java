/*
 * Copyright (c) 2026 Adam
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
package com.adam.app.tetrisgame.domain.usecase;

import com.adam.app.tetrisgame.domain.bridge.SettingsBridge;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SettingsUseCase {
    public enum Action {
        IS_SOUND_ENABLED,
        SET_SOUND_ENABLED,
        GET_SPEED,
        SET_SPEED,
        GET_HIGH_SCORE,
        SAVE_HIGH_SCORE
    }

    private final SettingsBridge mBridge;

    @Inject
    public SettingsUseCase(SettingsBridge bridge) {
        this.mBridge = bridge;
    }

    public Object execute(Action action, Object param) {
        switch (action) {
            case IS_SOUND_ENABLED:
                return mBridge.isSoundEffectEnabled();
            case SET_SOUND_ENABLED:
                mBridge.setSoundEffectEnabled((boolean) param);
                break;
            case GET_SPEED:
                try {
                    return Integer.parseInt(mBridge.getSpeed());
                } catch (NumberFormatException e) {
                    return 1;
                }
            case SET_SPEED:
                mBridge.setSpeed((String) param);
                break;
            case GET_HIGH_SCORE:
                return mBridge.getHighScore();
            case SAVE_HIGH_SCORE:
                int score = (int) param;
                if (score > mBridge.getHighScore()) {
                    mBridge.setHighScore(score);
                }
                break;
        }
        return null;
    }
}
