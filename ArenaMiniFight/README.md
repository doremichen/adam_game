# ArenaMiniFight
A real-time multiplayer Android battle arena game built with **Java**, **Android Service**,  
**Room Database**, **JNI (C++)**, and **WebSocket networking**.

This project is designed as a step-by-step evolution:
1. **Basic Version** — Chat room + Player movement sync  
2. **Battle Version** — Attacks, bullets, HP, result screen  
3. **Full Arena Version** — Rooms, lobby, AI bots (JNI), matchmaking  

This repository contains a complete educational-level architecture following:
- Use Case Driven Development  
- Domain Model → Analysis Model → Design Model  
- MVVM + Repository + Service Layer  
- JNI/C++ for performance-critical logic  

---

# 🚀 Features

## ✅ Version 1 — Basic Multiplayer  
- Real-time WebSocket networking  
- Background **Foreground Service** keeps connection alive  
- Player movement synchronization  
- Multiplayer chat room  
- Canvas-based mini map  

## ✅ Version 2 — Battle System  
- Attack & projectile system  
- HP and damage calculation  
- Game result screen with Safe Args  
- Collision detection using **JNI (C++)** for speed  

## ✅ Version 3 — Full Arena  
- Lobby / Room / Matchmaking  
- Local AI Bots (JNI pathfinding)  
- Replay system (Room database)  
- Expandable skill system  

---

# 🏗 System Architecture

```text
+---------------------------+
|        UI Layer           |
| (Fragments, CanvasView)   |
+-------------+-------------+
              |
              v
+---------------------------+
|  MVVM ViewModel Layer     |
+-------------+-------------+
              |
              v
+---------------------------+
|     Repository Layer      |
+-------------+-------------+
              |
              v
+---------------------------+
|  Service Layer (Java)     |
|  - NetworkService         |
|  - GameService            |
+-------------+-------------+
              |
              v
+---------------------------+
|    JNI C++ Engine         |
|  - Collision Engine       |
|  - AI Pathfinding         |
+-------------+-------------+
              |
              v
+---------------------------+
|   Room Database Storage   |
+---------------------------+

# 📦 Architecture Overview

## 📦 Project Structure

```text
ArenaMiniFight/
└── app/
    ├── java/com/arena/app/
    │   ├── ui/
    │   │   ├── game/
    │   │   ├── lobby/
    │   │   ├── room/
    │   │   └── result/
    │   ├── viewmodel/
    │   ├── domain/
    │   │   ├── model/
    │   │   └── usecase/
    │   ├── data/
    │   │   ├── repository/
    │   │   ├── service/
    │   │   └── database/
    │   └── jni/
    │       ├── collision_engine.cpp
    │       ├── ai_pathfinder.cpp
    │       └── native_lib.cpp
    ├── cpp/
    │   └── CMakeLists.txt
    └── res/
        ├── layout/
        ├── xml/
        └── navigation/

# 📘 Use Case Overview

## 🟦 Version 1 — Basic UC

UC-01 Start Game

UC-02 Auto Login (Guest)

UC-03 Enter Lobby Chat

UC-04 Send Chat Message

UC-05 Move Player

UC-06 Sync Other Players

UC-07 Background Service Maintains Connection

## 🟩 Version 2 — Battle UC

UC-08 Attack

UC-09 Hit Detection (JNI)

UC-10 HP Reduction

UC-11 End Game (Result Screen)

## 🟥 Version 3 — Full Arena UC

UC-12 Create Room

UC-13 Matchmaking

UC-14 Enter Battle Arena

UC-15 AI Movement (JNI)

UC-16 Save Replay (Room DB)

# 🧩 Domain Model (Entities)

```text
classDiagram
    class Player {
        +id: String
        +name: String
        +x: float
        +y: float
        +direction: float
        +hp: int
    }

    class ChatMessage {
        +senderId: String
        +senderName: String
        +message: String
        +timestamp: long
    }

    class Projectile {
        +ownerId: String
        +x: float
        +y: float
        +dx: float
        +dy: float
    }

    class GameRoom {
        +roomId: String
        +players: List<Player>
        +isBattleStarted: boolean
    }

# 🧠 Analysis Model
```text
classDiagram
    class NetworkService {
        +connect()
        +disconnect()
        +send(json)
        +observeEvents()
    }

    class MovementService {
        +sendMove()
        +observePositions()
    }

    class CombatService {
        +sendAttack()
        +observeDamage()
    }

    class JNIEngine {
        +checkCollision()
        +calculatePath()
    }

# 🏛 Design Model — Sample Sequence

## Attack → Projectile → Collision (JNI)
```text
sequenceDiagram
    actor Player
    Player ->> GameViewModel: onAttack()
    GameViewModel ->> CombatService: sendAttack()
    CombatService ->> NetworkService: send(projectile)
    NetworkService ->> Server: broadcast()

    Server ->> All Clients: projectile_update
    NetworkService ->> JNIEngine: checkCollision()
    JNIEngine ->> GameState: applyDamage()
    GameState ->> GameView: refresh UI

# 🔧 Build & Run
* Android Studio Requirements
* Android Studio Giraffe / Hedgehog / Iguana 以上
* NDK installed
* CMake enabled

## Build Steps

1. git clone https://github.com/doremichen/adam_game.git
2. open with Android Studio
3. select ArenaMiniFight project
4. Build > Make Project
5. Run on device

# 🗺 Roadmap
## Version 1
* Chat
* Movement
* WebSocket Service
* Mini map

## Version 2
* Projectiles
* JNI collision detection
* HP
* Result screen

## Version 3
* Matching
* AI bot (JNI)
* Replay DB
* Combat Skills
* Multi-room server