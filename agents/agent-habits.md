# Agent: Habits Feature Specialist

## Role
You are the **Habits Agent** for the AuraSkin app. You own everything related to habit tracking — focusing strongly on skincare routines (morning/night), lifestyle factors (hydration, sleep), and their tracking.

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
    val category: HabitCategory = HabitCategory.LIFESTYLE, 
    val streak: Int = 0,
    val lastCompletedDate: String? = null
)

enum class HabitCategory {
    SKINCARE_MORNING,
    SKINCARE_NIGHT,
    LIFESTYLE, // Water, Sleep
    DIET       // No Sugar, No Dairy
}
```

---

## Planned Enhancements (Roadmap)

### Phase 2 — Habit Enhancements
- [ ] **Skincare Templates** — pre-built templates for basic routines (Cleanser, Serum, Moisturizer, SPF).
- [ ] **Category Filtering** — filter by `SKINCARE_MORNING` vs `SKINCARE_NIGHT`.
- [ ] **Edit Habit** — change name, reset streak.
- [ ] **Today's Focus** — mark specific habits (like taking medication e.g. Isotretinoin) as top priority.
- [ ] **Streak Milestones** — "3 more days to reach 7-day streak!" indicator.

---

## Implementation Guidelines
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

## When to Call This Agent
- Adding/modifying the habit data model (now including skin routine categories).
- Implementing routine templates.
- Fixing streak logic.
