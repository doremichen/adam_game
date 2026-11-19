# General Class Design & Visualization Rules

This document defines the universal standards for class design and architectural visualization (PlantUML) for Android projects to ensure consistency, maintainability, and clear communication across teams.

---

## 1. Architectural Integrity (Clean Architecture + MVVM)

All class designs must strictly adhere to the following layers and dependency rules (dependencies flow inwards):

### Domain Layer (Core Business Logic)
- **`[RULE-ARCH-01]` Entities**: Pure data models and logic representing business objects. Must have zero dependencies on Android frameworks or other layers.
- **`[RULE-ARCH-02]` Repository Interfaces**: Define contracts for data operations, isolating business logic from data sources.

### Application Layer (Business Processes)
- **`[RULE-ARCH-03]` UseCases**: Orchestrate specific business actions. Should be atomic and follow a standardized execution pattern.
- **`[RULE-ARCH-04]` Domain Services**: Complex logic that involves multiple entities but doesn't belong in the UI or Infrastructure.

### Infrastructure Layer (Technical Implementation)
- **`[RULE-ARCH-05]` Repository Implementations**: Concrete logic for data persistence (Room, SharedPreferences, Retrofit).
- **`[RULE-ARCH-06]` External Framework Wrappers**: Bridges to device-specific APIs (Sensors, Bluetooth, etc.).

### UI (Presentation) Layer
- **`[RULE-ARCH-07]` Views (Activities/Fragments/Composables)**: Dumb components responsible for rendering and user interaction.
- **`[RULE-ARCH-08]` ViewModels**: Maintain UI state and bridge the View with the Application/Domain layer. Must interact **only** with UseCases or Repository Interfaces.

---

## 2. Visualization Standards (PlantUML)

When documenting architecture via `class_design.puml`, follow these formatting rules:

- **Diagram Configuration**:
    - `[RULE-VIZ-01]` Use **Orthogonal Lines** for a clean grid-like structure.
    - `[RULE-VIZ-02]` Set package style to `rectangle`.
    ```puml
    skinparam linetype ortho
    skinparam packageStyle rectangle
    ```
- **Class Content**:
    - `[RULE-VIZ-03]` **Methods**: Always list key **Public Methods (`+`)**. Omit obvious getters/setters unless they carry logic.
    - `[RULE-VIZ-04]` **Members**: List **Private Members (`-`)** only if they clarify critical state or internal dependencies.
- **Relationships**:
    - `[RULE-VIZ-05]` **Explicit Descriptions**: Every relationship line MUST have a description in quotes (e.g., `--> : "Observes state"`).
    - `[RULE-VIZ-06]` **Interface Realization**: Use `..|>` for "implements".
    - `[RULE-VIZ-07]` **Dependency/Association**: Use `-->` for "uses" or "depends on".

---

## 3. Core Design Patterns

### `[RULE-PAT-01]` Highly Cohesive Enum-based UseCase
To maximize cohesion and eliminate `switch-case` clutter, encapsulate execution logic directly within the `Action` Enum using a **generic** abstract method:
```java
public final class ExampleUseCase {
    private final IRepository mRepository;

    public enum Action {
        ACTION_A {
            @Override <T> T handle(IRepository repo, Object data) { 
                return (T) repo.doA(data); 
            }
        };
        // Using <T> here eliminates the need for casting at the execute level
        abstract <T> T handle(IRepository repo, Object data);
    }
    
    public <T> T execute(Action action, Object data) {
        return action.handle(mRepository, data);
    }
}
```

### `[RULE-PAT-02]` Dependency Inversion
- High-level modules (ViewModels/UseCases) must not depend on low-level implementation details (Database/Net).
- Always depend on **Interfaces** defined in the Domain layer.

### `[RULE-PAT-03]` Reactive State Management
- Use `LiveData` or `StateFlow` for persistent state.
- Use `SingleLiveEvent` (or an equivalent Event wrapper) for one-time UI events (Navigation, SnackBar).

---

## 4. Naming & Package Standards

- `[RULE-NAM-01]` **Packages**: Must mirror the architectural layers (e.g., `[root].domain`, `[root].ui.[feature]`).
- **Naming**:
    - `[RULE-NAM-02]` Interfaces: Prefix with `I` (e.g., `IRepository`).
    - `[RULE-NAM-03]` Implementations: Suffix with `Impl` (e.g., `RepositoryImpl`).
    - `[RULE-NAM-04]` ViewModels: Suffix with `ViewModel`.
    - `[RULE-NAM-05]` Activities: Suffix with `Activity`.

---
*Last Updated: 2026-08-27*
