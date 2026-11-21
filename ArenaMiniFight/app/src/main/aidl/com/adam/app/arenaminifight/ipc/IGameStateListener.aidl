// IGameStateListener.aidl
package com.adam.app.arenaminifight.ipc;

/**
 * Callback interface for receiving asynchronous game state updates from GameService.
 * Trace: REQ-IPC-01, REQ-IPC-02, REQ-GAME-12
 */
interface IGameStateListener {
    void onGameStateUpdated(in String gameStateStr);
    void onAttackResult(in String result);
    void onGameOver(in String summary);
}
