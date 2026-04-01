# Agent: App Widget Specialist

## Role
You own the **Glance-based home screen widgets** — HabitsWidget and FavoriteQuotesWidget — including their state, update triggers, and pin flow from inside the app.

---

## Current Implementation

### Files
| File | Location |
|---|---|
| `HabitsWidget.kt` | `feature:habits/src/main/...` |
| `FavoriteQuotesWidget.kt` | `feature:home/src/main/...` |

### Widget Architecture (Glance)
```
Widget (GlanceAppWidget)
  → reads data from Room via Repository (via coroutine)
  → renders Glance Composable UI
  → action buttons → ActionCallback → updates data + calls update()
```

---

## HabitsWidget
- Shows list of user habits with current streak
- Tap habit name → opens app to HabitsScreen (deep link)
- **Increment button** → `IncrementStreakActionCallback` → updates Room → refreshes widget
- Refreshes: on `HabitsWidget.update()` call, and every 30 min via `updatePeriodMillis`

### HabitsWidget Update Trigger
```kotlin
// Call this after any habit state change
suspend fun refreshHabitsWidget(context: Context) {
    GlanceAppWidgetManager(context)
        .getGlanceIds(HabitsWidget::class.java)
        .forEach { id -> HabitsWidget().update(context, id) }
}
```

---

## FavoriteQuotesWidget
- Shows a random favorite quote from the user's saved favorites
- Tap → opens app to `quotes/favorites`
- Refresh button → picks a new random favorite
- Refreshes every 6 hours

---

## Pin Widget From App
```kotlin
// Triggered from HabitsScreen "Add Widget" button
val widgetManager = AppWidgetManager.getInstance(context)
val provider = ComponentName(context, HabitsWidgetReceiver::class.java)
if (widgetManager.isRequestPinAppWidgetSupported) {
    widgetManager.requestPinAppWidget(provider, null, null)
}
```

---

## AndroidManifest Registration
```xml
<receiver android:name=".HabitsWidgetReceiver" android:exported="true">
    <intent-filter>
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
    </intent-filter>
    <meta-data
        android:name="android.appwidget.provider"
        android:resource="@xml/habits_widget_info" />
</receiver>
```

---

## Rules
- Widgets must never block the main thread — all DB reads in a coroutine scope
- Widget UI uses only Glance composables — not standard Compose
- Widget data refreshes must be triggered after any relevant data mutation
- Widget `ActionCallback` implementations live in the same module as the widget
