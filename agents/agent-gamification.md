# Agent: Gamification Feature Specialist

## Role
You own the **achievements, badges, and XP system** in the Thinkora app.

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

## Achievement Definitions (Seed Data)

| Badge | Requirement | Icon |
|---|---|---|
| First Step | Complete any habit 1 time | 🌱 |
| 3-Day Streak | 3 consecutive days | 🔥 |
| Week Warrior | 7-day streak | 🏆 |
| Month Master | 30-day streak | 👑 |
| Habit Builder | 5 habits created | 🧱 |
| Quote Lover | 10 quotes favorited | 💛 |
| Consistent | Complete all habits in a day | ⭐ |

---

## Achievement Unlock Flow
```
IncrementStreakUseCase completes
  → CheckAchievementsUseCase(habitId, newStreak)
    → GamificationRepository.getUnlocked()
    → For each un-unlocked achievement: evaluate condition
    → GamificationRepository.unlock(achievementId)
      → AnalyticsManager.logEvent("achievement_unlocked", badgeName)
```

---

## Roadmap
- [ ] **XP System** — earn XP per habit completion, level up
- [ ] **Level display** on HomeScreen ("Level 7 — Consistency Pro")
- [ ] **Confetti animation** on badge unlock (Compose animation)
- [ ] **Share badge** → connect to `agent-social-sharing.md`

---

## Rules
- Achievement checks must happen inside a use case, not inside ViewModel
- Badge unlock is idempotent — re-checking an already unlocked badge is a no-op
- All achievement seed data goes in the Room `prepopulate` callback
