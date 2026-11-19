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

package com.adam.app.arenaminifight.domain.gateway;

import androidx.lifecycle.LiveData;

import com.adam.app.arenaminifight.domain.model.ChatMessage;
import com.adam.app.arenaminifight.domain.model.Player;

import java.util.List;

/**
 * Gateway interface contract for game operations and IPC communication with background service.
 */
public interface IGameGateway {

    /**
     * Generic callback interface for asynchronous operations.
     *
     * @param <T> the result data type
     */
    interface Callback<T> {
        /**
         * Called when the result is available.
         *
         * @param result the operation result
         */
        void onResult(T result);
    }

    /**
     * Returns whether the game service is bound and ready.
     *
     * @return LiveData boolean representing readiness state
     */
    LiveData<Boolean> isReady();

    /**
     * Binds to the background GameService.
     */
    void bindGameService();

    /**
     * Unbinds from the background GameService.
     */
    void unbindGameService();

    /**
     * Sends a chat message to the server.
     *
     * @param chat     the chat message to send
     * @param callback the result callback
     */
    void sendChat(ChatMessage chat, Callback<ChatMessage> callback);

    /**
     * Spawns a player or AI bot in the game.
     *
     * @param name     the player display name
     * @param callback the result callback
     */
    void spawnPlayer(String name, Callback<Player> callback);

    /**
     * Starts the game simulation loop.
     *
     * @param callback the game state update callback
     */
    void startGame(Callback<List<Player>> callback);

    /**
     * Stops the game simulation loop and resets engine.
     */
    void stopGame();

    /**
     * Moves a player to the target coordinates.
     *
     * @param playerId the unique player identifier
     * @param x        target X coordinate
     * @param y        target Y coordinate
     */
    void movePlayer(String playerId, float x, float y);

    /**
     * Stops player movement.
     *
     * @param playerId the unique player identifier
     */
    void stopPlayerMove(String playerId);

    /**
     * Performs a player attack or skill.
     *
     * @param playerId the unique player identifier
     * @param isSkill  true if casting skill, false if normal attack
     * @param callback the result callback
     */
    void playerAttack(String playerId, boolean isSkill, Callback<String> callback);
}
