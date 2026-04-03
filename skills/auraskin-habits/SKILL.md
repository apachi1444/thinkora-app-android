---
name: auraskin-habits
description: |
  AuraSkin habit tracking patterns — Habit domain model, HabitCategory enum, streak logic, HabitRepository, use cases, HabitsScreen/HabitsViewModel MVI wiring, skincare routine templates, and Glance widget refresh. Use this skill whenever writing or reviewing anything related to habit CRUD, streak calculation, skincare templates, habit completion, category filtering, or the habits home screen widget. Trigger on phrases like "habit", "streak", "HabitCategory", "SKINCARE_MORNING", "SKINCARE_NIGHT", "routine", "HabitsWidget", "habit template", "habit completion", "IncrementStreakUseCase", or "HabitsScreen".
---

# AuraSkin — Habits Feature

## Core Principle

Habits represent the user's daily skincare and lifestyle routines. They are tracked by streak — consecutive days completed. Every habit belongs to a `HabitCategory`. Streak updates always flow through `IncrementStreakUseCase` and must refresh the Glance home screen widget after every state change.

---

## Domain Model

Lives in `core:domain`. Pure Kotlin — no Android imports.

```kotlin
data class Habit(
    val id: Long = 0,
    val name: String,
    val category: HabitCategory = HabitCategory.LIFESTYLE,
    val streak: Int = 0,
    val lastCompletedDate: String? = null,   // ISO-8601 "YYYY-MM-DD" or null
    val isCompletedToday: Boolean = false,
    val iconRes: String? = null              // drawable name for custom icon
)

enum class HabitCategory {
    SKINCARE_MORNING,
    SKINCARE_NIGHT,
    LIFESTYLE,   // Hydration, Sleep, Exercise
    DIET         // No Sugar, No Dairy, etc.
}
```

---

## Repository Interface (Domain)

```kotlin
interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit): EmptyResult<DataError.Local>
    suspend fun updateHabit(habit: Habit): EmptyResult<DataError.Local>
    suspend fun deleteHabit(id: Long): EmptyResult<DataError.Local>
    suspend fun incrementStreak(habitId: Long): EmptyResult<DataError.Local>
    suspend fun resetStreakIfMissed(habitId: Long): EmptyResult<DataError.Local>
    fun getHabitsByCategory(category: HabitCategory): Flow<List<Habit>>
}
```

---

## Streak Logic

The streak increments only once per day. The rule:

```
if lastCompletedDate == yesterday → streak + 1
if lastCompletedDate == today     → no-op (already done)
if lastCompletedDate is older     → streak resets to 1
```

Implement this in `IncrementStreakUseCase`, not in the ViewModel or DAO:

```kotlin
class IncrementStreakUseCase(private val repo: HabitRepository) {
    suspend operator fun invoke(habitId: Long, habit: Habit): EmptyResult<DataError.Local> {
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()

        if (habit.lastCompletedDate == today) return Result.Success(Unit) // already done

        val newStreak = if (habit.lastCompletedDate == yesterday) habit.streak + 1 else 1
        val updated = habit.copy(
            streak = newStreak,
            lastCompletedDate = today,
            isCompletedToday = true
        )
        return repo.updateHabit(updated)
    }
}
```

---

## Skincare Templates

Pre-built habit templates reduce friction for new users. Define templates in `core:domain`:

```kotlin
object HabitTemplates {
    val morningRoutine = listOf(
        Habit(name = "Cleanser", category = HabitCategory.SKINCARE_MORNING),
        Habit(name = "Toner", category = HabitCategory.SKINCARE_MORNING),
        Habit(name = "Vitamin C Serum", category = HabitCategory.SKINCARE_MORNING),
        Habit(name = "Moisturizer", category = HabitCategory.SKINCARE_MORNING),
        Habit(name = "SPF 50+", category = HabitCategory.SKINCARE_MORNING),
    )
    val nightRoutine = listOf(
        Habit(name = "Makeup Remover", category = HabitCategory.SKINCARE_NIGHT),
        Habit(name = "Cleanser", category = HabitCategory.SKINCARE_NIGHT),
        Habit(name = "Retinol / Serum", category = HabitCategory.SKINCARE_NIGHT),
        Habit(name = "Night Cream", category = HabitCategory.SKINCARE_NIGHT),
    )
    val lifestyle = listOf(
        Habit(name = "Drink 8 glasses of water", category = HabitCategory.LIFESTYLE),
        Habit(name = "Sleep 8 hours", category = HabitCategory.LIFESTYLE),
        Habit(name = "30 min exercise", category = HabitCategory.LIFESTYLE),
    )
}
```

Use `AddHabitTemplateUseCase` to batch-insert a template:

```kotlin
class AddHabitTemplateUseCase(private val repo: HabitRepository) {
    suspend operator fun invoke(habits: List<Habit>) =
        habits.forEach { repo.insertHabit(it) }
}
```

---

## MVI State / Actions / Events

```kotlin
data class HabitsState(
    val habits: List<Habit> = emptyList(),
    val selectedCategory: HabitCategory? = null,  // null = show all
    val isLoading: Boolean = false,
    val error: UiText? = null
)

sealed interface HabitsAction {
    data class OnHabitCompleted(val habit: Habit) : HabitsAction
    data class OnDeleteHabit(val habitId: Long) : HabitsAction
    data class OnCategorySelected(val category: HabitCategory?) : HabitsAction
    data class OnAddHabit(val habit: Habit) : HabitsAction
    data object OnApplyMorningTemplate : HabitsAction
    data object OnApplyNightTemplate : HabitsAction
}

sealed interface HabitsEvent {
    data object HabitSaved : HabitsEvent
    data class ShowError(val message: UiText) : HabitsEvent
}
```

---

## Glance Widget Refresh

After **every** habit state change (complete, add, delete), refresh the Glance widget:

```kotlin
// In HabitsViewModel, after any successful repo call:
private fun refreshWidget() {
    viewModelScope.launch {
        HabitsWidget().updateAll(getApplication())
    }
}
```

Never skip the widget refresh — users rely on it to check today's progress from their home screen.

---

## Streak Flow Diagram

```
HabitsScreen (tap habit checkbox)
  → HabitsViewModel.onAction(OnHabitCompleted)
    → IncrementStreakUseCase(habitId, habit)
      → HabitRepository.updateHabit(updated)
        → HabitDao.update()
    → AnalyticsManager.logEvent("habit_completed", habitName, category)
    → refreshWidget()
```

---

## Firebase Events

| Event Name | When | Parameters |
|---|---|---|
| `habit_completed` | Streak incremented successfully | `habit_name: String`, `category: String`, `streak: Int` |
| `habit_added` | New habit inserted | `category: String` |
| `habit_template_applied` | Template used | `template_name: String` |
| `habit_deleted` | Habit removed | — |

---

## Rules

1. Always use `IncrementStreakUseCase` for streak updates — never update the streak in the DAO directly.
2. `lastCompletedDate` is always ISO-8601 `"YYYY-MM-DD"`.
3. After every habit state change, call `HabitsWidget().updateAll(context)`.
4. Skincare product tracking (Cleanser, Serum, etc.) lives here as Habits — not as a separate product log.
5. Category filtering (`selectedCategory == null` → show all) is ViewModel logic; the DAO always returns all habits.
6. Fire `habit_completed` analytics event on every streak increment.

---

## Checklist: Adding / Modifying Habits

- [ ] Update `Habit` or `HabitCategory` in `core:domain` if model changes
- [ ] Mirror changes in `HabitEntity` + mappers in `core:data`
- [ ] Add/update use case in `core:domain`
- [ ] Update `HabitsState`, `HabitsAction`, `HabitsViewModel` in `feature:habits`
- [ ] Verify widget refresh is called after each state change
- [ ] Fire appropriate Firebase event via `AnalyticsManager`
- [ ] Write a unit test for the affected use case (especially streak logic)
