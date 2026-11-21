# Arena Mini Fight - Architecture Design Document (4+1 View Model + Modern Android Stack)

## Revision History / 版本歷史
| Date / 日期 | Version / 版本 | Description / 說明 | Author / 作者 |
| :--- | :--- | :--- | :--- |
| 2025-02-21 | 1.0.0 | Initial Architecture Design Document creation | Developer |
| 2025-02-21 | 2.0.0 | Updated Architecture Design with detailed Mermaid diagrams | Developer |
| 2025-02-21 | 3.0.0 | Added Menu navigation flow, Setting, and Leaderboard architecture components | Developer |
| 2025-02-21 | 3.1.0 | Added About screen navigation and UI component wiring in architecture views | Developer |

---

## 1. Introduction / 簡介
This document outlines the system architecture for **Arena Mini Fight**, adopting the **4+1 Architectural View Model** and modern Android architectural best practices (**MVVM**, **Clean Architecture**, **Hilt DI**, **View Binding**, and **Data Binding**). It traces directly to the System Requirements Specification (`system_requirement_spec.md`) and Use Case Specification (`use_case_spec.md`).

本文件概述 **Arena Mini Fight** 的系統架構，採用 **4+1 架構視圖模型** 與現代 Android 架構最佳實踐（**MVVM**、**Clean Architecture**、**Hilt DI**、**View Binding** 與 **Data Binding**）。本文件直接對應系統需求規格書與 Use Case 規格書。

---

## 2. 1. Use Case View / 使用案例視圖 (The "+1" View)
The Use Case View serves as the foundation, driving the architectural design by capturing the functional requirements and user interactions.

### 2.1 Use Case Diagram (Use Case 視圖方塊圖)
```mermaid
graph TD
    subgraph Actors [Actors / 參與者]
        Player["A01 Player (玩家)"]
        AI["A02 AI Enemy (AI 敵人)"]
        Service["A03 Game Service (遊戲服務)"]
    end

    subgraph MenuFlow [Menu & Navigation / 選單與導航]
        UC_M01["UC-MENU-01: 應用程式啟動與閃屏 (Splash)"]
        UC_M02["UC-MENU-02: 瀏覽選單列表 (Menu List)"]
        UC_M03["UC-MENU-03: 遊戲設定 (Setting)"]
        UC_M04["UC-MENU-04: 查看排行榜 (Leaderboard)"]
        UC_M05["UC-MENU-05: 關於遊戲 (About)"]
    end

    subgraph CoreGameplay [Core Gameplay / 核心遊玩]
        UC01["UC-GAME-01: 啟動遊戲"]
        UC02["UC-GAME-02: 控制角色移動 (虛擬方向鍵)"]
        UC03["UC-GAME-03: 普通攻擊"]
        UC04["UC-GAME-04: 釋放技能"]
        UC05["UC-GAME-05: AI 自動追擊"]
        UC06["UC-GAME-06: 碰撞檢測"]
        UC07["UC-GAME-07: 計算傷害"]
        UC08["UC-GAME-08: 擊敗敵人取得經驗"]
        UC09["UC-GAME-09: 選擇升級技能"]
        UC10["UC-GAME-10: 產生敵人"]
        UC11["UC-GAME-11: 暫停遊戲"]
        UC12["UC-GAME-12: 遊戲結束與離開 (Path A / Path B)"]
    end

    subgraph IPC [IPC / 行程間通訊]
        UC13["UC-IPC-01: 取得遊戲狀態 (Binder)"]
        UC14["UC-IPC-02: 發送控制指令 (Binder)"]
    end

    subgraph JNI [JNI / Native 介面]
        UC15["UC-JNI-01: JNI 計算碰撞"]
        UC16["UC-JNI-02: JNI AI 尋路"]
        UC17["UC-JNI-03: JNI 物理更新 (60 FPS)"]
    end

    Player --> UC_M01
    UC_M01 --> UC_M02
    UC_M02 --> UC01
    UC_M02 --> UC_M03
    UC_M02 --> UC_M04
    UC_M02 --> UC_M05

    Player --> UC02
    Player --> UC03
    Player --> UC04
    Player --> UC09
    Player --> UC11
    Player --> UC12
    Player --> UC13
    Player --> UC14

    AI --> UC05

    Service --> UC10
    Service --> UC17

    UC02 --> UC14
    UC03 --> UC15
    UC05 --> UC16
    UC06 --> UC15
    UC07 --> UC06
    UC08 --> UC07
    UC12 --> UC07
```

- **Traceability to SRS & Use Cases**:
  - **Menu Flow (`UC-MENU-01` ~ `UC-MENU-05`)**: Splash screen, Menu List routing, Setting persistence, Leaderboard Room DB querying, and About screen.
  - **Core Gameplay (`UC-GAME-01` ~ `UC-GAME-12`)**: Handled by UI, Domain Use Cases, Repositories, and Native C++ Engine.
  - **IPC Architecture (`UC-IPC-01` ~ `UC-IPC-02`)**: Handled across UI Process and Game Process via Android Binder (`IGameService.aidl`).
  - **JNI & Native Engine (`UC-JNI-01` ~ `UC-JNI-03`)**: Handled by Native Engine wrappers interfacing with C++ core for physics, collision, and pathfinding.

---

## 3. 2. Logical View / 邏輯視圖 (Clean Architecture + MVVM)
The Logical View describes the application's package structure, layers, and class-level responsibilities following **Clean Architecture** principles and **MVVM**.

### 3.1 Corrected Clean Architecture Layers (修正後的 Clean Architecture 分層)
According to the requested Clean Architecture hierarchy from **Inner to Outer**:

1. **Entity to Domains**: 
   - Core business models and domain entities (e.g., `Player`, `Enemy`, `GameState`, `Point`, `ScoreRecord`). This is the innermost core layer.
2. **Use Cases + Interface Adapters to Applications**: 
   - Interactors containing business rules, ViewModels, Presenters, and Repositories. This middle layer coordinates data flow and business logic between entities and frameworks.
3. **Frameworks & Drivers to Infrastructures**: 
   - UI layer (`SplashActivity`, `MenuActivity`, `GameActivity`, `SettingActivity`, `LeaderboardActivity`, `AboutActivity` with **View Binding** & **Data Binding**), local storage (Room DB & SharedPreferences), AIDL remote services (`IGameService.aidl`), and JNI native engine wrappers. This is the outermost layer.

```mermaid
graph TD
    subgraph Outer ["3. Frameworks and Drivers (Infrastructures)"]
        UI["UI Layer: Splash, Menu, Game, Setting, Leaderboard, About (View/Data Binding)"]
        Storage["Local Storage: Room DB (Leaderboard) & SharedPreferences (Setting)"]
        IPC["Remote Service / AIDL Binder Connector"]
        JNI["JNI Native Engine Bridge"]
    end

    subgraph Middle ["2. Use Cases + Interface Adapters (Applications)"]
        VM["ViewModels and LiveData"]
        UC["Use Cases / Interactors"]
        Repo["Repository Implementations"]
    end

    subgraph Inner ["1. Entity (Domains)"]
        DomainModels["Domain Entities: Player, Enemy, GameState, Point, ScoreRecord"]
    end

    UI --> VM
    VM --> UC
    UC --> Repo
    Repo --> Storage
    Repo --> IPC
    Repo --> JNI
    UC --> DomainModels
    Repo --> DomainModels
```

---

## 4. 3. Process View / 行程與執行緒視圖 (Process View)
The Process View defines runtime processes, thread management, and IPC communication (`REQ-IPC-01` ~ `REQ-IPC-03`, `REQ-NFR-02`).

### 4.1 Multi-Process Architecture (Multi-Process Separation) / 多行程架構
To demonstrate advanced Android IPC and isolate game workload:
- **UI Process (`com.example.arenaminifight`)**:
  - Hosts `SplashActivity`, `MenuActivity`, `GameActivity`, `SettingActivity`, `LeaderboardActivity`, `AboutActivity`, ViewModels, View Binding, Data Binding, Room DB, and SharedPreferences.
- **Game Process (`com.example.arenaminifight:game`)**:
  - Hosts `GameService` (`Service` running in remote process), game loop ticks, C++ JNI Native Engine, physics simulation (`updateWorld`), and AI pathfinding.
- **IPC Communication**:
  - Connected via Android Binder IPC (`IGameService.aidl`).
  - `GameState getGameState();` (Polled / Observed by UI)
  - `movePlayer(float x, float y)` (Sent from UI to Game Service)

### 4.2 Sequence Diagram (序列方塊圖 - 應用程式啟動與選單流程)
```mermaid
sequenceDiagram
    autonumber
    actor Player as A01 Player (玩家)
    participant Splash as SplashActivity
    participant Menu as MenuActivity
    participant Game as GameActivity
    participant Service as GameService (Remote)

    Player->>Splash: 點擊 App 圖示啟動
    Splash->>Splash: 初始化設定與本地儲存
    Splash-->>Menu: 自動跳轉至 Menu List
    Player->>Menu: 點擊「Start Game」
    Menu->>Game: 啟動 GameActivity
    Game->>Service: 繫結 GameService 並初始化 Native 引擎
    Service-->>Game: 競技場就緒，進入遊戲
```

### 4.3 State Diagram (狀態方塊圖 - App 導航與遊戲生命週期)
```mermaid
stateDiagram-v2
    [*] --> Splash : App Launch (UC-MENU-01)
    Splash --> MenuList : Init Completed (UC-MENU-02)
    
    MenuList --> Setting : Click Setting (UC-MENU-03)
    Setting --> MenuList : Back to Menu
    
    MenuList --> Leaderboard : Click Leaderboard (UC-MENU-04)
    Leaderboard --> MenuList : Back to Menu

    MenuList --> About : Click About (UC-MENU-05)
    About --> MenuList : Back to Menu
    
    MenuList --> GameSession : Click Start Game (UC-GAME-01)
    
    state GameSession {
        [*] --> Active
        Active --> Paused : Home Key (UC-GAME-11)
        Paused --> Active : Resume
    }

    GameSession --> GameOver : HP = 0 (Path A / Save Score)
    GameSession --> ExitConfirm : Back Button (Path B)
    
    ExitConfirm --> Active : Resume Clicked
    ExitConfirm --> MenuList : Exit Clicked
    GameOver --> MenuList : Restart / Return to Menu
    GameOver --> [*] : Close App
```

---

## 5. 4. Development view / 開發視圖 (Component & Dependency Injection with Hilt)
The Development View organizes the codebase into modular components and defines dependency injection wiring (`REQ-NFR-03`).

### 5.1 Class Diagram (類別方塊圖 - 架構關聯與 DI)
```mermaid
classDiagram
    class MenuActivity {
        -ActivityMenuBinding binding
        -MenuViewModel viewModel
    }

    class GameActivity {
        -ActivityGameBinding binding
        -GameViewModel viewModel
    }

    class LeaderboardActivity {
        -ActivityLeaderboardBinding binding
        -LeaderboardViewModel viewModel
    }

    class AboutActivity {
        -ActivityAboutBinding binding
    }

    class GameRepositoryImpl {
        -IGameService remoteService
        -ScoreDao scoreDao
        -SettingDataSource settingDataSource
    }

    class ScoreDao {
        <<interface>>
        +List~ScoreRecord~ getTopScores()
        +void insertScore(ScoreRecord score)
    }

    class GameService {
        -IGameServiceStub binder
        -NativeEngineWrapper nativeEngine
    }

    GameActivity --> GameViewModel : Uses MVVM
    MenuActivity --> MenuViewModel : Uses MVVM
    LeaderboardActivity --> LeaderboardViewModel : Uses MVVM
    AboutActivity --> AboutViewModel : Uses MVVM
    GameViewModel --> GameRepositoryImpl : Injected by Hilt
    LeaderboardViewModel --> GameRepositoryImpl : Injected by Hilt
    GameRepositoryImpl --> ScoreDao : Room DB
    GameRepositoryImpl ..|> IGameService : AIDL Binder
    GameService --> NativeEngineWrapper : C++ Engine
```

### 5.2 UI Binding Strategy / UI 綁定策略
- **View Binding**: Used across all layouts (`ActivitySplashBinding`, `ActivityMenuBinding`, `ActivityGameBinding`, `ActivitySettingBinding`, `ActivityLeaderboardBinding`, `ActivityAboutBinding`) to eliminate `findViewById` boilerplate and ensure null safety.
- **Data Binding**: Used in layout XML files for reactive variable binding (e.g., binding player HP, score, and leaderboard records directly to ViewModel observables).

---

## 6. 5. Physical View / 實體部署視圖 (Deployment View)
The Physical View describes how software artifacts are deployed onto target hardware and runtime environments.

### 6.1 Component Deployment Diagram (組件部署方塊圖)
```mermaid
graph TB
    subgraph TargetDevice ["Android Mobile Target Device"]
        subgraph APK ["Arena Mini Fight APK"]
            subgraph UIProc ["UI Process (Activities & ViewModels)"]
                Activities["Activities & ViewModels"]
                RoomDB["Room Database (Leaderboard)"]
                Prefs["SharedPreferences (Setting)"]
                VB_DB["View Binding and Data Binding"]
            end

            subgraph GameProc ["Game Process (Remote Service)"]
                Service["GameService (Remote Service)"]
                AIDL["IGameService.aidl (Binder Stub)"]
                JNI_Bridge["JNI Native Bridge (Java)"]
            end

            subgraph NativeCore ["C++ Native Layer"]
                SO["libarenaminifight_engine.so"]
                Engine["Physics / AI / Collision Engine"]
            end
        end
    end

    Activities --> VB_DB
    Activities --> RoomDB
    Activities --> Prefs
    Activities -- "Binder IPC" --> AIDL
    AIDL --> Service
    Service --> JNI_Bridge
    JNI_Bridge -- "System.loadLibrary" --> SO
    SO --> Engine
```

### 6.2 Hardware & Runtime Target / 硬體與執行環境
- **Target Device**: Android Mobile Devices (Phones / Tablets running Android 8.0+ / API 26+).
- **Input Hardware**: Touchscreen virtual directional buttons and menu touch navigation (`UC-MENU-02`, `UC-GAME-02`).
- **Native Library Deployment**:
  - Compiled C++ shared libraries (`libarenaminifight_engine.so`) packaged inside the APK `.so` libraries directory, loaded via `System.loadLibrary("arenaminifight_engine")`.
- **Process Topology**:
  - Single APK containing dual-process declaration (`android:process=":game"` in `AndroidManifest.xml` for `GameService`).
