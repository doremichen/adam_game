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

package com.adam.app.battlecity.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adam.app.battlecity.model.*
import com.adam.app.battlecity.ui.GameViewModel
import com.adam.app.battlecity.ui.theme.BattleCityTheme

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()

    GameScreenContent(
        gameState = gameState,
        onMove = { viewModel.onMoveInput(it) },
        onFire = { viewModel.onFireInput() },
        onRestart = { viewModel.restartGame() },
        onNextStage = { viewModel.nextStage() }
    )
}

@Composable
fun GameScreenContent(
    gameState: GameState,
    onMove: (Direction?) -> Unit,
    onFire: () -> Unit,
    onRestart: () -> Unit,
    onNextStage: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val isTablet = maxWidth > 600.dp

            if (isTablet) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Game Area (Left)
                    Box(
                        modifier = Modifier
                            .weight(1.5f)
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .background(Color.Black)
                            .align(Alignment.CenterVertically)
                    ) {
                        BattleCanvas(gameState)
                        if (gameState.isGameOver) {
                            GameOverOverlay(isWin = false, onRestart = onRestart)
                        } else if (gameState.isWin) {
                            GameOverOverlay(isWin = true, onRestart = onNextStage)
                        }
                    }

                    // Info and Controls (Right)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusBar(gameState)
                        ControlsArea(
                            onMove = onMove,
                            onFire = onFire
                        )
                    }
                }
            } else {
                // Mobile Portrait Layout
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StatusBar(gameState)
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(Color.Black)
                    ) {
                        BattleCanvas(gameState)
                        if (gameState.isGameOver) {
                            GameOverOverlay(isWin = false, onRestart = onRestart)
                        } else if (gameState.isWin) {
                            GameOverOverlay(isWin = true, onRestart = onNextStage)
                        }
                    }

                    ControlsArea(
                        onMove = onMove,
                        onFire = onFire
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBar(gameState: GameState) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InfoChip("Stage", gameState.stage.toString())
            InfoChip("Enemies", (gameState.enemiesLeftToSpawn + gameState.enemyTanks.size).toString())
            InfoChip("Health", gameState.playerTank.health.toString(), color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun InfoChip(label: String, value: String, color: Color = MaterialTheme.colorScheme.onSecondaryContainer) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = color.copy(alpha = 0.7f))
        Text(value, style = MaterialTheme.typography.titleMedium, color = color)
    }
}

@Composable
fun BattleCanvas(gameState: GameState) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val cellSize = size.width / GameConstants.GRID_SIZE

        // Draw Grid
        gameState.grid.forEachIndexed { x, col ->
            col.forEachIndexed { y, tile ->
                drawTile(tile, x, y, cellSize)
            }
        }

        // Draw Base
        drawBase(gameState.base, cellSize)

        // Draw Projectiles
        gameState.projectiles.forEach { projectile ->
            drawProjectile(projectile, cellSize)
        }

        // Draw Player
        drawTank(gameState.playerTank, cellSize, Color(0xFF4CAF50)) // Green for player

        // Draw Enemies
        gameState.enemyTanks.forEach { enemy ->
            val color = enemy.type?.color ?: Color(0xFFF44336)
            drawTank(enemy, cellSize, color)
        }
    }
}

private fun DrawScope.drawTile(tile: TileType, x: Int, y: Int, cellSize: Float) {
    val offset = Offset(x * cellSize, y * cellSize)
    val rectSize = Size(cellSize, cellSize)

    when (tile) {
        TileType.BRICK -> {
            drawRect(Color(0xFF795548), offset, rectSize)
            drawRect(Color.Black, offset, rectSize, style = androidx.compose.ui.graphics.drawscope.Stroke(1f))
        }
        TileType.STEEL -> {
            drawRect(Color(0xFF9E9E9E), offset, rectSize)
            drawRect(
                color = Color.White,
                topLeft = offset + Offset(cellSize * 0.1f, cellSize * 0.1f),
                size = Size(cellSize * 0.8f, cellSize * 0.8f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(2f)
            )
        }
        TileType.BUSH -> {
            drawRect(Color(0xAA4CAF50), offset, rectSize)
        }
        TileType.WATER -> {
            drawRect(Color(0xFF2196F3), offset, rectSize)
        }
        TileType.EMPTY -> {}
    }
}

private fun DrawScope.drawBase(base: GameBase, cellSize: Float) {
    val offset = Offset(base.position.x * cellSize, base.position.y * cellSize)
    val color = if (base.isDestroyed) Color.DarkGray else Color(0xFFFFD700)
    drawRect(color, offset, Size(cellSize, cellSize))
    if (!base.isDestroyed) {
        drawCircle(Color.White, radius = cellSize / 4, center = offset + Offset(cellSize / 2, cellSize / 2))
    }
}

private fun DrawScope.drawTank(tank: Tank, cellSize: Float, color: Color) {
    val offset = Offset(tank.position.x * cellSize, tank.position.y * cellSize)
    val tankSize = cellSize * 0.9f
    drawRect(color, offset + Offset(cellSize * 0.05f, cellSize * 0.05f), Size(tankSize, tankSize))
    
    val barrelWidth = cellSize * 0.2f
    val barrelLength = cellSize * 0.5f
    val center = offset + Offset(cellSize / 2, cellSize / 2)
    
    when (tank.direction) {
        Direction.UP -> drawRect(color, Offset(center.x - barrelWidth / 2, offset.y - barrelLength / 2), Size(barrelWidth, barrelLength + cellSize / 2))
        Direction.DOWN -> drawRect(color, Offset(center.x - barrelWidth / 2, center.y), Size(barrelWidth, barrelLength + cellSize / 2))
        Direction.LEFT -> drawRect(color, Offset(offset.x - barrelLength / 2, center.y - barrelWidth / 2), Size(barrelLength + cellSize / 2, barrelWidth))
        Direction.RIGHT -> drawRect(color, Offset(center.x, center.y - barrelWidth / 2), Size(barrelLength + cellSize / 2, barrelWidth))
    }
}

private fun DrawScope.drawProjectile(projectile: Projectile, cellSize: Float) {
    val offset = Offset(projectile.position.x * cellSize, projectile.position.y * cellSize)
    drawCircle(Color.White, radius = cellSize * 0.15f, center = offset + Offset(cellSize / 2, cellSize / 2))
}

@Composable
fun ControlsArea(onMove: (Direction?) -> Unit, onFire: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ControlButton(Icons.Default.KeyboardArrowUp) { onMove(Direction.UP) }
            Row {
                ControlButton(Icons.AutoMirrored.Filled.KeyboardArrowLeft) { onMove(Direction.LEFT) }
                Spacer(modifier = Modifier.size(16.dp))
                FilledTonalButton(
                    onClick = { onMove(null) },
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        "Stop",
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                ControlButton(Icons.AutoMirrored.Filled.KeyboardArrowRight) { onMove(Direction.RIGHT) }
            }
            ControlButton(Icons.Default.KeyboardArrowDown) { onMove(Direction.DOWN) }
        }

        LargeFloatingActionButton(
            onClick = onFire,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
        ) {
            Icon(Icons.Default.Whatshot, contentDescription = "Fire", modifier = Modifier.size(48.dp))
        }
    }
}

@Composable
fun ControlButton(icon: ImageVector, onClick: () -> Unit) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
    }
}

@Composable
fun GameOverOverlay(isWin: Boolean, onRestart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (isWin) "MISSION ACCOMPLISHED" else "GAME OVER",
                style = MaterialTheme.typography.displaySmall,
                color = if (isWin) Color.Green else Color.Red
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isWin) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                )
            ) {
                Text(if (isWin) "NEXT STAGE" else "RETRY")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun GameScreenTabletPreview() {
    BattleCityTheme {
        GameScreenContent(
            gameState = GameViewModel.createInitialState(),
            onMove = {},
            onFire = {},
            onRestart = {},
            onNextStage = {}
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun GameScreenMobilePreview() {
    BattleCityTheme {
        GameScreenContent(
            gameState = GameViewModel.createInitialState(),
            onMove = {},
            onFire = {},
            onRestart = {},
            onNextStage = {}
        )
    }
}

