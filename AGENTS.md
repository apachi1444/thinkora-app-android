# AGENTS.md — Thinkora Android App
> **For AI models (Antigravity, Claude, Gemini, GPT-4o, etc.):**
> This is the master orchestration file. Read this first. It will point you to the right sub-agent for every task. Always follow the agent instructions exactly. Never skip a layer or bypass the architecture rules.

---

## 📱 What is Thinkora?

Thinkora is a **habit tracking + daily inspiration** Android app built with modern Android practices. Users track habits with streaks, get daily motivational quotes, and view analytics of their progress.

- **Package**: `com.apachi.thinkora`
- **Min SDK**: 24 | **Target SDK**: 34
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material3
- **Architecture**: Clean Architecture + MVVM
- **DI**: Hilt 2.48
- **Storage**: Room (habits, quotes, completions) + DataStore (preferences)
- **Widgets**: Glance (habits widget + favorites widget)
- **Analytics**: Firebase Analytics (BOM 32.7.1)
- **Ads**: Google Mobile Ads 23.0.0
- **Async**: Kotlin Coroutines + Flow

---

## 🗂️ Module Map

```
:app                       ← Entry point, DI wiring, Navigation
:core:domain               ← Pure Kotlin: models, repo interfaces, use cases
:core:data                 ← Room, DataStore, repo implementations
:core:designsystem         ← Colors, Typography, Shapes, Tokens
:core:ui                   ← Shared Composable components
:feature:home              ← HomeScreen, HomeViewModel, FavoriteQuotesWidget
:feature:habits            ← HabitsScreen, HabitsViewModel, HabitsWidget
:feature:analytics         ← AnalyticsScreen, AnalyticsViewModel
:feature:gamification      ← AchievementsScreen, GamificationViewModel
:feature:quotes            ← Quotes browsing and favorites
:feature:search            ← SearchScreen (quotes + habits)
:feature:settings          ← SettingsScreen, SettingsViewModel
:feature:notifications     ← NotificationsScreen, NotificationsViewModel
:feature:onboarding        ← OnboardingScreen, OnboardingViewModel
:feature:drawer            ← App navigation drawer
:feature:main              ← Main navigation host
:feature:widget            ← Home screen widget entry point
:feature:category          ← Category browsing for quotes
```

---

## 🤖 Agent Directory

Each agent specializes in one part of the codebase. When given a task, **identify which agent(s) apply** and follow their instructions.

| Task Area | Agent File | Triggers |
|---|---|---|
| Architecture, layers, module structure | [`agents/agent-architecture.md`](agents/agent-architecture.md) | New module, refactoring, layering violation |
| Habit tracking, streaks, widget | [`agents/agent-habits.md`](agents/agent-habits.md) | Habit CRUD, streak logic, Glance widget |
| Quotes, favorites, categories | [`agents/agent-quotes.md`](agents/agent-quotes.md) | Quote model, categories, favorites |
| Analytics & charts | [`agents/agent-analytics.md`](agents/agent-analytics.md) | Weekly bar chart, history, heatmap |
| Gamification & badges | [`agents/agent-gamification.md`](agents/agent-gamification.md) | Achievements, badges, XP/levels |
| Notifications & reminders | [`agents/agent-notifications.md`](agents/agent-notifications.md) | Daily quote reminders, habit reminders |
| Home screen | [`agents/agent-home.md`](agents/agent-home.md) | HomeScreen layout, today's focus, summaries |
| Onboarding | [`agents/agent-onboarding.md`](agents/agent-onboarding.md) | First launch flow, name + interests |
| Search | [`agents/agent-search.md`](agents/agent-search.md) | Full-text search across quotes and habits |
| Settings & preferences | [`agents/agent-settings.md`](agents/agent-settings.md) | DataStore prefs, language, theme, export |
| App widgets | [`agents/agent-widget.md`](agents/agent-widget.md) | Glance widgets: HabitsWidget, FavoritesWidget |
| Cloud sync (Firebase) | [`agents/agent-cloud-sync.md`](agents/agent-cloud-sync.md) | Firestore sync, auth, data backup |
| Firebase & analytics events | [`agents/agent-firebase.md`](agents/agent-firebase.md) | Firebase setup, `google-services.json`, events |
| Design system & UI tokens | [`agents/agent-design-system.md`](agents/agent-design-system.md) | Colors, typography, spacing, dark mode |
| Navigation graph | [`agents/agent-navigation.md`](agents/agent-navigation.md) | Routes, back stack, deep links |
| Social sharing | [`agents/agent-social-sharing.md`](agents/agent-social-sharing.md) | Share quotes as images |
| Testing | [`agents/agent-testing.md`](agents/agent-testing.md) | Unit tests, UI tests, test strategy |
| CI/CD pipeline | [`agents/agent-cicd.md`](agents/agent-cicd.md) | GitHub Actions, build, lint, deploy |
| **Acne Tracker App (NEW)** | [`agents/agent-acne-app.md`](agents/agent-acne-app.md) | New standalone app proposal & spec |

---

## 🔁 Agent Orchestration Protocol

When an AI model reads this file, follow this decision tree:

```
1. Understand the user's task
2. Identify affected module(s) from the Module Map above
3. Find the matching Agent(s) in the Agent Directory
4. Read the full agent .md file before writing any code
5. Follow the agent's rules, templates, and flow diagrams
6. If multiple agents apply: start with agent-architecture.md,
   then proceed feature-agent → data agent → test agent
7. After changes: update task.md progress if it exists
```

---

## 📋 Current Status & Priorities

### ✅ Done
- Habit tracking with streak counters
- `HabitCompletionEntity` for history
- Weekly bar chart (AnalyticsScreen)
- Custom Quotes (add/delete)
- Firebase Analytics events (`habit_completed`, `custom_quote_added`)
- App Drawer navigation
- Onboarding flow (name, interests)
- i18n & RTL support
- Glance widgets (Habits + FavoriteQuotes)

### 🔴 Critical Next
1. Place `google-services.json` in `app/` → see [`agents/agent-firebase.md`](agents/agent-firebase.md)
2. Enable `com.google.gms.google-services` plugin in `app/build.gradle`

### 🟡 Roadmap
- Cloud Sync via Firebase Firestore → see [`agents/agent-cloud-sync.md`](agents/agent-cloud-sync.md)
- Gamification badges → see [`agents/agent-gamification.md`](agents/agent-gamification.md)
- Social sharing → see [`agents/agent-social-sharing.md`](agents/agent-social-sharing.md)
- Enhanced analytics (calendar heatmap) → see [`agents/agent-analytics.md`](agents/agent-analytics.md)
- Unit & UI tests → see [`agents/agent-testing.md`](agents/agent-testing.md)
- CI/CD pipeline → see [`agents/agent-cicd.md`](agents/agent-cicd.md)

---

## ⚠️ Golden Rules (Always Apply)

1. **Never access Room/DataStore from a feature module directly**
2. **Always use use cases as the bridge between feature and data**
3. **All strings in `res/values/strings.xml`** (i18n required)
4. **All new screens need a route in `Screen.kt`**
5. **All Firebase events must go through `AnalyticsManager`**
6. **Every PR needs a corresponding unit test for the use case**

---

*Last updated: 2026-04-01 | maintainer: Antigravity AI*
