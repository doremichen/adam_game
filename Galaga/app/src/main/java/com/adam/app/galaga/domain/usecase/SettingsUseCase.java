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

package com.adam.app.galaga.domain.usecase;

import com.adam.app.galaga.data.local.prefs.GameSettings;
import com.adam.app.galaga.domain.repository.ISettingsRepository;

import javax.inject.Inject;

/**
 * SettingsUseCase - Enum based UseCase implementation for app settings
 */
public class SettingsUseCase implements IUseCase<SettingsUseCase.Request, Object> {

    private final SettingsUCBridge mBridge;

    @Inject
    public SettingsUseCase(SettingsUCBridge bridge) {
        this.mBridge = bridge;
    }

    @Override
    public Object execute(Request request) {
        ActionType type = request.getType();
        type.setData(request.getData());
        return type.execute(new ExecutionContext(mBridge));
    }

    public static class Request {
        private final ActionType mType;
        private final Object mData;

        public Request(ActionType type, Object data) {
            this.mType = type;
            this.mData = data;
        }

        public ActionType getType() { return mType; }
        public Object getData() { return mData; }
    }

    public enum ActionType {
        UPDATE_AUTO_FIRE {
            @Override
            public Object execute(ExecutionContext context) {
                context.getBridge().setAutoFire((Boolean) getData());
                return null;
            }
        },
        UPDATE_SHOT_STYLE {
            @Override
            public Object execute(ExecutionContext context) {
                context.getBridge().setShotStyle((GameSettings.ShotStyle) getData());
                return null;
            }
        },
        UPDATE_SOUND_EFFECTS {
            @Override
            public Object execute(ExecutionContext context) {
                context.getBridge().setSoundEffects((Boolean) getData());
                return null;
            }
        },
        UPDATE_BGM {
            @Override
            public Object execute(ExecutionContext context) {
                context.getBridge().setBgm((Boolean) getData());
                return null;
            }
        };

        private Object mData;
        public Object getData() { return mData; }
        public void setData(Object data) { this.mData = data; }

        public abstract Object execute(ExecutionContext context);
    }

    public static class ExecutionContext {
        private final SettingsUCBridge mBridge;
        public ExecutionContext(SettingsUCBridge bridge) { this.mBridge = bridge; }
        public SettingsUCBridge getBridge() { return mBridge; }
    }

    public static class SettingsUCBridge {
        private final ISettingsRepository mSettingsRepository;

        @Inject
        public SettingsUCBridge(ISettingsRepository settingsRepository) {
            this.mSettingsRepository = settingsRepository;
        }

        public void setAutoFire(boolean enable) {
            mSettingsRepository.setAutoFire(enable);
        }

        public void setShotStyle(GameSettings.ShotStyle style) {
            mSettingsRepository.setShotStyle(style);
        }

        public void setSoundEffects(boolean enable) {
            mSettingsRepository.setSoundEffects(enable);
        }

        public void setBgm(boolean enable) {
            mSettingsRepository.setBgm(enable);
        }
    }
}
