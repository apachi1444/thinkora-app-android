# Agent: Settings Feature Specialist

## Role
You own the **settings and preferences** system — DataStore-backed user preferences, language switching, theme control, and data export.

---

## Current Implementation

### Files
| File | Layer |
|---|---|
| `SettingsRepository.kt` | Interface (`core:domain`) |
| `UserPreferences.kt` | Domain Model (`core:domain`) |
| `SettingsScreen.kt` | UI (`feature:settings`) |
| `SettingsViewModel.kt` | ViewModel (`feature:settings`) |

### Settings Currently Available
- Language selector (English / Arabic — full RTL support)
- Notification toggle
- About / version info

### DataStore Schema (`UserPreferences`)
```kotlin
data class UserPreferences(
    val userName: String,
    val interests: List<String>,
    val isOnboardingComplete: Boolean,
    val preferredLanguage: String,   // "en" | "ar"
    val isDarkMode: Boolean,
    val notificationsEnabled: Boolean
)
```

---

## Roadmap
- [ ] **Dark/Light Mode toggle** — read from `UserPreferences.isDarkMode`, apply via `AppCompatDelegate`
- [ ] **Export data** — export habits + completions as CSV file, share via `Intent.ACTION_SEND`
- [ ] **Delete all data** — wipe Room DB + DataStore (with confirmation dialog)
- [ ] **Account settings** — will appear when Cloud Sync is implemented

---

## Language Switching Implementation
```kotlin
// SettingsViewModel
fun setLanguage(locale: String) {
    viewModelScope.launch {
        settingsRepository.setLanguage(locale)  // Saves to DataStore
        // Force activity recreation for locale change
    }
}

// MainActivity — apply locale on start
val locale = Locale(preferences.preferredLanguage)
Locale.setDefault(locale)
resources.configuration.setLocale(locale)
```

---

## CSV Export Template
```kotlin
fun exportHabitsToCSV(habits: List<Habit>, completions: List<HabitCompletion>): String {
    val sb = StringBuilder()
    sb.appendLine("Habit Name,Streak,Last Completed")
    habits.forEach { sb.appendLine("${it.name},${it.streak},${it.lastCompletedDate}") }
    return sb.toString()
}
```

---

## Rules
- All preferences go through `SettingsRepository` — no direct DataStore access from ViewModel
- Language change must restart the activity (or use `LocaleManager` on API 33+)
- Data export requires `WRITE_EXTERNAL_STORAGE` on API < 29; use `MediaStore` on API 29+
- A confirmation dialog is mandatory before "Delete all data"
