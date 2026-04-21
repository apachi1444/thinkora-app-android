# Technical Documentation

## 🛠 Technical Architecture

AuraSkin is built using modern Android development practices and libraries:

- **Language**: Kotlin
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material3)
- **Architecture Pattern**: Clean Architecture with MVVM (Model-View-ViewModel)
  - **Presentation**: UI components and ViewModels (`feature` modules).
  - **Domain**: Business logic, Models, and Use Cases (`core:domain` module).
  - **Data**: Room, DataStore, and repository implementations (`core:data` module).
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/) 2.48
- **Local Storage**:
  - **Room Database**: Habits, quotes, skin logs, achievements, notifications, product cabinet.
  - **DataStore**: User preferences (onboarding status, name, theme, language, reminders).
- **Navigation**: Jetpack Compose Navigation with 5-tab bottom nav + drawer.
- **Widgets**: [Glance](https://developer.android.com/jetpack/compose/glance) for interactive home screen widgets.
- **Analytics**: Firebase Analytics (BOM 32.7.1) via `AnalyticsManager`.
- **Ads**: Google Mobile Ads 23.0.0 (banner ads, configurable).
- **Asynchronous Programming**: Kotlin Coroutines & Flow.

## 🎨 Design System: AuraSkin Luminous Calm

| Token | Light | Dark |
|---|---|---|
| Primary | `#76B599` | `#81BBA1` |
| On Primary | `#FFFFFF` | `#FFFFFF` |
| Background | `#FCFCFC` | `#141A1E` |
| Surface | `#FFFFFF` | `#1E262B` |
| Surface Variant | `#F1F5F9` | `#2B363D` |
| Text Primary | `#2F3A41` | `#E2E8F0` |
| Text Secondary | `#6B7280` | `#9CA3AF` |
| Accent | `#EFA39E` | `#5A2A28` |
| Outline | `#E2E8F0` | `#374151` |
| Error | `#EF4444` | `#F87171` |
| Warning | `#F97316` | `#FB923C` |
| Success | `#76B599` | `#81BBA1` |
| Rating | `#FFB020` | — |

- **Typography**: Manrope / Inter, sans-serif
- **Shapes**: Cards `24px`, Buttons `16px`, Avatars `50%`
- **Elevation**: Subtle 2dp shadows

## 📂 Project Structure

The project follows a multi-module structure organized by layer and feature:

```
com.apachi.auraskin
├── app/                     # Entry point, DI wiring, MainActivity, WorkManager
├── core/
│   ├── data/                # Room DB, DAOs, Entities, DataStore, Repositories impl
│   ├── domain/              # Models, Repository interfaces, Use Cases, Screen routes
│   ├── designsystem/        # Colors, Typography, Shapes, Reusable components
│   └── ui/                  # Shared composables (BannerAd, HeroQuoteCard)
├── feature/
│   ├── home/                # Home Dashboard (greeting, streak, today's habits)
│   ├── habits/              # Habits List + Glance Widget
│   ├── analytics/           # Insights & Analytics (bar charts, correlations)
│   ├── gamification/        # Achievements / Badges screen
│   ├── quotes/              # Custom Quotes (list + add form)
│   ├── category/            # Category-filtered quote browsing
│   ├── search/              # Full-text search across quotes & habits
│   ├── settings/            # App preferences (theme, language, reminders)
│   ├── notifications/       # Notification center
│   ├── onboarding/          # 4-step onboarding flow
│   ├── drawer/              # Zoom drawer (animated side menu)
│   ├── main/                # Main navigation host (bottom nav + drawer shell)
│   └── widget/              # Home screen widget entry point
├── designs/                 # Stitch design mockups (PNG exports)
├── docs/                    # Project documentation
└── agents/                  # AI agent instruction files
```

### New Modules (Designed, Pending Implementation)

| Module | Screen | Stitch ID |
|---|---|---|
| `feature:skinlog` | Daily Skin Log | SCREEN_13 |
| `feature:cabinet` | My Cabinet (Product Inventory) | SCREEN_27 |
| `feature:guide` | Morning Guide (Step-by-Step) | SCREEN_12 |
| `feature:habitmanager` | Habit Manager (Tabbed Routines) | SCREEN_10 |

## 🧭 Navigation Architecture

### 5-Tab Bottom Navigation

```
HOME → HomeScreen (home_screen)
LOG → SkinLogScreen (skin_log_screen)          [NEW]
HABITS → HabitsScreen (habits_screen)
INSIGHTS → InsightsScreen (insights_screen)    [NEW]
VAULT → VaultScreen (vault_screen)             [NEW]
```

### Side Drawer Routes

```
Achievements → AchievementsScreen (achievements_screen)
Analytics → AnalyticsScreen (analytics_screen)
My Quotes → CustomQuotesScreen (custom_quotes_screen)
Cabinet → CabinetScreen (cabinet_screen)       [NEW]
Morning Guide → MorningGuideScreen             [NEW]
```

### Screen Route Registry (`Screen.kt`)

```kotlin
sealed class Screen(val route: String) {
    // Onboarding
    object OnboardingScreen
    // Main Shell
    object MainScreen
    // Bottom Nav Tabs
    object HomeScreen, SkinLogScreen, HabitsScreen, InsightsScreen, VaultScreen
    // Habit Sub-screens
    object HabitManagerScreen, MorningGuideScreen
    // Skin & Cabinet
    object CabinetScreen
    // Quotes
    object CustomQuotesScreen, AddQuoteScreen, CategoryQuotesScreen
    // Gamification
    object AchievementsScreen
    // Utility
    object SettingsScreen, NotificationsScreen, SearchScreen
    // Legacy
    object AnalyticsScreen
}
```

## 🚀 Roadmap & Provisions (Technical)

### Technical Improvements
- [ ] **Custom Font Integration**: Add Manrope font family to `Type.kt` and designsystem resources.
- [ ] **New Feature Modules**: Create `feature:skinlog`, `feature:cabinet`, `feature:guide`, `feature:habitmanager`.
- [ ] **Domain Models**: Add `SkinLog`, `Product`, `RoutineStep` models with Room entities.
- [ ] **Chart Library**: Integrate Vico or similar for correlation line graphs in Insights.
- [ ] **Unit & UI Tests**: Increase test coverage for domain logic and UI components.
- [ ] **CI/CD Pipeline**: Automate build and testing processes.

## 📦 Setup & Installation

1. Clone the repository.
2. Open in Android Studio (Koala or newer recommended).
3. Place `google-services.json` in `app/` directory for Firebase.
4. Sync Gradle project.
5. Run on an emulator or physical device (Minimum SDK: 24).
