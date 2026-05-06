# Project Plan

Battle City - Tank Game

## Project Brief

# Battle City - Project Brief

## Features
- **Tank Combat Mechanics**: Responsive on-screen controls for tank movement and a projectile-based firing system.
- **Destructible Environments**: A dynamic grid-based battlefield featuring breakable brick walls, impenetrable steel blocks, and tactical cover like bushes.
- **Enemy AI Tiers**: Autonomous enemy tanks with distinct behaviors, varying movement speeds, and different armor levels to challenge the player.
- **Stage Progression**: A level-based system with clear win/loss conditions, including protecting a central base from enemy fire.

## High-Level Technical Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Navigation**: Jetpack Navigation 3 (State-driven architecture)
- **Adaptive Strategy**: Compose Material Adaptive library for seamless transitions between mobile, tablet, and foldable displays.
- **Concurrency**: Kotlin Coroutines for the game loop, movement logic, and collision detection.

## Implementation Steps

### Task_1_GameFoundation: Define core game models (Tank, Projectile, Tile types, Base) and implement the GameViewModel with a Coroutine-based game loop and spatial collision detection logic.
- **Status:** COMPLETED
- **Updates:** Implemented core game models (Tank, Projectile, TileType, GameBase, GameState). Created GameViewModel with a 60 FPS Coroutine-based game loop and collision detection logic. Resolved build issues (AAR metadata) by updating SDK versions. Added MIT license headers to all files.
- **Acceptance Criteria:**
  - Core game state models defined
  - Game loop running using Coroutines
  - Grid-based collision detection logic implemented

### Task_2_PlayerControlsAndUI: Implement the game screen using Jetpack Compose Canvas. Add on-screen controls (DPad/Joystick and Fire button) and connect them to player tank movement and firing mechanics.
- **Status:** COMPLETED
- **Updates:** Implemented GameScreen using Compose Canvas. Added on-screen D-Pad and Fire controls. Integrated player movement and firing mechanics with collision detection. Created Game Over/Victory overlays. Set up MainActivity to host the game.
- **Acceptance Criteria:**
  - Game rendered on Compose Canvas
  - Player can move in four directions
  - Firing mechanic destroys brick tiles but not steel
  - Controls are responsive and intuitive

### Task_3_EnemyAIAndStages: Implement enemy AI tiers with varying speeds and armor. Add spawning logic and stage management including win/loss conditions (protecting the base).
- **Status:** COMPLETED
- **Updates:** Implemented three enemy tiers (Basic, Fast, Armored) with autonomous movement and firing. Added a wave-based spawning system and procedural level generation. Implemented player health (3 HP) and a status bar (Stage, HP, Enemy count). Added Win/Loss/Next Stage transitions.
- **Acceptance Criteria:**
  - Enemies spawn and exhibit basic autonomous behavior
  - Enemies can damage player and base
  - Level ends correctly on base destruction or enemy depletion

### Task_4_AdaptiveUIAndFinalize: Refine UI with Material 3 'vibrant and energetic' themes, implement adaptive layouts for various screen sizes, create an adaptive app icon, and perform final Run and Verify.
- **Status:** COMPLETED
- **Updates:** Refined UI with Material 3 vibrant colors. Implemented adaptive layout for mobile and tablets. Enabled full Edge-to-Edge display. Created adaptive app icon. Verified all features and fixed UI alignment issues reported by critic. Final build passes and app is stable.
- **Acceptance Criteria:**
  - Material 3 theme applied with vibrant colors
  - Adaptive layout works on mobile and tablets
  - Full edge-to-edge display implemented
  - Adaptive app icon created
  - Build passes, all existing tests pass, and app does not crash
- **Duration:** N/A

