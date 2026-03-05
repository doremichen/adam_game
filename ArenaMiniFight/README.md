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

🟦 Version 1 — Basic UC
UC-01 Start Game: 使用者點擊開始按鈕，初始化遊戲環境並進入遊戲流程。

UC-02 Auto Login (Guest): 系統自動為使用者建立遊客帳號，無需手動輸入帳密即可快速進入遊戲。

UC-03 Enter Lobby Chat: 使用者成功進入大廳，建立與伺服器的 WebSocket 或 Socket 連線。

UC-04 Send Chat Message: 使用者在大廳輸入文字並發送，其他在線玩家可即時看見。

UC-05 Initialize Player & Spawn (新增): 進入遊戲畫面時，由 JNI 引擎分配初始座標（PointF）與屬性，並在 SurfaceView 繪製出玩家角色。

UC-06 Move Player: 點擊螢幕傳送目標座標給 JNI 引擎，計算位移並更新本地玩家位置。

UC-07 Sync Other Players: 接收來自 Service 的遠端玩家數據，在畫面上即時繪製其他玩家的圓圈。

UC-08 Background Service Maintains Connection: 當 App 切換至背景時，Android Service 持續維持連線狀態，避免斷線。

🟩 Version 2 — Battle UC
UC-09 Attack: 玩家觸發攻擊指令（如點擊角色或攻擊鈕），產生攻擊判定範圍。

UC-10 Hit Detection (JNI): 由 C++ 引擎高速計算兩個玩家座標是否重疊，判定是否擊中目標。

UC-11 HP Reduction: 當被擊中時，同步更新 Player 物件中的 mHp 數值並反應在血條上。

UC-12 End Game (Result Screen): 當 HP 歸零或計時結束，結算比賽結果並顯示結算畫面。

🟥 Version 3 — Full Arena UC
UC-13 Create Room: 使用者可以建立自定義房間，等待其他玩家加入。

UC-14 Matchmaking: 系統自動尋找程度相當的對手，並將兩者配對進同一場戰鬥。

UC-15 Enter Battle Arena: 切換場景至戰鬥專用競技場地，載入地圖邊界數據。

UC-16 AI Movement (JNI): 在無人填補空位時，由 JNI 引擎運算 AI 敵人的隨機移動與追逐邏輯。

UC-17 Save Replay (Room DB): 遊戲結束後將戰鬥軌跡存入 Room 資料庫，供日後回放檢視。

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