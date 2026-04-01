# Agent: Acne Tracker App — Full Specification & AI Agent Guide

> **Status**: 🟡 Proposed — new standalone Android app
> **Inspiration**: Built with the same architecture patterns as Thinkora (Clean Architecture, MVVM, Hilt, Room, Compose)
> **For AI models**: This file is the single source of truth for any task related to the Acne Tracker app. Read it fully before writing any code.

---

## 📱 What is the Acne Tracker App?

The **Acne Tracker** is a standalone Android application that helps users monitor their skin health journey. Users log daily skin condition photos and notes, track product usage and diet triggers, observe improvement trends over time, and receive personalized skincare tips. The app uses on-device AI (ML Kit / Google AI) to optionally analyze skin condition from photos.

- **App Name**: `ClearSkin` *(working title — subject to change)*
- **Package**: `com.apachi.clearskin`
- **Min SDK**: 26 | **Target SDK**: 34
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material3
- **Architecture**: Clean Architecture + MVVM (same as Thinkora)
- **DI**: Hilt 2.51+
- **Storage**: Room (logs, products, triggers) + DataStore (preferences)
- **Camera**: CameraX + Photo Picker API
- **AI/ML**: ML Kit (on-device) or Gemini API (cloud) for skin analysis
- **Charts**: Vico or MPAndroidChart for trend visualization
- **Notifications**: WorkManager (daily reminders)
- **Firebase**: Analytics + Firestore (optional sync) + Firebase Storage (photos)
- **Async**: Kotlin Coroutines + Flow

---

## 🗂️ Module Map (Proposed)

```
:app                        ← Entry point, DI wiring, Navigation, MainActivity
:core:domain                ← Pure Kotlin: models, repo interfaces, use cases
:core:data                  ← Room, DataStore, repo implementations
:core:designsystem          ← Skin-tone inspired color palette, Typography, Shapes
:core:ui                    ← Shared composables (loading states, photo viewer, etc.)
:feature:onboarding         ← First launch: skin type quiz, goals setup
:feature:dashboard          ← HomeScreen: today's log status, streak, quick stats
:feature:log                ← DailyLogScreen: photo capture, notes, mood, diet
:feature:progress           ← ProgressScreen: trend charts, before/after gallery
:feature:products           ← ProductsScreen: skincare routine tracker
:feature:triggers           ← TriggersScreen: food/lifestyle trigger tracking
:feature:insights           ← InsightsScreen: AI-powered analysis & tips
:feature:calendar           ← CalendarScreen: heatmap of skin condition over time
:feature:settings           ← SettingsScreen: reminders, theme, data export
:feature:notifications      ← WorkManager-based daily reminders
```

---

## 📐 Data Models (Domain Layer — `core:domain`)

### `SkinLog`
```kotlin
data class SkinLog(
    val id: Long = 0,
    val date: String,               // ISO-8601 "YYYY-MM-DD"
    val photoUri: String?,          // local URI or Firebase Storage URL
    val conditionScore: Int,        // 1 (severe) – 5 (clear)
    val notes: String = "",
    val mood: Mood = Mood.NEUTRAL,
    val triggers: List<String> = emptyList(),
    val productsUsed: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
```

### `Mood` (enum)
```kotlin
enum class Mood { GREAT, GOOD, NEUTRAL, BAD, TERRIBLE }
```

### `SkinProduct`
```kotlin
data class SkinProduct(
    val id: Long = 0,
    val name: String,
    val brand: String = "",
    val category: ProductCategory,  // CLEANSER, MOISTURIZER, SERUM, SPF, TREATMENT, OTHER
    val isActive: Boolean = true,
    val startDate: String,
    val notes: String = ""
)
```

### `Trigger`
```kotlin
data class Trigger(
    val id: Long = 0,
    val name: String,              // "Dairy", "Sugar", "Stress", "Poor Sleep"
    val category: TriggerCategory, // DIET, LIFESTYLE, ENVIRONMENT, HORMONAL
    val severity: Int = 1          // 1–3 scale
)
```

### `UserSkinProfile`
```kotlin
data class UserSkinProfile(
    val skinType: SkinType,        // OILY, DRY, COMBINATION, NORMAL, SENSITIVE
    val skinGoal: String,          // "Clear acne", "Reduce redness", etc.
    val notificationsEnabled: Boolean = true,
    val reminderTime: String = "20:00"
)
```

---

## 🔁 Repository Interfaces (Domain Layer)

```kotlin
interface SkinLogRepository {
    fun getAllLogs(): Flow<List<SkinLog>>
    suspend fun getLogByDate(date: String): SkinLog?
    suspend fun insertLog(log: SkinLog): Long
    suspend fun updateLog(log: SkinLog)
    suspend fun deleteLog(id: Long)
    fun getLogsBetween(start: String, end: String): Flow<List<SkinLog>>
}

interface ProductRepository {
    fun getAllProducts(): Flow<List<SkinProduct>>
    suspend fun insertProduct(product: SkinProduct): Long
    suspend fun updateProduct(product: SkinProduct)
    suspend fun deleteProduct(id: Long)
}

interface TriggerRepository {
    fun getAllTriggers(): Flow<List<Trigger>>
    suspend fun insertTrigger(trigger: Trigger): Long
    suspend fun deleteTrigger(id: Long)
}

interface SkinProfileRepository {
    fun getProfile(): Flow<UserSkinProfile?>
    suspend fun saveProfile(profile: UserSkinProfile)
}
```

---

## ✅ Use Cases (Domain Layer — `core:domain`)

| Use Case | Responsibility |
|---|---|
| `LogDailySkinConditionUseCase` | Validate and insert a new `SkinLog` |
| `GetSkinTrendUseCase` | Return last N days of logs for chart rendering |
| `GetBeforeAfterUseCase` | Return first log and most recent log |
| `IdentifyTopTriggersUseCase` | Correlate triggers with bad skin days |
| `GetSkinStreakUseCase` | Count consecutive days logged |
| `AnalyzeSkinWithAIUseCase` | Send photo to ML Kit / Gemini for analysis |
| `SaveProductUseCase` | Add or update a skincare product |
| `GetSkinProfileUseCase` | Fetch `UserSkinProfile` from DataStore |
| `SaveSkinProfileUseCase` | Persist `UserSkinProfile` to DataStore |

---

## 🖥️ Feature Screens

### `feature:onboarding`
- **OnboardingScreen** — multi-step wizard:
  1. Welcome (app intro)
  2. Skin type selection (OILY / DRY / COMBINATION / NORMAL / SENSITIVE)
  3. Goal setup ("What do you want to achieve?")
  4. Reminder time picker
- **OnboardingViewModel** — saves `UserSkinProfile` via use case

### `feature:dashboard`
- **DashboardScreen** — shows:
  - Current streak (days logged in a row)
  - Today's log status (logged / not yet)
  - Weekly sparkline chart
  - Quick "Log Today" FAB
- **DashboardViewModel** — observes `SkinLogRepository.getAllLogs()`

### `feature:log`
- **LogScreen** — daily log entry:
  - Camera/gallery photo picker (CameraX)
  - Condition score slider (1–5 with emoji)
  - Mood picker
  - Trigger multi-select chip group
  - Products used chip group
  - Free-text notes
- **LogViewModel** — writes via `LogDailySkinConditionUseCase`

### `feature:progress`
- **ProgressScreen** — visual history:
  - Line chart: condition score over 30/90 days (Vico)
  - Before/After photo comparison
  - "Worst week" vs "Best week" summary
- **ProgressViewModel** — calls `GetSkinTrendUseCase`

### `feature:triggers`
- **TriggersScreen** — trigger management:
  - Add/remove triggers
  - Correlation view: "Dairy appears on 80% of bad skin days"
- **TriggersViewModel** — calls `IdentifyTopTriggersUseCase`

### `feature:products`
- **ProductsScreen** — routine tracker:
  - List active products by category
  - Add/edit/archive products
  - Timeline: when each product was started
- **ProductsViewModel** — calls `ProductRepository` via use case

### `feature:insights`
- **InsightsScreen** — AI-powered tips:
  - Skin analysis result (from ML Kit or Gemini)
  - Personalized tip of the day (rule-based or AI)
  - "Your top trigger this month" card
- **InsightsViewModel** — calls `AnalyzeSkinWithAIUseCase`

### `feature:calendar`
- **CalendarScreen** — green/red heatmap of skin scores per day
- **CalendarViewModel** — fetches 90-day logs and maps to color grid

### `feature:settings`
- **SettingsScreen** — reminder time, theme, data export (CSV), delete account
- **SettingsViewModel** — manages DataStore preferences

---

## 🤖 AI / ML Integration

### Option A — On-Device (ML Kit)
```kotlin
// In AnalyzeSkinWithAIUseCase
val image = InputImage.fromBitmap(bitmap, 0)
val detector = FaceDetector.getClient()
// Use labeling + custom TFLite model for skin analysis
```

### Option B — Gemini API (Cloud)
```kotlin
// Multimodal prompt to Gemini
val response = generativeModel.generateContent(
    content {
        image(bitmap)
        text("Analyze this skin photo. Rate acne severity 1-5. List visible concerns.")
    }
)
```

**Recommendation**: Start with Option B (Gemini API) for MVP — faster to build, better accuracy. Migrate to on-device ML later for privacy.

---

## 🎨 Design System — `core:designsystem`

This app gets its **own design system** separate from Thinkora.

### Color Palette
```kotlin
// Soothing, dermatology-inspired palette
val ClearSkinPrimary = Color(0xFF7EB8A0)      // Sage green (calm)
val ClearSkinSecondary = Color(0xFFF4A6A0)    // Soft peach (skin)
val ClearSkinTertiary = Color(0xFFB8C8E8)     // Lavender (gentle)
val ClearSkinBackground = Color(0xFFFAF7F5)   // Warm off-white
val ClearSkinSurface = Color(0xFFFFFFFF)
val ClearSkinError = Color(0xFFE57373)
```

### Typography
- Headlines: `Nunito` (Google Fonts — rounded, friendly)
- Body: `Inter` (clean, readable)

### Condition Score Colors
```kotlin
val ScoreColors = mapOf(
    1 to Color(0xFFE57373), // Severe  → deep red
    2 to Color(0xFFFFB74D), // Bad     → orange
    3 to Color(0xFFFFD54F), // Neutral → yellow
    4 to Color(0xFFAED581), // Good    → light green
    5 to Color(0xFF66BB6A)  // Clear   → green
)
```

---

## 🗃️ Room Database Schema

```sql
-- skin_logs table
CREATE TABLE skin_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    date TEXT NOT NULL UNIQUE,
    photo_uri TEXT,
    condition_score INTEGER NOT NULL,
    notes TEXT DEFAULT '',
    mood TEXT NOT NULL DEFAULT 'NEUTRAL',
    triggers TEXT NOT NULL DEFAULT '[]',  -- JSON array
    products_used TEXT NOT NULL DEFAULT '[]',
    created_at INTEGER NOT NULL
);

-- skin_products table
CREATE TABLE skin_products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    brand TEXT DEFAULT '',
    category TEXT NOT NULL,
    is_active INTEGER NOT NULL DEFAULT 1,
    start_date TEXT NOT NULL,
    notes TEXT DEFAULT ''
);

-- triggers table
CREATE TABLE triggers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    severity INTEGER NOT NULL DEFAULT 1
);
```

---

## 🔔 Notifications (WorkManager)

```kotlin
// DailyReminderWorker.kt (in feature:notifications)
class DailyReminderWorker(ctx: Context, params: WorkerParameters) 
    : CoroutineWorker(ctx, params) {
    
    override suspend fun doWork(): Result {
        val channel = "clearskin_daily_reminder"
        showNotification(
            title = "Time for your skin check-in 🌿",
            message = "Log today's skin condition to track your progress."
        )
        return Result.success()
    }
}
```

Schedule on app start and when user changes reminder time:
```kotlin
WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    "daily_skin_reminder",
    ExistingPeriodicWorkPolicy.UPDATE,
    PeriodicWorkRequestBuilder<DailyReminderWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(calculateDelayToTarget(reminderTime), TimeUnit.MILLISECONDS)
        .build()
)
```

---

## 📊 Firebase Integration

### Events to Log
```kotlin
// In AnalyticsManager
fun logSkinLogCreated(score: Int)     → "skin_log_created" + score param
fun logPhotoAnalyzed()                → "ai_photo_analyzed"
fun logProductAdded(category: String) → "product_added" + category param
fun logTriggerIdentified(name: String)→ "trigger_identified" + name param
```

### Firestore Structure (Optional Cloud Sync)
```
users/{uid}/
  profile/
    skin_type, skin_goal, reminder_time
  logs/{date}/
    condition_score, mood, triggers[], products_used[], notes, photo_url
  products/{id}/
    name, brand, category, is_active
```

---

## 🛣️ Navigation Graph

```kotlin
// Routes (Screen.kt)
sealed class ClearSkinScreen(val route: String) {
    object Onboarding    : ClearSkinScreen("onboarding")
    object Dashboard     : ClearSkinScreen("dashboard")
    object Log           : ClearSkinScreen("log/{date}")
    object Progress      : ClearSkinScreen("progress")
    object Triggers      : ClearSkinScreen("triggers")
    object Products      : ClearSkinScreen("products")
    object Insights      : ClearSkinScreen("insights")
    object Calendar      : ClearSkinScreen("calendar")
    object Settings      : ClearSkinScreen("settings")
}
```

Bottom navigation tabs: **Dashboard | Progress | Log (FAB) | Insights | Settings**

---

## 🏁 MVP Scope

For the MVP, build only:
1. ✅ Onboarding (skin type + goal)
2. ✅ Dashboard (streak, today status)
3. ✅ Daily Log (photo, score, notes)
4. ✅ Progress (line chart + before/after)
5. ✅ Settings (reminder time)

Defer to Phase 2:
- Triggers correlation analysis
- AI skin analysis (Gemini)
- Products tracker
- Calendar heatmap
- Cloud sync

---

## ⚠️ Architecture Rules (Same as Thinkora)

1. **Never access Room from feature modules directly** — always via use cases
2. **Domain layer is pure Kotlin** — no Android imports
3. **All strings in `res/values/strings.xml`** (Arabic + English i18n)
4. **All Firebase events through `AnalyticsManager`**
5. **Photos stored locally** (Room stores URI) + optionally backed up to Firebase Storage
6. **NEVER store raw photos in Room** — only URIs

---

## 📋 Implementation Order (Recommended)

```
Phase 1 — Foundation
  1. Create project (com.apachi.clearskin)
  2. Setup core:domain (models + interfaces)
  3. Setup core:data (Room DB, DAOs, repositories)
  4. Setup core:designsystem (palette, typography)
  5. Setup :app (Hilt, Navigation, MainActivity)

Phase 2 — MVP Features
  6. feature:onboarding
  7. feature:dashboard
  8. feature:log (with CameraX)
  9. feature:progress (with Vico charts)
  10. feature:settings + WorkManager reminders

Phase 3 — Intelligence Layer
  11. feature:insights (Gemini API integration)
  12. feature:triggers (correlation engine)
  13. feature:products
  14. feature:calendar (heatmap)

Phase 4 — Cloud & Polish
  15. Firebase Firestore sync
  16. Firebase Storage (photo backup)
  17. Gamification (streaks, badges)
  18. Unit + UI tests
  19. CI/CD (GitHub Actions)
  20. Play Store release
```

---

*Last updated: 2026-04-01 | maintainer: Antigravity AI | status: PROPOSED*
