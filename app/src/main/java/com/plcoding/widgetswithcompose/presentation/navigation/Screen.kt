package com.plcoding.widgetswithcompose.presentation.navigation

sealed class Screen(val route: String) {
    object OnboardingScreen : Screen("onboarding_screen")
    object HomeScreen : Screen("home_screen")
}
