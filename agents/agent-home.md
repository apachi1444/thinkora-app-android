# Agent: Home Screen Specialist

## Role
You own the **HomeScreen** layout, daily summary cards, today's focus section, and the FavoriteQuotesWidget.

---

## Current Implementation

### Files
| File | Layer |
|---|---|
| `HomeScreen.kt` | UI (`feature:home`) |
| `HomeViewModel.kt` | ViewModel (`feature:home`) |
| `FavoriteQuotesWidget.kt` | Glance Widget (`feature:home`) |

### Current HomeScreen Sections
1. **Greeting** — "Good morning, {name}!" (time-aware, from DataStore)
2. **Daily Quote card** — today's randomly selected quote
3. **Habit overview** — short list with streak counts
4. **Quick action** — "Log a habit" button

---

## Roadmap for HomeScreen

### Today's Focus Card
```kotlin
// Show top 1–3 habits marked as "focus" for the day
@Composable
fun TodaysFocusCard(habits: List<Habit>) {
    Card {
        habits.take(3).forEach { habit ->
            HabitFocusRow(habit)
        }
    }
}
```

### Weekly Summary Card
```kotlin
// "This week you completed X / Y habits"
@Composable
fun WeeklySummaryCard(completed: Int, total: Int) {
    LinearProgressIndicator(progress = completed.toFloat() / total)
    Text("$completed / $total habits this week")
}
```

### Streak Milestone Banner
```kotlin
// "3 more days to reach your 7-day streak!"
@Composable
fun StreakMilestoneBanner(habit: Habit, nextMilestone: Int) {
    val daysLeft = nextMilestone - habit.streak
    Text("$daysLeft more days to reach $nextMilestone-day streak! 🔥")
}
```

### Empty State
```kotlin
// When user has no habits yet
@Composable
fun EmptyHabitsState(onCreateClick: () -> Unit) {
    Column(horizontalAlignment = CenterHorizontally) {
        Icon(Icons.Outlined.AddTask, contentDescription = null)
        Text("Start building your first habit")
        Button(onClick = onCreateClick) { Text("Create Habit") }
    }
}
```

---

## FavoriteQuotesWidget (Glance)
- Shows the user's favorite quotes as a scrollable list on the Android home screen
- Tapping a quote navigates into the app (deep link to `quotes/favorites`)
- Widget refreshes every 6 hours or when favorites change

---

## Rules
- `HomeViewModel` must use use cases — no direct repository calls
- Time-aware greeting uses `Calendar.getInstance().get(Calendar.HOUR_OF_DAY)` inside ViewModel
- "Today's focus" habits are determined by a `GetTodaysFocusUseCase`
- Widget state is refreshed via `GlanceAppWidgetManager.requestPinAppWidget()`
