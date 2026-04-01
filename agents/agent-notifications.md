# Agent: Notifications Feature Specialist

## Role
You own all **notification and reminder** logic — daily quote reminders, per-habit reminders, notification scheduling, and the NotificationsScreen UI.

---

## Current Implementation

### Files
| File | Layer |
|---|---|
| `Notification.kt` | Domain Model (`core:domain`) |
| `NotificationRepository.kt` | Interface (`core:domain`) |
| `NotificationRepositoryImpl.kt` | Implementation (`core:data`) |
| `SaveNotificationUseCase.kt` | Use Case (`core:domain`) |
| `NotificationsScreen.kt` | UI (`feature:notifications`) |
| `NotificationsViewModel.kt` | ViewModel (`feature:notifications`) |

### Domain Model: `Notification`
```kotlin
data class Notification(
    val id: Long = 0,
    val title: String,
    val message: String,
    val scheduledTime: String,    // "HH:mm" format
    val type: NotificationType,   // DAILY_QUOTE, HABIT_REMINDER
    val isEnabled: Boolean = true,
    val habitId: Long? = null     // null for DAILY_QUOTE type
)
```

---

## Notification Flow

### Daily Quote Reminder
```
User enables in NotificationsScreen
  → SaveNotificationUseCase(type = DAILY_QUOTE, time = "08:00")
    → WorkManager.enqueueUniquePeriodicWork("daily_quote", KEEP)
      → DailyQuoteWorker: picks a random quote from DB → shows notification
```

### Per-Habit Reminder (Roadmap)
```
User sets reminder time on HabitDetailScreen
  → SaveNotificationUseCase(type = HABIT_REMINDER, habitId = X, time = "20:00")
    → WorkManager.enqueueUniquePeriodicWork("habit_$habitId", UPDATE)
      → HabitReminderWorker: builds notification for that habit
```

---

## WorkManager Setup (`:app` module)
```kotlin
// Called on app start in MainActivity or AppModule
WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    "daily_quote_reminder",
    ExistingPeriodicWorkPolicy.KEEP,
    PeriodicWorkRequestBuilder<DailyQuoteWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(calculateDelay("08:00"), TimeUnit.MILLISECONDS)
        .setConstraints(Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build())
        .build()
)
```

---

## Notification Channel Registration
```kotlin
// In AppModule or App class
val channel = NotificationChannel(
    "thinkora_reminders",
    "Thinkora Reminders",
    NotificationManager.IMPORTANCE_DEFAULT
).apply { description = "Daily habit and quote reminders" }
notificationManager.createNotificationChannel(channel)
```

---

## Roadmap
- [ ] **Per-habit reminder time** — one WorkManager task per habit
- [ ] **Notification history screen** — show recent triggered notifications
- [ ] **Snooze support** — 1-hour snooze via notification action button

---

## Rules
- All scheduling goes through WorkManager — no `AlarmManager` usage
- Each unique WorkManager tag = `"habit_reminder_${habitId}"` or `"daily_quote"`
- Never schedule work from inside a ViewModel — use a use case that delegates to a `NotificationScheduler` abstraction
- `POST_NOTIFICATIONS` permission must be requested on Android 13+ (API 33)
