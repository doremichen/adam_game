# ArenaMiniFight (JNI, IPC & Clean Architecture Lab)

ArenaMiniFight is an advanced Android development project demonstrating the integration of **Clean Architecture + MVVM**, **Google Hilt Dependency Injection**, **Inter-Process Communication (IPC)**, and a **Native C++ Game Engine (JNI)**.  
The project showcases how to decouple core game logic, physics calculation, and UI rendering across layers and processes for optimal stability, performance, and testability.

---

# 🏗 Technical & Architectural Core

## 1. Clean Architecture + MVVM & Google Hilt
- **Domain Layer**: Contains pure business entities (`Player`, `ChatMessage`) and Gateway contracts (`IGameGateway`) with zero framework dependencies.
- **Application Layer**: Houses highly cohesive, Enum-based UseCases (`GameUseCase`, `LobbyUseCase`, `WelcomeUseCase`).
- **Infrastructure Layer**: Concrete `GameGatewayImpl` communicating via Android `Messenger` IPC.
- **Presentation Layer**: Activities and Fragments observing ViewModels powered by Google Hilt (`@HiltViewModel`, `@AndroidEntryPoint`).
- **One-Time Events**: Utilizes `SingleLiveEvent` to prevent duplicate event triggers on configuration changes.

## 2. JNI (C++) Game Engine Integration
- **Native Entity Management**: Utilizes C++ `std::map` in `arenaminifight.cpp` to maintain player entities (`PlayerObject`), bypassing Java GC overhead.
- **60 FPS Physics Simulation**: Background `EngineLoopManager` ticks at 16ms intervals, updating movement and wall-bounding physics.
- **State Synchronization**: High-performance JNI state formatting (`getGameState()`) broadcasted to clients over IPC.

## 3. IPC (Inter-Process Communication) & Game Services
- **Messenger Pattern**: Bi-directional communication bridge between UI process and background `GameService`.
- **Lifecycle Protection**: Thread-safe start/stop cycles preventing memory leaks and process crashes.

---

# 🏛 System Architecture

```text
+-----------------------------------------------------------------------------------+
|                           UI LAYER (Presentation)                                 |
|  - WelcomeFragment, LobbyFragment, GameFragment, RoomFragment, ResultFragment     |
|  - GameSurfaceView (Canvas rendering at 60 FPS), SettingsActivity                 |
+-----------------------------------------+-----------------------------------------+
                                          |
                                          v (Data Binding / SingleLiveEvent)
+-----------------------------------------------------------------------------------+
|                             PRESENTATION (ViewModel)                              |
|  - WelcomeViewModel, LobbyViewModel, GameViewModel, RoomViewModel, ResultViewModel|
+-----------------------------------------+-----------------------------------------+
                                          |
                                          v (Invokes Enum-based UseCases)
+-----------------------------------------------------------------------------------+
|                             APPLICATION & DOMAIN LAYER                            |
|  - UseCases: WelcomeUseCase, LobbyUseCase, GameUseCase                           |
|  - Entities: Player, ChatMessage                                                  |
|  - Gateway Interface: IGameGateway                                                |
+-----------------------------------------+-----------------------------------------+
                                          |
                                          v (Implements IGameGateway)
+-----------------------------------------------------------------------------------+
|                           INFRASTRUCTURE (Gateway)                                |
|  - GameGatewayImpl (Manages ServiceConnection & Messenger IPC Client)             |
+-----------------------------------------+-----------------------------------------+
                                          | (IPC via Messenger / Messages)
                                          v
+-----------------------------------------------------------------------------------+
|                            BACKGROUND GAME SERVICE                                |
|  - GameService (Bound Service running in background)                              |
|  - EngineLoopManager (16ms ScheduledExecutorService)                              |
+-----------------------------------------+-----------------------------------------+
                                          |
                                          v (JNI C++ Native Bridge)
+-----------------------------------------------------------------------------------+
|                           NATIVE C++ GAME ENGINE                                  |
|  - NativeEngine (JNI Wrapper)                                                     |
|  - arenaminifight.cpp (std::map<string, PlayerObject>, 60FPS physics & constants)  |
+-----------------------------------------------------------------------------------+
```

---

# 🧩 Game Use Cases (UC-01 to UC-14)

| Use Case ID | Category | Description | Status |
| :--- | :--- | :--- | :--- |
| **UC-01** | Welcome & Init | User welcome and background service binding | Implemented ✅ |
| **UC-02** | Lobby & Social | Lobby chat messaging with server echo simulation | Implemented ✅ |
| **UC-03** | Navigation | Enter game arena / transition flows | Implemented ✅ |
| **UC-04** | Spawning | Spawn main player and AI bots via JNI | Implemented ✅ |
| **UC-05** | Controls | Player touch movement and position updating | Implemented ✅ |
| **UC-06** | Physics & Sync | 60 FPS C++ physics loop and state broadcasting | Implemented ✅ |
| **UC-07** | Lifecycle | Stop game and reset native C++ memory map | Implemented ✅ |
| **UC-08** | Matchmaking | Room creation and player slot management | Implemented ✅ |
| **UC-09** | Customization | Character class selection (Warrior / Assassin) | Implemented ✅ |
| **UC-10** | Combat | In-game action buttons (ATK / SKILL) | Implemented ✅ |
| **UC-11** | Settlement | Match results screen, stats, and rewards | Implemented ✅ |
| **UC-12** | Social | Friends and direct messaging | Roadmap ⏳ |
| **UC-13** | Economy | In-App purchases and Battle Pass | Roadmap ⏳ |
| **UC-14** | Settings | Audio (BGM/SFX) & FPS (60/30) Preferences | Implemented ✅ |

---

# 🧠 Implementation Highlights

- **Google Hilt DI**: Strict dependency injection across all ViewModels, Activities, and Fragments.
- **Gateway Pattern (`IGameGateway`)**: Clean separation between domain logic and IPC communication details.
- **PreferenceFragmentCompat**: Standard Android settings UI with `SharedPreferences` persistence.
- **Zero Magic Values (CQ-01)**: All configuration constants centralized in `GameConstants.java` and C++ `constexpr`.
- **MIT License Compliance (GP-03)**: Every source file includes the standard MIT License header.
