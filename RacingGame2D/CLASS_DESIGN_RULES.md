# General Class Design & Visualization Rules

This document defines the universal standards for class design and architectural visualization (PlantUML) for Android projects to ensure consistency, maintainability, and clear communication across teams.

---

## 1. Architectural Integrity (Clean Architecture + MVVM)

All class designs must strictly adhere to the following layers and dependency rules (dependencies flow inwards):

### Domain Layer (Core Business Logic)
- **Entities**: Pure data models and logic representing business objects. Must have zero dependencies on Android frameworks or other layers.
- **Repository Interfaces**: Define contracts for data operations, isolating business logic from data sources.

### Application Layer (Business Processes)
- **UseCases**: Orchestrate specific business actions. Should be atomic and follow a standardized execution pattern.
- **Domain Services**: Complex logic that involves multiple entities but doesn't belong in the UI or Infrastructure.

### Infrastructure Layer (Technical Implementation)
- **Repository Implementations**: Concrete logic for data persistence (Room, SharedPreferences, Retrofit).
- **External Framework Wrappers**: Bridges to device-specific APIs (Sensors, Bluetooth, etc.).

### UI (Presentation) Layer
- **Views (Activities/Fragments/Composables)**: Dumb components responsible for rendering and user interaction.
- **ViewModels**: Maintain UI state and bridge the View with the Application/Domain layer. Must interact **only** with UseCases or Repository Interfaces.

---

## 2. Visualization Standards (PlantUML)

When documenting architecture via `class_design.puml`, follow these formatting rules:

- **Diagram Configuration**:
    - Use **Orthogonal Lines** for a clean grid-like structure.
    - Set package style to `rectangle`.
    ```puml
    skinparam linetype ortho
    skinparam packageStyle rectangle
    ```
- **Class Content**:
    - **Methods**: Always list key **Public Methods (`+`)**. Omit obvious getters/setters unless they carry logic.
    - **Members**: List **Private Members (`-`)** only if they clarify critical state or internal dependencies.
- **Relationships**:
    - **Explicit Descriptions**: Every relationship line MUST have a description in quotes (e.g., `--> : "Observes state"`).
    - **Interface Realization**: Use `..|>` for "implements".
    - **Dependency/Association**: Use `-->` for "uses" or "depends on".

---

## 3. Core Design Patterns

### Highly Cohesive Enum-based UseCase
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

### Dependency Inversion
- High-level modules (ViewModels/UseCases) must not depend on low-level implementation details (Database/Net).
- Always depend on **Interfaces** defined in the Domain layer.

### Reactive State Management
- Use `LiveData` or `StateFlow` for persistent state.
- Use `SingleLiveEvent` (or an equivalent Event wrapper) for one-time UI events (Navigation, SnackBar).

---

## 4. Naming & Package Standards

- **Packages**: Must mirror the architectural layers (e.g., `[root].domain`, `[root].ui.[feature]`).
- **Naming**:
    - Interfaces: Prefix with `I` (e.g., `IRepository`).
    - Implementations: Suffix with `Impl` (e.g., `RepositoryImpl`).
    - ViewModels: Suffix with `ViewModel`.
    - Activities: Suffix with `Activity`.

---
*Last Updated: 2026-08-27*
