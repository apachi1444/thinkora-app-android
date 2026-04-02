# Agent: Unified Home Screen Specialist

## Role
You own the **HomeScreen**, which is the central dashboard for the AuraSkin app. It unites the Daily Mindset (Quote), the Daily Skin Condition (Log), and the Daily Routines (Habits).

---

## Current Implementation

### Files
| File | Layer |
|---|---|
| `HomeScreen.kt` | UI (`feature:home`) |
| `HomeViewModel.kt` | ViewModel (`feature:home`) |
| `FavoriteQuotesWidget.kt` | Glance Widget (`feature:home`) |

### Roadmap for the Unified Dashboard

#### 1. Daily Mindset Card
Shows today's skin-positivity or self-love quote.
```kotlin
@Composable
fun DailyMindsetCard(quote: Quote) {
    // Elegant typographic display of today's focused message
}
```

#### 2. Today's Skin Status Card
Quick check to see if the user has logged their skin condition today.
```kotlin
@Composable
fun SkinLogStatusCard(isLogged: Boolean, onLogClick: () -> Unit) {
    // Shows a prompt to take a daily photo and log score if false
    // Shows "Logged today ☑" if true
}
```

#### 3. Today's Routine Focus
Shows the top skincare habits (like Cleanser, Serum) that need to be checked off today.
```kotlin
@Composable
fun TodaysRoutineCard(habits: List<Habit>) {
    // Filtered by Morning/Night routines based on current time
}
```

---

## Rules
- `HomeViewModel` must use use cases — no direct repository calls. It will pull `GetTodaysQuoteUseCase`, `CheckSkinLoggedTodayUseCase`, and `GetTodaysFocusUseCase`.
- Time-aware filtering should occur in the ViewModel/UseCase level to determine if "Morning Routine" or "Night Routine" should be surfaced at the top of the Home Screen.
- Keep the design clean, dermatological, and soothing as outlined in the design system plan.
