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

package com.adam.app.arenaminifight.domain.usecase;

import com.adam.app.arenaminifight.domain.gateway.IGameGateway;
import com.adam.app.arenaminifight.domain.model.Player;

import java.util.List;

/**
 * Highly cohesive Enum-based UseCase for Game operations.
 */
public final class GameUseCase {

    private final IGameGateway mGateway;

    public GameUseCase(IGameGateway gateway) {
        mGateway = gateway;
    }

    public enum Action {
        SPAWN_PLAYER {
            @Override
            @SuppressWarnings("unchecked")
            <T> T handle(IGameGateway gw, Object data) {
                SpawnParams params = (SpawnParams) data;
                gw.spawnPlayer(params.mName, params.mCallback);
                return null;
            }
        },
        START_GAME {
            @Override
            @SuppressWarnings("unchecked")
            <T> T handle(IGameGateway gw, Object data) {
                IGameGateway.Callback<List<Player>> callback = (IGameGateway.Callback<List<Player>>) data;
                gw.startGame(callback);
                return null;
            }
        },
        STOP_GAME {
            @Override
            <T> T handle(IGameGateway gw, Object data) {
                gw.stopGame();
                return null;
            }
        },
        MOVE_PLAYER {
            @Override
            <T> T handle(IGameGateway gw, Object data) {
                MoveParams params = (MoveParams) data;
                gw.movePlayer(params.mPlayerId, params.mX, params.mY);
                return null;
            }
        },
        PLAYER_ATTACK {
            @Override
            @SuppressWarnings("unchecked")
            <T> T handle(IGameGateway gw, Object data) {
                AttackParams params = (AttackParams) data;
                gw.playerAttack(params.mPlayerId, params.mIsSkill, params.mCallback);
                return null;
            }
        },
        STOP_MOVE_PLAYER {
            @Override
            <T> T handle(IGameGateway gw, Object data) {
                StopMoveParams params = (StopMoveParams) data;
                gw.stopPlayerMove(params.mPlayerId);
                return null;
            }
        };

        abstract <T> T handle(IGameGateway gw, Object data);
    }

    public <T> T execute(Action action, Object data) {
        return action.handle(mGateway, data);
    }

    public static class SpawnParams {
        public final String mName;
        public final IGameGateway.Callback<Player> mCallback;

        public SpawnParams(String name, IGameGateway.Callback<Player> callback) {
            mName = name;
            mCallback = callback;
        }
    }

    public static class MoveParams {
        public final String mPlayerId;
        public final float mX;
        public final float mY;

        public MoveParams(String playerId, float x, float y) {
            mPlayerId = playerId;
            mX = x;
            mY = y;
        }
    }

    public static class AttackParams {
        public final String mPlayerId;
        public final boolean mIsSkill;
        public final IGameGateway.Callback<String> mCallback;

        public AttackParams(String playerId, boolean isSkill, IGameGateway.Callback<String> callback) {
            mPlayerId = playerId;
            mIsSkill = isSkill;
            mCallback = callback;
        }
    }

    public static class StopMoveParams {
        public final String mPlayerId;

        public StopMoveParams(String playerId) {
            mPlayerId = playerId;
        }
    }
}
