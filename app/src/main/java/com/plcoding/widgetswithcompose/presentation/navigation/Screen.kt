package com.plcoding.widgetswithcompose.presentation.navigation

sealed class Screen(val route: String) {
    object OnboardingScreen : Screen("onboarding_screen")
    object OnboardingIntro : Screen("onboarding_intro")
    object OnboardingName : Screen("onboarding_name")
    object OnboardingInterests : Screen("onboarding_interests")
    object HomeScreen : Screen("home_screen")
    object HabitsScreen : Screen("habits_screen")
    object SettingsScreen : Screen("settings_screen")
    object MainScreen : Screen("main_screen")
    object CategoryQuotesScreen : Screen("category_quotes_screen/{categoryName}") {
        fun createRoute(categoryName: String) = "category_quotes_screen/$categoryName"
    }
}
