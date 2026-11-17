# ArenaMiniFight - System Architecture & Design Specification
# ArenaMiniFight - 系統架構與設計規範

---

## 1. Document Overview
## 1. 文件概覽

[中文]
本文件詳細規範了 **ArenaMiniFight** Android 遊戲系統的架構設計、完整使用情境 (Use Cases)、資料介面與版本歷史。本專案整合了跨行程通訊 (IPC)、原生 C++ 遊戲引擎 (JNI) 以及現代化的 Clean Architecture + MVVM 架構。

[English]
This document specifies the architectural design, comprehensive Use Cases, data contracts, and version history for the **ArenaMiniFight** Android game system. The project integrates Inter-Process Communication (IPC), a native C++ game engine (JNI), and modern Clean Architecture + MVVM design patterns.

---

## 2. Version History Table
## 2. 版本歷史記錄表

[中文 / English]

| Version / 版本 | Date / 日期 | Author / 作者 | Description (ZH / EN) / 描述 |
| :--- | :--- | :--- | :--- |
| **v1.0.0** | 2026-03-05 | Adam Chen | **ZH:** 初始版本 - 包含 Clean Architecture + MVVM、Messenger IPC、原生 JNI 引擎與類別設計。<br>**EN:** Initial Release - Baseline architecture featuring Clean Architecture + MVVM, Messenger IPC, Native JNI Engine, and class designs. |
| **v1.0.1** | 2026-03-05 | Adam Chen | **ZH:** 架構與程式碼標準重構 - 引入 Domain 介面 (`IGameRepository`)、列舉型 UseCase、修正命名錯字、加入 `SingleLiveEvent`、支援 JDK 17 與繁體中文國際化。<br>**EN:** Architecture & Code Standard Refactoring: Introduced Domain interfaces (`IGameRepository`), Enum-based UseCases, fixed naming typos, added `SingleLiveEvent`, JDK 17 support, and Traditional Chinese i18n. |
| **v1.1.0** | 2026-03-05 | Adam Chen | **ZH:** 擴充完整遊戲 Use Cases (UC-01 至 UC-14) - 涵蓋登入驗證、配對與組隊、商城經濟、好友系統、結算與設定等完整多人競技手遊功能。<br>**EN:** Expanded full game Use Cases (UC-01 through UC-14) covering authentication, matchmaking/parties, economy, social friends, settlement, and game settings. |

---

## 3. Game Use Cases (Comprehensive List)
## 3. 遊戲完整使用情境 (Use Cases)

### UC-01: User Welcome & Service Initialization
### UC-01: 使用者歡迎與服務初始化

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Application is launched on device. / 應用程式已在裝置上啟動。
- **Main Flow / 主要流程**:
  1. **[ZH]** 使用者開啟 App，顯示 `WelcomeFragment`。
     **[EN]** User opens app, displaying `WelcomeFragment`.
  2. **[ZH]** `WelcomeViewModel` 透過 `WelcomeUseCase` 執行 `BIND_SERVICE`。
     **[EN]** `WelcomeViewModel` executes `BIND_SERVICE` via `WelcomeUseCase`.
  3. **[ZH]** `GameRepositoryImpl` 繫結 `GameService`。
     **[EN]** `GameRepositoryImpl` binds `GameService`.
  4. **[ZH]** 當服務連線成功，就緒狀態 (`isReady`) 變為 `true`。
     **[EN]** Once connected, readiness state (`isReady`) becomes `true`.
  5. **[ZH]** 顯示成功 Toast 並導航至 `LobbyFragment`。
     **[EN]** Displays success toast and navigates to `LobbyFragment`.
- **Postconditions / 後置條件**: `GameService` is bound and ready. / `GameService` 已繫結並就緒。

---

### UC-02: Lobby Chat Messaging
### UC-02: 大廳聊天訊息

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Player is in Lobby. / 玩家位於大廳。
- **Main Flow / 主要流程**:
  1. **[ZH]** 玩家點擊輸入框，彈出對話框。
     **[EN]** Player taps input area, opening input dialog.
  2. **[ZH]** 輸入聊天文字並確認。
     **[EN]** Enters chat text and confirms.
  3. **[ZH]** `LobbyViewModel` 透過 `LobbyUseCase` 執行 `SEND_CHAT`。
     **[EN]** `LobbyViewModel` executes `SEND_CHAT` via `LobbyUseCase`.
  4. **[ZH]** 透過 IPC 發送 `UC_SEND_CHAT` 至 `GameService`。
     **[EN]** Sends `UC_SEND_CHAT` IPC message to `GameService`.
  5. **[ZH]** 經由 `NetworkService` 模擬伺服器回應。
     **[EN]** Simulated via `NetworkService` server echo delay.
  6. **[ZH]** 接收回應並更新 `RecyclerView` 聊天列表。
     **[EN]** Receives response and updates `RecyclerView` chat list.
- **Postconditions / 後置條件**: Chat message is rendered. / 聊天訊息已渲染。

---

### UC-03: Enter Game Arena
### UC-03: 進入遊戲競技場

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Player is in Lobby. / 玩家位於大廳。
- **Main Flow / 主要流程**:
  1. **[ZH]** 玩家點擊「開始遊戲」按鈕。
     **[EN]** Player clicks "Start Game" button.
  2. **[ZH]** 觸發導航事件，從 `LobbyFragment` 轉場至 `GameFragment`。
     **[EN]** Triggers navigation event, transitioning from `LobbyFragment` to `GameFragment`.
- **Postconditions / 後置條件**: Game Fragment is active. / 遊戲介面已啟用。

---

### UC-04: Spawn Main Player & AI Bots
### UC-04: 生成主玩家與 AI 機器人

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Game Fragment is initialized. / 遊戲介面已初始化。
- **Main Flow / 主要流程**:
  1. **[ZH]** `GameFragment` 初始化玩家 (`initPlayerFromJni`)。
     **[EN]** `GameFragment` initializes player (`initPlayerFromJni`).
  2. **[ZH]** `GameUseCase` 執行 `SPAWN_PLAYER`。
     **[EN]** `GameUseCase` executes `SPAWN_PLAYER`.
  3. **[ZH]** `GameService` 呼叫 C++ JNI `nativeInitializePlayer`。
     **[EN]** `GameService` calls C++ JNI `nativeInitializePlayer`.
  4. **[ZH]** C++ 引擎在記憶體中建立 `PlayerObject` 並回傳。
     **[EN]** C++ engine creates `PlayerObject` in memory and returns.
  5. **[ZH]** 渲染至 `GameSurfaceView`。
     **[EN]** Rendered on `GameSurfaceView`.
- **Postconditions / 後置條件**: Player exists in native engine and UI. / 玩家存在於原生引擎與 UI 中。

---

### UC-05: Player Touch Movement & Control
### UC-05: 玩家觸控移動與控制

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Player exists in game world. / 玩家存在於遊戲世界中。
- **Main Flow / 主要流程**:
  1. **[ZH]** 玩家在 `GameSurfaceView` 上觸控或滑動。
     **[EN]** Player touches or drags on `GameSurfaceView`.
  2. **[ZH]** 擷取座標 `(x, y)` 並透過 `GameUseCase` 執行 `MOVE_PLAYER`。
     **[EN]** Captures coordinates `(x, y)` and executes `MOVE_PLAYER` via `GameUseCase`.
  3. **[ZH]** 呼叫 JNI `updatePlayerPosition` 更新原生引擎座標。
     **[EN]** Calls JNI `updatePlayerPosition` to update native engine coordinates.
- **Postconditions / 後置條件**: Position updated in C++ engine. / C++ 引擎中的座標已更新。

---

### UC-06: Native Physics Loop & State Synchronization
### UC-06: 原生物理迴圈與狀態同步

- **Actor / 參與者**: `EngineLoopManager` / 引擎迴圈管理員
- **Preconditions / 前置條件**: Game loop started via `UC_START_GAME`. / 遊戲迴圈已啟動。
- **Main Flow / 主要流程**:
  1. **[ZH]** 背景執行緒以 16ms 執行更新 (`updateEngine`)。
     **[EN]** Background thread executes updates every 16ms (`updateEngine`).
  2. **[ZH]** C++ 引擎計算物理位移與邊界碰撞。
     **[EN]** C++ engine computes physics displacement and boundary collision.
  3. **[ZH]** 透過 `getGameState()` 取得字串格式狀態並廣播 (`UC_GAME_STATE_UPDATE`)。
     **[EN]** Retrieves state string via `getGameState()` and broadcasts (`UC_GAME_STATE_UPDATE`).
  4. **[ZH]** `GameSurfaceView` 進行 60 FPS 繪製。
     **[EN]** `GameSurfaceView` renders at 60 FPS.
- **Postconditions / 後置條件**: UI synchronized with physics engine. / UI 與物理引擎保持同步。

---

### UC-07: Stop Game & Native Engine Reset
### UC-07: 停止遊戲與原生引擎重置

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Game is active. / 遊戲進行中。
- **Main Flow / 主要流程**:
  1. **[ZH]** 玩家點擊離開或返回。
     **[EN]** Player clicks exit or returns.
  2. **[ZH]** 執行 `STOP_GAME`，停止 `EngineLoopManager`。
     **[EN]** Executes `STOP_GAME`, stopping `EngineLoopManager`.
  3. **[ZH]** 呼叫 JNI `resetEngine()` 清除 C++ `g_players` 記憶體。
     **[EN]** Calls JNI `resetEngine()` to clear C++ `g_players` memory.
- **Postconditions / 後置條件**: Native memory cleaned, preventing memory leaks. / 原生記憶體已清理，防止記憶體洩漏。

---

### UC-08: Matchmaking & Party / Room Management (New)
### UC-08: 快速配對與房間組隊管理 (新增)

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Player is authenticated and in the lobby. / 玩家已驗證並位於大廳。
- **Main Flow / 主要流程**:
  1. **[ZH]** 玩家點擊「尋找對戰」或建立自訂房間。
     **[EN]** Player taps "Find Match" or creates a custom room.
  2. **[ZH]** 配對系統依據 MMR (積分) 與延遲 (Ping) 搜尋同級玩家。
     **[EN]** Matchmaking system searches for peers based on MMR and ping.
  3. **[ZH]** 若超時 (如 45 秒)，自動補入 AI 機器人確保快速開局。
     **[EN]** If timed out (e.g., 45s), AI bots are backfilled for quick starts.
  4. **[ZH]** 所有玩家確認準備後，載入競技場場景。
     **[EN]** Once ready, all players load into the arena scene.
- **Postconditions / 後置條件**: Players enter the pre-game countdown or arena match. / 玩家進入賽前倒數或競技場。

---

### UC-09: Character / Avatar Selection & Customization (New)
### UC-09: 角色選擇與外觀自訂 (新增)

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Player is in room/lobby before match start. / 玩家在開賽前的大廳或房間中。
- **Main Flow / 主要流程**:
  1. **[ZH]** 玩家開啟角色選擇介面，瀏覽已解鎖的英雄/外觀。
     **[EN]** Player opens character selection screen to browse unlocked heroes/skins.
  2. **[ZH]** 點選特定角色並確認鎖定。
     **[EN]** Selects a specific character and confirms lock-in.
  3. **[ZH]** 伺服器記錄玩家選擇並套用至遊戲實例。
     **[EN]** Server records player selection and applies to the game instance.
- **Postconditions / 後置條件**: Character stats and skin loaded into match. / 角色數值與外觀已載入對局。

---

### UC-10: In-Game Combat & Ability/Attack Casting (New)
### UC-10: 局內戰鬥與攻擊/技能釋放 (新增)

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Player is inside an active arena match. / 玩家處於進行中的競技場對局內。
- **Main Flow / 主要流程**:
  1. **[ZH]** 玩家操作虛擬搖桿進行移動或點擊攻擊/技能按鈕。
     **[EN]** Player uses virtual joystick to move or taps attack/ability buttons.
  2. **[ZH]** 遊戲端傳送輸入指令封包 (`Action_ID`, 座標/方向) 至伺服器/服務。
     **[EN]** Client sends input packet (`Action_ID`, coordinates/direction) to server/service.
  3. **[ZH]** 伺服器驗證冷效與碰撞判定，計算傷害與血量扣減。
     **[EN]** Server validates cooldowns and collision, calculating damage and HP deduction.
  4. **[ZH]** 廣播最新血量與狀態，客戶端更新血條與特效 (VFX)。
     **[EN]** Broadcasts updated HP and state; clients update health bars and visual effects.
- **Postconditions / 後置條件**: Entities updated; player HP modified or defeated. / 實體更新；玩家血量變更或被擊敗。

---

### UC-11: Match Settlement, Scoring & Rewards (New)
### UC-11: 對局結算、積分與獎勵 (新增)

- **Actor / 參與者**: Game System / 遊戲系統
- **Preconditions / 前置條件**: Match win/loss condition is met (e.g., time expires or boss defeated). / 達到勝負條件 (時間到或擊敗目標)。
- **Main Flow / 主要流程**:
  1. **[ZH]** 遊戲引擎判定對局結束，觸發結算邏輯。
     **[EN]** Game engine determines match over and triggers settlement logic.
  2. **[ZH]** 計算玩家擊殺數、生存時間、傷害輸出與積分 (Trophies/XP)。
     **[EN]** Calculates player kills, survival time, damage output, and trophies/XP.
  3. **[ZH]** 顯示結算畫面 (`ResultFragment`)，發放獎勵貨幣。
     **[EN]** Displays settlement screen (`ResultFragment`) and awards currencies.
  4. **[ZH]** 玩家點擊返回按鈕，返回大廳。
     **[EN]** Player clicks return button to go back to the lobby.
- **Postconditions / 後置條件**: Match stats saved; player returns to lobby. / 對局數據已儲存，玩家返回大廳。

---

### UC-12: Social Friends & Direct Messaging (New)
### UC-12: 好友系統與私訊互動 (新增)

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Player is online in lobby. / 玩家線上並在大廳中。
- **Main Flow / 主要流程**:
  1. **[ZH]** 玩家點擊好友清單標籤，檢視好友上線狀態。
     **[EN]** Player taps friend list tab to view online status of friends.
  2. **[ZH]** 點選好友進行私人聊天或發送組隊邀請。
     **[EN]** Taps a friend to chat privately or send party invite.
  3. **[ZH]** 接受邀請後自動加入組隊房間。
     **[EN]** Automatically joins party room upon accepting invite.
- **Postconditions / 後置條件**: Social connection established. / 社交連結已建立。

---

### UC-13: In-App Purchase, Economy & Battle Pass (New)
### UC-13: 商城經濟、內購與戰令系統 (新增)

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Player is in main menu / store tab. / 玩家位於主選單或商城標籤。
- **Main Flow / 主要流程**:
  1. **[ZH]** 玩家瀏覽商城，選購造型或寶箱。
     **[EN]** Player browses store to purchase skins or chests.
  2. **[ZH]** 透過 Google Play Billing / 內購流程完成付費或使用遊戲內貨幣兌換。
     **[EN]** Completes transaction via Google Play Billing or exchanges in-game soft currency.
  3. **[ZH]** 系統解鎖並發放道具至玩家背包。
     **[EN]** System unlocks and delivers item to player inventory.
- **Postconditions / 後置條件**: Inventory updated with purchased items. / 背包已更新獲得購買的道具。

---

### UC-14: Game Settings, Audio & Graphics Customization (New)
### UC-14: 遊戲設定、音效與畫質調整 (新增)

- **Actor / 參與者**: Mobile Player / 手機玩家
- **Preconditions / 前置條件**: Player is anywhere in app menus/game. / 玩家在應用程式任一選單或對局中。
- **Main Flow / 主要流程**:
  1. **[ZH]** 玩家點擊設定圖示，調整音量 (BGM/SFX)、畫質 (60FPS/30FPS) 或搖桿靈敏度。
     **[EN]** Player taps settings icon, adjusting audio (BGM/SFX), graphics (60FPS/30FPS), or joystick sensitivity.
  2. **[ZH]** 系統即時儲存偏好設定至 SharedPreferences。
     **[EN]** System instantly saves preferences to SharedPreferences.
- **Postconditions / 後置條件**: Preferences applied. / 偏好設定已套用。

---

## 5. System Architecture & Class Design Block Diagram
## 5. 系統架構與類別設計方塊圖

[中文 / English]
```text
+-----------------------------------------------------------------------------------+
|                        UI LAYER (Presentation) / UI 呈現層                          |
|  - WelcomeFragment --> WelcomeViewModel --> WelcomeUseCase                        |
|  - LobbyFragment   --> LobbyViewModel   --> LobbyUseCase                          |
|  - GameFragment    --> GameViewModel    --> GameUseCase                           |
|  - ResultFragment  --> ResultViewModel                                            |
|  - RoomFragment    --> RoomViewModel                                              |
+-----------------------------------------+-----------------------------------------+
                                          |
                                          v (Depends on Domain Interface / 依賴領域介面)
+-----------------------------------------------------------------------------------+
|                               DOMAIN LAYER / 領域層                               |
|  - IGameRepository (Interface Contract / 介面合約)                                |
|  - Entities / 實體: Player, ChatMessage, MatchResult, PlayerInfo                  |
+-----------------------------------------+-----------------------------------------+
                                          |
                                          v (Implements / 實作介面)
+-----------------------------------------------------------------------------------+
|                        INFRASTRUCTURE LAYER / 基礎設施層                          |
|  - GameRepositoryImpl (Singleton, IPC Messenger Client, ServiceConnection)        |
+-----------------------------------------+-----------------------------------------+
                                          | (IPC Messenger & Handlers / 跨行程通訊)
                                          v
+-----------------------------------------------------------------------------------+
|                        BACKGROUND GAME SERVICE / 背景服務層                       |
|  - GameService (Bound Service, Message Handler)                                   |
|  - EngineLoopManager (16ms ScheduledExecutorService)                              |
|  - NetworkService (Simulation & Network Handling)                                 |
+-----------------------------------------+-----------------------------------------+
                                          |
                                          v (JNI Native Bridge / 原生呼叫)
+-----------------------------------------------------------------------------------+
|                         NATIVE C++ ENGINE / 原生 C++ 引擎                         |
|  - NativeEngine (JNI Java Wrapper / JNI 包裝器)                                   |
|  - arenaminifight.cpp (std::map<string, PlayerObject>, 60FPS Physics Loop)        |
+-----------------------------------------------------------------------------------+
```
