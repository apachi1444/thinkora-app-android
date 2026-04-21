# Project Status and Next Steps

> Last updated: 2026-04-21

## Design Status — Stitch Screens

All 10 core screens have been designed in Stitch with full light + dark mode support using the **AuraSkin Luminous Calm** theme.

| # | Screen | Stitch ID | Design | Code |
|---|---|---|---|---|
| 1 | Home Dashboard | SCREEN_17 | ✅ Final | ✅ Implemented (needs design refresh) |
| 2 | Habits List | SCREEN_15 | ✅ Final | ✅ Implemented (needs design refresh) |
| 3 | Habit Manager | SCREEN_10 | ✅ Final | ❌ Not implemented |
| 4 | Daily Skin Log | SCREEN_13 | ✅ Final | ❌ Not implemented |
| 5 | My Cabinet | SCREEN_27 | ✅ Final | ❌ Not implemented |
| 6 | Insights & Analytics | SCREEN_28 | ✅ Final | ⚠️ Partial (basic bar chart exists) |
| 7 | Morning Guide | SCREEN_12 | ✅ Final | ❌ Not implemented |
| 8 | Mindset Vault | SCREEN_5 | ✅ Final | ⚠️ Partial (quotes + achievements separate) |
| 9 | Settings | SCREEN_18 | ✅ Final | ✅ Implemented (needs design refresh) |
| 10 | Notifications | SCREEN_23 | ✅ Final | ✅ Implemented (needs design refresh) |

### Previously Designed (also in Stitch)
| Screen | Status |
|---|---|
| Onboarding Step 1 — Skin Focus | ✅ Designed + Implemented |
| Onboarding Step 3 — Daily Rituals | ✅ Designed + Implemented |
| Sign In | ✅ Designed, ❌ Not implemented |
| Sign Up | ✅ Designed, ❌ Not implemented |

---

## Completed Features

### 1. Habit Tracking (Core)
- **Data Layer**: `Habit` model, `HabitEntity`, `HabitDao`, `HabitRepository` with CRUD + streak increment.
- **UI**: `HabitsScreen` with list, swipe-to-delete, add dialog, bulk increment.
- **Widget**: Glance-powered `HabitsWidget` with interactive streak increment.

### 2. Home Dashboard
- **UI**: `HomeScreen` with greeting, streak card, top 3 habits preview.
- **Navigation**: Hamburger → Drawer, Bell → Notifications.

### 3. 4-Step Onboarding
- **UI**: `OnboardingScreen` → `FocusScreen` → `SkinTypeScreen` → `RoutineScreen` → `CommitmentScreen`.
- **Data**: User preferences persisted via DataStore (name, skin type, focus, notifications).

### 4. Custom Quotes
- **Data Layer**: `Quote` model with `isCustom` flag. `QuoteDao`, `QuoteRepository` support add/delete.
- **UI**: `CustomQuotesScreen` (list) + `AddQuoteScreen` (form with category chips).

### 5. Detailed Analytics
- **Data Layer**: `HabitCompletionEntity` tracks historical completions.
- **UI**: `AnalyticsScreen` with weekly bar chart per habit.
- **Logic**: Completions logged automatically on streak increment.

### 6. Gamification
- **Data Layer**: `Achievement` model, `AchievementEntity`, `AchievementDao`.
- **UI**: `AchievementsScreen` with locked/unlocked badge cards.
- **Logic**: `CheckAchievementsUseCase` evaluates badges on habit events.

### 7. Settings
- **UI**: Dark mode toggle, language (EN/AR), daily reminder toggle + time picker, notifications link, delete account.
- **Data**: All preferences via DataStore. Reminder scheduling via WorkManager.

### 8. Notifications
- **UI**: `NotificationsScreen` with mark all read, DND toggle, relative timestamps.
- **Data**: `NotificationEntity`, `NotificationDao`, `NotificationRepository`.

### 9. Firebase Analytics
- **Infrastructure**: Firebase BOM 32.7.1, `AnalyticsManager` wrapper.
- **Events**: `habit_completed`, `custom_quote_added` logged via repositories.

### 10. Navigation & Drawer
- **Bottom Nav**: Currently 3 tabs (Home / Habits / Settings).
- **Drawer**: `ZoomDrawer` with animated scale + translate. Links to Achievements, Analytics.

---

## Technical Next Steps

### 🔴 Critical (Before Next Build)
1. **`google-services.json`**: Must be placed in `app/` for Firebase to initialize.
2. **Update Bottom Nav to 5 Tabs**: Modify `MainScreen.kt` to use HOME / LOG / HABITS / INSIGHTS / VAULT.
3. **Refresh Existing Screens**: Apply Stitch designs to HomeScreen, HabitsScreen, Settings, Notifications.

### 🟡 High Priority (New Feature Modules)
4. **Create `feature:skinlog`**: Daily Skin Log screen with sliders, mood selector, notes.
   - New domain model: `SkinLog` (already exists as entity).
   - New DAO methods for daily log CRUD.
5. **Create `feature:cabinet`**: Product inventory with PAO tracking.
   - New domain model: `Product` with name, volume, openDate, PAO.
   - New Room entity + DAO.
6. **Create `feature:guide`**: Morning Guide with step-by-step + timers.
   - Reads from user's active habits/routines.
   - References products from Cabinet.
7. **Create `feature:habitmanager`**: Tabbed routine view (Morning / Night / Lifestyle).
   - Extends `Habit` model with `category` field.
   - Tab-filtered habit list with progress bars.
8. **Merge Vault Screen**: Combine `CustomQuotesScreen` + `AchievementsScreen` into unified `VaultScreen` with tab bar (SAVED QUOTES / ACHIEVEMENTS).

### 🟢 Medium Priority (Enhancements)
9. **Enhanced Insights**: Add skin score line graph overlay + AI wellness tips.
   - Consider Vico chart library for line graphs.
10. **Design System Update**: Add Manrope font, update border radii to 24px/16px per Stitch tokens.
11. **Sign In / Sign Up**: Implement auth screens (designs exist in Stitch).
12. **Data Export**: CSV/JSON export from Settings.

### 🔵 Future
13. **Cloud Sync**: Firebase Firestore for cross-device data.
14. **Social Sharing**: Share quotes/progress as styled images.
15. **Onboarding Enhancements**: Add daily rituals step with toggle switches (Stitch design exists).

---

## Functional Next Steps

1. **UI Refresh**: Apply all 10 Stitch designs to existing and new screens.
2. **Navigation Overhaul**: Switch from 3-tab to 5-tab bottom nav per Stitch mockups.
3. **Skin Intelligence Layer**: Implement skin log + insights correlation engine.
4. **Product Cabinet**: Full product tracking with smart alerts.
5. **Morning Guide**: Guided routine experience with timers.
