# My Device Info

一個基於 Clean Architecture 與 MVVM 架構開發的 Android 裝置資訊監測工具。本專案旨在展示高品質的 Java 開發規範與現代 Android 開發技術。

## 🏗 核心架構 (Architecture)

本專案嚴格遵循 **Clean Architecture** 與 **MVVM** 模式，確保代碼的高可測試性與低耦合度：

-   **Domain Layer**: 包含核心業務實體 (Entities)、狀態列舉 (Enums) 與 Repository 介面。完全獨立，不依賴任何 Android 框架。
-   **Application Layer**: 使用 **Enum-based UseCase** 模式封裝業務邏輯，將操作原子化。
-   **Infrastructure Layer**: 負責技術實作。包含透過 **AIDL (IPC)** 與後台服務通訊的 Repository 實作，以及硬體感測器採集邏輯。
-   **UI Layer**: 採用 **MVVM** 模式。使用 **Data Binding** 實現 View 與 ViewModel 的雙向同步，並利用 **SingleLiveEvent** 處理一次性 UI 事件。

## 🛠 技術棧 (Tech Stack)

### 核心開發
-   **Language**: Java (JDK 17)
-   **Dependency Injection**: [Google Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
-   **Jetpack Components**:
    -   **Navigation**: 處理 Fragment 間的導航與返回棧管理。
    -   **LiveData & ViewModel**: 響應式狀態管理。
    -   **Data Binding**: 消除 UI 樣板代碼。
-   **UI Framework**: Material Design 3 (M3)

### 系統與硬體整合
-   **IPC (Inter-Process Communication)**: 使用 **AIDL** 實現前端 UI 與後台監測服務 (`InfoService`) 的通訊。
-   **Connectivity**: 透過 `ConnectivityManager` (NetworkCapabilities) 監測現代網路狀態。
-   **File I/O**: 使用 **MediaStore API** 安全地將報告匯出至系統下載資料夾。
-   **Device Metrics**: 整合 `BatteryManager`, `TelephonyManager`, `WifiInfo`, `SensorManager` 等系統服務。

## 💎 開發標準 (Quality Standards)

本專案遵循嚴格的 [CODING_RULES.md](CODING_RULES.md)：

-   **Zero Warnings Policy**: 所有建置必須符合無錯誤、無警告標準。
-   **Builder Pattern**: 針對 4 個參數以上的類別強制使用 Builder 模式，提升可讀性。
-   **Mandatory Javadoc**: 除了 Setter/Getter 外的所有方法皆須具備完整英文註解。
-   **Magic Value Elimination**: 所有的狀態字串與魔術數字皆封裝於 `Constants` 或領域層 Enum 中。
-   **Type-Safe State**: 使用 Enum-based Mapping 取代傳統的 `switch-case` 或 `if-else` 判斷。

## 📊 設計圖檔 (Design Diagrams)

詳細的設計細節可以參考根目錄下的 `.puml` 檔案：
-   [全域架構圖](class_design.puml)
-   [領域層設計](class_design_domain.puml)
-   [UI 層模組設計](class_design_ui.puml)
-   [導航機制筆記](note.md)

---
*Created by Adam Chen*
