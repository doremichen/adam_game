# DemoSet Project Coding Rules

This document defines the coding standards and architectural guidelines for the **DemoSet** project to ensure consistency, maintainability, and high quality across all modules.

---

## 1. General Principles
- **Language**: Core logic and system demos should prioritize **Java (JDK 17)** or **Kotlin** as per the specific module's architecture.
- **Documentation**: All comments, Javadocs, and technical notes must be in **English**.
- **Licensing**: Every source file (`.java`, `.kt`, `.xml`) must include the **MIT License header**.

## 2. Naming Conventions
### Java/Kotlin Code
- **Member Variables**: Use the `m` prefix (e.g., `mUserRepository`, `mBinding`).
- **Static/Class Variables**: Use the `s` prefix (e.g., `sInstance`).
- **Constants**: Use `UPPER_SNAKE_CASE` (e.g., `DEFAULT_TIMEOUT`, `ACTION_PROFILE_UNAVAILABLE`).
- **Method/Variable Names**: Use `camelCase`.
- **Class Names**: Use `PascalCase`.

### Resources
- **Layout IDs**: Use `snake_case` with type prefix (e.g., `tv_title`, `btn_submit`).
- **String Keys**: Use descriptive prefixes (e.g., `ps_` for Private Space module).

## 3. Code Cleanliness & Best Practices
- **No Magic Values**: Avoid "magic numbers" or "magic strings". Define them as named constants in a `Constants` class or move them to `strings.xml`.
- **Full Qualified Names (FQN)**: Avoid using full paths in code (e.g., `android.view.View`). Use proper `import` statements instead.
- **View Access**: **Strictly prohibit** the use of `findViewById`. Use **View Binding** or **Data Binding**.
- **Memory Safety**: 
    - Always unregister BroadcastReceivers and Listeners in `onDestroy()` or `onCleared()`.
    - Use `ApplicationContext` for long-lived components.
- **Thread Safety**: 
    - Use `synchronized` blocks, atomic variables, or thread-safe collections (`ConcurrentHashMap`, etc.) where needed.
    - Ensure UI updates always happen on the **Main Thread**.

## 4. Architectural Standards
- **Pattern**: Follow **Clean Architecture + MVVM (Model-View-ViewModel)**.
- **Layers**:
    - **Domain**: Pure logic, Use Cases, and Repository Interfaces.
        - **UseCase Consolidation**: Avoid creating excessive UseCase classes for related features. Encapsulate related actions into an **Enum-based UseCase** pattern and use a **Bridge** module to provide Hilt-injected dependencies.
    - **Data**: Implementation of repositories, API clients, and DB access.
        - **Repository Responsibility**: Repositories must only be responsible to UseCases. They should not be directly accessed by ViewModels or other layers.
    - **Presentation**: UI, Adapters, and ViewModels.
        - **ViewModel Interaction**: ViewModels should only issue commands to UseCases and observe results via LiveData/Flow.
- **Dependency Injection**: 
    - Use **Google Hilt** for all dependency management.
    - Prefer `@Binds` over `@Provides` when mapping implementation to interface to reduce generated code size and improve build performance.
- **Modularization**: Support **Static Modularization** (independent feature libraries) to ensure decoupling.

## 5. Design Patterns
Leverage established patterns to solve common problems:
- **Repository Pattern**: Abstract data sources.
- **Strategy Pattern**: Version-aware logic (e.g., handling different Android APIs).
- **Observer Pattern**: Reactive UI using LiveData or Flow.
- **Builder Pattern**: For complex object construction with many parameters.
- **Singleton Pattern**: Managed via Hilt.

## 6. Internationalization (i18n)
- All user-facing strings must be defined in `res/values/strings.xml`.
- Support for **English** (default) and **Traditional Chinese** (`values-zh-rTW`) is mandatory for all new features.

---

*Last Updated: 2026-07-30*
