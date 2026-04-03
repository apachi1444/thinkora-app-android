---
name: auraskin-home
description: |
  AuraSkin Home workflow and dashboard patterns — unified dashboard, today's focus, recent logs, streak summaries, and HomeScreen MVI wiring. Use this skill whenever writing or reviewing anything related to the main Home dashboard, gathering data from habits and skin logs to display on the main screen. Trigger on phrases like "home screen", "dashboard", "HomeScreen", "HomeViewModel", "today's focus", or "recent logs".
---

# AuraSkin — Home / Unified Dashboard

## Core Principle
The Home module (`feature:home`) is a unified read-only dashboard that aggregates data from **both** `Habits` and `Skin Tracking`. The `HomeViewModel` does not own its own core database logic; instead, it observes the data flows from `SkinLogRepository` and `HabitRepository` to display "Today's Focus" and weekly summaries.

---

## State and Data Sourcing

The `HomeState` aggregates information:

```kotlin
data class HomeState(
    val todayHabits: List<Habit> = emptyList(),
    val todaySkinLog: SkinLog? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null
)
```

The ViewModel collects states using combined flows:

```kotlin
class HomeViewModel(
    private val habitRepository: HabitRepository,
    private val skinLogRepository: SkinLogRepository
) : ViewModel() {
    
    val state = combine(
        habitRepository.getAllHabits(),
        skinLogRepository.getAllLogs() // or purely Today's log
    ) { habits, skinLogs ->
        // Transform the data for the UI
        HomeState(
            todayHabits = habits,
            todaySkinLog = skinLogs.find { it.date == LocalDate.now().toString() }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeState(isLoading = true)
    )
}
```

---

## Rules

1. **Read-Only Dashboard**: The `feature:home` module shouldn't perform complex business logic like calculating streaks or handling photo uploads. It observes state and dispatches navigation events. 
2. **Navigation over Logic**: If a user clicks a habit to complete it on the Home screen, the Action might dispatch a quick UseCase update, but generally, complex editing pushes the user to `feature:habits`.
3. **Cross-Feature Imports**: `feature:home` is one of the few feature modules allowed to depend on `core:domain` entities from *both* Skin and Habits contexts.

---

## Checklist: Home Dashboard Features

- [ ] Combine data flows smoothly to avoid UI flickering.
- [ ] Ensure empty states are gracefully handled (e.g. "You haven't logged your skin today").
- [ ] Keep state transformation off the main thread where possible.
