package com.apachi.auraskin.domain.navigation

sealed class Screen(val route: String) {
    // --- Onboarding ---
    object OnboardingScreen : Screen("onboarding_screen")
    object OnboardingIntro : Screen("onboarding_intro")
    object OnboardingName : Screen("onboarding_name")
    object OnboardingInterests : Screen("onboarding_interests")
    object OnboardingNotifications : Screen("onboarding_notifications")

    // --- Main Shell ---
    object MainScreen : Screen("main_screen")

    // --- Bottom Navigation Tabs (5-tab layout) ---
    object HomeScreen : Screen("home_screen")
    object SkinLogScreen : Screen("skin_log_screen")
    object HabitsScreen : Screen("habits_screen")
    object InsightsScreen : Screen("insights_screen")
    object VaultScreen : Screen("vault_screen")

    // --- Habits Sub-Screens ---
    object HabitManagerScreen : Screen("habit_manager_screen")
    object MorningGuideScreen : Screen("morning_guide_screen")

    // --- Skin & Cabinet ---
    object CabinetScreen : Screen("cabinet_screen")

    // --- Analytics (legacy route, now merged into Insights) ---
    object AnalyticsScreen : Screen("analytics_screen")

    // --- Quotes ---
    object CustomQuotesScreen : Screen("custom_quotes_screen")
    object AddQuoteScreen : Screen("add_quote_screen")
    object CategoryQuotesScreen : Screen("category_quotes_screen/{categoryName}") {
        fun createRoute(categoryName: String) = "category_quotes_screen/$categoryName"
    }

    // --- Gamification ---
    object AchievementsScreen : Screen("achievements_screen")

    // --- Utility Screens ---
    object SettingsScreen : Screen("settings_screen")
    object NotificationsScreen : Screen("notifications_screen")
    object SearchScreen : Screen("search_screen")
}
