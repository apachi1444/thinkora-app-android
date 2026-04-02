# Agent: Gamification Feature Specialist

## Role
You own the **achievements, badges, and XP system** in the AuraSkin app, encouraging consistency in skincare routines and daily skin photo logging.

---

## Current Implementation

### Files
| File | Layer |
|---|---|
| `Achievement.kt` | Domain Model (`core:domain`) |
| `GamificationRepository.kt` | Interface (`core:domain`) |
| `GamificationViewModel.kt` | ViewModel (`feature:gamification`) |
| `AchievementsScreen.kt` | UI (`feature:gamification`) |

### Domain Model: `Achievement`
```kotlin
data class Achievement(
    val id: Long = 0,
    val title: String,
    val description: String,
    val badgeIcon: String,       // vector drawable name
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val requirement: Int         // e.g. streakDays required
)
```

---

## Defined Badges (AuraSkin Focused)

| Badge | Requirement | Icon |
|---|---|---|
| First Step | Complete your first skincare habit | 🌱 |
| The 7-Day Glow Up | 7-day habit streak + 7 days of skin logs | ✨ |
| Hydration Hero | Complete "Drink Water" habit for 14 days | 💧 |
| Mindful Journey | Favorite 10 quotes | 💛 |
| Month Master | 30-day routine streak | 👑 |
| The Perfect Day | Complete all morning & night habits | ⭐ |

---

## Roadmap
- [ ] **Level System** — earn XP for logging photos and completing routines (e.g. "Level 5 — Consistency Pro").
- [ ] **Confetti animation** on badge unlock.
- [ ] **Share badge** → connect to `agent-social-sharing.md`.

---

## Rules
- Achievement checking must happen in a UseCase (`CheckAchievementsUseCase`).
- Badge unlock is idempotent.
