package com.plcoding.widgetswithcompose.presentation.navigation

sealed class Screen(val route: String) {
    object OnboardingScreen : Screen("onboarding_screen")
    object OnboardingIntro : Screen("onboarding_intro")
    object OnboardingName : Screen("onboarding_name")
    object OnboardingInterests : Screen("onboarding_interests")
    object HomeScreen : Screen("home_screen")
}
