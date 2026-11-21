# Arena Mini Fight - Test Specification & Traceability Matrix

## Revision History / 版本歷史
| Date / 日期 | Version / 版本 | Description / 說明 | Author / 作者 |
| :--- | :--- | :--- | :--- |
| 2025-02-21 | 1.0.0 | Initial Test Specification and Traceability Matrix creation (Unit Tests, Integration Tests, Use Case & SRS Traceability) / 建立初始測試規格書與需求追溯矩陣（單元測試、整合測試、Use Case 與 SRS 追溯） | Developer |

---

## 1. Introduction / 簡介
This document defines the testing strategy, test cases, and requirement traceability for **Arena Mini Fight**. It covers unit testing for Domain Use Cases and Presentation ViewModels, integration testing for Room Database and AIDL IPC, and end-to-end traceability against the Use Case Specification (`use_case_spec.md`) and System Requirement Specification (`system_requirement_spec.md`).

本文件定義 **Arena Mini Fight** 的測試策略、測試案例與需求對應追溯。涵蓋領域 Use Case 與 Presentation ViewModel 的單元測試、Room DB 與 AIDL IPC 的整合測試，以及對應 Use Case 與 SRS 的需求追溯矩陣。

---

## 2. Test Strategy / 測試策略
1. **Unit Testing (單元測試)**:
   - Framework: JUnit 4 + Mockito + AndroidX Core Testing.
   - Target: Pure Java Domain Use Cases (business logic verification like damage calculation, score sorting) and ViewModels.
2. **Integration Testing (整合測試)**:
   - Framework: AndroidX Test (AndroidJUnit4) + Room In-Memory Database testing.
   - Target: Room DAO queries (`ScoreDao`), SharedPreferences settings persistence, and AIDL Binder IPC remote service binding.
3. **UI / Instrumentation Testing (UI 與儀器測試)**:
   - Framework: Espresso + Navigation Testing.
   - Target: Navigation flow from Splash $\rightarrow$ Menu $\rightarrow$ Settings/Leaderboard/About, and Virtual D-pad input handling.

---

## 3. Test Cases / 測試案例定義

### 3.1 Unit Test Cases (UT)

| Test ID | Target Component / 測試目標 | Test Scenario / 測試情境 | Expected Result / 預期結果 | Traced Req / 對應需求 |
| :--- | :--- | :--- | :--- | :--- |
| **UT-01** | `GameViewModel` | Trigger `onExitClicked()` | `mExitEvent` LiveData emits `true`. | `UC-GAME-12` |
| **UT-02** | `ScoreEntity` / `ScoreRecord` | Initialize score data model with survival time and kills | Properties are correctly assigned and accessible via getters. | `UC-MENU-04` |
| **UT-03** | `GameRepositoryImpl` | Save score record via repository | Score successfully inserted into database without blocking UI thread. | `REQ-NFR-04` |

### 3.2 Integration Test Cases (IT)

| Test ID | Target Component / 測試目標 | Test Scenario / 測試情ジェ | Expected Result / 預期結果 | Traced Req / 對應需求 |
| :--- | :--- | :--- | :--- | :--- |
| **IT-01** | `ScoreDao` (Room DB) | Insert score entity and query `getTopScores()` | Returns top 10 scores ordered by survival time descending. | `UC-MENU-04`, `REQ-MENU-04` |
| **IT-02** | `GameService` (AIDL IPC) | Bind remote service and invoke `movePlayer()` | Remote service receives coordinates and forwards to native engine without crash. | `UC-IPC-01`, `UC-IPC-02` |
| **IT-03** | `SharedPreferences` | Modify and read audio/graphics preferences | Settings correctly persisted and retrieved. | `UC-MENU-03`, `REQ-MENU-03` |

---

## 4. Requirement Traceability Matrix / 需求追溯矩陣
| Req ID / SRS 需求 | Use Case ID / 對應 Use Case | Test Case ID / 對應測試案例 | Verification Status / 驗證狀態 |
| :--- | :--- | :--- | :--- |
| **REQ-MENU-01** | `UC-MENU-01` | Espresso UI Test (Splash navigation) | Verified |
| **REQ-MENU-02** | `UC-MENU-02` | Espresso UI Test (Menu clicks) | Verified |
| **REQ-MENU-03** | `UC-MENU-03` | `IT-03` (SharedPreferences persistence) | Verified |
| **REQ-MENU-04** | `UC-MENU-04` | `IT-01` (`ScoreDao` Room DB query) | Verified |
| **REQ-MENU-05** | `UC-MENU-05` | Espresso UI Test (About screen display) | Verified |
| **REQ-GAME-01** | `UC-GAME-01` | Integration Test (Engine init & GameService bind) | Verified |
| **REQ-GAME-02** | `UC-GAME-02` | Instrumentation Test (Virtual D-pad input) | Verified |
| **REQ-GAME-03** | `UC-GAME-03` | JNI Combat Test (`nativePlayerAttack`) | Verified |
| **REQ-GAME-04** | `UC-GAME-04` | JNI Skill Test (`nativePlayerAttack` with skill) | Verified |
| **REQ-GAME-11** | `UC-GAME-11` | Lifecycle Test (`onPause` / `onResume`) | Verified |
| **REQ-GAME-12** | `UC-GAME-12` | `UT-01`, `IT-01` (Exit dialog & Score persistence) | Verified |
| **REQ-IPC-01** | `UC-IPC-01` | `IT-02` (Binder IPC connection) | Verified |
| **REQ-IPC-02** | `UC-IPC-02` | `IT-02` (GameState retrieval via AIDL) | Verified |
| **REQ-JNI-01** | `UC-JNI-01` | C++ Engine Unit Test (`checkCollision`) | Verified |
| **REQ-JNI-02** | `UC-JNI-02` | C++ Engine Unit Test (`pathFind`) | Verified |
| **REQ-JNI-03** | `UC-JNI-03` | C++ Engine 60 FPS Loop Test (`updateWorld`) | Verified |
| **REQ-NFR-04** | N/A | `IT-01`, `IT-03` (Background thread persistence) | Verified |
