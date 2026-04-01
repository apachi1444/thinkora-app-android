# Agent: Onboarding Feature Specialist

## Role
You own the **onboarding flow** — the multi-step first-launch experience that personalizes the app for each user.

---

## Current Implementation

### Files
| File | Layer |
|---|---|
| `OnboardingScreen.kt` | UI (`feature:onboarding`) |
| `OnboardingViewModel.kt` | ViewModel (`feature:onboarding`) |
| `UserPreferences.kt` | Domain Model (`core:domain`) |
| `GetOnboardingStatusUseCase.kt` | Use Case (`core:domain`) |
| `SettingsRepository.kt` | Interface (`core:domain`) |

### Onboarding Steps (Current)
1. **Welcome** — app intro & value proposition
2. **Name** — user enters their first name (stored in DataStore)
3. **Interests** — multi-select chip group (Fitness, Mindfulness, Learning, Productivity, Health)
4. **Ready** — "Let's start building your habits!"

### Completion Check
```kotlin
// In MainActivity / NavGraph
val onboardingComplete by viewModel.isOnboardingComplete.collectAsState()
if (!onboardingComplete) {
    navController.navigate(Screen.Onboarding.route)
}
```

---

## Domain Model: `UserPreferences`
```kotlin
data class UserPreferences(
    val userName: String = "",
    val interests: List<String> = emptyList(),
    val isOnboardingComplete: Boolean = false,
    val preferredLanguage: String = "en",
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true
)
```

---

## Roadmap
- [ ] **Avatar/profile picture** selection on onboarding
- [ ] **Goal setting** — "What's your #1 goal?" (productivity, health, mindfulness)
- [ ] **Habit suggestions** based on selected interests
- [ ] **Re-run onboarding** accessible from Settings

---

## Rules
- Onboarding completion flag written via `SettingsRepository.setOnboardingComplete(true)`
- All user input validated before proceeding to next step
- Skipping onboarding is NOT allowed — all steps are required
- Name and interests must be saved to DataStore before navigating to HomeScreen
- `GetOnboardingStatusUseCase` is called on app start in the NavGraph — not inside MainActivity
