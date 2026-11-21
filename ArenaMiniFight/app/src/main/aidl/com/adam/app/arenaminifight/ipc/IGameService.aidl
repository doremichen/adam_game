// IGameService.aidl
package com.adam.app.arenaminifight.ipc;

import com.adam.app.arenaminifight.ipc.IGameStateListener;
import com.adam.app.arenaminifight.domain.model.Player;

/**
 * AIDL Remote Interface for GameService IPC communication.
 * Trace: REQ-IPC-01, REQ-IPC-02, REQ-IPC-03, REQ-GAME-01, REQ-GAME-10
 */
interface IGameService {
    void registerListener(IGameStateListener listener);
    void unregisterListener(IGameStateListener listener);

    Player spawnPlayer(in String name);
    void startGame();
    void stopGame();
    void movePlayer(in String playerId, float x, float y);
    void stopPlayerMove(in String playerId);
    void playerAttack(in String playerId, boolean isSkill);
    String getGameState();
    void spawnEnemy();
}
