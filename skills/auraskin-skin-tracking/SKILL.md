---
name: auraskin-skin-tracking
description: |
  AuraSkin skin tracking patterns — SkinLog domain model, SkinLogEntity, photo URI handling, conditionScore scale, mood, triggers, SkinLogRepository, use cases, and the SkinScreen/SkinViewModel MVI wiring. Use this skill whenever writing or reviewing anything related to skin logging, condition scores, photo capture, triggers, daily skin notes, or SkinLogRepository. Trigger on phrases like "skin log", "conditionScore", "photoUri", "skin condition", "skin photo", "CameraX", "triggers", "Mood", "daily skin", "skin score", or "SkinScreen".
---

# AuraSkin — Skin Tracking Feature

## Core Principle

The skin tracking feature is the heartbeat of AuraSkin. Every `SkinLog` represents a single day's skin snapshot. Always store only the photo URI — **never raw Bitmaps in Room**. All logic flows through use cases. The feature is tightly coupled with the Home dashboard and Analytics.

---

## Domain Model

Lives in `core:domain`. Pure Kotlin — no Android imports.

```kotlin
data class SkinLog(
    val id: Long = 0,
    val date: String,               // ISO-8601 "YYYY-MM-DD"
    val photoUri: String? = null,   // local URI or Firebase Storage URL
    val conditionScore: Int,        // 1 (severe) – 5 (clear)
    val notes: String = "",
    val mood: Mood = Mood.NEUTRAL,
    val triggers: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

enum class Mood { HAPPY, NEUTRAL, SAD, STRESSED, ANXIOUS }
```

**conditionScore scale:**

| Score | Meaning |
|---|---|
| 1 | Severe breakout / very irritated |
| 2 | Moderate breakout |
| 3 | Mild breakout / uneven |
| 4 | Mostly clear |
| 5 | Completely clear / glowing |

---

## Repository Interface (Domain)

```kotlin
interface SkinLogRepository {
    suspend fun insertLog(log: SkinLog): EmptyResult<DataError.Local>
    suspend fun getLogByDate(date: String): Result<SkinLog?, DataError.Local>
    suspend fun getAllLogs(): Flow<List<SkinLog>>
    suspend fun deleteLog(id: Long): EmptyResult<DataError.Local>
    suspend fun getLogsInRange(from: String, to: String): Result<List<SkinLog>, DataError.Local>
}
```

---

## Room Entity

Lives in `core:data`.

```kotlin
@Entity(tableName = "skin_logs")
data class SkinLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,         // ISO-8601 "YYYY-MM-DD", indexed for range queries
    val photoUri: String?,
    val conditionScore: Int,
    val notes: String,
    val mood: String,         // Mood.name()
    val triggers: String,     // JSON array string e.g. "[\"Dairy\",\"Stress\"]"
    val createdAt: Long
)
```

Use a `TypeConverter` or store `triggers` as a JSON string — **never a `List<String>` directly in a `@Entity`**.

```kotlin
// Mapper
fun SkinLogEntity.toDomain() = SkinLog(
    id = id, date = date, photoUri = photoUri,
    conditionScore = conditionScore, notes = notes,
    mood = Mood.valueOf(mood),
    triggers = Json.decodeFromString(triggers),
    createdAt = createdAt
)

fun SkinLog.toEntity() = SkinLogEntity(
    id = id, date = date, photoUri = photoUri,
    conditionScore = conditionScore, notes = notes,
    mood = mood.name,
    triggers = Json.encodeToString(triggers),
    createdAt = createdAt
)
```

---

## Use Cases

All use cases live in `core:domain`. Each is a single-responsibility class.

```kotlin
class InsertSkinLogUseCase(private val repo: SkinLogRepository) {
    suspend operator fun invoke(log: SkinLog) = repo.insertLog(log)
}

class GetTodaySkinLogUseCase(private val repo: SkinLogRepository) {
    suspend operator fun invoke(): Result<SkinLog?, DataError.Local> {
        val today = LocalDate.now().toString() // "YYYY-MM-DD"
        return repo.getLogByDate(today)
    }
}

class GetSkinLogsInRangeUseCase(private val repo: SkinLogRepository) {
    suspend operator fun invoke(from: String, to: String) =
        repo.getLogsInRange(from, to)
}
```

---

## Photo Handling

- Use Android's **Photo Picker** (`PickVisualMedia`) or **CameraX** for capturing a photo.
- Store only the resulting `Uri.toString()` in `SkinLog.photoUri`.
- **Never** store `Bitmap` bytes in Room.
- Persist the URI before inserting the log; after process death the URI must still be valid (use `contentResolver.takePersistableUriPermission` for gallery URIs).

```kotlin
// In ViewModel
fun onAction(action: SkinAction) {
    when (action) {
        is SkinAction.OnPhotoSelected -> {
            _state.update { it.copy(selectedPhotoUri = action.uri.toString()) }
        }
        is SkinAction.OnSaveLog -> saveLog()
    }
}
```

---

## MVI State / Actions / Events

```kotlin
data class SkinState(
    val date: String = LocalDate.now().toString(),
    val conditionScore: Int = 3,
    val mood: Mood = Mood.NEUTRAL,
    val notes: String = "",
    val selectedPhotoUri: String? = null,
    val triggers: List<String> = emptyList(),
    val isSaving: Boolean = false,
    val error: UiText? = null,
    val todayLogExists: Boolean = false
)

sealed interface SkinAction {
    data class OnScoreChange(val score: Int) : SkinAction
    data class OnMoodChange(val mood: Mood) : SkinAction
    data class OnNotesChange(val notes: String) : SkinAction
    data class OnPhotoSelected(val uri: Uri) : SkinAction
    data class OnTriggerToggled(val trigger: String) : SkinAction
    data object OnSaveLog : SkinAction
}

sealed interface SkinEvent {
    data object LogSaved : SkinEvent
    data class ShowError(val message: UiText) : SkinEvent
}
```

---

## Firebase Events

Fire through `AnalyticsManager` — never call `FirebaseAnalytics` directly.

| Event Name | When | Parameters |
|---|---|---|
| `skin_log_created` | New log inserted | `score: Int`, `mood: String` |
| `skin_log_updated` | Existing log updated | `score: Int` |
| `skin_photo_added` | Photo URI attached to a log | — |

---

## Rules

1. **NEVER** store raw `Bitmap` in Room. Only store `photoUri: String?`.
2. Always use `InsertSkinLogUseCase` / `GetTodaySkinLogUseCase` — never call `SkinLogRepository` directly from the ViewModel.
3. `date` is always ISO-8601 `"YYYY-MM-DD"` — use `LocalDate.now().toString()`.
4. The `triggers` field is a `List<String>` at domain/UI level; serialize to JSON in the entity.
5. Fire `skin_log_created` analytics event on every new insert.
6. Skin data is consumed by `:feature:analytics` — do not break the `getLogsInRange` contract.

---

## Checklist: Adding / Modifying Skin Tracking

- [ ] Update `SkinLog` in `core:domain` if the model changes
- [ ] Mirror changes in `SkinLogEntity` + update mappers in `core:data`
- [ ] Add/update use case in `core:domain`
- [ ] Update `SkinLogRepository` interface + `SkinLogRepositoryImpl`
- [ ] Update `SkinState`, `SkinAction`, `SkinViewModel` in `feature:skin`
- [ ] Wire new actions in `SkinScreen`
- [ ] Fire appropriate Firebase event via `AnalyticsManager`
- [ ] Write a unit test for the new use case
