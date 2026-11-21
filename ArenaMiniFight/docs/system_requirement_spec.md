# Arena Mini Fight - System Requirement Specification (SRS)

## Revision History / 版本歷史
| Date / 日期 | Version / 版本 | Description / 說明 | Author / 作者 |
| :--- | :--- | :--- | :--- |
| 2025-02-21 | 1.0.0 | Initial System Requirement Specification creation with Use Case Traceability | Developer |
| 2025-02-21 | 1.1.0 | Updated SRS to align with revised Use Cases | Developer |
| 2025-02-21 | 1.2.0 | Added Menu Flow, Setting, and Leaderboard storage requirements and traceabilities | Developer |
| 2025-02-21 | 1.3.0 | Added About screen requirement (REQ-MENU-05) to align with Use Case and UI specs | Developer |

---

## 1. Introduction / 簡介
This document defines the system requirements for **Arena Mini Fight**, mapping high-level architecture and functional requirements to the corresponding Use Cases (`use_case_spec.md`).
本文件定義 **Arena Mini Fight** 的系統需求，並將高層級架構與功能需求對應至對應的 Use Case (`use_case_spec.md`)。

---

## 2. Functional Requirements / 功能性需求 (FR)

### 2.1 Menu & Navigation Requirements / 選單與導航需求
| Req ID | Requirement Description / 需求描述 | Traced UC / 對應 Use Case |
| :--- | :--- | :--- |
| **REQ-MENU-01** | The system shall display a Splash Screen upon app launch, initialize configurations/storage, and automatically navigate to the Menu List. / 系統應在 App 啟動時顯示閃屏、初始化設定與儲存，並自動跳轉至選單列表。 | `UC-MENU-01` |
| **REQ-MENU-02** | The system shall present a Menu List allowing players to select among "Start Game", "Setting", "Leaderboard", and "About". / 系統應呈現選單列表，允許玩家在「Start Game」、「Setting」、「Leaderboard」與「About」之間進行選擇。 | `UC-MENU-02` |
| **REQ-MENU-03** | The system shall provide a Setting screen allowing players to modify audio/graphics preferences and persist them in local storage. / 系統應提供設定畫面，允許玩家修改音量與畫質偏好並持久化至本地儲存。 | `UC-MENU-03` |
| **REQ-MENU-04** | The system shall provide a Leaderboard screen querying local database (Room) for top survival scores and kill counts. / 系統應提供排行榜畫面，查詢本地資料庫（Room）以取得最高生存分數與擊殺數。 | `UC-MENU-04` |
| **REQ-MENU-05** | The system shall provide an About screen displaying application version information, copyright details, and brief game instructions. / 系統應提供關於畫面，顯示應用程式版本資訊、版權細節與簡短遊戲說明。 | `UC-MENU-05` |

---

### 2.2 Core Gameplay Requirements / 核心遊戲功能需求

| Req ID | Requirement Description / 需求描述 | Traced UC / 對應 Use Case |
| :--- | :--- | :--- |
| **REQ-GAME-01** | The system shall initialize the Game Service, load Native Game Engines, and establish the game arena when "Start Game" is selected. / 當選擇「Start Game」時，系統應初始化遊戲服務、載入 Native 遊戲引擎並建立競技場。 | `UC-GAME-01` |
| **REQ-GAME-02** | The system shall capture player input from mobile virtual directional buttons (Up, Down, Left, Right), pass movement commands through ViewModel to Service, and invoke native `movePlayer()`. / 系統應擷取手機虛擬方向鍵的玩家輸入，透過 ViewModel 傳遞移動指令至 Service，並呼叫 native `movePlayer()`。 | `UC-GAME-02` |
| **REQ-GAME-03** | The system shall process normal attack requests from the player, invoking native attack logic and calculating hit collisions. / 系統應處理玩家的普通攻擊請求，呼叫 native 攻擊邏輯並計算擊中碰撞。 | `UC-GAME-03` |
| **REQ-GAME-04** | The system shall check skill cooldowns and execute special skills via JNI, updating animations and skill effects. / 系統應檢查技能冷卻並透過 JNI 執行特殊技能，更新動畫與技能效果。 | `UC-GAME-04` |
| **REQ-GAME-05** | The AI system shall perform periodic ticks to search for the nearest player and compute pathfinding via native algorithms. / AI 系統應進行定期 Tick 以搜尋最近的玩家並透過 native 演算法計算尋路。 | `UC-GAME-05`, `UC-JNI-02` |
| **REQ-GAME-06** | The physics engine shall perform collision detection between entities and return hit/miss results. / 物理引擎應執行實體間的碰撞檢測並回傳 hit/miss 結果。 | `UC-GAME-06`, `UC-JNI-01` |
| **REQ-GAME-07** | The system shall compute damage based on attack and defense attributes (`damage = max(1, attack - defense)`) and update HP, EXP, and kill counts. / 系統應根據攻擊與防禦屬性計算傷害並更新 HP、EXP 與擊殺數。 | `UC-GAME-07` |
| **REQ-GAME-08** | The system shall grant experience points upon enemy defeat and evaluate level-up thresholds. / 系統應在擊敗敵人時發放經驗值並評估升級門檻。 | `UC-GAME-08` |
| **REQ-GAME-09** | The system shall display an upgrade selection dialog upon level-up, allowing players to choose enhancements or skills. / 系統應在升級時顯示升級選擇對話框，允許玩家選擇強化或技能。 | `UC-GAME-09` |
| **REQ-GAME-10** | The system shall automatically spawn new AI enemies every 10 seconds to increase difficulty. / 系統應每 10 秒自動生成新的 AI 敵人以增加難度。 | `UC-GAME-10` |
| **REQ-GAME-11** | The system shall support background execution via `GameService` when the application is paused (e.g., Home key). / 當應用程式暫停時，系統應支援透過 `GameService` 進行背景執行。 | `UC-GAME-11` |
| **REQ-GAME-12** | The system shall handle game over and exit scenarios: automatically stop the game, persist score to Room DB, and transition to the summary/result screen (ResultFragment) whether player HP drops to 0 (Path A) or player triggers manual exit/back (Path B). / 系統應處理遊戲結束與離開情境：無論是玩家 HP 歸零（Path A）或手動觸發離開/返回（Path B），系統皆應自動中止遊戲、將分數持久化至 Room DB，並跳轉至結算畫面（ResultFragment）。 | `UC-GAME-12` |

---

### 2.3 IPC Architecture Requirements / IPC 架構需求

| Req ID | Requirement Description / 需求描述 | Traced UC / 對應 Use Case |
| :--- | :--- | :--- |
| **REQ-IPC-01** | The application shall be split into UI Process and Game Process, communicating via Android Binder IPC (`IGameService.aidl`). / 應用程式應拆分為 UI Process 與 Game Process，透過 Android Binder IPC 進行通訊。 | `UC-IPC-01`, `UC-IPC-02` |
| **REQ-IPC-02** | The UI process shall be able to fetch real-time game state (`GameState`) from the remote game service. / UI 行程應能從遠端遊戲服務取得即時遊戲狀態 (`GameState`)。 | `UC-IPC-01` |
| **REQ-IPC-03** | The UI process shall transmit control commands (e.g., movement vectors from virtual D-pad) to the Game Service via Binder IPC. / UI 行程應透過 Binder IPC 將控制指令傳遞至 Game Service。 | `UC-IPC-02` |

---

### 2.4 JNI & Native Engine Requirements / JNI 與 Native 引擎需求

| Req ID | Requirement Description / 需求描述 | Traced UC / 對應 Use Case |
| :--- | :--- | :--- |
| **REQ-JNI-01** | The C++ Native Engine shall provide high-performance collision calculation functions exposed via JNI (`checkCollision`). / C++ Native 引擎應提供透過 JNI 暴露的高效能碰撞計算函式。 | `UC-JNI-01` |
| **REQ-JNI-02** | The C++ Native Engine shall implement A* / pathfinding algorithms exposed via JNI (`pathFind`) for AI navigation. / C++ Native 引擎應實作透過 JNI 暴露的 A* / 尋路演算法供 AI 導航使用。 | `UC-JNI-02` |
| **REQ-JNI-03** | The C++ Native Engine shall drive the physics simulation and world updates every frame via JNI (`updateWorld`). / C++ Native 引擎應透過 JNI 驅動每個畫面的物理模擬與世界更新。 | `UC-JNI-03` |

---

## 3. Non-Functional Requirements / 非功能性需求 (NFR)

| Req ID | Requirement Description / 需求描述 |
| :--- | :--- |
| **REQ-NFR-01** | **Performance**: The native physics loop (`updateWorld`) and rendering shall maintain a stable 60 FPS. / **效能**：Native 物理迴圈與渲染應維持穩定的 60 FPS。 |
| **REQ-NFR-02** | **Stability**: IPC communication failures (e.g., Service disconnection) must be gracefully handled with auto-rebind mechanisms. / **穩定性**：IPC 通訊失敗必須透過自動重新繫結機制進行優雅處理。 |
| **REQ-NFR-03** | **Architecture**: Clear separation of concerns following MVVM and Clean Architecture patterns in Java, communicating with C++ core via JNI and Binder IPC. / **架構**：遵循 Java 中的 MVVM 與 Clean Architecture 模式進行清晰的關注點分離。 |
| **REQ-NFR-04** | **Persistence**: Local storage (SharedPreferences / DataStore for Settings, Room DB for Leaderboard scores) must operate reliably without blocking the UI thread. / **持久化**：本地儲存（設定用 SharedPreferences / DataStore，排行榜用 Room DB）必須穩定運作且不阻塞 UI 執行緒。 |
