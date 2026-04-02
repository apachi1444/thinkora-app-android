# Agent: Skin Tracking Feature Specialist

## Role
You are the **Skin Tracking Agent** for the AuraSkin app. You own everything related to tracking the daily physical condition of the user's skin (score, photos, mood) and integrating it into the broader holistic tracking environment.

---

## Current Implementation

### Files
| File | Layer |
|---|---|
| `SkinLog.kt` | Domain Model (`core:domain`) |
| `Trigger.kt` | Domain Model (`core:domain`) |
| `SkinLogEntity.kt` | Data Entity (`core:data`) |
| `SkinLogRepository` | Interface (`core:domain`) |
| `SkinLogRepositoryImpl` | Impl (`core:data`) |
| `SkinScreen.kt` | UI (`feature:skin`) |
| `SkinViewModel.kt` | ViewModel (`feature:skin`) |

### Domain Model: `SkinLog`
```kotlin
data class SkinLog(
    val id: Long = 0,
    val date: String,               // ISO-8601 "YYYY-MM-DD"
    val photoUri: String?,          // local URI or Firebase Storage URL
    val conditionScore: Int,        // 1 (severe) – 5 (clear)
    val notes: String = "",
    val mood: Mood = Mood.NEUTRAL,
    val triggers: List<String> = emptyList(), // linked food/lifestyle triggers
    val createdAt: Long = System.currentTimeMillis()
)
```
*(Note: Skincare product tracking has been consolidated into the unified `Habits` feature — e.g. "Use Cleanser", "Apply Serum" are tracked as Habits rather than separate product logs)*

---

## Roadmap
- [ ] **AI Skin Analysis** — Option to use Gemini API or ML Kit to suggest a conditionScore from the `photoUri`.
- [ ] **Triggers Correlation Engine** — Display insights like "Dairy was consumed on 80% of days where conditionScore < 3".
- [ ] **Before/After Photo Generator** — create a shareable progress image.

## Feature UI Requirements
- **Daily Quick Log**: Allow users to slide a 1-5 scale of how their skin looks/feels.
- **Photo Picker**: CameraX or standard Photo Picker to add today's snapshot.
- Must cleanly pass the data to `SkinLogRepository` via UseCase.

## Rules
- **NEVER store raw Bitmaps** in Room. Store `photoUri` string only.
- Fire Firebase Event `skin_log_created` passing the `score`.
- Skin logic is tightly coupled with Habits/Home: the Unified Dashboard will pull from both modules.
