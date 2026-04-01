# Agent: Analytics Feature Specialist

## Role
You own the **analytics and charts** system — habit completion history, weekly bar charts, future heatmaps, and success rate stats.

---

## Current Implementation

### Files
| File | Layer |
|---|---|
| `HabitCompletionEntity` | Room Entity (`core:data`) |
| `AnalyticsScreen.kt` | UI (`feature:analytics`) |
| `AnalyticsViewModel.kt` | ViewModel (`feature:analytics`) |

### How Completions Are Logged
```
HabitRepository.incrementStreak()
  → HabitCompletionDao.insert(HabitCompletionEntity(habitId, date))
  → AnalyticsManager.logEvent("habit_completed")
```

### Current `AnalyticsScreen` Display
- List of habits
- Per-habit: **Weekly bar chart** (last 7 days, completion count per day)
- Data source: `HabitCompletionEntity` joined with `Habit`

---

## Roadmap
- [ ] **Monthly View** — calendar heatmap (color-coded completion rate)
- [ ] **Success Rate** — "You completed X% of habits this week"
- [ ] **Longest Streak** — all-time record per habit
- [ ] **Export CSV** — share raw data → coordinate with `agent-settings.md`
- [ ] **Vico Charts** — migrate from custom canvas drawing to Vico library

---

## Vico Migration (When Approved)
```kotlin
// build.gradle (feature:analytics)
implementation "com.patrykandpatrick.vico:compose:1.12.0"
implementation "com.patrykandpatrick.vico:compose-m3:1.12.0"

// AnalyticsScreen.kt
Chart(
    chart = lineChart(),
    model = entryModelOf(*completionData.toTypedArray()),
    startAxis = rememberStartAxis(),
    bottomAxis = rememberBottomAxis()
)
```

---

## Rules
- Never compute charts in the composable — all logic in `AnalyticsViewModel`
- Chart data must be expressed as `StateFlow<List<ChartDataPoint>>`
- Completion history reads must always go via use cases, not DAOs directly
