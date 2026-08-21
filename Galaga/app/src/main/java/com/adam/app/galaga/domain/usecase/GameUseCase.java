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

import androidx.lifecycle.LiveData;

import com.adam.app.galaga.data.local.entities.ScoreRecord;
import com.adam.app.galaga.data.model.LevelConfig;
import com.adam.app.galaga.domain.repository.IGameRepository;
import com.adam.app.galaga.domain.repository.ILevelRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * GameUseCase - Enum based UseCase implementation with polymorphic actions
 */
public class GameUseCase implements IUseCase<GameUseCase.Request, Object> {

    private final GameUCBridge mBridge;

    @Inject
    public GameUseCase(GameUCBridge bridge) {
        this.mBridge = bridge;
    }

    @Override
    public Object execute(Request request) {
        // Delegate execution to the action type, providing context
        ActionType type = request.getType();
        // set data
        type.setData(request.getData());
        return type.execute(new ExecutionContext(mBridge));
    }

    /**
     * Request class encapsulating action type and payload
     */
    public static class Request {
        private final ActionType mType;
        private final Object mData;

        public Request(ActionType type, Object data) {
            this.mType = type;
            this.mData = data;
        }

        public ActionType getType() {
            return mType;
        }

        public Object getData() {
            return mData;
        }
    }

    /**
     * ActionType Enum - Defines available actions and their specific execution logic
     */
    public enum ActionType {
        INSERT_SCORE {
            @Override
            public Object execute(ExecutionContext context) {
                context.getBridge().insertScore((ScoreRecord) getData());
                return null;
            }
        },
        GET_TOP_SCORES {
            @Override
            public Object execute(ExecutionContext context) {
                return context.getBridge().getTop10Scores();
            }
        },
        GET_LEVEL_CONFIG {
            @Override
            public Object execute(ExecutionContext context) {
                return context.getBridge().getLevelConfig((Integer) getData());
            }
        };

        private Object mData;

        public Object getData() {
            return mData;
        }

        public void setData(Object data) {
            this.mData = data;
        }

        /**
         * Abstract execution method
         * @param context ExecutionContext containing bridge
         * @return Execution result
         */
        public abstract Object execute(ExecutionContext context);
    }

    /**
     * ExecutionContext - Wrapper for bridge
     */
    public static class ExecutionContext {
        private final GameUCBridge mBridge;

        public ExecutionContext(GameUCBridge bridge) {
            this.mBridge = bridge;
        }

        public GameUCBridge getBridge() {
            return mBridge;
        }
    }

    /**
     * GameUCBridge - Holds Hilt injected dependencies and provides services
     */
    public static class GameUCBridge {
        private final IGameRepository mGameRepository;
        private final ILevelRepository mLevelRepository;

        @Inject
        public GameUCBridge(IGameRepository gameRepository, ILevelRepository levelRepository) {
            this.mGameRepository = gameRepository;
            this.mLevelRepository = levelRepository;
        }

        public void insertScore(ScoreRecord record) {
            mGameRepository.insertScore(record);
        }

        public LiveData<List<ScoreRecord>> getTop10Scores() {
            return mGameRepository.getTop10Scores();
        }

        public LevelConfig getLevelConfig(int levelId) {
            return mLevelRepository.getLevelConfig(levelId);
        }
    }
}
