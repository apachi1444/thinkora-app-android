package com.plcoding.widgetswithcompose.domain.model

data class UserPreferences(
    val userName: String,
    val interestCategories: List<String>,
    val isOnboardingCompleted: Boolean
)
