# Arena Mini Fight - Use Case Specification

## Revision History / 版本歷史
| Date / 日期 | Version / 版本 | Description / 說明 | Author / 作者 |
| :--- | :--- | :--- | :--- |
| 2025-02-21 | 1.0.0 | Initial Use Case Specification creation / 建立初始 Use Case 規格書 | Developer |
| 2025-02-21 | 1.1.0 | Added bilingual (Chinese/English) support and revision history table / 新增中英文對照與版本歷史表格 | Developer |
| 2025-02-21 | 2.0.0 | Expanded all Use Cases to detailed standard specification format | Developer |
| 2025-02-21 | 2.1.0 | Standardized IDs (UC-GAME-01 to UC-JNI-03) for global traceability | Developer |
| 2025-02-21 | 2.7.0 | Added Menu Flow Use Cases (Splash, Menu List, Setting, Leaderboard) | Developer |
| 2025-02-21 | 2.8.0 | Converted all text-based arrow flows into Mermaid flowchart diagrams / 將所有文字流程全面改為 Mermaid 流程圖表示 | Developer |

---

## Project Overview / 專案概述
**Arena Mini Fight** is a top-down arena survival game where players control a hero to move, attack, level up skills, defeat AI enemies, and survive to the end.
這是一款俯視角（Top-down）競技場生存遊戲。玩家控制英雄在競技場中移動、攻擊、升級技能，擊敗 AI 敵人並存活到最後。

## System Architecture Target / 系統架構目標
```
UI Layer
    ↓
ViewModel
    ↓
Repository
    ↓
GameService
    ↓ IPC (AIDL/Binder)
NativeGameEngine (JNI)
```
- **Java/Kotlin** → **JNI** → **C++ Game Engine**
- C++ Game Engine is responsible for / C++ 遊戲引擎負責:
  - Collision Detection / 碰撞檢測
  - AI
  - Path Finding / 尋路
  - Physics / 物理模擬

---

## Actors / 參與者
- **A01 Player / 玩家**: Controls the character to perform combat and actions. / 控制角色進行戰鬥。
- **A02 AI Enemy / AI 敵人**: System-controlled enemies. / 系統控制的敵人。
- **A03 Game Service / 遊戲服務**: Background execution of game logic. / 背景執行遊戲邏輯。
- **A04 System / 系統**: Internal application framework, Binder IPC, and Native Engine. / 內部應用程式框架、Binder IPC 與 Native 引擎。

---

## Use Case List / 使用案例列表

### [Menu & Navigation / 選單與導航]

#### [UC-MENU-01] 應用程式啟動與閃屏 (Splash Screen)
- **Scope / 範圍**: Game Application / 遊戲應用程式
- **Level / 層級**: Primary Task / 主要任務
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: App is installed on device. / App 已安裝於裝置上。
- **Trigger / 觸發條件**: Player taps app icon to launch. / 玩家點擊 App 圖示啟動。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. 系統啟動 App 並顯示 Splash Screen] --> B[2. 背景初始化設定與本地儲存]
      B --> C[3. 短暫延遲後自動導航至 Menu List]
  ```

---

#### [UC-MENU-02] 瀏覽選單列表 (Menu List)
- **Scope / 範圍**: Game Application / 遊戲應用程式
- **Level / 層級**: Primary Task / 主要任務
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: Splash screen initialization completed (`UC-MENU-01`). / 閃屏初始化完成。
- **Trigger / 觸發條件**: App enters main menu. / App 進入主選單。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. 系統顯示 Menu List 選單] --> B{2. 玩家選擇選項}
      B -->|Start Game| C[觸發 UC-GAME-01: 開始遊戲]
      B -->|Setting| D[觸發 UC-MENU-03: 設定畫面]
      B -->|Leaderboard| E[觸發 UC-MENU-04: 排行榜畫面]
  ```

---

#### [UC-MENU-03] 遊戲設定 (Setting)
- **Scope / 範圍**: Game Application / 遊戲應用程式
- **Level / 層級**: Primary Task / 主要任務
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: Player is on the Menu List (`UC-MENU-02`). / 玩家位於選單列表。
- **Trigger / 觸發條件**: Player clicks "Setting". / 玩家點擊「Setting」。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. UI 開啟設定畫面: 音量、畫質、靈敏度] --> B[2. 玩家修改偏好設定]
      B --> C[3. 系統將設定儲存至本地 SharedPreferences]
      C --> D[4. 玩家點擊返回按鈕返回 Menu List]
  ```

---

#### [UC-MENU-04] 查看排行榜 (Leaderboard)
- **Scope / 範圍**: Game Application / 遊戲應用程式
- **Level / 層級**: Primary Task / 主要任務
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: Player is on the Menu List (`UC-MENU-02`). / 玩家位於選單列表。
- **Trigger / 觸發條件**: Player clicks "Leaderboard". / 玩家點擊「Leaderboard」。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. UI 開啟排行榜畫面] --> B[2. 系統查詢本地 Room Database 取得最高分記錄]
      B --> C[3. UI 顯示格式化的分數列表]
      C --> D[4. 玩家點擊返回按鈕返回 Menu List]
  ```

---

### [Core Gameplay / 核心遊玩]

#### [UC-GAME-01] 啟動遊戲 (Start Game)
- **Scope / 範圍**: Game Application / 遊戲應用程式
- **Level / 層級**: Primary Task / 主要任務
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: Player selected "Start Game" from Menu List (`UC-MENU-02`). / 玩家從選單列表選擇「Start Game」。
- **Trigger / 觸發條件**: Player clicks "Start Game". / 玩家點擊「Start Game」。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. 玩家在選單點擊 Start] --> B[2. UI 觸發 ViewModel 並實例化 GameService]
      B --> C[3. GameService 透過 JNI 初始化 Native 遊戲引擎]
      C --> D[4. Native 引擎載入競技場地圖與初始實體]
      D --> E[5. 系統將 UI 切換至遊戲畫面並啟動遊戲迴圈]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[3a. 引擎初始化失敗] --> B[系統記錄錯誤並顯示初始化失敗對話框]
  ```
- **Postconditions / 後置條件**: The game arena is established, and player can control character. / 競技場已建立，玩家可以控制角色。

---

#### [UC-GAME-02] 控制角色移動 (Control Character Movement)
- **Scope / 範圍**: Game Application (Mobile Touch UI)
- **Level / 層級**: Subfunction / 子功能
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: UC-GAME-01 completed; active game session. / UC-GAME-01 已完成；遊戲行程進行中。
- **Trigger / 觸發條件**: Player touches virtual directional buttons (Up, Down, Left, Right). / 玩家按住虛擬方向鍵。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. 玩家按住虛擬方向鍵 上下左右] --> B[2. UI 擷取輸入並傳送 MoveCommand 至 ViewModel]
      B --> C[3. ViewModel 傳送指令至 GameService 透過 Binder IPC]
      C --> D[4. GameService 透過 JNI 呼叫 native movePlayer]
      D --> E[5. Native 引擎更新玩家物理座標與速度]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[1a. 玩家放開方向鍵] --> B[UI 傳送零速度向量，停止移動]
  ```
- **Postconditions / 後置條件**: Character position updated successfully. / 角色位置成功更新。

---

#### [UC-GAME-03] 普通攻擊 (Normal Attack)
- **Scope / 範圍**: Game Application / 遊戲應用程式
- **Level / 層級**: Primary Task / 主要任務
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: Game is active and player is alive. / 遊戲進行中且玩家存活。
- **Trigger / 觸發條件**: Player presses "Attack" touch button. / 玩家按下「Attack」按鈕。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. 玩家按下 UI 上的攻擊按鈕] --> B[2. Service 接收攻擊事件]
      B --> C[3. Service 透過 JNI 呼叫 native attack]
      C --> D[4. Native 引擎計算判定框並呼叫 UC-GAME-06 碰撞檢測]
      D --> E[5. 若判定擊中，透過 UC-GAME-07 計算並套用傷害]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[3a. 攻擊冷卻中] --> B[系統忽略輸入或顯示冷卻指示器]
  ```
- **Postconditions / 後置條件**: Attack action performed and damage processed. / 執行攻擊並完成傷害處理。

---

#### [UC-GAME-04] 釋放技能 (Cast Skill)
- **Scope / 範圍**: Game Application / 遊戲應用程式
- **Level / 層級**: Primary Task / 主要任務
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: Skill unlocked and cooldown elapsed. / 技能已解鎖且冷卻完畢。
- **Trigger / 觸發條件**: Player taps skill touch button. / 玩家點擊技能按鈕。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. 玩家點擊技能按鈕] --> B[2. 系統檢查冷卻與資源消耗]
      B --> C[3. 若符合條件，JNI 在 Native 引擎中執行技能邏輯]
      C --> D[4. Native 引擎更新動畫並計算傷害]
      D --> E[5. UI 更新冷卻遮罩]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[2a. 技能冷卻中或資源不足] --> B[施法失敗並提供視覺警告]
  ```
- **Postconditions / 後置條件**: Skill executed and targets take effects. / 技能施放成功並生效。

---

#### [UC-GAME-05] AI 自動追擊 (AI Auto Chase)
- **Scope / 範圍**: Game Engine / 遊戲引擎
- **Level / 層級**: Subfunction / 子功能
- **Primary Actor / 主要參與者**: A02 AI Enemy / AI 敵人
- **Preconditions / 前置條件**: AI Enemy spawned and active. / 敵人已生成並活躍。
- **Trigger / 觸發條件**: Periodic AI tick timer in engine. / 引擎定期 AI Tick 觸發。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. C++ 引擎觸發 AI Tick] --> B[2. AI 掃描競技場搜尋最近玩家位置]
      B --> C[3. Native 引擎呼叫 UC-JNI-02 進行尋路計算]
      C --> D[4. AI 沿著路徑朝玩家方向執行移動]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[2a. 玩家超出仇恨範圍] --> B[AI 進入待機或巡邏狀態]
  ```
- **Postconditions / 後置條件**: AI position updated closer to player. / AI 位置更新。

---

#### [UC-GAME-06] 碰撞檢測 (Collision Detection)
- **Scope / 範圍**: Native C++ Game Engine / Native C++ 遊戲引擎
- **Level / 層級**: Subfunction / 子功能
- **Primary Actor / 主要參與者**: A04 System / 系統
- **Preconditions / 前置條件**: Collidable entities exist in physics world. / 物理世界存在可碰撞實體。
- **Trigger / 觸發條件**: Entity movement or attack. / 實體移動或攻擊發生。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. Native 呼叫 UC-JNI-01 checkCollision] --> B[2. 物理引擎計算邊界框/圓形重疊]
      B --> C[3. 引擎回傳 Hit 或 Miss 結果]
  ```
- **Postconditions / 後置條件**: Collision status returned. / 回傳碰撞狀態。

---

#### [UC-GAME-07] 計算傷害 (Calculate Damage)
- **Scope / 範圍**: Game Engine / 遊戲引擎
- **Level / 層級**: Subfunction / 子功能
- **Primary Actor / 主要參與者**: A04 System / 系統
- **Preconditions / 前置條件**: UC-GAME-06 returns **Hit**. / UC-GAME-06 回傳 Hit。
- **Trigger / 觸發條件**: Successful hit confirmation. / 確認擊中。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. Native 引擎計算傷害 damage = max 1, attack - defense] --> B[2. 目標 HP 扣除計算出的傷害值]
      B --> C[3. 系統更新實體數值 HP, EXP, 擊殺數]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[2a. 目標 HP 歸零] --> B[觸發死亡事件，若為敵人則啟動 UC-GAME-08]
  ```
- **Postconditions / 後置條件**: HP and stats updated. / 數值成功更新。

---

#### [UC-GAME-08] 擊敗敵人取得經驗 (Defeat Enemy and Gain EXP)
- **Scope / 範圍**: Game Application / 遊戲應用程式
- **Level / 層級**: Primary Task / 主要任務
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: Enemy HP reaches 0. / 敵人 HP 歸零。
- **Trigger / 觸發條件**: Enemy destroyed event. / 敵人銷毀事件。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. Native 引擎偵測敵人死亡] --> B[2. Game Service 發放 EXP 並增加擊殺數]
      B --> C[3. 系統檢查是否超過升級門檻]
      C --> D[4. 若達到門檻，觸發升級並啟動 UC-GAME-09]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[3a. 未達升級門檻] --> B[經驗條更新但不升級]
  ```
- **Postconditions / 後置條件**: Enemy removed, stats updated. / 移除敵人並更新數值。

---

#### [UC-GAME-09] 選擇升級技能 (Select Upgrade Skill)
- **Scope / 範圍**: Game Application & UI / 遊戲應用程式與 UI
- **Level / 層級**: Primary Task / 主要任務
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: Player levels up (UC-GAME-08). / 玩家升級。
- **Trigger / 觸發條件**: Level-up UI popup. / 升級彈跳視窗。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. 遊戲暫停，彈出升級選擇對話框] --> B[2. UI 顯示選項: 攻擊+20%, 火球, 冰箭, 護盾]
      B --> C[3. 玩家選擇其中一項]
      C --> D[4. 系統套用增益與技能至玩家]
      D --> E[5. 遊戲恢復正常執行]
  ```
- **Postconditions / 後置條件**: Player gains upgrade. / 玩家獲得升級。

---

#### [UC-GAME-10] 產生敵人 (Spawn Enemy)
- **Scope / 範圍**: Game Engine / 遊戲引擎
- **Level / 層級**: Subfunction / 子功能
- **Primary Actor / 主要參與者**: A03 Game Service / 遊戲服務
- **Preconditions / 前置條件**: Game session active. / 遊戲行程進行中。
- **Trigger / 觸發條件**: Spawner timer ticks every 10s. / 每 10 秒計時器觸發。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. 計時器達到 10 秒間隔] --> B[2. 引擎呼叫 spawnEnemy]
      B --> C[3. Native 引擎計算競技場邊緣安全座標]
      C --> D[4. 實例化新 AI 敵人並加入物理世界]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[3a. 達到敵人數量上限] --> B[略過生成直到活躍敵人減少]
  ```
- **Postconditions / 後置條件**: New enemy appears. / 新敵人出現。

---

#### [UC-GAME-11] 暫停遊戲 (Pause Game)
- **Scope / 範圍**: Game Application / 遊戲應用程式
- **Level / 層級**: Primary Task / 主要任務
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: Game session running. / 遊戲進行中。
- **Trigger / 觸發條件**: Player presses Home key or switches away. / 玩家按下 Home 鍵。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. Activity 收到 onPause] --> B[2. UI 暫停渲染與互動]
      B --> C[3. 背景 GameService 保持運作維持狀態]
      C --> D[4. 玩家返回時 onResume 恢復 UI 與連接]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[3a. 記憶體不足回收行程] --> B[遊戲狀態儲存至 Repository 以供恢復]
  ```
- **Postconditions / 後置條件**: Game safely paused/resumed. / 遊戲安全暫停與恢復。

---

#### [UC-GAME-12] 遊戲結束與離開 (Game Over & Exit)
- **Scope / 範圍**: Game Application / 遊戲應用程式
- **Level / 層級**: Primary Task / 主要任務
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: Game session active. / 遊戲行程進行中。
- **Trigger / 觸發條件**: 
  1. Player HP reaches 0 (Path A). / 玩家 HP 歸零。
  2. Player presses Back button or exit action (Path B). / 玩家按下 Back 按鈕或離開動作。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A{觸發條件} -->|Path A: HP = 0 或 Path B: 確認離開| B1[1. 自動中止遊戲與 Native 引擎]
      B1 --> B2[2. 將分數持久化至 Room DB 排行榜]
      B2 --> B3[3. 自動跳轉至結算畫面 ResultFragment]
      
      A -->|取消離開| C1[關閉確認對話框，遊戲恢復]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[Path A Alt: 玩家點擊 Restart] --> B[觸發 UC-GAME-01 重置競技場]
      C[Path B Alt: 點擊對話框外部取消] --> D[等同繼續，遊戲恢復]
  ```
- **Postconditions / 後置條件**: Session terminated/paused and score persisted. / 行程終止或暫停，分數已持久化。

---

### [IPC / 行程間通訊]

#### [UC-IPC-01] Activity 透過 IPC 取得遊戲狀態 (Get Game State via IPC)
- **Scope / 範圍**: Android IPC / System / Android IPC / 系統
- **Level / 層級**: Subfunction / 子功能
- **Primary Actor / 主要參與者**: A01 Player / 玩家 (via UI Process)
- **Preconditions / 前置條件**: UI and Game Processes running separately. / 雙行程獨立執行。
- **Trigger / 觸發條件**: UI periodic polling. / UI 定期輪詢。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. UI Process 透過 Binder 呼叫 IGameService.aidl] --> B[2. Binder 跨行程傳遞請求至 Game Process]
      B --> C[3. Game Process 執行 getGameState 打包狀態]
      C --> D[4. Binder 將 GameState 物件回傳至 UI Process]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[2a. Binder 連線中斷] --> B[嘗試自動重新連線或顯示錯誤]
  ```
- **Postconditions / 後置條件**: UI renders remote game state. / UI 渲染遠端狀態。

---

#### [UC-IPC-02] Activity 發送控制指令 (Send Control Commands from Activity)
- **Scope / 範圍**: Android IPC / System / Android IPC / 系統
- **Level / 層級**: Subfunction / 子功能
- **Primary Actor / 主要參與者**: A01 Player / 玩家
- **Preconditions / 前置條件**: IPC connection established. / IPC 已連線。
- **Trigger / 觸發條件**: Player taps virtual buttons. / 玩家點擊虛擬按鈕。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. UI Process 透過 Binder IPC 呼叫 movePlayer] --> B[2. IPC 機制序列化參數傳遞至 Game Process 服務]
      B --> C[3. Game Service 接收座標並轉發至 Native Engine]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[1a. 交易失敗] --> B[透過重新繫結處理 DeadObjectException]
  ```
- **Postconditions / 後置條件**: Command received by game service. / 服務成功收到指令。

---

### [JNI / Native 介面]

#### [UC-JNI-01] JNI 計算碰撞 (JNI Collision Calculation)
- **Scope / 範圍**: Native C++ Engine / Native C++ 引擎
- **Level / 層級**: Subfunction / 子功能
- **Primary Actor / 主要參與者**: A04 System / 系統
- **Preconditions / 前置條件**: Native library loaded. / Native 庫已載入。
- **Trigger / 觸發條件**: Java layer requests collision check. / Java 層請求碰撞檢查。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. Java 層呼叫 native checkCollision] --> B[2. JNI 橋接器轉換參數至 C++]
      B --> C[3. C++ 物理引擎執行碰撞計算並回傳布林值]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[1a. 無效實體 ID] --> B[回傳 false 並記錄警告]
  ```
- **Postconditions / 後置條件**: Collision result provided. / 提供碰撞結果。

---

#### [UC-JNI-02] JNI 計算 AI 路徑 (JNI AI Pathfinding)
- **Scope / 範圍**: Native C++ Engine / Native C++ 引擎
- **Level / 層級**: Subfunction / 子功能
- **Primary Actor / 主要參與者**: A02 AI Enemy / 系統
- **Preconditions / 前置條件**: Map data initialized in native engine. / 地圖已初始化。
- **Trigger / 觸發條件**: AI pathfinding requested during `UC-GAME-05`. / 請求尋路。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. Java 層呼叫 native pathFind] --> B[2. JNI 轉換座標結構]
      B --> C[3. C++ A* / JPS 演算法計算最佳路徑]
      C --> D[4. 建構 Point 陣列透過 JNI 回傳至 Java 層]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[3a. 找不到路徑] --> B[回傳空陣列或直線備用]
  ```
- **Postconditions / 後置條件**: Waypoints returned. / 回傳導航點。

---

#### [UC-JNI-03] JNI 物理更新 (JNI Physics Update)
- **Scope / 範圍**: Native C++ Engine / Native C++ 引擎
- **Level / 層級**: Subfunction / 子功能
- **Primary Actor / 主要參與者**: A04 System / 系統
- **Preconditions / 層級**: Game loop running in `GameService`. / 遊戲迴圈執行中。
- **Trigger / 觸發條件**: Every frame clock tick (60 FPS). / 每個畫面時脈 (60 FPS)。
- **Normal Flow / 正常流程**:
  ```mermaid
  flowchart TD
      A[1. GameService 每 frame 呼叫 native updateWorld deltaTime] --> B[2. JNI 傳遞 deltaTime 至物理引擎]
      B --> C[3. C++ 物理引擎進行模擬疊代更新速度與碰撞]
  ```
- **Alternative Flows / 替代流程**:
  ```mermaid
  flowchart TD
      A[1a. deltaTime 異常過大] --> B[限制在最大閾值以防物理穿透]
  ```
- **Postconditions / 後置條件**: World physics advanced. / 世界物理推進。
