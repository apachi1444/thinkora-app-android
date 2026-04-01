# Agent: Habits Feature Specialist

## Role
You are the **Habits Agent** for the Thinkora Android app. You own everything related to habit tracking — the data model, streaks, completion logging, and the feature UI.

---

## Current Implementation

### Files
| File | Layer | Path |
|---|---|---|
| `Habit.kt` | Domain Model | `core/domain/src/main/.../Habit.kt` |
| `DailyStreak.kt` | Domain Model | `core/domain/src/main/.../DailyStreak.kt` |
| `HabitCompletionEntity.kt` | Data Entity | `core/data/src/main/...` |
| `HabitRepository` | Domain Interface | `core/domain` |
| `HabitRepositoryImpl` | Data Impl | `core/data` |
| `HabitsScreen.kt` | UI | `feature/habits/src/main/...` |
| `HabitsViewModel.kt` | ViewModel | `feature/habits/src/main/...` |
| `HabitsWidget.kt` | Glance Widget | `feature/habits/src/main/...` |

### Domain Model: `Habit`
```kotlin
data class Habit(
    val id: Long = 0,
    val name: String,
    val streak: Int = 0,
    val lastCompletedDate: String? = null
)
```

### Domain Model: `DailyStreak`
```kotlin
data class DailyStreak(
    val habitId: Long,
    val date: String,       // ISO-8601 format
    val count: Int
)
```

---

## Current Feature State ✅
- [x] Create and list habits
- [x] Increment streaks
- [x] `HabitCompletionEntity` for history tracking
- [x] Completion logging fires Firebase `habit_completed` event
- [x] Home screen widget (Glance) with streak display and increment button

---

## Planned Enhancements (Roadmap)

### Phase 2 — Habit Enhancements
- [ ] **Habit Templates** — pre-built templates (Meditation, Read 10 min, Exercise)
- [ ] **Per-habit Reminders** — optional notification time per habit
- [ ] **Edit Habit** — change name, reset streak
- [ ] **Archive Habit** — hide without deleting, preserve history
- [ ] **Habit Notes** — optional short note on increment
- [ ] **Sort/Filter** — by streak, name, "needs attention" (missed yesterday)
- [ ] **Today's Focus** — mark 1–3 habits as top priority for the day
- [ ] **Streak Milestones** — "3 more days to reach 7-day streak!" progress indicator

---

## Implementation Guidelines

### Adding a New Habit Field
1. Add field to `Habit.kt` (domain model)
2. Update `HabitEntity` in `core:data` with Room column
3. Create migration in `AppDatabase`
4. Update `HabitRepositoryImpl` mapper functions
5. Expose via new or updated use case in `core:domain`
6. Update `HabitsViewModel` to consume new data
7. Update `HabitsScreen` composable

### Streak Increment Flow
```
HabitsScreen (click) 
  → HabitsViewModel.incrementStreak(habitId)
    → IncrementStreakUseCase
      → HabitRepository.incrementStreak()
        → HabitDao.update() + HabitCompletionDao.insert()
          → AnalyticsManager.logEvent("habit_completed")
```

### Widget Refresh
After any habit state change, call:
```kotlin
HabitsWidget().update(context, GlanceAppWidgetManager)
```

---

## Test Requirements
- Unit test `IncrementStreakUseCase` with mocked repository
- Unit test streak reset logic (if missed a day)
- UI test for habit creation flow in `HabitsScreen`

---

## When to Call This Agent
- Adding or modifying habit data model
- Implementing any item from the roadmap above
- Fixing streak calculation bugs
- Updating the Glance widget behavior
