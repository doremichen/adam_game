/*
 * Copyright (c) 2025. MIT License.
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

package com.adam.app.battlecity.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adam.app.battlecity.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(createInitialState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var moveDirection: Direction? = null
    private var isFiring = false
    private var lastPlayerFireTime = 0L
    private var lastSpawnTime = 0L

    init {
        startGameLoop()
    }

    companion object {
        fun createInitialState(stage: Int = 1): GameState {
            val grid = MutableList(GameConstants.GRID_SIZE) {
                MutableList(GameConstants.GRID_SIZE) { TileType.EMPTY }
            }

            // Procedural level generation based on stage
            val random = Random(stage.toLong())
            for (i in 0 until GameConstants.GRID_SIZE) {
                for (j in 0 until GameConstants.GRID_SIZE) {
                    if (i % 2 == 0 && j % 2 == 0 && i > 1 && i < 24 && j > 1 && j < 24) {
                        val r = random.nextFloat()
                        grid[i][j] = when {
                            r < 0.3f -> TileType.BRICK
                            r < 0.4f -> TileType.STEEL
                            r < 0.5f -> TileType.WATER
                            r < 0.6f -> TileType.BUSH
                            else -> TileType.EMPTY
                        }
                    }
                }
            }

            val playerPos = Position(12f, 24f)
            val basePos = Position(12f, 25f)

            // Clear area around player and base
            for (x in 11..13) {
                for (y in 23..25) {
                    if (x < GameConstants.GRID_SIZE && y < GameConstants.GRID_SIZE) {
                        grid[x][y] = TileType.EMPTY
                    }
                }
            }

            return GameState(
                playerTank = Tank("player", playerPos, Direction.UP, true, health = 3),
                enemyTanks = emptyList(),
                grid = grid,
                base = GameBase(basePos),
                stage = stage,
                enemiesLeftToSpawn = 10 + stage * 5
            )
        }
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (isActive) {
                if (!_gameState.value.isGameOver && !_gameState.value.isWin) {
                    updateGameState()
                }
                delay(GameConstants.GAME_TICK_MS)
            }
        }
    }

    fun onMoveInput(direction: Direction?) {
        moveDirection = direction
    }

    fun onFireInput() {
        isFiring = true
    }

    fun restartGame() {
        _gameState.value = createInitialState(1)
    }

    fun nextStage() {
        _gameState.value = createInitialState(_gameState.value.stage + 1)
    }

    private fun updateGameState() {
        _gameState.update { currentState ->
            val currentTime = System.currentTimeMillis()

            // 1. Spawning Logic
            val (newEnemiesLeftToSpawn, enemiesAfterSpawn) = handleSpawning(currentState, currentTime)

            // 2. Update Player Movement
            var newPlayerTank = handlePlayerMovement(currentState.playerTank, enemiesAfterSpawn, currentState)

            // 3. Handle Projectiles Firing
            val (projectilesAfterFiring, enemiesAfterFiring) = handleFiring(currentState, enemiesAfterSpawn, newPlayerTank, currentTime)

            // 4. Update Projectiles Movement & Collisions
            val collisionResult = processProjectiles(projectilesAfterFiring, currentState, enemiesAfterFiring, newPlayerTank)
            
            newPlayerTank = if (collisionResult.playerHit) {
                newPlayerTank.copy(health = newPlayerTank.health - 1)
            } else newPlayerTank

            // 5. Update Enemies AI Movement
            val updatedEnemies = handleEnemyAI(collisionResult.remainingEnemies, newPlayerTank, collisionResult.updatedGrid, collisionResult.updatedBase)

            val isWin = newEnemiesLeftToSpawn == 0 && updatedEnemies.isEmpty()
            val isGameOver = collisionResult.updatedBase.isDestroyed || newPlayerTank.health <= 0

            currentState.copy(
                playerTank = newPlayerTank,
                enemyTanks = updatedEnemies,
                projectiles = collisionResult.updatedProjectiles,
                grid = collisionResult.updatedGrid,
                base = collisionResult.updatedBase,
                isGameOver = isGameOver,
                isWin = isWin,
                enemiesLeftToSpawn = newEnemiesLeftToSpawn
            )
        }
    }

    private fun handleSpawning(state: GameState, currentTime: Long): Pair<Int, List<Tank>> {
        var leftToSpawn = state.enemiesLeftToSpawn
        val currentEnemies = state.enemyTanks.toMutableList()

        if (leftToSpawn > 0 && currentEnemies.size < GameConstants.MAX_ENEMIES_ON_SCREEN) {
            if (currentTime - lastSpawnTime > GameConstants.SPAWN_COOLDOWN_MS) {
                val spawnPoint = GameConstants.SPAWN_POINTS.random()
                if (canMoveTo(spawnPoint, state.grid, state.base, currentEnemies + state.playerTank)) {
                    val type = EnemyType.entries.random()
                    currentEnemies.add(
                        Tank(
                            id = UUID.randomUUID().toString(),
                            position = spawnPoint,
                            direction = Direction.DOWN,
                            isPlayer = false,
                            health = type.health,
                            speed = type.speed,
                            type = type,
                            lastFireTime = currentTime
                        )
                    )
                    leftToSpawn--
                    lastSpawnTime = currentTime
                }
            }
        }
        return Pair(leftToSpawn, currentEnemies)
    }

    private fun handlePlayerMovement(player: Tank, enemies: List<Tank>, state: GameState): Tank {
        var updatedPlayer = player
        moveDirection?.let { dir ->
            val nextPos = updatedPlayer.position.translate(dir.dx * updatedPlayer.speed, dir.dy * updatedPlayer.speed)
            updatedPlayer = if (canMoveTo(nextPos, state.grid, state.base, enemies)) {
                updatedPlayer.copy(position = nextPos, direction = dir)
            } else {
                updatedPlayer.copy(direction = dir)
            }
        }
        return updatedPlayer
    }

    private fun handleFiring(state: GameState, enemies: List<Tank>, player: Tank, currentTime: Long): Pair<List<Projectile>, List<Tank>> {
        val projectiles = state.projectiles.toMutableList()
        val updatedEnemies = enemies.toMutableList()

        // Player
        if (isFiring) {
            if (currentTime - lastPlayerFireTime > 500) {
                projectiles.add(Projectile(UUID.randomUUID().toString(), player.position, player.direction, "player"))
                lastPlayerFireTime = currentTime
            }
            isFiring = false
        }

        // Enemies
        for (i in updatedEnemies.indices) {
            val enemy = updatedEnemies[i]
            val type = enemy.type ?: EnemyType.BASIC
            if (currentTime - enemy.lastFireTime > type.fireCooldownMs) {
                projectiles.add(Projectile(UUID.randomUUID().toString(), enemy.position, enemy.direction, enemy.id))
                updatedEnemies[i] = enemy.copy(lastFireTime = currentTime)
            }
        }
        return Pair(projectiles, updatedEnemies)
    }

    private data class CollisionResult(
        val updatedProjectiles: List<Projectile>,
        val updatedGrid: List<List<TileType>>,
        val updatedBase: GameBase,
        val remainingEnemies: List<Tank>,
        val playerHit: Boolean
    )

    private fun processProjectiles(projectiles: List<Projectile>, state: GameState, enemies: List<Tank>, player: Tank): CollisionResult {
        val updatedProjectiles = mutableListOf<Projectile>()
        val updatedGrid = state.grid.map { it.toMutableList() }
        var updatedBase = state.base
        val remainingEnemies = enemies.toMutableList()
        var playerHit = false

        for (p in projectiles) {
            val nextPos = p.position.translate(p.direction.dx * p.speed, p.direction.dy * p.speed)

            if (nextPos.x < 0 || nextPos.x >= GameConstants.GRID_SIZE || nextPos.y < 0 || nextPos.y >= GameConstants.GRID_SIZE) continue

            val gx = nextPos.x.toInt()
            val gy = nextPos.y.toInt()
            val tile = updatedGrid[gx][gy]
            var hit = false

            when (tile) {
                TileType.BRICK -> { updatedGrid[gx][gy] = TileType.EMPTY; hit = true }
                TileType.STEEL -> hit = true
                else -> {}
            }

            if (gx == updatedBase.position.x.toInt() && gy == updatedBase.position.y.toInt()) {
                updatedBase = updatedBase.copy(isDestroyed = true)
                hit = true
            }

            if (p.ownerId == "player") {
                val hitIdx = remainingEnemies.indexOfFirst { isCollision(it.position, nextPos, 0.7f) }
                if (hitIdx != -1) {
                    val enemy = remainingEnemies[hitIdx]
                    if (enemy.health > 1) remainingEnemies[hitIdx] = enemy.copy(health = enemy.health - 1)
                    else remainingEnemies.removeAt(hitIdx)
                    hit = true
                }
            } else if (isCollision(player.position, nextPos, 0.7f)) {
                playerHit = true
                hit = true
            }

            if (!hit) updatedProjectiles.add(p.copy(position = nextPos))
        }

        return CollisionResult(updatedProjectiles, updatedGrid.map { it.toList() }, updatedBase, remainingEnemies, playerHit)
    }

    private fun handleEnemyAI(enemies: List<Tank>, player: Tank, grid: List<List<TileType>>, base: GameBase): List<Tank> {
        return enemies.map { enemy ->
            val nextPos = enemy.position.translate(enemy.direction.dx * enemy.speed, enemy.direction.dy * enemy.speed)
            val others = enemies.filter { it.id != enemy.id } + player

            if (canMoveTo(nextPos, grid, base, others) && Random.nextFloat() > 0.02f) {
                enemy.copy(position = nextPos)
            } else {
                val bestDir = Direction.entries.shuffled().find { dir ->
                    canMoveTo(enemy.position.translate(dir.dx * enemy.speed, dir.dy * enemy.speed), grid, base, others)
                } ?: Direction.entries.random()
                enemy.copy(direction = bestDir)
            }
        }
    }

    private fun isCollision(pos1: Position, pos2: Position, threshold: Float): Boolean {
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        return dx * dx + dy * dy < threshold * threshold
    }

    private fun canMoveTo(pos: Position, grid: List<List<TileType>>, base: GameBase, otherTanks: List<Tank>): Boolean {
        if (pos.x < 0 || pos.x >= GameConstants.GRID_SIZE - 1 || pos.y < 0 || pos.y >= GameConstants.GRID_SIZE - 1) return false

        val corners = listOf(
            Position(pos.x, pos.y),
            Position(pos.x + 0.9f, pos.y),
            Position(pos.x, pos.y + 0.9f),
            Position(pos.x + 0.9f, pos.y + 0.9f)
        )

        val gridClear = corners.all { c ->
            val gx = c.x.toInt()
            val gy = c.y.toInt()
            if (gx !in 0 until GameConstants.GRID_SIZE || gy !in 0 until GameConstants.GRID_SIZE) return@all false
            if (gx == base.position.x.toInt() && gy == base.position.y.toInt()) return@all false
            val tile = grid[gx][gy]
            tile == TileType.EMPTY || tile == TileType.BUSH
        }

        return gridClear && otherTanks.all { !isCollision(pos, it.position, 0.9f) }
    }
}
