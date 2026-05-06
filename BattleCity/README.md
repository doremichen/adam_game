# Battle City - Jetpack Compose Edition

A modern Android remake of the classic NES game **Battle City**, built entirely with **Jetpack Compose** and **Kotlin**.

## 🚀 Features

-   **Classic Gameplay**: Control your tank, protect your base (the Eagle), and destroy all enemy tanks.
-   **Procedural Level Generation**: Each stage is dynamically generated using a seed based on the stage number, providing varied layouts for bricks, steel, water, and bushes.
-   **Adaptive Layouts**: Full support for both **Mobile** and **Tablet** form factors. The UI adapts automatically to provide the best control experience for the screen size.
-   **Diverse Enemy Types**: Includes different enemy types (Basic, Fast, Power, Armor) with varying speeds, health, and firing cooldowns.
-   **Modern Tech Stack**: Uses the latest Android development practices including state-of-the-art State Management and UI rendering.

## 🛠 Tech Stack

-   **Kotlin**: The primary language for the project.
-   **Jetpack Compose**: Used for the entire UI and the game engine rendering via the `Canvas` API.
-   **Modern Architecture (MVVM)**: 
    -   `GameViewModel`: Manages the game loop, physics, and state transitions using `MutableStateFlow`.
    -   `GameState`: A single source of truth for the game world, including tank positions, projectiles, and grid layout.
-   **StateFlow & Lifecycle**: Efficiently handles game state updates and UI collection with `collectAsStateWithLifecycle`.
-   **Material 3**: Modern UI components for the status bar and control overlays.
-   **AndroidX Lifecycle**: Handles ViewModel lifecycle and game loop synchronization.

## 🎮 How to Play

1.  **Move**: Use the Directional buttons (Up, Down, Left, Right) to navigate your tank.
2.  **Stop**: Tap the center "Stop" button to halt movement.
3.  **Fire**: Tap the "Flame" action button to fire projectiles at enemies and brick walls.
4.  **Objective**: Destroy all enemy tanks in the stage while ensuring your base at the bottom of the map remains intact.

## 🏗 Project Structure

-   `model/`: Data classes representing game entities (Tank, Projectile, TileType, etc.).
-   `ui/components/`: Compose functions for rendering the game world (`BattleCanvas`) and UI controls (`ControlsArea`).
-   `ui/GameViewModel`: The core game engine logic, including collision detection, AI movement, and projectile physics.
-   `ui/theme/`: Material 3 theme definitions.

---
*Created as a demonstration of high-performance rendering and state management in Jetpack Compose.*
