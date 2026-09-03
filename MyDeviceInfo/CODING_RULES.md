# Android Project Coding Rules

This document defines the coding standards and architectural guidelines for Android projects to ensure consistency, maintainability, and quality.

---

## 1. General Principles
- **GP-01**: Core logic should prioritize **Java (JDK 17)** or **Kotlin**.
- **GP-02**: All comments, Javadocs, and notes must be in **English**.
- **GP-03**: **Mandatory Javadoc**: All methods, except for simple getters and setters, must include a Javadoc comment explaining the purpose, parameters, and return value.
- **GP-04**: Every source file must include the **MIT License header**.
- **GP-04**: **Indentation**: Use 4 spaces for indentation (no tabs).
- **GP-05**: **Line Length**: Limit lines to 120 characters where possible.
- **GP-06**: **Zero Warnings Policy**: All builds must be strictly free of both errors and warnings. Unused code, deprecated APIs (where alternatives exist), and styling issues should be resolved immediately.

## 2. Naming Conventions
### Java/Kotlin
- **NC-01**: Member variables must use the `m` prefix (e.g., `mBinding`).
- **NC-02**: Static variables must use the `s` prefix (e.g., `sInstance`).
- **NC-03**: Constants must be `UPPER_SNAKE_CASE`.
- **NC-04**: Classes use `PascalCase`, methods and variables use `camelCase`.
### Resources
- **NC-05**: Layout IDs use `snake_case` with type prefix (e.g., `tv_title`).
- **NC-06**: Resource files (drawables, layouts) use `snake_case`. Prefixes are encouraged (e.g., `bg_`, `ic_`, `item_`).

## 3. Code Quality & Clean Code
- **CQ-01**: **No Magic Values**: Strictly prohibit "magic numbers" or strings. Encapsulate all project-wide constants and strings in a dedicated `Constants` class or move user-facing strings to `strings.xml`.
- **CQ-02**: **View Access**: Strictly prohibit `findViewById`. Use **Data Binding** (preferred) or **View Binding**.
- **CQ-03**: **Unified Logging**: Prohibit direct use of `Log.d`. Use `Utils.logDebug(label, message)`.
- **CQ-04**: **SOLID & DRY**: Adhere to SOLID principles and avoid code duplication.
- **CQ-05**: **Method Focus**: Keep methods short and focused on a single responsibility.
- **CQ-06**: **Dead Code**: Strictly prohibit unused imports, variables, or methods. No commented-out code.
- **CQ-07**: **Encapsulation**: All class members must be `private`. Public getters/setters must not include `m` in their names.
- **CQ-08**: **Dependency Injection**: Prefer constructor injection. Fields holding dependencies should be `final`.
- **CQ-09**: **Thread Safety**: Ensure UI updates happen on the Main Thread. Use background threads for I/O. For high-frequency periodic background tasks, prioritize power-saving components like `TaskScheduler` that monitor device state (e.g., screen on/off).
- **CQ-10**: **Imports**: Strictly prohibit wildcard imports (e.g., `import java.util.*`).
- **CQ-11**: **Null Safety**: Use `@Nullable` and `@NonNull` annotations to define nullability contracts.
- **CQ-12**: **Data Binding**: Mandate **Data Binding** for all XML layouts to facilitate MVVM data synchronization.
- **CQ-13**: **UI Notifications**: ViewModels must use **SingleLiveEvent** (or a similar one-time event wrapper) to notify the UI of transient events (e.g., navigation, toasts, dialogs) to avoid multiple triggers on configuration changes.
- **CQ-14**: **State Management**: For components with complex life cycles or state transitions, prioritize using the **State Pattern** (Enum-based) to ensure clear logic and high maintainability.

## 4. Architectural Standards
- **AS-01**: Follow **Clean Architecture + MVVM**.
- **AS-02**: **Layers**:
    - **Domain**: Core entities and pure logic. Absolutely independent.
    - **Application**: Business processes and Use Cases. Encapsulate related actions in **Enum-based UseCases**.
    - **Infrastructure**: Technical implementations (Repositories, DB, SharedPreferences). Repositories only respond to UseCases.
    - **UI / Presentation**: UI, Adapters, and ViewModels. ViewModels only interact with UseCases.
- **AS-03**: Use **Google Hilt** for all dependency management.

## 5. Design Patterns & Testing
- **DP-01**: Leverage **Repository**, **Strategy**, **Observer**, **Builder**, **Singleton**, and **State (Enum-based)** patterns.
- **DP-02**: **Builder Pattern Requirement**: For any class with more than 4 constructor parameters, the **Builder Pattern** must be implemented and used for object creation to ensure readability and maintainability.
- **TS-01**: **Unit Testing**: Business logic in UseCases and ViewModels must have corresponding unit tests.
- **TS-02**: **Immutability**: Favor immutable data structures (e.g., `final` fields, `List` instead of `ArrayList` in APIs).

## 6. Internationalization (i18n)
- **IN-01**: All user-facing strings must be defined in `strings.xml`.
- **IN-02**: Support for **English** (default) and **Traditional Chinese** (`values-zh-rTW`) is mandatory.

---

*Last Updated: 2026-08-27*
