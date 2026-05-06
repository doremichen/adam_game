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
import kotlin.math.roundToInt
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
            var newEnemiesLeftToSpawn = currentState.enemiesLeftToSpawn
            val currentEnemies = currentState.enemyTanks.toMutableList()
            if (newEnemiesLeftToSpawn > 0 && currentEnemies.size < GameConstants.MAX_ENEMIES_ON_SCREEN) {
                if (currentTime - lastSpawnTime > GameConstants.SPAWN_COOLDOWN_MS) {
                    val spawnPoint = GameConstants.SPAWN_POINTS.random()
                    // Check if spawn point is clear
                    if (canMoveTo(spawnPoint, currentState.grid, currentState.base, currentEnemies + currentState.playerTank)) {
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
                        newEnemiesLeftToSpawn--
                        lastSpawnTime = currentTime
                    }
                }
            }

            // 2. Update Player Movement
            var newPlayerTank = currentState.playerTank
            moveDirection?.let { dir ->
                val nextPos = newPlayerTank.position.translate(dir.dx * newPlayerTank.speed, dir.dy * newPlayerTank.speed)
                if (canMoveTo(nextPos, currentState.grid, currentState.base, currentEnemies)) {
                    newPlayerTank = newPlayerTank.copy(position = nextPos, direction = dir)
                } else {
                    newPlayerTank = newPlayerTank.copy(direction = dir)
                }
            }

            // 3. Handle Projectiles
            val newProjectiles = currentState.projectiles.toMutableList()
            
            // Player Firing
            if (isFiring) {
                if (currentTime - lastPlayerFireTime > 500) {
                    newProjectiles.add(
                        Projectile(
                            id = UUID.randomUUID().toString(),
                            position = newPlayerTank.position,
                            direction = newPlayerTank.direction,
                            ownerId = "player"
                        )
                    )
                    lastPlayerFireTime = currentTime
                }
                isFiring = false
            }

            // Enemy Firing AI
            for (i in currentEnemies.indices) {
                val enemy = currentEnemies[i]
                val type = enemy.type ?: EnemyType.BASIC
                if (currentTime - enemy.lastFireTime > type.fireCooldownMs) {
                    // Simple firing AI: fire if facing player or base, or just random
                    newProjectiles.add(
                        Projectile(
                            id = UUID.randomUUID().toString(),
                            position = enemy.position,
                            direction = enemy.direction,
                            ownerId = enemy.id
                        )
                    )
                    currentEnemies[i] = enemy.copy(lastFireTime = currentTime)
                }
            }

            // 4. Update Projectiles Logic
            val updatedProjectiles = mutableListOf<Projectile>()
            val updatedGrid = currentState.grid.map { it.toMutableList() }
            var updatedBase = currentState.base
            val remainingEnemies = currentEnemies.toMutableList()
            var playerHit = false

            for (p in newProjectiles) {
                val nextPos = p.position.translate(p.direction.dx * p.speed, p.direction.dy * p.speed)
                
                if (nextPos.x < 0 || nextPos.x >= GameConstants.GRID_SIZE || nextPos.y < 0 || nextPos.y >= GameConstants.GRID_SIZE) {
                    continue
                }

                val gridX = nextPos.x.toInt()
                val gridY = nextPos.y.toInt()
                val tile = updatedGrid[gridX][gridY]
                
                var hit = false
                if (tile == TileType.BRICK) {
                    updatedGrid[gridX][gridY] = TileType.EMPTY
                    hit = true
                } else if (tile == TileType.STEEL) {
                    hit = true
                }

                if (gridX == updatedBase.position.x.toInt() && gridY == updatedBase.position.y.toInt()) {
                    updatedBase = updatedBase.copy(isDestroyed = true)
                    hit = true
                }

                if (p.ownerId == "player") {
                    val hitEnemyIndex = remainingEnemies.indexOfFirst { 
                        isCollision(it.position, nextPos, 0.7f)
                    }
                    if (hitEnemyIndex != -1) {
                        val enemy = remainingEnemies[hitEnemyIndex]
                        if (enemy.health > 1) {
                            remainingEnemies[hitEnemyIndex] = enemy.copy(health = enemy.health - 1)
                        } else {
                            remainingEnemies.removeAt(hitEnemyIndex)
                        }
                        hit = true
                    }
                } else {
                    // Enemy projectile hitting player
                    if (isCollision(newPlayerTank.position, nextPos, 0.7f)) {
                        playerHit = true
                        hit = true
                    }
                }

                if (!hit) {
                    updatedProjectiles.add(p.copy(position = nextPos))
                }
            }

            if (playerHit) {
                newPlayerTank = newPlayerTank.copy(health = newPlayerTank.health - 1)
            }

            // 5. Update Enemies AI Movement
            val updatedEnemies = remainingEnemies.map { enemy ->
                val nextPos = enemy.position.translate(enemy.direction.dx * enemy.speed, enemy.direction.dy * enemy.speed)
                val otherTanks = remainingEnemies.filter { it.id != enemy.id } + newPlayerTank
                
                if (canMoveTo(nextPos, updatedGrid, updatedBase, otherTanks) && Random.nextFloat() > 0.02f) {
                    enemy.copy(position = nextPos)
                } else {
                    // Smart-ish turn: pick a direction that is clear
                    val possibleDirs = Direction.entries.shuffled()
                    val bestDir = possibleDirs.find { dir ->
                        val p = enemy.position.translate(dir.dx * enemy.speed, dir.dy * enemy.speed)
                        canMoveTo(p, updatedGrid, updatedBase, otherTanks)
                    } ?: Direction.entries.random()
                    enemy.copy(direction = bestDir)
                }
            }

            val isWin = newEnemiesLeftToSpawn == 0 && updatedEnemies.isEmpty()
            val isGameOver = updatedBase.isDestroyed || newPlayerTank.health <= 0

            currentState.copy(
                playerTank = newPlayerTank,
                enemyTanks = updatedEnemies,
                projectiles = updatedProjectiles,
                grid = updatedGrid,
                base = updatedBase,
                isGameOver = isGameOver,
                isWin = isWin,
                enemiesLeftToSpawn = newEnemiesLeftToSpawn
            )
        }
    }

    private fun isCollision(pos1: Position, pos2: Position, threshold: Float): Boolean {
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        return dx * dx + dy * dy < threshold * threshold
    }

    private fun canMoveTo(pos: Position, grid: List<List<TileType>>, base: GameBase, otherTanks: List<Tank>): Boolean {
        if (pos.x < 0 || pos.x >= GameConstants.GRID_SIZE - 1 || pos.y < 0 || pos.y >= GameConstants.GRID_SIZE - 1) {
            return false
        }
        
        val corners = listOf(
            Position(pos.x, pos.y),
            Position(pos.x + 0.9f, pos.y),
            Position(pos.x, pos.y + 0.9f),
            Position(pos.x + 0.9f, pos.y + 0.9f)
        )

        val gridClear = corners.all { corner ->
            val gx = corner.x.toInt()
            val gy = corner.y.toInt()
            if (gx < 0 || gx >= GameConstants.GRID_SIZE || gy < 0 || gy >= GameConstants.GRID_SIZE) return@all false
            if (gx == base.position.x.toInt() && gy == base.position.y.toInt()) return@all false
            val tile = grid[gx][gy]
            tile == TileType.EMPTY || tile == TileType.BUSH
        }

        if (!gridClear) return false

        // Check collision with other tanks
        return otherTanks.all { tank ->
            !isCollision(pos, tank.position, 0.9f)
        }
    }
}

