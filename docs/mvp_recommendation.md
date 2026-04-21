# MVP Recommendation & Feature Phases

## Current MVP Scope (Phase 1 — Habits First)

**The current MVP focuses on habit tracking as the core loop.** All Phase 1 features are implemented and functional:

- ✅ Habit CRUD with streaks
- ✅ Home Dashboard with greeting + streak card + today's habits
- ✅ 4-step onboarding (Skin Focus → Skin Type → Routine → Commitment)
- ✅ Glance home screen widget
- ✅ Settings (dark mode, language, reminders)
- ✅ Notifications center
- ✅ Achievements / badges
- ✅ Weekly analytics (bar charts)
- ✅ Custom quotes (add, view, delete)
- ✅ Zoom drawer navigation

---

## Phase 2 — Skin Intelligence (Designed, Pending Implementation)

All Phase 2 screens are **fully designed in Stitch** with light + dark mode. They extend the app from a simple habit tracker into a holistic skin intelligence platform.

### New Screens (from Stitch)

| Screen | Stitch ID | Description | New Module |
|---|---|---|---|
| Daily Skin Log | SCREEN_13 | Condition sliders, mood, notes | `feature:skinlog` |
| Habit Manager | SCREEN_10 | Morning/Night/Lifestyle tabs with progress | `feature:habitmanager` |
| My Cabinet | SCREEN_27 | Product inventory + PAO alerts | `feature:cabinet` |
| Morning Guide | SCREEN_12 | Step-by-step routine with timers | `feature:guide` |
| Insights & Analytics | SCREEN_28 | Correlation charts + AI tips | Enhanced `feature:analytics` |
| Mindset Vault | SCREEN_5 | Unified quotes + achievements hub | Enhanced `feature:quotes` |

### Navigation Upgrade
- **FROM**: 3-tab bottom nav (Home / Habits / Settings)
- **TO**: 5-tab bottom nav (Home / Log / Habits / Insights / Vault)
- Settings moves to drawer or accessible from Home header

### New Domain Models Needed
- `SkinLog`: date, conditionScore, mood, notes, photoUri
- `Product`: name, brand, volume, remainingPercent, openDate, paoDays
- `RoutineStep`: habitId, order, durationSeconds, productId
- `Habit.category`: extend with Morning / Night / Lifestyle enum

---

## Phase 3 — Growth & Social

- [ ] Sign In / Sign Up (designs exist in Stitch)
- [ ] Cloud Sync (Firebase Firestore)
- [ ] Social Sharing (quotes/progress as images)
- [ ] Data Export (CSV/JSON)
- [ ] Invite Friends

---

## Recommendation

> **Phase 1 (MVP) is complete.** The app is functional with habits, quotes, analytics, and gamification.
>
> **Phase 2 is fully designed.** All 10 Stitch screens are ready. Implementation should follow this order:
> 1. Navigation upgrade (3-tab → 5-tab)
> 2. UI refresh of existing screens to match Stitch designs
> 3. Daily Skin Log (core new data)
> 4. Insights & Analytics (correlation engine)
> 5. Cabinet + Morning Guide (productivity features)
> 6. Habit Manager (routine categorization)
> 7. Mindset Vault unification
